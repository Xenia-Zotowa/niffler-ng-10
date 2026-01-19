package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.page.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import guru.qa.niffler.extension.CategoryExtension;
import java.util.UUID;
import static com.codeborne.selenide.Selenide.closeWebDriver;
import static com.codeborne.selenide.Selenide.webdriver;

@WebTest
@ExtendWith(CategoryExtension.class)
public class LoginTest {


    private static final Config CFG = Config.getInstance();
    private static final LoginPage loginPage = Selenide.open(CFG.frontUrl(), LoginPage.class);
    private static final MainPage mainPage = Selenide.open(CFG.frontUrl(), MainPage.class);
    private static final ProfilePage profilePage = Selenide.open(CFG.frontUrl(), ProfilePage.class);
    private static final EditSpendingPage editSpendingPage = Selenide.open(CFG.frontUrl(), EditSpendingPage.class);
    private static final RegisterPage registerPage = Selenide.open(CFG.frontUrl(), RegisterPage.class);



    @Test
    @DisplayName("Тест: Регистрация с существующим именем пользователя")
    void shouldNotRegisterUserWithExistingUsername() {

        String EXISTING_USERNAME = "existing_user_" + UUID.randomUUID().toString().substring(0, 8);
        String EXISTING_PASSWORD = "Pass12345!";


        System.out.println("Регистрируем первого пользователя: " + EXISTING_USERNAME);
        loginPage.clickRegister();

        registerPage.registerUser(EXISTING_USERNAME, EXISTING_PASSWORD, EXISTING_PASSWORD);
        System.out.println("Первый пользователь успешно зарегистрирован");

        closeWebDriver();

        System.out.println("Пытаемся зарегистрировать пользователя с существующим именем: " + EXISTING_USERNAME);
        loginPage.clickRegister();

        registerPage.notRegisterUser(EXISTING_USERNAME, "Different3!", "Different3!");

        registerPage.checkingForAnError()
                .checkingPageRegister();

        System.out.println(" Тест пройден: Система правильно отклонила регистрацию с существующим именем");

        closeWebDriver();

    }

    @Test
    @DisplayName("Тест: Пароль и подтверждение пароля не совпадают")
    void shouldShowErrorIfPasswordAndConfirmPasswordAreNotEqual() {
        String randomUsername = "user_" + UUID.randomUUID().toString().substring(0, 8);
        String password = "Pass12345!";
        String wrongConfirmPassword = "WrongPass123!";

        System.out.println("Регистрируем пользователя с несовпадающими паролями");


        loginPage.clickRegister();

        registerPage.notRegisterUser(randomUsername, password, wrongConfirmPassword);

        registerPage.passwordMismatchCheck();

        System.out.println("Тест пройден: Система правильно показала ошибку при несовпадающих паролях");

        closeWebDriver();

    }

    @Test
    @DisplayName("Тест: Главная страница отображается после успешного входа")
    void mainPageShouldBeDisplayedAfterSuccessLogin() {

        String randomUsername = "user_" + UUID.randomUUID().toString().substring(0, 8);
        String password = "Pass12345!";

        loginPage.registerUser(randomUsername, password);
        closeWebDriver();
        System.out.println("Выполняем вход с правильными учетными данными");

        mainPage.loginUser(randomUsername, password);

        mainPage.checkThatPageLoaded()
                .CheckingThePresenceOfTheHeaderAndTheButtonForAddingExpenses();

        System.out.println("Тест пройден: Главная страница успешно отображается после входа");

        closeWebDriver();
    }

    @Test
    @DisplayName("Тест: Остаемся на странице логина при неправильных учетных данных")
    void userShouldStayOnLoginPageAfterLoginWithBadCredentials() {
        String wrongUsername = "nonexistent_user_" + UUID.randomUUID().toString().substring(0, 8);
        String wrongPassword = "WrongPass123!";

        System.out.println(" Пытаемся войти с неправильными учетными данными");

        loginPage.login(wrongUsername, wrongPassword);

        loginPage.CheckingThatYouAreStillOnTheLoginPage()
                .WeAreCheckingThatALoginErrorMessageHasAppeared();

        String currentUrl = webdriver().driver().url();
        if (!currentUrl.contains("login") && !currentUrl.contains("auth")) {
            throw new AssertionError("Пользователь не остался на странице логина. Текущий URL: " + currentUrl);
        }

        System.out.println(" Тест пройден: Пользователь остался на странице логина при неправильных учетных данных");

        closeWebDriver();
    }
}