package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.model.CurrencyValues;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

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
  private final SelenideElement menuButton = $("button[aria-label=\"Menu\"]");

//  a.MuiButtonBase-root.MuiButton-contained[href="/spending"]
  public MainPage checkThatPageLoaded() {
    spendingTable.should(visible);
    return this;
  }

  public EditSpendingPage editSpending(String description) {
    spendingTable.$$("tbody tr").find(text(description)).$$("td").get(5).click();
    return new EditSpendingPage();
  }

  public MainPage checkThatTableContains(String description) {
    spendingTable.$$("tbody tr").find(text(description)).should(visible);
    return this;
  }

  public MainPage openAddSpendingForm() {
    addSpendingButton.click();
    return this;
  }

  public MainPage setAmount(double amount) {
    amountInput.setValue(String.valueOf(amount));
    return this;
  }

  public MainPage setCategory(String category) {
    categoryInput.setValue(category);
    return this;
  }
  public MainPage createCategory(String categoryName) {
    $("button[data-test='create-category']").click();
    $("input[name='categoryName']").setValue(categoryName);
    $("button[data-test='save-category']").click();
    return this;
  }

  public MainPage setCurrency(CurrencyValues currency) {
    System.out.println("💰 Выбираем валюту: " + currency);

    // Открываем выпадающий список
    currencySelect.click();

    // Ждем пока список откроется
    listbox.shouldBe(visible);

    // Выбираем валюту по data-value
    $("li[data-value='" + currency.name() + "']").click();

    // Ждем пока список закроется
    listbox.shouldNotBe(visible);

    return this;
  }


  public MainPage setDescription(String description) {
    descriptionInput.setValue(description);
    return this;
  }

  public MainPage clickAddButton() {
    addButton.click();
    return this;
  }

  public void logout() {
    // реализация выхода
  }

  public MainPage setNewSpendingDescription(String newDescription) {
    // реализация установки нового описания
    return this;
  }


  public ProfilePage goToProfile() {
    menuButton.click();
    profileLink.click();
    return new ProfilePage();
  }

  public MainPage save() {
    // реализация сохранения
    return this;
  }
}
