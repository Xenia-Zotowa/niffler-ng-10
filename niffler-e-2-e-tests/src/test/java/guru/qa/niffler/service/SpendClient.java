package guru.qa.niffler.service;

import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface SpendClient {

// ==================== SPENDS (Траты) ====================

  SpendJson addSpend(SpendJson spend);
  CategoryJson addCategory(CategoryJson category);

  SpendJson editSpend(SpendJson spend);

  SpendJson getSpend(String id);

  List<SpendJson> getAllSpends(String username);

  void removeSpend(String id);

  // ==================== CATEGORIES (Категории) ====================

//  CategoryJson addCategory(CategoryJson category);

  CategoryJson updateCategory(CategoryJson category);

  List<CategoryJson> getAllCategories(String username);

  Optional<CategoryJson> findCategoryByNameAndUsername(String categoryName, String username);

  // ==================== FILTERED SPENDS (Фильтрованные траты) ====================

  List<SpendJson> getSpendsByCategory(String username, String category);

  List<SpendJson> getSpendsByPeriod(String username, String fromDate, String toDate);

  List<SpendJson> getSpendsByCategoryAndPeriod(String username, String category, String fromDate, String toDate);

  // ==================== STATISTICS (Статистика) ====================

  Double getTotalSpends(String username);

  Double getTotalSpendsByCategory(String username, String category);

  Map<String, Double> getCategoryDistribution(String username);

  // ==================== CURRENCY (Валюты) ====================

  Map<String, Double> getCurrencyRates();

  Double convertCurrency(Double amount, String fromCurrency, String toCurrency);
}