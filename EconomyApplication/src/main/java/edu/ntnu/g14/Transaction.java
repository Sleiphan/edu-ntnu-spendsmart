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
      if (fromAccountId.isEmpty())
        throw new IllegalArgumentException("From account ID cannot be empty");
      if (toAccountId.isEmpty())
        throw new IllegalArgumentException("To account ID cannot be empty");
      if (amount <= 0)
        throw new IllegalArgumentException("Amount must be greater than 0");
      if (description == null)
        throw new IllegalArgumentException("Description cannot be null");
      if (dateOfTransaction == null)
        throw new IllegalArgumentException("Date of transaction cannot be null");

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
