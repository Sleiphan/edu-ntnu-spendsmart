package edu.ntnu.g14.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TestInvoice {

  private LocalDate dueDate;
  private BigDecimal amount;
  private String recipientAccountNumber;
  private String comment;

  @BeforeEach
  void setUp() {
    dueDate = LocalDate.now();
    amount = BigDecimal.valueOf(1000);
    recipientAccountNumber = "1234.56.78910";
    comment = "Sample invoice";
  }

  @Test
  void testInvoiceConstructor_withValidParameters() {
    Invoice invoice = new Invoice(dueDate, amount, recipientAccountNumber, comment);

    assertEquals(dueDate, invoice.getDueDate());
    assertEquals(amount, invoice.getAmount());
    assertEquals(recipientAccountNumber, invoice.getRecipientAccountNumber());
    assertEquals(comment, invoice.getComment());
  }

  @Test
  void testInvoiceConstructor_withInvalidAmount() {
    BigDecimal invalidAmount = BigDecimal.valueOf(-1);

    assertThrows(IllegalArgumentException.class,
        () -> new Invoice(dueDate, invalidAmount, recipientAccountNumber, comment));
  }

  @Test
  void testInvoiceConstructor_withInvalidRecipientAccountNumber() {
    String invalidRecipientAccountNumber = "1245.67.8910";

    assertThrows(IllegalArgumentException.class,
        () -> new Invoice(dueDate, amount, invalidRecipientAccountNumber, comment));
  }

  @Test
  void testInvoiceConstructor_withNullComment() {
    String nullComment = null;

    assertThrows(IllegalArgumentException.class,
        () -> new Invoice(dueDate, amount, recipientAccountNumber, nullComment));
  }

  @Test
  void testToCSVString() {
    Invoice invoice = new Invoice(dueDate, amount, recipientAccountNumber, comment);
    String expectedCSV = dueDate.format(Invoice.dateFormatter) + ";" +
        amount.toPlainString() + ";" +
        recipientAccountNumber + ";" +
        "\"" + comment + "\"";

    assertEquals(expectedCSV, invoice.toCSVString());
  }

  @Test
  void testFromCSVString() {
    String csvString = dueDate.format(Invoice.dateFormatter) + ";" +
        amount.toPlainString() + ";" +
        recipientAccountNumber + ";" +
        "\"" + comment + "\"";
    Invoice invoice = Invoice.fromCSVString(csvString);

    assertEquals(dueDate, invoice.getDueDate());
    assertEquals(amount, invoice.getAmount());
    assertEquals(recipientAccountNumber, invoice.getRecipientAccountNumber());
    assertEquals(comment, invoice.getComment());
  }
}

