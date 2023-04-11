package edu.ntnu.g14;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

public class TestBudget {

    @Test
    public void csv_parsing_ok() {
        Budget budget = new Budget((byte) 1, GenderCategory.MALE);
        budget.setSavings(new BigDecimal("2"));
        budget.setSalary(new BigDecimal("3"));
        budget.addBudgetItem(new BudgetItem(new BigDecimal("4"), "Expense 1", ExpenditureCategory.LEISURE));
        budget.addBudgetItem(new BudgetItem(new BigDecimal("5"), "Expense 2", ExpenditureCategory.TRAVEL));

        String csv = budget.toCSV();
        Budget copy = Budget.fromCSV(csv);

        assert(budget.equals(copy));
    }
}