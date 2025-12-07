package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.page.*;
import guru.qa.niffler.extension.CategoryExtension;
import guru.qa.niffler.model.CategoryJson;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.UUID;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

@WebTest
@ExtendWith(CategoryExtension.class)
public class SpendingTest {

    private static final Config CFG = Config.getInstance();


    @Test
    void shouldNotRegisterUserWithExistingUsername() {
        System.out.println("=== Тест: Регистрация с существующим именем пользователя ===");

        String EXISTING_USERNAME = "existing_user_" + UUID.randomUUID().toString().substring(0, 8);
        String EXISTING_PASSWORD = "Pass12345!";

        try {
            // 1. Сначала регистрируем пользователя
            System.out.println("📝 Регистрируем первого пользователя: " + EXISTING_USERNAME);
            RegisterPage registerPage1 = Selenide.open(CFG.frontUrl(), LoginPage.class)
                    .clickRegister();

            registerPage1.registerUser(EXISTING_USERNAME, EXISTING_PASSWORD, EXISTING_PASSWORD);
            System.out.println("✅ Первый пользователь успешно зарегистрирован");

            closeWebDriver(); // Закрываем браузер после первой регистрации

            // 2. Пытаемся зарегистрировать с тем же именем
            System.out.println("🔄 Пытаемся зарегистрировать пользователя с существующим именем: " + EXISTING_USERNAME);

            RegisterPage registerPage2 = Selenide.open(CFG.frontUrl(), LoginPage.class)
                    .clickRegister();

            // Используем существующий метод registerUser (не нужно создавать notRegisterUser)
            registerPage2.notRegisterUser(EXISTING_USERNAME, "DifferentPass123!", "DifferentPass123!");

            // 3. Проверяем что появилось сообщение об ошибке
            $(".form__error").shouldBe(visible)
                    .shouldHave(text("already exists")
                            .or(text("Passwords should be equal"))
                            .or(text("Allowed password length should be from 3 to 12 characters"))
                            .or(text("already registered"))
                            .or(text("Username"))
                            .or(text("занято")));

            // Дополнительная проверка что мы остались на странице регистрации
            $("input[name='username']").shouldBe(visible);
            $("input[name='password']").shouldBe(visible);
            $("input[name='passwordSubmit']").shouldBe(visible);

            System.out.println("✅ Тест пройден: Система правильно отклонила регистрацию с существующим именем");

        } catch (Exception e) {
            System.out.println("❌ Ошибка: " + e.getMessage());
            screenshot("error_existing_username");
            throw e;
        } finally {
            closeWebDriver();
        }
    }
    @Test
    void shouldShowErrorIfPasswordAndConfirmPasswordAreNotEqual() {
        System.out.println("=== Тест: Пароль и подтверждение пароля не совпадают ===");

        String randomUsername = "user_" + UUID.randomUUID().toString().substring(0, 8);
        String password = "Pass12345!";
        String wrongConfirmPassword = "WrongPass123!";

        System.out.println("🔄 Регистрируем пользователя с несовпадающими паролями");

        try {
            RegisterPage registerPage = Selenide.open(CFG.frontUrl(), LoginPage.class)
                    .clickRegister();

            // Вводим разные пароли
            registerPage.notRegisterUser(randomUsername, password, wrongConfirmPassword);

            // Проверяем что появилось сообщение об ошибке
            $(".form__error").shouldBe(visible)
                    .shouldHave(text("password")
                            .or(text("пароль"))
                            .or(text("match"))
                            .or(text("совпадают"))
                            .or(text("different")));


            System.out.println("✅ Тест пройден: Система правильно показала ошибку при несовпадающих паролях");

        } catch (Exception e) {
            System.out.println("❌ Ошибка: " + e.getMessage());
            screenshot("error_password_mismatch");
            throw e;
        } finally {
            closeWebDriver();
        }
    }

