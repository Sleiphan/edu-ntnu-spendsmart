package edu.ntnu.g14;

import java.time.Instant;
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

    @Override
    public boolean equals(Object o) {
      if (!(o instanceof Transaction))
        return false;

      Transaction t = (Transaction) o;
      if (amount != t.amount)
        return false;
      if (!toAccountId.equals(t.toAccountId))
        return false;
      if (!fromAccountId.equals(t.fromAccountId))
        return false;
      if (!description.equals(t.description))
        return false;
      if (!dateOfTransaction.equals(t.dateOfTransaction))
        return false;

      return true;
    }

    public static final String CSV_FIELD_DELIMITER = ";";
    public String toCSVString() {
      String sb = fromAccountId + CSV_FIELD_DELIMITER +
              toAccountId + CSV_FIELD_DELIMITER +
              amount + CSV_FIELD_DELIMITER +
              description + CSV_FIELD_DELIMITER +
              dateOfTransaction.toInstant().toEpochMilli();
      return sb;
    }

    public static Transaction fromCSVString(String csvString) {
      String[] fields = csvString.split(CSV_FIELD_DELIMITER);
      String fromAccountId   = fields[0];
      String toAccountId     = fields[1];
      short amount           = Short.parseShort(fields[2]);
      String description     = fields[3];
      Date dateOfTransaction = Date.from(Instant.ofEpochMilli(Long.parseLong(fields[4])));

      return new Transaction(fromAccountId, toAccountId, amount, description, dateOfTransaction);
    }
}
