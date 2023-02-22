package edu.ntnu.g14;

import java.util.Date;

public class Transaction {

  //These private fields should maybe be public instead?
    private String fromAccountId;
    private String toAccountId;
    private short amount;
    private String description;
    private Date dateOfTransaction;

    public Transaction(String fromAccountId, String toAccountId, short amount, String description, Date dateOfTransaction) {
        this.fromAccountId = fromAccountId;
        this.toAccountId = toAccountId;
        this.amount = amount;
        this.description = description;
        this.dateOfTransaction = dateOfTransaction;
    }

    public String getFromAccountId() {
        return fromAccountId;
    }

    public String getToAccountId() {
        return toAccountId;
    }

    public short getAmount() {
        return amount;
    }

    public String getDescription() {
        return description;
    }

    public Date getDateOfTransaction() {
        return dateOfTransaction;
    }

    public void setFromAccountId(String fromAccountId) {
        this.fromAccountId = fromAccountId;
    }

    public void setToAccountId(String toAccountId) {
        this.toAccountId = toAccountId;
    }

    public void setAmount(short amount) {
        this.amount = amount;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDateOfTransaction(Date dateOfTransaction) {
        this.dateOfTransaction = dateOfTransaction;
    }

    //This method needs a better string representation of the transaction
    @Override
    public String toString() {
        return "Transaction{" + "fromAccountId=" + fromAccountId + ", toAccountId=" + toAccountId + ", amount=" + amount + ", description=" + description + ", dateOfTransaction=" + dateOfTransaction + '}';
    }
}
