package edu.ntnu.g14.model;

import edu.ntnu.g14.model.BudgetCategory;
import edu.ntnu.g14.model.Transaction;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TestTransaction {
  String fromAccountId;
  String toAccountId;
  BigDecimal amount;
  String description;
  LocalDate dateOfTransaction;
  BudgetCategory category;

  @BeforeEach
  void setUp() {
    fromAccountId = "1111.11.11111";
    toAccountId = "2222.22.22222";
    amount = BigDecimal.valueOf(100);
    description = "Test transaction";
    dateOfTransaction = LocalDate.now();
    category = BudgetCategory.LEISURE;
  }

  @Test
  void csvParsing() {
    Transaction t = new Transaction(fromAccountId, toAccountId, amount,
        description, dateOfTransaction, category);
    Transaction t2 = Transaction.fromCSVString(t.toCSVString());
    assertEquals(t, t2);
  }

  @Test
  void testConstructor_withValidData() {
    assertDoesNotThrow(() -> new Transaction(fromAccountId, toAccountId, amount,
        description, dateOfTransaction, category));
  }

  @Test
  void testConstructor_withInvalidFromAccountNumber() {
    String invalidFromAccountNumber = "123.4567.8910";
    assertThrows(IllegalArgumentException.class, () -> new Transaction(invalidFromAccountNumber, toAccountId, amount,
        description, dateOfTransaction, category));
  }

  @Test
  void testConstructor_withInvalidToAccountNumber() {
    String invalidToAccountNumber = "123.4567.8910";
    assertThrows(IllegalArgumentException.class, () -> new Transaction(fromAccountId, invalidToAccountNumber, amount,
        description, dateOfTransaction, category));
  }

  @Test
  void testConstructor_withNullDescription() {
    assertThrows(IllegalArgumentException.class, () -> new Transaction(fromAccountId, toAccountId, amount,
        null, dateOfTransaction, category));
  }

  @Test
  void testConstructor_withNullDateOfTransaction() {
    assertThrows(IllegalArgumentException.class, () -> new Transaction(fromAccountId, toAccountId, amount,
        description, null, category));
  }

  @Test
  void testTransactionBuilder_withValidData() {
    Transaction.TransactionBuilder transactionBuilder = new Transaction.TransactionBuilder()
        .fromAccountNumber(fromAccountId)
        .toAccountNumber(toAccountId)
        .amount(amount)
        .description(description)
        .dateOfTransaction(dateOfTransaction)
        .category(category);

    assertDoesNotThrow(transactionBuilder::build);
  }

  @Test
  void testTransactionBuilder_withInvalidFromAccountNumber() {
    String invalidFromAccountNumber = "123.4567.8910";
    Transaction.TransactionBuilder transactionBuilder = new Transaction.TransactionBuilder();

    assertThrows(IllegalArgumentException.class, () -> transactionBuilder.fromAccountNumber(invalidFromAccountNumber));
  }

  @Test
  void testTransactionBuilder_withInvalidToAccountNumber() {
    String invalidToAccountNumber = "123.4567.8910";
    Transaction.TransactionBuilder transactionBuilder = new Transaction.TransactionBuilder();

    assertThrows(IllegalArgumentException.class, () -> transactionBuilder.toAccountNumber(invalidToAccountNumber));
  }

  @Test
  void testTransactionBuilder_withNullDescription() {
    Transaction.TransactionBuilder transactionBuilder = new Transaction.TransactionBuilder();

    assertThrows(IllegalArgumentException.class, () -> transactionBuilder.description(null));
  }
  @Test
  void testTransactionBuilder_withNullDateOfTransaction() {
    Transaction.TransactionBuilder transactionBuilder = new Transaction.TransactionBuilder();

    assertThrows(IllegalArgumentException.class, () -> transactionBuilder.dateOfTransaction(null));
  }

  @Test
  void testTransactionBuilder_withValidDataAndToString() {
    Transaction.TransactionBuilder transactionBuilder = new Transaction.TransactionBuilder()
        .fromAccountNumber(fromAccountId)
        .toAccountNumber(toAccountId)
        .amount(amount)
        .description(description)
        .dateOfTransaction(dateOfTransaction)
        .category(category);

    Transaction transaction = transactionBuilder.build();
    String expectedToString = "Transaction{fromAccountNumber=" + fromAccountId + ", toAccountNumber=" + toAccountId +
        ", amount=" + amount + ", description=" + description + ", dateOfTransaction=" + dateOfTransaction +
        ", category=" + category + '}';

    assertEquals(expectedToString, transaction.toString());
  }
}

