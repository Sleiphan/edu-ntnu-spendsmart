package edu.ntnu.g14;

import edu.ntnu.g14.model.BudgetCategory;
import edu.ntnu.g14.model.Transaction;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;

class TestTransaction {

  @Test
  void csvParsing() {
    String fromAccountId = "1111.11.11111";
    String toAccountId = "1111.11.11111";

    String description = "Desc";
    LocalDate dateOfTransaction = LocalDate.now();
    BudgetCategory category = BudgetCategory.LEISURE;

    Transaction t = new Transaction(fromAccountId, toAccountId, BigDecimal.valueOf(1000),
        description, dateOfTransaction, category);
    Transaction t2 = Transaction.fromCSVString(t.toCSVString());
    assert t.equals(t2);
  }
}