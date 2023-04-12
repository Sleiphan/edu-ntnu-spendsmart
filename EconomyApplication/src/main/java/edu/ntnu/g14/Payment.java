package edu.ntnu.g14;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Payment extends Transaction {
    private String CID;
    private LocalDate dueDate;

    public Payment(String fromAccountId, BigDecimal amount, String description, String toAccountId, LocalDate dueDate, String CID, LocalDate dateOfTransaction) {
        super(fromAccountId, toAccountId, amount, description, dateOfTransaction);

        if (CID == null)
            throw new IllegalArgumentException("Customer ID cannot be null");
        if (dueDate.isBefore(LocalDate.now()))
            throw new IllegalArgumentException("This date has already passed");
        this.CID = CID;
        this.dueDate = dueDate;
    }

    private String getCID() {
        return CID;
    }

    private LocalDate getDueDate() {
        return dueDate;
    }

    private void setCID(String CID) {
        this.CID = CID;
    }

    private void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }
}
