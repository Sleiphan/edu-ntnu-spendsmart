package edu.ntnu.g14;

import java.util.Date;

public class Payment extends Transaction{
    private String CID;
    private Date dueDate;

  public Payment(String fromAccountId, String toAccountId, short amount, String description,
      Date dateOfTransaction, String CID, Date dueDate) {
    super(fromAccountId, toAccountId, amount, description, dateOfTransaction);
    this.CID = CID;
    this.dueDate = dueDate;
  }

  private String getCID() {
    return CID;
  }

  private Date getDueDate() {
    return dueDate;
  }

  private void setCID(String CID) {
    this.CID = CID;
  }

  private void setDueDate(Date dueDate) {
    this.dueDate = dueDate;
  }
}
