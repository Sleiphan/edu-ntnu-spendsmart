package edu.ntnu.g14;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class AccountWithProperty {
    private StringProperty accountType;
    private StringProperty amount;
    private StringProperty accountNumber;
    private StringProperty accountName;

    AccountWithProperty(String accountType, String amount, String accountName, String accountNumber) {
        this.accountType = new SimpleStringProperty(accountType);
        this.amount = new SimpleStringProperty(amount);
        this.accountName = new SimpleStringProperty(accountName);
        this.accountNumber = new SimpleStringProperty(accountNumber);
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
        return accountType.get();
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
