package edu.ntnu.g14;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * This class represents an account in the bank system and that a user has.
 */
public class Account {
    // Type of account, as AccountCategory
    private final AccountCategory accountType;
    // Funds in the account, as BigDecimal
    private BigDecimal amount;
    // Unique reference of account in the bank system, as String
    private final String accountNumber;
    // Name of the account assigned by a user, as String
    private String accountName;

    /**
     * This constructor fascilitates the creation of instances of this class
     * @param accountType Type of account, as AccountCategory
     * @param amount Funds in the account, as BigDecimal
     * @param accountNumber Unique reference of account in the bank system, as String
     * @param accountName Name of the account assigned by a user, as String
     */
    public Account(final AccountCategory accountType, final BigDecimal amount, final String accountNumber,
                   final String accountName) {
        this.accountType = accountType;
        if (amount.intValue() < 0) {
            throw new IllegalArgumentException("Amount needs to be zero or positive");
        } else {
            this.amount = amount;
        }
        // TODO: Check if accountName is occupied
        if (accountName.isBlank()) {
            this.accountName = accountType.toString();
        } else {
            this.accountName = accountName;
        }
        // TODO: Check if account with this accountNumber already exists
        if (accountNumber.length() < 13) {
            throw new IllegalArgumentException("Account number must contain 11 digits, seperated by 2 dots");
        } else {
            this.accountNumber = accountNumber;
        }
    }

    /**
     * This method removes an amount from this account
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
     * @param augend Augend, as BigDecimal
     */
    public void addAmount(BigDecimal augend) {
        this.amount = this.amount.add(augend);
    }

    /**
     * This method returns the account type of an account
     * @return accountType, as AccountCategory
     */
    public AccountCategory getAccountType() {
        return accountType;
    }

    /**
     * This method changes the name of an account. The new name must be unique to a users accounts
     * @param newName
     * @return true if name change is successful, false if not
     */
    public boolean changeAccountName(String newName) {
        // TODO: Check if accountName is already occupied
        if (newName.isBlank()) {
            throw new IllegalArgumentException("Account name cannot be left blank");
        }
        this.accountName = newName;
        return true;
    }

    /**
     * This method returns the amount in an account
     * @return amount, as BigDecimal
     */
    public BigDecimal getAmount() {
        return amount;
    }

    /**
     * This method returns the account number of an account
     * @return accountNumber, as String
     */
    public String getAccountNumber() {
        return accountNumber;
    }

    /**
     * This method returns the name of an account
     * @return accountName, as String
     */
    public String getAccountName() {
        return accountName;
    }
}
