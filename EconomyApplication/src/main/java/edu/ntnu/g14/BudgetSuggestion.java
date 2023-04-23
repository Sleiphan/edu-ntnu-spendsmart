package edu.ntnu.g14;

import edu.ntnu.g14.BudgetCategory;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

public class BudgetSuggestion {

    public Map<BudgetCategory, BigDecimal> suggestBudget(BigDecimal totalIncome) {
        Map<BudgetCategory, BigDecimal> budgetMap = new HashMap<>();

        int incomeBracket = determineIncomeBracket(totalIncome);

        switch (incomeBracket) {
            case 1:
                // Low-income budget
                budgetMap.put(BudgetCategory.FOOD_AND_DRINK, calculatePercentage(totalIncome, 40));
                budgetMap.put(BudgetCategory.CLOTHES_AND_SHOES, calculatePercentage(totalIncome, 10));
                budgetMap.put(BudgetCategory.PERSONAL_CARE, calculatePercentage(totalIncome, 10));
                budgetMap.put(BudgetCategory.LEISURE, calculatePercentage(totalIncome, 10));
                budgetMap.put(BudgetCategory.TRAVEL, calculatePercentage(totalIncome, 5));
                budgetMap.put(BudgetCategory.ALCOHOL_AND_TOBACCO, calculatePercentage(totalIncome, 2));
                budgetMap.put(BudgetCategory.OTHER, calculatePercentage(totalIncome, 23));
                break;
            case 2:
                // Mid-income budget
                budgetMap.put(BudgetCategory.FOOD_AND_DRINK, calculatePercentage(totalIncome, 30));
                budgetMap.put(BudgetCategory.CLOTHES_AND_SHOES, calculatePercentage(totalIncome, 12));
                budgetMap.put(BudgetCategory.PERSONAL_CARE, calculatePercentage(totalIncome, 12));
                budgetMap.put(BudgetCategory.LEISURE, calculatePercentage(totalIncome, 13));
                budgetMap.put(BudgetCategory.TRAVEL, calculatePercentage(totalIncome, 8));
                budgetMap.put(BudgetCategory.ALCOHOL_AND_TOBACCO, calculatePercentage(totalIncome, 2));
                budgetMap.put(BudgetCategory.OTHER, calculatePercentage(totalIncome, 23));
                break;
            case 3:
                //  Mid+ -income budget
                budgetMap.put(BudgetCategory.FOOD_AND_DRINK, calculatePercentage(totalIncome, 20));
                budgetMap.put(BudgetCategory.CLOTHES_AND_SHOES, calculatePercentage(totalIncome, 10));
                budgetMap.put(BudgetCategory.PERSONAL_CARE, calculatePercentage(totalIncome, 15));
                budgetMap.put(BudgetCategory.LEISURE, calculatePercentage(totalIncome, 15));
                budgetMap.put(BudgetCategory.TRAVEL, calculatePercentage(totalIncome, 10));
                budgetMap.put(BudgetCategory.ALCOHOL_AND_TOBACCO, calculatePercentage(totalIncome, 2));
                budgetMap.put(BudgetCategory.OTHER, calculatePercentage(totalIncome, 28));
                break;
            case 4:
                // High-income budget
                budgetMap.put(BudgetCategory.FOOD_AND_DRINK, calculatePercentage(totalIncome, 15));
                budgetMap.put(BudgetCategory.CLOTHES_AND_SHOES, calculatePercentage(totalIncome, 15));
                budgetMap.put(BudgetCategory.PERSONAL_CARE, calculatePercentage(totalIncome, 15));
                budgetMap.put(BudgetCategory.LEISURE, calculatePercentage(totalIncome, 15));
                budgetMap.put(BudgetCategory.TRAVEL, calculatePercentage(totalIncome, 15));
                budgetMap.put(BudgetCategory.ALCOHOL_AND_TOBACCO, calculatePercentage(totalIncome, 5));
                budgetMap.put(BudgetCategory.OTHER, calculatePercentage(totalIncome, 20));
            default:
                // Default budget suggestion
                budgetMap.put(BudgetCategory.FOOD_AND_DRINK, calculatePercentage(totalIncome, 10));
                budgetMap.put(BudgetCategory.CLOTHES_AND_SHOES, calculatePercentage(totalIncome, 10));
                budgetMap.put(BudgetCategory.PERSONAL_CARE, calculatePercentage(totalIncome, 10));
                budgetMap.put(BudgetCategory.LEISURE, calculatePercentage(totalIncome, 20));
                budgetMap.put(BudgetCategory.TRAVEL, calculatePercentage(totalIncome, 20));
                budgetMap.put(BudgetCategory.ALCOHOL_AND_TOBACCO, calculatePercentage(totalIncome, 4));
                budgetMap.put(BudgetCategory.OTHER, calculatePercentage(totalIncome, 6));
        }

        return budgetMap;
    }

    private int determineIncomeBracket(BigDecimal totalIncome) {
        int bracket;

        if (totalIncome.compareTo(BigDecimal.valueOf(5000)) <= 0) {
            bracket = 1; // Low-income
        } else if (totalIncome.compareTo(BigDecimal.valueOf(12000)) <= 0) {
            bracket = 2; // Mid-income
        } else if (totalIncome.compareTo(BigDecimal.valueOf(30000)) <=0) {
            bracket = 3; // Mid + -income
        } else {
            bracket = 4; // High-income
        }

        return bracket;
    }

    private BigDecimal calculatePercentage(BigDecimal base, int percentage) {
        return base.multiply(BigDecimal.valueOf(percentage)).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
    }

}

