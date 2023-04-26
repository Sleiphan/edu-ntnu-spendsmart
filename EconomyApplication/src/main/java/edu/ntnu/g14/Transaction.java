package edu.ntnu.g14;

import edu.ntnu.g14.frontend.ApplicationObjects;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * This class represents a transaction object. It contains information about the source and
 * destination accounts, amount, description, date of transaction, and category.
 *
 * @author G14
 * @version 1.0
 * @since 2023-04-25
 */
public class Transaction {

  private final String fromAccountNumber;
  private final String toAccountNumber;
  private final BigDecimal amount;
  private final String description;
  private final LocalDate dateOfTransaction;
  private final BudgetCategory category;
  public static final String regexAccountNumber = "[0-9]{4}+\\.[0-9]{2}+\\.[0-9]{5}";

  /**
   * This constructor creates a transaction object with the given parameters.
   *
   * @param fromAccountNumber The account number of the account that the money is being taken from
   * @param toAccountNumber   The account number of the account that the money is being transferred
   *                          to
   * @param amount            The amount of money that is being transferred
   * @param description       A description of the transaction
   * @param dateOfTransaction The date that the transaction was made
   * @param category          The category that the transaction belongs to
   */
  public Transaction(String fromAccountNumber, String toAccountNumber, BigDecimal amount,
      String description,
      LocalDate dateOfTransaction, BudgetCategory category) {
    if (!Pattern.matches(regexAccountNumber, fromAccountNumber) || !Pattern.matches(
        regexAccountNumber, toAccountNumber)) {
      throw new IllegalArgumentException("This is not an account number");
    }
    if (fromAccountNumber.isEmpty()) {
      throw new IllegalArgumentException("From account number cannot be empty");
    }
    if (toAccountNumber.isEmpty()) {
      throw new IllegalArgumentException("To account number cannot be empty");
    }
    if (description == null) {
      throw new IllegalArgumentException("Description cannot be null");
    }
    if (dateOfTransaction == null) {
      throw new IllegalArgumentException("Date of transaction cannot be null");
    }
    this.category = category;
    this.fromAccountNumber = fromAccountNumber;
    this.toAccountNumber = toAccountNumber;
    this.amount = amount;
    this.description = description;
    this.dateOfTransaction = dateOfTransaction;
  }


  public Transaction(TransactionBuilder transactionBuilder) {
    this.fromAccountNumber = transactionBuilder.fromAccountNumber;
    this.toAccountNumber = transactionBuilder.toAccountNumber;
    this.amount = transactionBuilder.amount;
    this.description = transactionBuilder.description;
    this.dateOfTransaction = transactionBuilder.dateOfTransaction;
    this.category = transactionBuilder.category;
  }

  /**
   * This method returns the account number of the account that the money is being taken from.
   *
   * @return The account number of the account that the money is being taken from
   */
  public String getFromAccountNumber() {
    return fromAccountNumber;
  }

  /**
   * This method returns the account number of the account that the money is being transferred to.
   *
   * @return The account number of the account that the money is being transferred to
   */
  public String getToAccountNumber() {
    return toAccountNumber;
  }

  /**
   * This method returns the amount of money that is being transferred.
   *
   * @return The amount of money that is being transferred
   */
  public BigDecimal getAmount() {
    return amount;
  }

  /**
   * This method returns a description of the transaction.
   *
   * @return A description of the transaction
   */
  public String getDescription() {
    return description;
  }

  /**
   * This method returns the date that the transaction was made.
   *
   * @return The date that the transaction was made
   */
  public LocalDate getDateOfTransaction() {
    return dateOfTransaction;
  }

  /**
   * This method returns the category that the transaction belongs to.
   *
   * @return The category that the transaction belongs to
   */
  public BudgetCategory getCategory() {
    return category;
  }

  //This method needs a better string representation of the transaction
  @Override
  public String toString() {
    return "Transaction{" + "fromAccountNumber=" + fromAccountNumber + ", toAccountNumber="
        + toAccountNumber + ", amount=" + amount + ", description=" + description
        + ", dateOfTransaction=" + dateOfTransaction + ", category=" + category + '}';
  }

