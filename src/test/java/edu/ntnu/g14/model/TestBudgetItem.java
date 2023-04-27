package edu.ntnu.g14.model;

import edu.ntnu.g14.model.BudgetCategory;
import edu.ntnu.g14.model.BudgetItem;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

public class TestBudgetItem {

  @Test
  public void equals_ok() {
    BudgetItem item1 = new BudgetItem(new BigDecimal("99.75"), "Expense", BudgetCategory.OTHER);
    BudgetItem item2 = new BudgetItem(new BigDecimal("99.75"), "Expense", BudgetCategory.OTHER);
    BudgetItem unequal = new BudgetItem(new BigDecimal("50.2"), "Another expense",
        BudgetCategory.OTHER);

    assert (item1.equals(item2));
    assert (item2.equals(item1));
    assert (!item1.equals(unequal));
    assert (!item2.equals(unequal));
    assert (!unequal.equals(item1));
    assert (!unequal.equals(item2));
  }

  @Test
  public void csv_parsing_ok() {
    BudgetItem item = new BudgetItem(new BigDecimal("99.75"), "Expense", BudgetCategory.OTHER);
    String csv = item.toCSV();
    BudgetItem copy = BudgetItem.fromCSV(csv);

    assert (item.equals(copy));
  }

}
