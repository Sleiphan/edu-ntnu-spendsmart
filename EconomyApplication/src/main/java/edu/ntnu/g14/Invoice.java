package edu.ntnu.g14;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Invoice {
    private LocalDate dueDate;
    private BigDecimal amount;
    private String recipientAccountNumber;
    private String comment;
    private static final DateTimeFormatter dateFormatter = 
    DateTimeFormatter.ofPattern("dd/MM/yyyy");
    public Invoice(final LocalDate dueDate, final BigDecimal amount, String recipientAccountNumber, String comment) {
        if (amount.intValue() < 1)
            throw new IllegalArgumentException("The invoice must have a value greater than 1");

        if (recipientAccountNumber.isBlank())
            throw new IllegalArgumentException("The recipients account number must be entered!");

        else if (recipientAccountNumber.length() != 13)
            throw new IllegalArgumentException("The recipients account number must contain 11 digits, " +
                    "seperated by 2 dots");

        if (dueDate.isBefore(LocalDate.now()))
            throw new IllegalArgumentException("This date has already passed");

        if (comment == null)
            throw new IllegalArgumentException("CID/Comment cannot be null");

        this.dueDate = dueDate;
        this.amount = amount;
        this.recipientAccountNumber = recipientAccountNumber;
        this.comment = comment;
    }

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

    public String getComment() { return comment; }

    public static final String CSV_FIELD_DELIMITER = ";";
    public String toCSVString() {
        return dueDate.format(dateFormatter) + CSV_FIELD_DELIMITER +
                        amount.toPlainString() + CSV_FIELD_DELIMITER +
                        recipientAccountNumber + CSV_FIELD_DELIMITER +
                        "\"" + comment + "\"";
    }

    public static Invoice fromCSVString(String csvString) {
      String[] fields = csvString.split(CSV_FIELD_DELIMITER + "(?!\\s)");
      String recipient   = fields[2];
      BigDecimal amount           = new BigDecimal(fields[1]);
      LocalDate dateOfTransaction = LocalDate.parse(fields[0], dateFormatter);
      String comment = fields[3].substring(1, fields[3].length() - 1);

      return new Invoice(dateOfTransaction, amount, recipient, comment);
    }
}
