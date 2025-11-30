package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.MainPage;
import guru.qa.niffler.page.RegisterPage;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static com.codeborne.selenide.Selenide.*;

@WebTest
public class SpendingTest {

    private static final Config CFG = Config.getInstance();

    @Test
    void registerLoginAndAddSpendingForThreeRandomUsers() {
        // Повторяем 3 раза для рандомных пользователей
        for (int i = 1; i <= 3; i++) {
            String randomUsername = "user_" + UUID.randomUUID().toString().substring(0, 8);
            String randomPassword = "Pass12345!";

            System.out.println("=== ЦИКЛ " + i + ": Работа с пользователем " + randomUsername + " ===");

            try {
                // 1. РЕГИСТРАЦИЯ ПОЛЬЗОВАТЕЛЯ
                System.out.println("📝 Шаг 1: Регистрация пользователя...");
                registerUser(randomUsername, randomPassword);


                //2. Авторизация пользователя
                System.out.println("🔐 Шаг 2: Авторизация пользователя...");
                MainPage mainPage = new LoginPage().login(randomUsername, randomPassword);


                // 3. ВНЕСЕНИЕ ТРАТЫ
                System.out.println("💰 Шаг 3: Внесение траты...");
                addThreeSpendings(mainPage, i);
sleep(9000);
                // 4. ВЫХОД
                System.out.println("🚪 Шаг 4: Выход из системы...");
                mainPage.logout();

                System.out.println("✅ ЦИКЛ " + i + " УСПЕШНО ЗАВЕРШЕН для пользователя: " + randomUsername + "\n");

            } catch (Exception e) {
                System.out.println("❌ Ошибка в цикле " + i + " для пользователя " + randomUsername + ": " + e.getMessage());
                screenshot("error_cycle_" + i);
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
     * 3. ВНЕСЕНИЕ ТРАТЫ
     */
    private void addThreeSpendings(MainPage mainPage, int cycleNumber) {
        // Массивы с разными данными для трат
        String[] categories = {"Учеба", "Образование", "Курсы"};
        CurrencyValues[] currencies = {CurrencyValues.RUB, CurrencyValues.USD, CurrencyValues.EUR};
        String[] descriptions = {
                "Обучение Niffler 2.0 юбилейный поток!",
                "Курсы по автоматизации тестирования",
                "Мастер-класс по Selenium WebDriver"
        };

        // Создаем 3 траты
        for (int j = 1; j <= 3; j++) {
            try {
                // Данные для траты
                String category = categories[j - 1]; // Берем по порядку из массива
                CurrencyValues currency = currencies[j - 1];
                String description = descriptions[j - 1] + " (Цикл: " + cycleNumber + ", Трата: " + j + ")";
                double amount = 5000 + (Math.random() * 45000); // Сумма от 5000 до 50000

                System.out.println("💸 Создаем трату " + j + "/3: " + (int) amount + " " + currency + " - " + description);

                // Создаем трату через интерфейс
                mainPage
                        .openAddSpendingForm()
                        .setAmount(amount)
                        .setCategory(category)
                        .setCurrency(currency)
                        .setDescription(description)
                        .clickAddButton();

                // Проверяем что трата добавилась в таблицу
                mainPage.checkThatTableContains(description);

                // Небольшая пауза между тратами для стабильности
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
}