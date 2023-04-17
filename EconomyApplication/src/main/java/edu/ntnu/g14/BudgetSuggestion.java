package edu.ntnu.g14;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

import edu.ntnu.g14.BudgetCategory;

public class BudgetSuggestion {

    public Map<BudgetCategory, BigDecimal> suggestBudget(BigDecimal totalIncome) {
        Map<BudgetCategory, BigDecimal> budgetMap = new HashMap<>();

        budgetMap.put(BudgetCategory.FOOD_AND_DRINK, calculatePercentage(totalIncome, 15));
        budgetMap.put(BudgetCategory.CLOTHES_AND_SHOES, calculatePercentage(totalIncome, 5));
        budgetMap.put(BudgetCategory.PERSONAL_CARE, calculatePercentage(totalIncome, 5));
        budgetMap.put(BudgetCategory.LEISURE, calculatePercentage(totalIncome, 10));
        budgetMap.put(BudgetCategory.TRAVEL, calculatePercentage(totalIncome, 10));
        budgetMap.put(BudgetCategory.ALCOHOL_AND_TOBACCO, calculatePercentage(totalIncome, 2));
        budgetMap.put(BudgetCategory.OTHER, calculatePercentage(totalIncome, 3));

        return budgetMap;
    }

    private BigDecimal calculatePercentage(BigDecimal base, int percentage) {
        BigDecimal result = base.multiply(BigDecimal.valueOf(percentage)).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
        return result;
    }

}