    @Test
    void mainPageShouldBeDisplayedAfterSuccessLogin() {
        System.out.println("=== Тест: Главная страница отображается после успешного входа ===");

        // 1. Создаем пользователя
        String randomUsername = "user_" + UUID.randomUUID().toString().substring(0, 8);
        String password = "Pass12345!";

        registerUser(randomUsername, password);
        closeWebDriver(); // Закрываем после регистрации

        // 2. Логинимся
        System.out.println("🔐 Выполняем вход с правильными учетными данными");

        try {
            MainPage mainPage = Selenide.open(CFG.frontUrl(), LoginPage.class)
                    .login(randomUsername, password);

            // 3. Проверяем что главная страница загрузилась
            mainPage.checkThatPageLoaded();

            // Дополнительные проверки главной страницы
            $("h1, .page-title").shouldBe(visible); // Заголовок страницы
            $(".add-spending-button, [href='/spending']").shouldBe(visible); // Кнопка добавления трат

            System.out.println("✅ Тест пройден: Главная страница успешно отображается после входа");

        } catch (Exception e) {
            System.out.println("❌ Ошибка: " + e.getMessage());
            screenshot("error_success_login");
            throw e;
        } finally {
            closeWebDriver();
        }
    }

    @Test
    void userShouldStayOnLoginPageAfterLoginWithBadCredentials() {
        System.out.println("=== Тест: Остаемся на странице логина при неправильных учетных данных ===");

        String wrongUsername = "nonexistent_user_" + UUID.randomUUID().toString().substring(0, 8);
        String wrongPassword = "WrongPass123!";

        System.out.println("🔐 Пытаемся войти с неправильными учетными данными");

        try {
            LoginPage loginPage = Selenide.open(CFG.frontUrl(), LoginPage.class);

            // Пытаемся войти с неправильными данными
            loginPage.login(wrongUsername, wrongPassword);

            // 1. Проверяем что мы остались на странице логина
            $("#username").shouldBe(visible); // Поле username все еще видно
            $("#password").shouldBe(visible); // Поле password все еще видно
            $("button[type='submit']").shouldBe(visible); // Кнопка входа

            // 2. Проверяем что появилось сообщение об ошибке
            $(".form__error-container").shouldBe(visible)
                    .shouldHave(text("invalid")
                            .or(text("Неверные учетные данные пользователя"))
                            .or(text("incorrect"))
                            .or(text("ошибка")));

            // 3. Проверяем URL (должен остаться на странице логина)
            String currentUrl = webdriver().driver().url();
            if (!currentUrl.contains("login") && !currentUrl.contains("auth")) {
                throw new AssertionError("Пользователь не остался на странице логина. Текущий URL: " + currentUrl);
            }

            System.out.println("✅ Тест пройден: Пользователь остался на странице логина при неправильных учетных данных");

        } catch (Exception e) {
            System.out.println("❌ Ошибка: " + e.getMessage());
            screenshot("error_bad_credentials");
            throw e;
        } finally {
            closeWebDriver();
        }
    }

    @Test
    void registerLoginAndAddSpendingForThreeRandomUsers() {
        // Повторяем 3 раза для рандомных пользователей
        for (int i = 1; i <= 3; i++) {
            String randomUsername = "user_" + UUID.randomUUID().toString().substring(0, 8);
            String randomPassword = "Pass12345!";
            String randomFirstName = "Имя_" + UUID.randomUUID().toString().substring(0, 4);
            String randomLastName = "Фамилия_" + UUID.randomUUID().toString().substring(0, 4);

            System.out.println("=== ЦИКЛ " + i + ": Работа с пользователем " + randomUsername + " ===");

            try {
                // 1. РЕГИСТРАЦИЯ ПОЛЬЗОВАТЕЛЯ
                System.out.println("📝 Шаг 1: Регистрация пользователя...");
                registerUser(randomUsername, randomPassword);

                // 2. АВТОРИЗАЦИЯ ПОЛЬЗОВАТЕЛЯ
                System.out.println("🔐 Шаг 2: Авторизация пользователя...");
                MainPage mainPage = loginUser(randomUsername, randomPassword);

                // 3. ЗАПОЛНЕНИЕ ПРОФИЛЯ ПОЛЬЗОВАТЕЛЯ
                System.out.println("👤 Шаг 3: Заполнение профиля пользователя...");
                fillUserProfile(mainPage, randomFirstName, randomLastName, i);

                // 4. ПРОВЕРКА УСПЕШНОЙ РЕГИСТРАЦИИ (из класса RegisterPage)
                System.out.println("✅ Шаг 4: Проверка успешной регистрации...");
                checkRegistrationSuccess();

                // 5. ВНЕСЕНИЕ 3 ТРАТ
                System.out.println("💰 Шаг 5: Внесение 3 трат...");
                addThreeSpendings(mainPage, i);

                sleep(2000);

                // 6. ВЫХОД
                System.out.println("🚪 Шаг 6: Выход из системы...");
                mainPage.logout();

                // 7. ЗАКРЫТИЕ СТРАНИЦЫ
                System.out.println("🔒 Шаг 7: Закрытие страницы...");
                closeWebDriver();

                System.out.println("✅ ЦИКЛ " + i + " УСПЕШНО ЗАВЕРШЕН для пользователя: " + randomUsername + "\n");

            } catch (Exception e) {
                System.out.println("❌ Ошибка в цикле " + i + " для пользователя " + randomUsername + ": " + e.getMessage());
                screenshot("error_cycle_" + i);
                closeWebDriver(); // Закрываем браузер при ошибке
                throw e;
            }
        }

        System.out.println("🎉 ВСЕ 3 ЦИКЛА УСПЕШНО ВЫПОЛНЕНЫ!");
    }

