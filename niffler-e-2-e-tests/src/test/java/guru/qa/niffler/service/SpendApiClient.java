package guru.qa.niffler.service;

import guru.qa.niffler.api.SpendApi;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;
import lombok.SneakyThrows;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class SpendApiClient implements SpendClient {

  private static final Config CFG = Config.getInstance();

  private final Retrofit retrofit = new Retrofit.Builder()
          .baseUrl(CFG.spendUrl())
          .addConverterFactory(JacksonConverterFactory.create())
          .build();

  private final SpendApi spendApi = retrofit.create(SpendApi.class);

  // ==================== SPENDS (Траты) ====================

  @SneakyThrows
  @Override
  public SpendJson addSpend(SpendJson spend) {
    return spendApi.createSpend(spend)
            .execute()
            .body();
  }

  @SneakyThrows
  @Override
  public SpendJson editSpend(SpendJson spend) {
    return spendApi.editSpend(spend)
            .execute()
            .body();
  }

  @SneakyThrows
  @Override
  public SpendJson getSpend(String id) {
    return spendApi.getSpend(id)
            .execute()
            .body();
  }

  @SneakyThrows
  @Override
  public List<SpendJson> getAllSpends(String username) {
    return spendApi.getAllSpends(username)
            .execute()
            .body();
  }

  @SneakyThrows
  @Override
  public void removeSpend(String id) {
    spendApi.removeSpend(id)
            .execute();
  }

  // ==================== CATEGORIES (Категории) ====================

  @SneakyThrows
  @Override
  public CategoryJson addCategory(CategoryJson category) {
    return spendApi.addCategory(category)
            .execute()
            .body();
  }

  @SneakyThrows
  @Override
  public CategoryJson updateCategory(CategoryJson category) {
    return spendApi.updateCategory(category)
            .execute()
            .body();
  }

  @SneakyThrows
  @Override
  public List<CategoryJson> getAllCategories(String username) {
    return spendApi.getAllCategories(username)
            .execute()
            .body();
  }

  @SneakyThrows
  @Override
  public Optional<CategoryJson> findCategoryByNameAndUsername(String categoryName, String username) {
    List<CategoryJson> categories = getAllCategories(username);
    return categories.stream()
            .filter(c -> c.name().equals(categoryName))
            .findFirst();
  }

  // ==================== FILTERED SPENDS (Фильтрованные траты) ====================

  @SneakyThrows
  @Override
  public List<SpendJson> getSpendsByCategory(String username, String category) {
    return spendApi.getSpendsByCategory(username, category)
            .execute()
            .body();
  }

  @SneakyThrows
  @Override
  public List<SpendJson> getSpendsByPeriod(String username, String fromDate, String toDate) {
    return spendApi.getSpendsByPeriod(username, fromDate, toDate)
            .execute()
            .body();
  }

  @SneakyThrows
  @Override
  public List<SpendJson> getSpendsByCategoryAndPeriod(String username, String category, String fromDate, String toDate) {
    return spendApi.getSpendsByCategoryAndPeriod(username, category, fromDate, toDate)
            .execute()
            .body();
  }

  // ==================== STATISTICS (Статистика) ====================

  @SneakyThrows
  @Override
  public Double getTotalSpends(String username) {
    return spendApi.getTotalSpends(username)
            .execute()
            .body();
  }

  @SneakyThrows
  @Override
  public Double getTotalSpendsByCategory(String username, String category) {
    return spendApi.getTotalSpendsByCategory(username, category)
            .execute()
            .body();
  }

  @SneakyThrows
  @Override
  public Map<String, Double> getCategoryDistribution(String username) {
    return spendApi.getCategoryDistribution(username)
            .execute()
            .body();
  }

  // ==================== CURRENCY (Валюты) ====================

  @SneakyThrows
  @Override
  public Map<String, Double> getCurrencyRates() {
    return spendApi.getCurrencyRates()
            .execute()
            .body();
  }

  @SneakyThrows
  @Override
  public Double convertCurrency(Double amount, String fromCurrency, String toCurrency) {
    return spendApi.convertCurrency(amount, fromCurrency, toCurrency)
            .execute()
            .body();
  }
}