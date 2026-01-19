package guru.qa.niffler.page;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.model.CurrencyValues;
import io.qameta.allure.Step;

import java.util.UUID;

import static com.codeborne.selenide.Selenide.*;

public class EditSpendingPage {
    private static final Config CFG = Config.getInstance();

    private final SelenideElement descriptionInput = $("#description");
    private final SelenideElement saveBtn = $("#save");

    public EditSpendingPage setNewSpendingDescription(String description) {
        descriptionInput.val(description);
        saveBtn.click();
        return this;
    }

    public MainPage save() {
        saveBtn.click();
        return new MainPage();
    }

    @Step("ВНЕСЕНИЕ 3 ТРАТ")
    public EditSpendingPage addThreeSpendings(EditSpendingPage editSpendingPage, int cycleNumber) {
        for (int j = 1; j <= 3; j++) {
            String category = "Категория_" + cycleNumber + "_" + j + "_" +
                    UUID.randomUUID().toString().substring(0, 4);
            MainPage mainPage = Selenide.open(CFG.frontUrl(), MainPage.class);


            CurrencyValues currency = CurrencyValues.values()[j - 1]; // RUB, USD, EUR
            String description = "Трата цикла " + cycleNumber + " номер " + j;
            double amount = 1000 + (Math.random() * 9000);

            System.out.println(" Создаем трату " + j + "/3 в категории: " + category);

            mainPage
                    .openAddSpendingForm()
                    .setAmount(amount)
                    .setCategory(category)
                    .setCurrency(currency)
                    .setDescription(description)
                    .clickAddButton();

            mainPage.checkThatTableContains(description);
            sleep(500);

            System.out.println(" Трата " + j + "/3 успешно создана");


        }
        screenshot("all_spendings_cycle_" + cycleNumber);
        System.out.println(" Все 3 траты успешно созданы для цикла " + cycleNumber);

        return this;
    }
}