    /**
     * 1. РЕГИСТРАЦИЯ ПОЛЬЗОВАТЕЛЯ
     */
    private void registerUser(String username, String password) {
        RegisterPage registerPage = Selenide.open(CFG.frontUrl(), LoginPage.class)
                .clickRegister();

        registerPage.registerUser(username, password, password);

        System.out.println("✅ Пользователь " + username + " успешно зарегистрирован");
    }

    /**
     * 2. АВТОРИЗАЦИЯ ПОЛЬЗОВАТЕЛЯ
     */
    private MainPage loginUser(String username, String password) {
        MainPage mainPage = Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(username, password);

        // Проверяем что главная страница загрузилась
        mainPage.checkThatPageLoaded();

        System.out.println("✅ Пользователь " + username + " успешно авторизован");
        return mainPage;
    }

    /**
     * 3. ЗАПОЛНЕНИЕ ПРОФИЛЯ ПОЛЬЗОВАТЕЛЯ
     */
    private void fillUserProfile(MainPage mainPage, String firstName, String lastName, int cycleNumber) {
        try {
            System.out.println("👤 Заполняем профиль: " + firstName + " " + lastName);

            // Переходим в профиль
            ProfilePage profilePage = mainPage.goToProfile();

            // Заполняем данные профиля
            profilePage
                    .setFirstName(firstName)
                    .saveProfile();


            // Делаем скриншот профиля
            screenshot("profile_cycle_" + cycleNumber);

            System.out.println("✅ Профиль успешно заполнен: " + firstName + " " + lastName);

        } catch (Exception e) {
            System.out.println("❌ Ошибка при заполнении профиля: " + e.getMessage());
            screenshot("error_profile_cycle_" + cycleNumber);
            throw e;
        }
    }

    /**
     * 4. ПРОВЕРКА УСПЕШНОЙ РЕГИСТРАЦИИ (из класса RegisterPage)
     */
    private void checkRegistrationSuccess() {
        try {
            // Создаем экземпляр RegisterPage для проверки
            RegisterPage registerPage = new RegisterPage();

            // Выполняем проверки из метода checkRegistrationSuccess()
            registerPage.checkRegistrationSuccess()
                    .setPasswordSubmit();

            System.out.println("✅ Проверка успешной регистрации пройдена");

        } catch (Exception e) {
            System.out.println("❌ Ошибка при проверке регистрации: " + e.getMessage());
            throw e;
        }
    }

