package edu.ntnu.g14;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * The invoice class represents an invoice.
 */
public class Invoice {
    private LocalDate dueDate;
    private BigDecimal amount;
    private String recipientAccountNumber;
    private String comment;
    private static final DateTimeFormatter dateFormatter = 
    DateTimeFormatter.ofPattern("dd/MM/yyyy");

    /**
    * Constructor for an Invoice object with a due date, an amount, 
    * a recipient account number, and a comment.
    * Throws IllegalArgumentException if amount is less than 1, 
    * recipient account number is blank or does not contain 11 digits 
    * separated by 2 dots, or comment is null.
    *
    * @param dueDate                  the due date of the invoice
    * @param amount                   the amount of the invoice
    * @param recipientAccountNumber   the recipient account number of the invoice
    * @param comment                  the comment of the invoice
    * @throws IllegalArgumentException if amount is less than 1, 
    * recipient account number is blank or does not contain 11 digits separated by 2 dots, or comment is null.
    */
    public Invoice(final LocalDate dueDate, final BigDecimal amount, String recipientAccountNumber, String comment) {
        if (amount.intValue() < 1)
            throw new IllegalArgumentException("The invoice must have a value greater than 1");

        if (recipientAccountNumber.isBlank())
            throw new IllegalArgumentException("The recipients account number must be entered!");

        else if (recipientAccountNumber.length() != 13)
            throw new IllegalArgumentException("The recipients account number must contain 11 digits, " +
                    "seperated by 2 dots");

        if (comment == null)
            throw new IllegalArgumentException("CID/Comment cannot be null");

        this.dueDate = dueDate;
        this.amount = amount;
        this.recipientAccountNumber = recipientAccountNumber;
        this.comment = comment;
    }

    /**
    * Changes the recipient account number of the invoice.
    * Throws IllegalArgumentException if the new recipient account number is blank or does not contain 11 digits separated by 2 dots.
    *
    * @param newRecipientAccountNumber the new recipient account number of the invoice
    * @return true if the recipient account number was successfully changed
    * @throws IllegalArgumentException if the new recipient account number is blank or does not contain 11 digits separated by 2 dots.
    */
    public boolean changeRecipientAccountNumber(String newRecipientAccountNumber) {
        if (recipientAccountNumber.isBlank()) {
            throw new IllegalArgumentException("The recipients account number must be entered!");
        }
        else if (recipientAccountNumber.length() != 13) {
            throw new IllegalArgumentException("The recipients account number must contain 11 digits, " +
                    "seperated by 2 dots");
        }
        this.recipientAccountNumber = newRecipientAccountNumber;
        return true;
    }

    /**
    * Changes the due date of the invoice.
    *
    * @param newDueDate the new due date for the invoice
    * @return true if the due date is successfully changed
    * @throws IllegalArgumentException if the new due date is in the past
    */
    public boolean changeDueDate(final LocalDate newDueDate) {
        if (newDueDate.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("This date has already passed");
        }
        this.dueDate = newDueDate;
        return true;
    }

    /**
    * Changes the amount of the invoice.
    *
    * @param newAmount the new amount for the invoice
    * @return true if the amount is successfully changed
    * @throws IllegalArgumentException if the new amount is less than 1
    */
    public boolean changeAmount(final BigDecimal newAmount) {
        if (amount.intValue() < 1) {
            throw new IllegalArgumentException("The invoice must have a value greater than 1");
        }
        this.amount = newAmount;
        return true;
    }

    /**
    * Returns a string representation of the invoice.
    *
    * @return a string representation of the invoice
    */
    public String toString() {
        return "[" + dueDate + "] " + ":" + amount + "kr \"" + comment + "\"";
    }

    /**
    * Returns the recipient account number for the invoice.
    *
    * @return the recipient account number
    */
    public String getRecipientAccountNumber() {
        return recipientAccountNumber;
    }

    /**
    * Returns the amount for the invoice.
    *
    * @return the amount
    */
    public BigDecimal getAmount() {
        return amount;
    }

    /**
    * Returns the due date for the invoice.
    *
    * @return the due date
    */
    public LocalDate getDueDate() {
        return dueDate;
    }

    /**
    * Returns the comment for the invoice.
    *
    * @return the comment
    */
    public String getComment() { return comment; }

    public static final String CSV_FIELD_DELIMITER = ";";
    
    /**
    * Returns a CSV string representation of the invoice.
    *
    * @return a CSV string representation of the invoice
    */
    public String toCSVString() {
        return dueDate.format(dateFormatter) + CSV_FIELD_DELIMITER +
                        amount.toPlainString() + CSV_FIELD_DELIMITER +
                        recipientAccountNumber + CSV_FIELD_DELIMITER +
                        "\"" + comment + "\"";
    }

    /**
    * Creates a new invoice object from a CSV string representation.
    *
    * @param csvString the CSV string representation of the invoice
    * @return a new invoice object created from the CSV string representation
    */
    public static Invoice fromCSVString(String csvString) {
      String[] fields = csvString.split(CSV_FIELD_DELIMITER + "(?!\\s)");
      String recipient   = fields[2];
      BigDecimal amount           = new BigDecimal(fields[1]);
      LocalDate dateOfTransaction = LocalDate.parse(fields[0], dateFormatter);
      String comment = fields[3].substring(1, fields[3].length() - 1);

      return new Invoice(dateOfTransaction, amount, recipient, comment);
    }
}
