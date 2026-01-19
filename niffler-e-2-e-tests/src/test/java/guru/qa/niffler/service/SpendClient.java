package guru.qa.niffler.service;

import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;

import java.util.List;
import java.util.Optional;

public interface SpendClient {

  // ==================== SPENDS (Траты) ====================

  SpendJson addSpend(SpendJson spend);

  SpendJson editSpend(SpendJson spend);

  SpendJson getSpend(String id, String username);

  List<SpendJson> getAllSpends(String username);

  void removeSpend(String username, List<String> ids);

  // ==================== CATEGORIES (Категории) ====================

  CategoryJson addCategory(CategoryJson category);

  void removeSpend(String id);

  CategoryJson updateCategory(CategoryJson category);

  List<CategoryJson> getAllCategories(String username);

  Optional<CategoryJson> findCategoryByNameAndUsername(String categoryName, String username);
}