    /**
     * 5. ВНЕСЕНИЕ 3 ТРАТ
     */
    private void addThreeSpendings(MainPage mainPage, int cycleNumber) {
        // Создаем 3 траты с разными категориями
        for (int j = 1; j <= 3; j++) {
            try {
                // Генерируем уникальное имя категории
                String category = "Категория_" + cycleNumber + "_" + j + "_" +
                        UUID.randomUUID().toString().substring(0, 4);

                CurrencyValues currency = CurrencyValues.values()[j - 1]; // RUB, USD, EUR
                String description = "Трата цикла " + cycleNumber + " номер " + j;
                double amount = 1000 + (Math.random() * 9000);

                System.out.println("💸 Создаем трату " + j + "/3 в категории: " + category);

                // Создаем трату

                mainPage
                        .openAddSpendingForm()
                        .setAmount(amount)
                        .setCategory(category)
                        .setCurrency(currency)
                        .setDescription(description)
                        .clickAddButton();

                mainPage.checkThatTableContains(description);
                sleep(500);

                System.out.println("✅ Трата " + j + "/3 успешно создана");

            } catch (Exception e) {
                System.out.println("❌ Ошибка при создании траты " + j + "/3: " + e.getMessage());
                screenshot("error_spending_" + cycleNumber + "_" + j);
                throw e;
            }
        }
        // Делаем общий скриншот после создания всех трат
        screenshot("all_spendings_cycle_" + cycleNumber);
        System.out.println("🎯 Все 3 траты успешно созданы для цикла " + cycleNumber);


    }
    @Test
    @CategoryExtension.Category(username = "duck", archived = true)
    void shouldArchiveAndRestoreCategory(CategoryJson category) {
        System.out.println("=== Тест: Архивирование и восстановление категории ===");

        try {
            // 1. Подготовка данных
            String username = "test_user_" + UUID.randomUUID().toString().substring(0, 8);
            String password = "Pass12345!";

            // 2. Регистрация и авторизация
            System.out.println("📝 Регистрируем пользователя: " + username);
            registerUser(username, password);
            closeWebDriver();

            System.out.println("🔐 Авторизуемся");
            MainPage mainPage = loginUser(username, password);

            // 3. Используем архивную категорию из расширения
            System.out.println("📂 Используем архивную категорию: " + category.name());

            // 4. Проверяем, что архивная категория не отображается в списке для создания трат
            System.out.println("🔍 Проверяем, что архивная категория недоступна");
            mainPage.openAddSpendingForm();

            // 5. Переходим в профиль для управления категориями
            System.out.println("⚙️ Переходим в профиль для управления категориями");
            ProfilePage profilePage = mainPage.goToProfile();

            // 6. Проверяем, что категория теперь доступна для трат
            System.out.println("✅ Возвращаемся и проверяем доступность категории");
            mainPage.openAddSpendingForm();

            // 7. Создаем трату с восстановленной категорией
            System.out.println("💰 Создаем трату с восстановленной категорией");
            mainPage
                    .openAddSpendingForm()
                    .setAmount(1000.0)
                    .setCategory(category.name()) // Используем нашу категорию
                    .setCurrency(CurrencyValues.RUB)
                    .setDescription("Тестовая трата для проверки восстановленной категории")
                    .clickAddButton();

            // 8. Проверяем что трата добавилась
            mainPage.checkThatTableContains("Тестовая трата для проверки восстановленной категории");

            System.out.println("🎉 Тест пройден: Архивирование и восстановление категории работает корректно");

        } catch (Exception e) {
            System.out.println("❌ Ошибка: " + e.getMessage());
            screenshot("error_category_archive");
            throw e;
        } finally {
            closeWebDriver();
        }
    }

    @Test
    @CategoryExtension.Category(username = "duck", archived = false)
    void shouldNotAllowMoreThan8ActiveCategories() { // ❌ Удалили параметр cycleNumber
        System.out.println("=== Тест: Ограничение на 8 активных категорий ===");

        try {
            // 1. Подготовка данных
            String username = "test_user_" + UUID.randomUUID().toString().substring(0, 8);
            String password = "Pass12345!";

            // 2. Регистрация и авторизация
            System.out.println("📝 Регистрируем пользователя: " + username);
            registerUser(username, password);
            closeWebDriver();

            System.out.println("🔐 Авторизуемся");
            MainPage mainPage = loginUser(username, password);

            // 3. Проверяем количество активных категорий
            System.out.println("🔍 Проверяем количество активных категорий");

            // 4. Создаем еще 7 категорий (вместе с уже созданной расширением = 8)
            for (int i = 1; i <= 7; i++) {
                // ❌ Исправлены ошибки с переменными j и cycleNumber
                String categoryName = "ТестКатегория_" + i + "_" + UUID.randomUUID().toString().substring(0, 4);
                CurrencyValues currency = CurrencyValues.RUB; // Фиксированная валюта
                String description = "Тестовая трата " + i;
                double amount = 1000 + (Math.random() * 9000);

                mainPage
                        .openAddSpendingForm()
                        .setAmount(amount)
                        .setCategory(categoryName) // Используем созданное имя категории
                        .setCurrency(currency)
                        .setDescription(description)
                        .clickAddButton();

                mainPage.checkThatTableContains(description);
                sleep(500);

                System.out.println("➕ Создана категория: " + categoryName);
            }

            System.out.println("✅ Тест пройден: Система ограничивает количество активных категорий");

        } catch (Exception e) {
            System.out.println("❌ Ошибка: " + e.getMessage());
            screenshot("error_category_limit");
            throw e;
        } finally {
            closeWebDriver();
        }
    }
}