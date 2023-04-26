package edu.ntnu.g14;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

public class TestBudget {

  @Test
  public void csv_parsing_ok() {
    Budget budget = new Budget((byte) 1, GenderCategory.MALE);
    budget.setSavings(new BigDecimal("2"));
    budget.setSalary(new BigDecimal("3"));
    budget.addBudgetItem(new BudgetItem(new BigDecimal("4"), "Expense 1", BudgetCategory.LEISURE));
    budget.addBudgetItem(new BudgetItem(new BigDecimal("5"), "Expense 2", BudgetCategory.TRAVEL));

    String csv = budget.toCSV();
    Budget copy = Budget.fromCSV(csv);

    assert (budget.equals(copy));
  }

  @Test
  public void enum_members_support_null() {
    Budget budget = new Budget(null);
    budget.setGender(null);
    budget.setSavings(new BigDecimal("2"));
    budget.setSalary(new BigDecimal("3"));
    budget.addBudgetItem(new BudgetItem(new BigDecimal("4"), "Expense 1", BudgetCategory.LEISURE));
    budget.addBudgetItem(new BudgetItem(new BigDecimal("5"), "Expense 2", BudgetCategory.TRAVEL));

    String csv = budget.toCSV();
    Budget copy = Budget.fromCSV(csv);

    assert (budget.equals(copy));
  }
}
