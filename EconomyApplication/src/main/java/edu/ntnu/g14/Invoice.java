package edu.ntnu.g14;

import java.math.BigDecimal;
import java.util.Date;

public class Invoice {
    private Date dueDate;
    private final String invoiceID;
    private BigDecimal amount;
    private String recipientAccountNumber;
    private String orgNumber;

    public Invoice(final Date dueDate, final BigDecimal amount, String recipientAccountNumber, String orgNumber) {
        if (amount.intValue() < 1) {
            throw new IllegalArgumentException("The invoice must have a value greater than 1");
        }
        if (recipientAccountNumber.isBlank()) {
            throw new IllegalArgumentException("The recipients account number must be entered!");
        }
        else if (recipientAccountNumber.length() < 14) {
            throw new IllegalArgumentException("The recipients account number must contain 11 digits, " +
                    "seperated by 2 dots");
        }
        if (dueDate.before(new Date())) {
            throw new IllegalArgumentException("This date has already passed");
        }
        this.dueDate = dueDate;
        this.amount = amount;
        this.recipientAccountNumber = recipientAccountNumber;
        this.orgNumber = orgNumber; //TODO: Explore what organization number is and what it consists of and implement a setter
        this.invoiceID = null; // TODO: Explore what an invoice ID should consist of and ultimately how it should be built
    }

    public boolean changeRecipientAccountNumber(String newRecipientAccountNumber) {
        if (recipientAccountNumber.isBlank()) {
            throw new IllegalArgumentException("The recipients account number must be entered!");
        }
        else if (recipientAccountNumber.length() < 14) {
            throw new IllegalArgumentException("The recipients account number must contain 11 digits, " +
                    "seperated by 2 dots");
        }
        this.recipientAccountNumber = newRecipientAccountNumber;
        return true;
    }
    public boolean changeDueDate(final Date newDueDate) {
        if (newDueDate.before(new Date())) {
            throw new IllegalArgumentException("This date has already passed");
        }
        this.dueDate = newDueDate;
        return true;
    }
    public boolean changeAmount(final BigDecimal newAmount) {
        if (amount.intValue() < 1) {
            throw new IllegalArgumentException("The invoice must have a value greater than 1");
        }
        this.amount = newAmount;
        return true;
    }
    public String getOrgNumber() {
        return orgNumber;
    }

    public String getRecipientAccountNumber() {
        return recipientAccountNumber;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public String getInvoiceId() {
        return invoiceID;
    }
}
