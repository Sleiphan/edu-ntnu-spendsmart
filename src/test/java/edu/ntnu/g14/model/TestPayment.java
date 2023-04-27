package edu.ntnu.g14.model;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;

class TestPayment {

  @Test
  void testConstructor_validParameters() {
    String fromAccountId = "1234.56.78910";
    BigDecimal amount = BigDecimal.valueOf(1000);
    String description = "Test payment";
    String toAccountId = "1234.56.78911";
    LocalDate dueDate = LocalDate.now().plusDays(5);
    String CID = "CUST-001";
    LocalDate dateOfTransaction = LocalDate.now();
    BudgetCategory category = BudgetCategory.ALCOHOL_AND_TOBACCO;

    Payment payment = new Payment(fromAccountId, amount, description, toAccountId, dueDate, CID,
        dateOfTransaction, category);

    assertNotNull(payment, "Payment object should not be null");
  }


  @Test
  void testConstructor_nullCID() {
    assertThrows(IllegalArgumentException.class, () -> {
      String fromAccountId = "1234.56.78910";
      BigDecimal amount = BigDecimal.valueOf(1000);
      String description = "Test payment";
      String toAccountId = "1234.56.78911";
      LocalDate dueDate = LocalDate.now().plusDays(5);
      String CID = null;
      LocalDate dateOfTransaction = LocalDate.now();
      BudgetCategory category = BudgetCategory.ALCOHOL_AND_TOBACCO;

      new Payment(fromAccountId, amount, description, toAccountId, dueDate, CID, dateOfTransaction,
          category);
    });
  }

  @Test
  void testConstructor_pastDueDate() {
    assertThrows(IllegalArgumentException.class, () -> {
      String fromAccountId = "1234.56.78910";
      BigDecimal amount = BigDecimal.valueOf(1000);
      String description = "Test payment";
      String toAccountId = "4321.65.01987";
      LocalDate dueDate = LocalDate.now().minusDays(1);
      String CID = "CUST-001";
      LocalDate dateOfTransaction = LocalDate.now();
      BudgetCategory category = BudgetCategory.ALCOHOL_AND_TOBACCO;

      new Payment(fromAccountId, amount, description, toAccountId, dueDate, CID, dateOfTransaction,
          category);
    });
  }
}
