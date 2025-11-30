package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.MainPage;
import guru.qa.niffler.page.RegistrationPage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static com.codeborne.selenide.Selenide.closeWebDriver;
import static org.junit.jupiter.api.Assertions.assertTrue;

@WebTest
class DynamicUserCreationTest {

    private static final Config CFG = Config.getInstance();
    private static final String BASE_PASSWORD = "12345";

    static Stream<Arguments> userDataProvider() {
        return Stream.of(
            Arguments.of("client_1", "Client", "One", "Client One"),
            Arguments.of("client_2", "Client", "Two", "Client Two"),
            Arguments.of("client_3", "Client", "Three", "Client Three"), 
            Arguments.of("client_4", "Client", "Four", "Client Four"),
            Arguments.of("client_5", "Client", "Five", "Client Five")
        );
    }

    @MethodSource("userDataProvider")
    @ParameterizedTest(name = "Create client {0} with name {1} {2}")
    @DisplayName("Создание клиентов с динамическими данными")
    void shouldCreateClientsWithDynamicData(String username, String firstName, 
                                          String lastName, String expectedFullName) {
        String newDescription = "Auto created client " + username;
        
        try {
            // Act - Регистрация
            RegistrationPage registrationPage = Selenide.open(CFG.frontUrl() + "/register", RegistrationPage.class);
            
            registrationPage
                .setUsername(username)
                .setPassword(BASE_PASSWORD)
                .confirmPassword(BASE_PASSWORD)
                .setFirstName(firstName)
                .setLastName(lastName)
                .submit();

            // Assert - Проверка успешного входа
            MainPage mainPage = Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(username, BASE_PASSWORD);
            
            assertTrue(mainPage.isLoaded(), "Main page should be loaded for user: " + username);
            
            // Дополнительные действия с newDescription если нужно
            System.out.println("Created user with description: " + newDescription);

        } finally {
            // Cleanup
            cleanupUserData(username);
        }
    }

    @AfterEach
    void cleanup() {
        closeWebDriver();
    }

    private void cleanupUserData(String username) {
        // Здесь может быть логика очистки данных пользователя
        // Например, через API или прямую работу с БД
        System.out.println("Cleaning up data for user: " + username);
        
        // Если в приложении есть функционал удаления пользователя:
        // userManagementPage.deleteUser(username);
    }
}