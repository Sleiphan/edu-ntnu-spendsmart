package edu.ntnu.g14.model;

import edu.ntnu.g14.model.BudgetCategory;
import edu.ntnu.g14.model.BudgetSuggestion;
import java.math.RoundingMode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TestBudgetSuggestion {
  private BudgetSuggestion budgetSuggestion;

  @BeforeEach
  void setUp() {
    budgetSuggestion = new BudgetSuggestion();
  }

  @Test
  void testSuggestBudget_lowIncome() {
    BigDecimal totalIncome = BigDecimal.valueOf(4500);
    Map<BudgetCategory, BigDecimal> budgetMap = budgetSuggestion.suggestBudget(totalIncome);

    assertNotNull(budgetMap, "Budget map should not be null");
    assertEquals(budgetMap.get(BudgetCategory.FOOD_AND_DRINK), calculatePercentage(totalIncome, 40));
    assertEquals(budgetMap.get(BudgetCategory.CLOTHES_AND_SHOES), calculatePercentage(totalIncome, 10));
    assertEquals(budgetMap.get(BudgetCategory.PERSONAL_CARE), calculatePercentage(totalIncome, 10));
    assertEquals(budgetMap.get(BudgetCategory.LEISURE), calculatePercentage(totalIncome, 10));
    assertEquals(budgetMap.get(BudgetCategory.TRAVEL), calculatePercentage(totalIncome, 5));
    assertEquals(budgetMap.get(BudgetCategory.ALCOHOL_AND_TOBACCO), calculatePercentage(totalIncome, 2));
    assertEquals(budgetMap.get(BudgetCategory.OTHER), calculatePercentage(totalIncome, 23));
  }

  @Test
  void testSuggestBudget_midIncome() {
    BigDecimal totalIncome = BigDecimal.valueOf(10000);
    Map<BudgetCategory, BigDecimal> budgetMap = budgetSuggestion.suggestBudget(totalIncome);

    assertNotNull(budgetMap, "Budget map should not be null");
    assertEquals(budgetMap.get(BudgetCategory.FOOD_AND_DRINK), calculatePercentage(totalIncome, 30));
    assertEquals(budgetMap.get(BudgetCategory.CLOTHES_AND_SHOES), calculatePercentage(totalIncome, 12));
    assertEquals(budgetMap.get(BudgetCategory.PERSONAL_CARE), calculatePercentage(totalIncome, 12));
    assertEquals(budgetMap.get(BudgetCategory.LEISURE), calculatePercentage(totalIncome, 13));
    assertEquals(budgetMap.get(BudgetCategory.TRAVEL), calculatePercentage(totalIncome, 8));
    assertEquals(budgetMap.get(BudgetCategory.ALCOHOL_AND_TOBACCO), calculatePercentage(totalIncome, 2));
    assertEquals(budgetMap.get(BudgetCategory.OTHER), calculatePercentage(totalIncome, 23));
  }

  @Test
  void testSuggestBudget_midPlusIncome() {
    BigDecimal totalIncome = BigDecimal.valueOf(20000);
    Map<BudgetCategory, BigDecimal> budgetMap = budgetSuggestion.suggestBudget(totalIncome);

    assertNotNull(budgetMap, "Budget map should not be null");
    assertEquals(budgetMap.get(BudgetCategory.FOOD_AND_DRINK), calculatePercentage(totalIncome, 20));
    assertEquals(budgetMap.get(BudgetCategory.CLOTHES_AND_SHOES), calculatePercentage(totalIncome, 10));
    assertEquals(budgetMap.get(BudgetCategory.PERSONAL_CARE), calculatePercentage(totalIncome, 15));
    assertEquals(budgetMap.get(BudgetCategory.LEISURE), calculatePercentage(totalIncome, 15));
    assertEquals(budgetMap.get(BudgetCategory.TRAVEL), calculatePercentage(totalIncome, 10));
    assertEquals(budgetMap.get(BudgetCategory.ALCOHOL_AND_TOBACCO), calculatePercentage(totalIncome, 2));
    assertEquals(budgetMap.get(BudgetCategory.OTHER), calculatePercentage(totalIncome, 28));
  }

  @Test
  void testSuggestBudget_highIncome() {
    BigDecimal totalIncome = BigDecimal.valueOf(45000);
    Map<BudgetCategory, BigDecimal> budgetMap = budgetSuggestion.suggestBudget(totalIncome);

    assertNotNull(budgetMap, "Budget map should not be null");
    assertEquals(budgetMap.get(BudgetCategory.FOOD_AND_DRINK), calculatePercentage(totalIncome, 15));
    assertEquals(budgetMap.get(BudgetCategory.CLOTHES_AND_SHOES), calculatePercentage(totalIncome, 15));
    assertEquals(budgetMap.get(BudgetCategory.PERSONAL_CARE), calculatePercentage(totalIncome, 15));
    assertEquals(budgetMap.get(BudgetCategory.LEISURE), calculatePercentage(totalIncome, 15));
    assertEquals(budgetMap.get(BudgetCategory.TRAVEL), calculatePercentage(totalIncome, 15));
    assertEquals(budgetMap.get(BudgetCategory.ALCOHOL_AND_TOBACCO), calculatePercentage(totalIncome, 5));
    assertEquals(budgetMap.get(BudgetCategory.OTHER), calculatePercentage(totalIncome, 20));
  }

  // Add more test cases for mid+ income and high income

  private BigDecimal calculatePercentage(BigDecimal base, int percentage) {
    return base.multiply(BigDecimal.valueOf(percentage))
        .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
  }
}
