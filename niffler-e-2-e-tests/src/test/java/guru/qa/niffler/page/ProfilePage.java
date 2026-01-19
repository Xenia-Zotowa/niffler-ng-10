package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;

public class ProfilePage {

    private final SelenideElement menuButton = $("button[aria-label=\"Menu\"]");
    private final SelenideElement profileLink = $("a[href*='/profile']");
    private final SelenideElement firstNameInput = $("#name, [name='name']");
    private final SelenideElement saveButton = $("button[type='submit'], .save-profile");
    private final SelenideElement successMessage = $(".alert-success, .success-message");
    private final SelenideElement mainPageLink = $("a[href*='/main']");


    @Step("Установить имя пользователя")
    public ProfilePage setFirstName(String firstName) {
        firstNameInput.setValue(firstName);
        return this;
    }


    @Step("Переход в профиль")
    public ProfilePage goToProfile() {
        menuButton.click();
        profileLink.click();
        return new ProfilePage();
    }

    @Step("ЗАПОЛНЕНИЕ ПРОФИЛЯ ПОЛЬЗОВАТЕЛЯ")
    public ProfilePage fillUserProfile(ProfilePage profilePage, String firstName, String lastName, int cycleNumber) {
        System.out.println("👤 Заполняем профиль: " + firstName + " " + lastName);
        profilePage
                .setFirstName(firstName)
                .saveProfile();

        screenshot("profile_cycle_" + cycleNumber);

        System.out.println(" Профиль успешно заполнен: " + firstName + " " + lastName);

        return profilePage;
    }

    @Step("Сохраняем профиль")
    public ProfilePage saveProfile() {
        saveButton.click();
        return this;
    }

    @Step("Переход в категорию")
    public ProfilePage goToCategories() {
        $("a[href='/categories']").click();
        return this;
    }

    @Step("Находим категорию и нажимаем кнопку архивации")
    public ProfilePage archiveCategory(String categoryName) {
        $$("tr").findBy(text(categoryName))
                .$("button[data-test='archive-category']").click();
        return this;
    }

    @Step("Находим архивную категорию и нажимаем кнопку восстановления")
    public ProfilePage restoreCategory(String categoryName) {
        $$("tr").findBy(text(categoryName))
                .$("button[data-test='restore-category']").click();
        return this;
    }

    @Step("Проверяем, что категория помечена как архивная")
    public ProfilePage checkCategoryArchived(String categoryName) {
        $$("tr").findBy(text(categoryName))
                .$("span[data-test='archived-badge']").shouldBe(visible);
        return this;
    }

    @Step("Проверяем, что категория '{categoryName}' активна (не имеет архивного значка)")
    public ProfilePage checkCategoryActive(String categoryName) {
        $$("tr").findBy(text(categoryName))
                .$("span[data-test='archived-badge']").shouldNotBe(visible);
        return this;
    }


}