package edu.ntnu.g14;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Invoice {
    private LocalDate dueDate;
    private final String invoiceID;
    private BigDecimal amount;
    private String recipientAccountNumber;
    public Invoice(final LocalDate dueDate, final BigDecimal amount, String recipientAccountNumber) {
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
        if (dueDate.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("This date has already passed");
        }
        this.dueDate = dueDate;
        this.amount = amount;
        this.recipientAccountNumber = recipientAccountNumber;
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
    public boolean changeDueDate(final LocalDate newDueDate) {
        if (newDueDate.isBefore(LocalDate.now())) {
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
    public String getRecipientAccountNumber() {
        return recipientAccountNumber;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public String getInvoiceId() {
        return invoiceID;
    }
}
