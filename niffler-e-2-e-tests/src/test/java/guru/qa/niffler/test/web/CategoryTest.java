package guru.qa.niffler.test.web;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.extension.CategoryExtension;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.page.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.UUID;

import static com.codeborne.selenide.Selenide.*;

@WebTest
@ExtendWith(CategoryExtension.class)
public class CategoryTest {

    private static final Config CFG = Config.getInstance();

    @Test
    @CategoryExtension.Category(username = "test_user", archived = true)
    void shouldArchiveCategoryInUI(CategoryJson category) {
        // Тест для проверки архивации через UI
        // ... реализация теста ...
    }

    @Test
    @CategoryExtension.Category(username = "test_user", archived = false)
    void shouldCreateAndArchiveMultipleCategories(CategoryJson category) {
        // Тест для проверки создания и архивации нескольких категорий
        // ... реализация теста ...
    }
}