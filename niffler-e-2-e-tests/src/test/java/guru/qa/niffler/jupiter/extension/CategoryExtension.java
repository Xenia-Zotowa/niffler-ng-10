package guru.qa.niffler.extension;

import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.service.SpendApiClient;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;
import org.junit.jupiter.api.extension.ExtensionContext.Namespace;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.UUID;

public class CategoryExtension implements BeforeEachCallback, AfterTestExecutionCallback, ParameterResolver {

    public static final Namespace NAMESPACE = Namespace.create(CategoryExtension.class);

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    public @interface Category {
        String username() default "";
        boolean archived() default false;
    }

    private final SpendApiClient spendApiClient;

    public CategoryExtension() {
        this.spendApiClient = new SpendApiClient();
    }

    // 1. МЕТОД из BeforeEachCallback - создание категории перед тестом
    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        AnnotationSupport.findAnnotation(context.getRequiredTestMethod(), Category.class)
                .ifPresent(categoryAnnotation -> {
                    String username = categoryAnnotation.username();
                    boolean archived = categoryAnnotation.archived();

                    String categoryName = "TestCategory_" + UUID.randomUUID().toString().substring(0, 8);
                    CategoryJson category = new CategoryJson(null, categoryName, username, false);

                    try {
                        CategoryJson created = spendApiClient.addCategory(category);

                        if (archived) {
                            CategoryJson archivedCategory = new CategoryJson(
                                    created.id(), created.name(), created.username(), true
                            );
                            created = spendApiClient.updateCategory(archivedCategory);
                        }

                        context.getStore(NAMESPACE).put(context.getUniqueId(), created);
                    } catch (Exception e) {
                        throw new RuntimeException("Failed to create category for test", e);
                    }
                });
    }

    // 2. МЕТОД из AfterTestExecutionCallback - архивация после теста (ОБЯЗАТЕЛЬНЫЙ!)
    @Override
    public void afterTestExecution(ExtensionContext context) throws Exception {
        CategoryJson category = context.getStore(NAMESPACE).get(context.getUniqueId(), CategoryJson.class);

        if (category != null && !category.archived()) {
            try {
                CategoryJson archivedCategory = new CategoryJson(
                        category.id(), category.name(), category.username(), true
                );
                spendApiClient.updateCategory(archivedCategory);
            } catch (Exception e) {
                System.err.println("Failed to archive category after test: " + e.getMessage());
            }
        }
        context.getStore(NAMESPACE).remove(context.getUniqueId());
    }

    // 3. МЕТОД из ParameterResolver - проверка типа параметра (ОБЯЗАТЕЛЬНЫЙ!)
    @Override
    public boolean supportsParameter(ParameterContext parameterContext,
                                     ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().equals(CategoryJson.class);
    }

    // 4. МЕТОД из ParameterResolver - получение значения параметра (ОБЯЗАТЕЛЬНЫЙ!)
    @Override
    public Object resolveParameter(ParameterContext parameterContext,
                                   ExtensionContext extensionContext) throws ParameterResolutionException {
        return extensionContext.getStore(NAMESPACE).get(extensionContext.getUniqueId(), CategoryJson.class);
    }
}