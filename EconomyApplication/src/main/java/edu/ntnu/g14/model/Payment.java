package edu.ntnu.g14.model;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * This class represents a payment object that extends the Transaction class. It contains
 * information about the source and destination accounts, amount, description, customer ID, due
 * date, date of transaction, and category.
 *
 * @author G14
 * @version 1.0
 * @since 2023-04-25
 */
public class Payment extends Transaction {

  private String CID;
  private LocalDate dueDate;

  /**
   * T constructor creates a payment object with the given parameters.
   *
   * @param fromAccountId     The account number of the account that the money is being taken from
   * @param amount            The amount of money that is being transferred
   * @param description       A description of the transaction
   * @param toAccountId       The account number of the account that the money is being transferred
   *                          to
   * @param dueDate           The date that the payment is due
   * @param CID               The customer ID of the customer that the payment is being made to
   * @param dateOfTransaction The date that the transaction was made
   * @param category          The category that the transaction belongs to
   */
  public Payment(String fromAccountId, BigDecimal amount, String description, String toAccountId,
      LocalDate dueDate, String CID, LocalDate dateOfTransaction, BudgetCategory category) {
    super(fromAccountId, toAccountId, amount, description, dateOfTransaction, category);

    if (CID == null) {
      throw new IllegalArgumentException("Customer ID cannot be null");
    }
    if (dueDate.isBefore(LocalDate.now())) {
      throw new IllegalArgumentException("This date has already passed");
    }
    this.CID = CID;
    this.dueDate = dueDate;
  }

}
