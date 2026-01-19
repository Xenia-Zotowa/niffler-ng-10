package guru.qa.niffler.page;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.config.Config;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;

public class LoginPage {
  private final SelenideElement usernameInput = $("#username");
  private final SelenideElement passwordInput = $("#password");
  private final SelenideElement submitBtn = $("#login-button");
  private final SelenideElement registerButton = $("a[href*='/register']");
  private final SelenideElement formErrorContainer = $(".form__error-container");

  private static final Config CFG = Config.getInstance();


  @Step("РЕГИСТРАЦИЯ ПОЛЬЗОВАТЕЛЯ")
  public MainPage registerUser(String username, String password) {
    RegisterPage registerPage = Selenide.open(CFG.frontUrl(), LoginPage.class)
            .clickRegister();

    registerPage.registerUser(username, password, password);

    System.out.println(" Пользователь " + username + " успешно зарегистрирован");
    return new MainPage();
  }



  @Step("Регистрация")
  public LoginPage login(String username, String password) {
    usernameInput.val(username);
    passwordInput.val(password);
    submitBtn.click();
    return new LoginPage();
  }

  @Step("Нажать на кнопку регистрации")
  public RegisterPage clickRegister() {
    registerButton.click();
    return page(RegisterPage.class);

  }

  @Step("Проверяем что мы остались на странице логина")
  public LoginPage CheckingThatYouAreStillOnTheLoginPage() {
    usernameInput.shouldBe(visible);
    passwordInput.shouldBe(visible);
    submitBtn.shouldBe(visible);
    return this;

  }

  @Step("Проверяем что появилось сообщение об ошибке ввода логина/пароля")
  public LoginPage WeAreCheckingThatALoginErrorMessageHasAppeared() {
    formErrorContainer.shouldBe(visible)
                    .shouldHave(text("Неверные учетные данные пользователя"));
    return this;

  }

}