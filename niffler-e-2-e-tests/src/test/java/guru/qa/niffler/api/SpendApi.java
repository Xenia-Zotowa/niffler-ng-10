package guru.qa.niffler.api;

import guru.qa.niffler.model.SpendJson;
import lombok.SneakyThrows;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import guru.qa.niffler.model.CategoryJson;
import retrofit2.http.*;

import java.util.List;
import java.util.Map;


public interface SpendApi {
  // ==================== SPENDS (Траты) ====================

  @POST("internal/spends/add")
  Call<SpendJson> createSpend(@Body SpendJson spend);

  @PATCH("internal/spends/edit")
  Call<SpendJson> editSpend(@Body SpendJson spend);

  @GET("internal/spends/{id}")
  Call<SpendJson> getSpend(@Path("id") String id);

  @GET("internal/spends/all")
  Call<List<SpendJson>> getAllSpends(@Query("username") String username);

  @DELETE("internal/spends/remove")
  Call<Void> removeSpend(@Query("id") String id);

  // ==================== CATEGORIES (Категории) ====================

  @POST("internal/categories/add")
  Call<CategoryJson> addCategory(@Body CategoryJson category);

  @PATCH("internal/categories/update")
  Call<CategoryJson> updateCategory(@Body CategoryJson category);

  @GET("internal/categories/all")
  Call<List<CategoryJson>> getAllCategories(@Query("username") String username);

  // ==================== FILTERED SPENDS (Фильтрованные траты) ====================

  @GET("internal/spends/byCategory")
  Call<List<SpendJson>> getSpendsByCategory(
          @Query("username") String username,
          @Query("category") String category
  );

  @GET("internal/spends/byPeriod")
  Call<List<SpendJson>> getSpendsByPeriod(
          @Query("username") String username,
          @Query("fromDate") String fromDate,
          @Query("toDate") String toDate
  );

  @GET("internal/spends/byCategoryAndPeriod")
  Call<List<SpendJson>> getSpendsByCategoryAndPeriod(
          @Query("username") String username,
          @Query("category") String category,
          @Query("fromDate") String fromDate,
          @Query("toDate") String toDate
  );

  // ==================== STATISTICS (Статистика) ====================

  @GET("internal/spends/total")
  Call<Double> getTotalSpends(@Query("username") String username);

  @GET("internal/spends/totalByCategory")
  Call<Double> getTotalSpendsByCategory(
          @Query("username") String username,
          @Query("category") String category
  );

  @GET("internal/spends/categoryDistribution")
  Call<Map<String, Double>> getCategoryDistribution(@Query("username") String username);

  // ==================== CURRENCY (Валюты) ====================

  @GET("internal/currency/rates")
  Call<Map<String, Double>> getCurrencyRates();

  @GET("internal/currency/convert")
  Call<Double> convertCurrency(
          @Query("amount") Double amount,
          @Query("fromCurrency") String fromCurrency,
          @Query("toCurrency") String toCurrency
  );
}
