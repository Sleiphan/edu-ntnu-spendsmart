package edu.ntnu.g14.model;

import edu.ntnu.g14.model.Budget;
import edu.ntnu.g14.model.BudgetCategory;
import edu.ntnu.g14.model.BudgetItem;
import edu.ntnu.g14.model.GenderCategory;
import edu.ntnu.g14.model.HouseholdCategory;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

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

    assertEquals(budget, copy);
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

    assertEquals(budget, copy);
  }

  @Test
  public void testConstructorWithHouseholdCategory() {
    Budget budget = new Budget(HouseholdCategory.LIVING_ALONE);
    assertEquals(HouseholdCategory.LIVING_ALONE, budget.getCategory());
  }

  @Test
  public void testAddAndRemoveBudgetItem() {
    Budget budget = new Budget((byte) 1, GenderCategory.MALE);
    BudgetItem budgetItem = new BudgetItem(new BigDecimal("4"), "Expense 1",
        BudgetCategory.LEISURE);
    budget.addBudgetItem(budgetItem);

    List<BudgetItem> entries = budget.getEntries();
    assertEquals(1, entries.size());
    assertEquals(budgetItem, entries.get(0));

    boolean removed = budget.removeBudgetItem(0);
    assertTrue(removed);
    assertTrue(budget.getEntries().isEmpty());
  }

  @Test
  public void testRemoveBudgetItemInvalidIndex() {
    Budget budget = new Budget((byte) 1, GenderCategory.MALE);
    assertThrows(IndexOutOfBoundsException.class, () -> budget.removeBudgetItem(0));
  }

  @Test
  public void testUpdateCalculations() {
    Budget budget = new Budget((byte) 1, GenderCategory.MALE);
    budget.setSavings(new BigDecimal("2"));
    budget.setSalary(new BigDecimal("3"));
    budget.addBudgetItem(new BudgetItem(new BigDecimal("4"), "Expense 1", BudgetCategory.LEISURE));
    budget.addBudgetItem(new BudgetItem(new BigDecimal("5"), "Expense 2", BudgetCategory.TRAVEL));

    // The current implementation of updateCalculations does nothing, so the test is just to
    // ensure the method is called without any issues.
    budget.updateCalculations();
  }

  @Test
  public void testClearEntries() {
    Budget budget = new Budget((byte) 1, GenderCategory.MALE);
    budget.addBudgetItem(new BudgetItem(new BigDecimal("4"), "Expense 1", BudgetCategory.LEISURE));
    budget.addBudgetItem(new BudgetItem(new BigDecimal("5"), "Expense 2", BudgetCategory.TRAVEL));

    budget.clearEntries();
    assertTrue(budget.getEntries().isEmpty());
  }

  @Test
  public void testSetSalary() {
    Budget budget = new Budget((byte) 1, GenderCategory.MALE);
    budget.setSalary(new BigDecimal("10000"));
    assertEquals(new BigDecimal("10000"), budget.getSalary());

    assertThrows(IllegalArgumentException.class, () -> budget.setSalary(null));
    assertThrows(IllegalArgumentException.class, () -> budget.setSalary(new BigDecimal("-10000")));
  }

  @Test
  public void testSetSavings() {
    Budget budget = new Budget((byte) 1, GenderCategory.MALE);
    budget.setSavings(new BigDecimal("20000"));
    assertEquals(new BigDecimal("20000"), budget.getSavings());

    assertThrows(IllegalArgumentException.class, () -> budget.setSavings(null));
    assertDoesNotThrow(() -> budget.setSavings(new BigDecimal("-20000")));
  }

  @Test
  public void testSetGender() {
    Budget budget = new Budget((byte) 1, GenderCategory.MALE);
    budget.setGender(GenderCategory.FEMALE);
    assertEquals(GenderCategory.FEMALE, budget.getGender());
  }
}
