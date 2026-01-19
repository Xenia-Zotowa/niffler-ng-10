package guru.qa.niffler.page;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.model.CurrencyValues;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.sleep;

public class MainPage {
    private final SelenideElement spendingTable = $("#spendings");
    private final SelenideElement addSpendingButton = $("a[href='/spending']");
    private final SelenideElement amountInput = $("#amount");
    private final SelenideElement categoryInput = $("#category");
    private final SelenideElement currencySelect = $("#currency");
    private final SelenideElement listbox = $("ul[role='listbox']");
    private final SelenideElement descriptionInput = $("#description");
    private final SelenideElement addButton = $("#save");
    private final SelenideElement profileLink = $("a[href*='/profile']");
    private final SelenideElement pageTitle = $("h1, .page-title");
//    private final SelenideElement menuButton = $("button[aria-label=\"Menu\"]");

    private static final Config CFG = Config.getInstance();



    @Step("Проверка загрузки страницы")
    public MainPage checkThatPageLoaded() {
        spendingTable.should(visible);
        return this;
    }

    @Step("АВТОРИЗАЦИЯ ПОЛЬЗОВАТЕЛЯ")
    public MainPage loginUser(String username, String password) {
        LoginPage loginPage = Selenide.open(CFG.frontUrl(), LoginPage.class);
        loginPage.login(username, password);
sleep(500);
        checkThatPageLoaded();

        System.out.println(" Пользователь " + username + " успешно авторизован");
        return this;
    }

    @Step("Редактирование расходов")
    public EditSpendingPage editSpending(String description) {
        spendingTable.$$("tbody tr").find(text(description)).$$("td").get(5).click();
        return new EditSpendingPage();
    }

    @Step("Проверка содержимого таблицы")
    public MainPage checkThatTableContains(String description) {
        spendingTable.$$("tbody tr").find(text(description)).should(visible);
        return this;
    }

    @Step("Открытие формы трат")
    public MainPage openAddSpendingForm() {
        addSpendingButton.click();
        return this;
    }

    @Step("Ввод суммы расходов")
    public MainPage setAmount(double amount) {
        amountInput.setValue(String.valueOf(amount));
        return this;
    }

    @Step("Ввод категории расходов")
    public MainPage setCategory(String category) {
        categoryInput.setValue(category);
        return this;
    }

    @Step("Создать категорию")
    public MainPage createCategory(String categoryName) {
        $("button[data-test='create-category']").click();
        $("input[name='categoryName']").setValue(categoryName);
        $("button[data-test='save-category']").click();
        return this;
    }

    @Step("Выбираем валюту")
    public MainPage setCurrency(CurrencyValues currency) {
        currencySelect.click();
        listbox.shouldBe(visible);
        $("li[data-value='" + currency.name() + "']").click();
        listbox.shouldNotBe(visible);
        return this;
    }

    @Step("Ввести описание")
    public MainPage setDescription(String description) {
        descriptionInput.setValue(description);
        return this;
    }

    @Step("Нажать на кнопку сохранить")
    public MainPage clickAddButton() {
        addButton.click();
        return this;
    }



    @Step("Проверка наличия заголовка и кнопки добавления трат")
    public ProfilePage CheckingThePresenceOfTheHeaderAndTheButtonForAddingExpenses() {
        pageTitle.shouldBe(visible);
        addSpendingButton.shouldBe(visible);
        return new ProfilePage();
    }

}
