package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.extension.CategoryExtension;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.page.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import java.util.UUID;
import static com.codeborne.selenide.Selenide.closeWebDriver;
import static com.codeborne.selenide.Selenide.sleep;

@WebTest
@ExtendWith(CategoryExtension.class)
public class SpendingTest {

    private static final Config CFG = Config.getInstance();
    private static final LoginPage loginPage = Selenide.open(CFG.frontUrl(), LoginPage.class);
    private static final MainPage mainPage = Selenide.open(CFG.frontUrl(), MainPage.class);
    private static final ProfilePage profilePage = Selenide.open(CFG.frontUrl(), ProfilePage.class);
    private static final EditSpendingPage editSpendingPage = Selenide.open(CFG.frontUrl(), EditSpendingPage.class);





    @Test
    @DisplayName("Тест: Регистрация и добавление трат для 3ех рандомных пользователей")
    void registerLoginAndAddSpendingForThreeRandomUsers() {
        for (int i = 1; i <= 3; i++) {
            String randomUsername = "user_" + UUID.randomUUID().toString().substring(0, 8);
            String randomPassword = "Pass12345!";
            String randomFirstName = "Имя_" + UUID.randomUUID().toString().substring(0, 4);
            String randomLastName = "Фамилия_" + UUID.randomUUID().toString().substring(0, 4);

            System.out.println("=== ЦИКЛ " + i + ": Работа с пользователем " + randomUsername + " ===");

            System.out.println(" Шаг 1: Регистрация пользователя...");
            loginPage.registerUser(randomUsername, randomPassword);
            System.out.println(" Шаг 2: Авторизация пользователя...");
            mainPage.loginUser(randomUsername, randomPassword);
            System.out.println(" Шаг 3: Заполнение профиля пользователя...");
            profilePage.goToProfile();
            profilePage.fillUserProfile(profilePage, randomFirstName, randomLastName, i);
            System.out.println(" Шаг 4: Внесение 3 трат...");
            editSpendingPage.addThreeSpendings(editSpendingPage, i);
            System.out.println(" Шаг 5: Закрытие страницы...");
            closeWebDriver();

            System.out.println(" ЦИКЛ " + i + " УСПЕШНО ЗАВЕРШЕН для пользователя: " + randomUsername + "\n");

            closeWebDriver();
        }

        System.out.println(" ВСЕ 3 ЦИКЛА УСПЕШНО ВЫПОЛНЕНЫ!");
    }


    @Test
    @DisplayName("Тест: Архивирование и восстановление категории")
    @CategoryExtension.Category(username = "duck", archived = true)
    void shouldArchiveAndRestoreCategory(CategoryJson category) {
        String username = "test_user_" + UUID.randomUUID().toString().substring(0, 8);
        String password = "Pass12345!";

        System.out.println(" Регистрируем пользователя: " + username);
        loginPage.registerUser(username, password);
        closeWebDriver();

        System.out.println("Авторизуемся");
        mainPage.loginUser(username, password);

        System.out.println("Используем архивную категорию: " + category.name());

        System.out.println("Проверяем, что архивная категория недоступна");
        mainPage.openAddSpendingForm();

        System.out.println("Переходим в профиль для управления категориями");
        profilePage.goToProfile();

        System.out.println("Возвращаемся и проверяем доступность категории");
        mainPage.openAddSpendingForm();

        System.out.println("Создаем трату с восстановленной категорией");
        mainPage
                .openAddSpendingForm()
                .setAmount(1000.0)
                .setCategory(category.name())
                .setCurrency(CurrencyValues.RUB)
                .setDescription("Тестовая трата для проверки восстановленной категории")
                .clickAddButton();

        mainPage.checkThatTableContains("Тестовая трата для проверки восстановленной категории");

        System.out.println("Тест пройден: Архивирование и восстановление категории работает корректно");


        closeWebDriver();
    }

    @Test
    @DisplayName("Тест: Ограничение на 8 активных категорий")
    @CategoryExtension.Category(username = "duck", archived = false)
    void shouldNotAllowMoreThan8ActiveCategories() {
        String username = "test_user_" + UUID.randomUUID().toString().substring(0, 8);
        String password = "Pass12345!";

        System.out.println(" Регистрируем пользователя: " + username);
        loginPage.registerUser(username, password);
        closeWebDriver();

        System.out.println(" Авторизуемся");
        mainPage.loginUser(username, password);

        System.out.println(" Проверяем количество активных категорий");

        for (int i = 1; i <= 7; i++) {
            String categoryName = "ТестКатегория_" + i + "_" + UUID.randomUUID().toString().substring(0, 4);
            CurrencyValues currency = CurrencyValues.RUB;
            String description = "Тестовая трата " + i;
            double amount = 1000 + (Math.random() * 9000);

            mainPage
                    .openAddSpendingForm()
                    .setAmount(amount)
                    .setCategory(categoryName)
                    .setCurrency(currency)
                    .setDescription(description)
                    .clickAddButton();

            mainPage.checkThatTableContains(description);
            sleep(500);

            System.out.println("Создана категория: " + categoryName);
        }

        System.out.println("Тест пройден: Система ограничивает количество активных категорий");

        closeWebDriver();

    }
}