  /**
   * This method is used to compare two transaction objects.
   *
   * @param o The object that is being compared to the transaction object
   * @return True if the transaction objects are equal, false otherwise
   */
  @Override
  public boolean equals(Object o) {
    if (!(o instanceof Transaction)) {
      return false;
    }

    Transaction t = (Transaction) o;
    if (!Objects.equals(amount, t.amount)) {
      return false;
    }
    if (!toAccountNumber.equals(t.toAccountNumber)) {
      return false;
    }
    if (!fromAccountNumber.equals(t.fromAccountNumber)) {
      return false;
    }
    if (!description.equals(t.description)) {
      return false;
    }
    if (!category.equals(t.category)) {
      return false;
    }
    return dateOfTransaction.equals(t.dateOfTransaction);
  }

  /**
   * This method is used to convert a transaction object to a string that can be saved to a CSV
   * file.
   */
  public static final String CSV_FIELD_DELIMITER = ";";

  public String toCSVString() {
    return dateOfTransaction.format(ApplicationObjects.dateFormatter) + CSV_FIELD_DELIMITER +
        amount + CSV_FIELD_DELIMITER +
        toAccountNumber + CSV_FIELD_DELIMITER +
        fromAccountNumber + CSV_FIELD_DELIMITER +
        description + CSV_FIELD_DELIMITER
        + category;
  }

  /**
   * This method is used to convert a string from a CSV file to a transaction object.
   *
   * @param csvString The string that is being converted to a transaction object
   * @return The transaction object that is created from the string
   */
  public static Transaction fromCSVString(String csvString) {
    String[] fields = csvString.split(CSV_FIELD_DELIMITER);
    String fromAccountNumber = fields[3];
    String toAccountNumber = fields[2];
    BigDecimal amount = new BigDecimal(fields[1]);
    String description = fields[4];
    LocalDate dateOfTransaction = LocalDate.parse(fields[0], ApplicationObjects.dateFormatter);
    BudgetCategory budgetCategory = BudgetCategory.valueOf(fields[5]);

    return new Transaction(fromAccountNumber, toAccountNumber, amount, description,
        dateOfTransaction, budgetCategory);
  }

  public static class TransactionBuilder {

    private String fromAccountNumber;
    private String toAccountNumber;
    private BigDecimal amount;
    private String description;
    private LocalDate dateOfTransaction;
    private BudgetCategory category;

    public Transaction build() {
      if (!validateBuilder()) {
        return new Transaction(this);
      } else {
        throw new IllegalStateException("Transaction not fully defined during build");
      }
    }

    public TransactionBuilder fromAccountNumber(String fromAccountNumber) {
      if (!Pattern.matches(regexAccountNumber, fromAccountNumber)) {
        throw new IllegalArgumentException("This is not an account number");
      }
      this.fromAccountNumber = fromAccountNumber;
      return this;
    }

    public TransactionBuilder toAccountNumber(String toAccountNumber) {
      if (!Pattern.matches(regexAccountNumber, toAccountNumber)) {
        throw new IllegalArgumentException("This is not an account number");
      }
      this.toAccountNumber = toAccountNumber;
      return this;
    }

    public TransactionBuilder description(String description) {
      if (description == null) {
        throw new IllegalArgumentException("Description cannot be null");
      }
      this.description = description;
      return this;
    }

    public TransactionBuilder dateOfTransaction(LocalDate dateOfTransaction) {
      if (dateOfTransaction == null) {
        throw new IllegalArgumentException("Date of transaction cannot be null");
      }
      this.dateOfTransaction = dateOfTransaction;
      return this;
    }

    public TransactionBuilder category(BudgetCategory category) {
      this.category = category;
      return this;
    }

    public TransactionBuilder amount(BigDecimal amount) {
      this.amount = amount;
      return this;
    }

    private boolean validateBuilder() {
      return this.dateOfTransaction == null || this.amount == null || this.category == null
          || this.description == null || this.fromAccountNumber == null
          || this.toAccountNumber == null;
    }
  }

}
