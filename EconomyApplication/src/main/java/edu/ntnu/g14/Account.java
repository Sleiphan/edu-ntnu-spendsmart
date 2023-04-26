package edu.ntnu.g14;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * This class represents an account in the bank system and that a user has.
 */
public class Account {

  // Type of account, as AccountCategory
  private AccountCategory accountType;
  // Funds in the account, as BigDecimal
  private BigDecimal amount;
  // Unique reference of account in the bank system, as String
  private final String accountNumber;
  // Name of the account assigned by a user, as String
  private String accountName;

  private static final String CSV_FIELD_DELIMITER = ";";

  /**
   * This constructor facilitates the creation of instances of this class
   *
   * @param accountType   Type of account, as AccountCategory
   * @param amount        Funds in the account, as BigDecimal
   * @param accountNumber Unique reference of account in the bank system, as String
   * @param accountName   Name of the account assigned by a user, as String
   */
  public Account(final AccountCategory accountType, final BigDecimal amount,
      final String accountNumber,
      final String accountName) {
    this.accountType = accountType;
    if (amount.intValue() < 0) {
      throw new IllegalArgumentException("Amount needs to be zero or positive");
    } else {
      this.amount = amount.setScale(2, RoundingMode.DOWN);
    }
    // TODO: Check if accountName is occupied
    if (accountName.isBlank()) {
      this.accountName = accountType.toString();
    } else {
      this.accountName = accountName;
    }
    // TODO: Check if account with this accountNumber already exists
    if (accountNumber.length() != 13) {
      throw new IllegalArgumentException(
          "Account number must contain 11 digits, seperated by 2 dots");
    } else {
      this.accountNumber = accountNumber;
    }
  }

  public Account(AccountBuilder account) {
    this.accountType = account.accountType;
    this.accountNumber = account.accountNumber;
    this.amount = account.amount;
    this.accountName = account.accountName;
  }

  /**
   * This method removes an amount from this account
   *
   * @param subtrahend Subtrahend, as BigDecimal
   * @return true if account has sufficient funds, false if not
   */
  public boolean removeAmount(BigDecimal subtrahend) {
    if (this.amount.subtract(subtrahend).floatValue() < 0) {
      throw new IllegalArgumentException("Account does not have sufficient funds");
    }
    this.amount = this.amount.subtract(subtrahend);
    return true;
  }

  /**
   * This method adds an amount to this account
   *
   * @param augend Augend, as BigDecimal
   */
  public void addAmount(BigDecimal augend) {
    this.amount = this.amount.add(augend);
  }

  /**
   * This method returns the account type of account
   *
   * @return accountType, as AccountCategory
   */
  public AccountCategory getAccountType() {
    return accountType;
  }

  /**
   * This method changes the name of an account. The new name must be unique to a users accounts
   *
   * @param newName
   */
  public void setAccountName(String newName) {
    if (newName.isBlank()) {
      throw new IllegalArgumentException("Account name cannot be left blank");
    }
    this.accountName = newName;
  }

  public void setAccountType(AccountCategory accountType) {
    this.accountType = accountType;
  }

  /**
   * This method returns the amount in an account
   *
   * @return amount, as BigDecimal
   */
  public BigDecimal getAmount() {
    return amount;
  }

  /**
   * This method returns the account number of an account
   *
   * @return accountNumber, as String
   */
  public String getAccountNumber() {
    return accountNumber;
  }

  /**
   * This method returns the name of an account
   *
   * @return accountName, as String
   */
  public String getAccountName() {
    return accountName;
  }

  public boolean equals(Object o) {
    if (!(o instanceof Account)) {
      return false;
    }

    Account a = (Account) o;
    return accountType == a.accountType &&
        amount.equals(a.amount) &&
        accountNumber.equals(a.accountNumber) &&
        accountName.equals(a.accountName);
  }

  /**
   * This method returns the CSV string of an account
   *
   * @return CSV string of Account, as String
   */
  public String toCSVString() {
    String sb = accountType + CSV_FIELD_DELIMITER +
        amount.toString() + CSV_FIELD_DELIMITER +
        accountNumber + CSV_FIELD_DELIMITER +
        accountName;

    return sb;
  }

  /**
   * This method parses an account CSV string, and instantiates an account
   *
   * @param csvString Account CSV String, as String
   * @return Account that equals the fields of CSV String, as Account
   */
  public static Account fromCSVString(String csvString) {
    String[] fields = csvString.split(CSV_FIELD_DELIMITER);
    AccountCategory accountType = AccountCategory.valueOf(fields[0]);
    BigDecimal amount = new BigDecimal(fields[1]);
    String accountNumber = fields[2];
    String accountName = fields[3];
    String finalAccountName =
        accountName.endsWith(",") ? accountName.substring(0, fields[3].length() - 1)
            : accountName;

    return new AccountBuilder().amount(amount)
        .accountCategory(accountType)
        .accountNumber(accountNumber)
        .accountName(finalAccountName)
        .build();
  }

  /**
   * This class is a builder class for Account
   */
  public static class AccountBuilder {

    private AccountCategory accountType;
    // Funds in the account, as BigDecimal
    private BigDecimal amount;
    // Unique reference of account in the bank system, as String
    private String accountNumber;
    // Name of the account assigned by a user, as String
    private String accountName;

    public Account build() {
      if (!validateBuilder()) {
        return new Account(this);
      } else {
        throw new IllegalStateException("Account not fully defined during build");
      }
    }

    public AccountBuilder accountCategory(AccountCategory accountType) {
      this.accountType = accountType;
      return this;
    }

    public AccountBuilder amount(BigDecimal amount) {
      if (amount.intValue() < 0) {
        throw new IllegalArgumentException("Amount needs to be zero or positive");
      }
      this.amount = amount;
      return this;
    }

    public AccountBuilder accountNumber(String accountNumber) {
      this.accountNumber = accountNumber;
      return this;
    }

    public AccountBuilder accountName(String accountName) {
      if (accountName.isBlank()) {
        this.accountName = accountType.toString();
      } else {
        this.accountName = accountName;
      }
      return this;
    }

    private boolean validateBuilder() {
      return this.accountType == null || this.amount == null || this.accountNumber == null
          || this.accountName == null;
    }
  }
}
