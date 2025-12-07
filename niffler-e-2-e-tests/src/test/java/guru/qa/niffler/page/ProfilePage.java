package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.model.CurrencyValues;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class ProfilePage {

    private final SelenideElement menuButton = $("button[aria-label=\"Menu\"]");
    private final SelenideElement profileLink = $("a[href*='/profile']");
    private final SelenideElement firstNameInput = $("#name, [name='name']");
    private final SelenideElement saveButton = $("button[type='submit'], .save-profile");
    private final SelenideElement successMessage = $(".alert-success, .success-message");
    private final SelenideElement mainPageLink = $("a[href*='/main']");


    public ProfilePage setFirstName(String firstName) {
        firstNameInput.setValue(firstName);
        return this;
    }


    public ProfilePage saveProfile() {
        saveButton.click();
        return this;
    }

    public ProfilePage goToCategories() {
        $("a[href='/categories']").click();
        return this;
    }

    public ProfilePage archiveCategory(String categoryName) {
        // Находим категорию и нажимаем кнопку архивации
        $$("tr").findBy(text(categoryName))
                .$("button[data-test='archive-category']").click();
        return this;
    }

    public ProfilePage restoreCategory(String categoryName) {
        // Находим архивную категорию и нажимаем кнопку восстановления
        $$("tr").findBy(text(categoryName))
                .$("button[data-test='restore-category']").click();
        return this;
    }

    public ProfilePage checkCategoryArchived(String categoryName) {
        // Проверяем, что категория помечена как архивная
        $$("tr").findBy(text(categoryName))
                .$("span[data-test='archived-badge']").shouldBe(visible);
        return this;
    }

    public ProfilePage checkCategoryActive(String categoryName) {
        // Проверяем, что категория активна (нет значка архивной)
        $$("tr").findBy(text(categoryName))
                .$("span[data-test='archived-badge']").shouldNotBe(visible);
        return this;
    }

//    public ProfilePage checkProfileSaved() {
//        successMessage.shouldBe(visible).shouldHave(text("success"));
//        return this;
//    }
//
//    public MainPage returnToMainPage() {
//        mainPageLink.click();
//        return new MainPage();
//    }
}