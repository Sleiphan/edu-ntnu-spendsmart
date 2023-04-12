package edu.ntnu.g14;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.math.BigDecimal;

public class AccountWithProperty {
    private final StringProperty accountType;
    private final StringProperty amount;
    private final StringProperty accountNumber;
    private final StringProperty accountName;

    public AccountWithProperty(String accountType, String amount, String accountName, String accountNumber) {
        this.accountType = new SimpleStringProperty(accountType);
        this.amount = new SimpleStringProperty(amount);
        this.accountName = new SimpleStringProperty(accountName);
        this.accountNumber = new SimpleStringProperty(accountNumber);
    }
    /**
     * This method removes an amount from this account
     * @param subtrahend Subtrahend, as BigDecimal
     * @return true if account has sufficient funds, false if not
     */
    public boolean removeAmount(BigDecimal subtrahend) {
        if (subtrahend.floatValue() > 0) {
            throw new IllegalArgumentException("Subtrahend cannot be positive, use addAmount method.");
        }
        BigDecimal amount = new BigDecimal(getAmount());
        if (amount.subtract(subtrahend).floatValue() < 0) {
            throw new IllegalArgumentException("Account does not have sufficient funds");
        }
        setAmount(amount.subtract(subtrahend).toString());
        return true;
    }

    /**
     * This method adds an amount to this account
     * @param augend Augend, as BigDecimal
     */
    public void addAmount(BigDecimal augend) {
        if (augend.floatValue() < 0) {
            throw new IllegalArgumentException("Augend cannot be negative, use removeAmount method.");
        }
        BigDecimal amount = new BigDecimal(getAmount());
        setAmount(amount.add(augend).toString());
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
        setAccountName(newName);
        return true;
    }

    public StringProperty accountNumberProperty() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber.set(accountNumber);
    }

    public String getAccountNumber() {
        return accountNumber.get();
    }

    public StringProperty amountProperty() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount.set(amount);
    }

    public String getAmount() {
        return amount.get();
    }

    public StringProperty accountTypeProperty() {
        return accountType;
    }
    public String getAccountType() {
        switch (this.accountType.get()) {
            case "Savings Account":
                return AccountCategory.SAVINGS_ACCOUNT.toString();
            case "Spendings Account":
                return AccountCategory.SPENDING_ACCOUNT.toString();
            case "Pensions account":
                return AccountCategory.PENSION_ACCOUNT.toString();
            case "Other":
                return AccountCategory.OTHER.toString();
            default:
                return null;
        }
    }

    public void setAccountType(String accountType) {
        this.accountType.set(accountType);
    }

    public StringProperty accountNameProperty() {
        return this.accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName.set(accountName);
    }

    public String getAccountName() {
        return accountName.get();
    }
}
