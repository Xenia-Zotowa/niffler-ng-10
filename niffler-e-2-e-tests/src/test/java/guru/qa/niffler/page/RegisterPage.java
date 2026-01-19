package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

public class RegisterPage {

    private final SelenideElement usernameInput = $("#username");
    private final SelenideElement passwordInput = $("input[name='password']");
    private final SelenideElement passwordSubmitInput = $("input[name='passwordSubmit']");
    private final SelenideElement submitButton = $("button[type='submit']");
    private final SelenideElement successAlert = $("div.alert-success");
    private final SelenideElement errorAlert = $("div.alert-danger");
    private final SelenideElement loginLink = $("a[href*='/login']");
    private final SelenideElement signIn = $(".form_sign-in");
    private final SelenideElement errorMassage = $(".form__error");
    private final SelenideElement formParagraphSuccess =  $("[class*='form__paragraph'][class*='form__paragraph_success']");

    @Step("Регистрация пользователя")
    public RegisterPage registerUser(String username, String password, String passwordSubmit) {
        usernameInput.setValue(username);
        passwordInput.setValue(password);
        passwordSubmitInput.setValue(passwordSubmit);
        submitButton.click();

        return this;

    }

    @Step("Не зарегистрированный пользователь")
    public RegisterPage notRegisterUser(String username, String password, String passwordSubmit) {
        usernameInput.setValue(username);
        passwordInput.setValue(password);
        passwordSubmitInput.setValue(passwordSubmit);
        submitButton.click();
        return this;

    }

    @Step("Проверка успешной регистрации")
    public RegisterPage checkRegistrationSuccess() {

        sleep(50);
        formParagraphSuccess.shouldBe(visible)
                .shouldHave(text("Congratulations! You've registered!"));
        signIn.click();
        return this;
    }

    @Step("Возврат к странице регистрации")
    public LoginPage returnToLogin() {
        loginLink.click();
        return page(LoginPage.class);
    }


    @Step("Проверяем что появилось сообщение об ошибке")
    public RegisterPage checkingForAnError() {
        errorMassage.shouldBe(visible)
                .shouldHave(text("already exists"));
        return this;

    }


    @Step("Дополнительная проверка на то, что мы остались на странице регистрации")
    public RegisterPage checkingPageRegister() {
        usernameInput.shouldBe(visible);
        passwordInput.shouldBe(visible);
        passwordSubmitInput.shouldBe(visible);
        return this;

    }

    @Step("Проверяем что появилось сообщение об ошибке о несовпадении пароля")
    public RegisterPage passwordMismatchCheck() {
        errorMassage.shouldBe(visible)
                .shouldHave(text("Passwords should be equal"));
        return this;

    }

}