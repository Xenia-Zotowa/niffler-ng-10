package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.page;

public class RegisterPage {

    private final SelenideElement usernameInput = $("input[name='username']");
    private final SelenideElement passwordInput = $("input[name='password']");
    private final SelenideElement passwordSubmitInput = $("input[name='passwordSubmit']");
    private final SelenideElement submitButton = $("button[type='submit']");
    private final SelenideElement successAlert = $("div.alert-success");
    private final SelenideElement errorAlert = $("div.alert-danger");
    private final SelenideElement loginLink = $("a[href*='/login']");
    private final SelenideElement registerForm = $("form");
    private final SelenideElement signIn = $(".form_sign-in");

    public RegisterPage registerUser(String username, String password, String passwordSubmit) {
        usernameInput.setValue(username);
        passwordInput.setValue(password);
        passwordSubmitInput.setValue(passwordSubmit);
        submitButton.click();
        signIn.click();
        return this;

    }

    public RegisterPage checkRegistrationSuccess() {
        // Пробуем разные способы проверки успешной регистрации

        // Способ 1: Проверяем что мы перешли на страницу логина
        loginLink.shouldBe(visible).shouldHave(text("Login"));

        // Способ 2: Проверяем что форма регистрации исчезла
        registerForm.shouldNotBe(visible);

        // Способ 3: Пробуем найти алерт успеха (если есть)
        try {
            if (successAlert.exists()) {
                successAlert.shouldBe(visible).shouldHave(text("success"));
            }
        } catch (Exception e) {
            // Игнорируем если алерта нет
            System.out.println("Alert успеха не найден, но это нормально");
        }

        // Способ 4: Проверяем что нет алерта ошибки
        if (errorAlert.exists()) {
            String errorText = errorAlert.getText();
            throw new AssertionError("Регистрация не удалась: " + errorText);
        }

        return this;
    }


    public LoginPage returnToLogin() {
        loginLink.click();
        return page(LoginPage.class);
    }
}