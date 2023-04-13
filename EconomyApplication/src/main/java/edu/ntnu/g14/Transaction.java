package edu.ntnu.g14;

import edu.ntnu.g14.frontend.ApplicationObjects;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Transaction {
    private final String fromAccountNumber;
    private final String toAccountNumber;
    private final BigDecimal amount;
    private final String description;
    private final LocalDate dateOfTransaction;
    private final BudgetCategory category;

    public Transaction(String fromAccountNumber, String toAccountNumber, BigDecimal amount, String description,
                       LocalDate dateOfTransaction, BudgetCategory category) {
        if (fromAccountNumber.isEmpty())
            throw new IllegalArgumentException("From account ID cannot be empty");
        if (toAccountNumber.isEmpty())
            throw new IllegalArgumentException("To account ID cannot be empty");
        if (description == null)
            throw new IllegalArgumentException("Description cannot be null");
        if (dateOfTransaction == null)
            throw new IllegalArgumentException("Date of transaction cannot be null");
        this.category = category;
        this.fromAccountNumber = fromAccountNumber;
        this.toAccountNumber = toAccountNumber;
        this.amount = amount;
        this.description = description;
        this.dateOfTransaction = dateOfTransaction;
    }

    public String getFromAccountNumber() {
        return fromAccountNumber;
    }

    public String getToAccountNumber() {
        return toAccountNumber;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getDescription() {
        return description;
    }

    public LocalDate getDateOfTransaction() {
        return dateOfTransaction;
    }

    public BudgetCategory getCategory() {
        return category;
    }

    //This method needs a better string representation of the transaction
    @Override
    public String toString() {
        return "Transaction{" + "fromAccountNumber=" + fromAccountNumber + ", toAccountNumber=" + toAccountNumber + ", amount=" + amount + ", description=" + description + ", dateOfTransaction=" + dateOfTransaction + ", category=" + category + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Transaction))
            return false;

        Transaction t = (Transaction) o;
        if (!Objects.equals(amount, t.amount))
            return false;
        if (!toAccountNumber.equals(t.toAccountNumber))
            return false;
        if (!fromAccountNumber.equals(t.fromAccountNumber))
            return false;
        if (!description.equals(t.description))
            return false;
        if (!category.equals(t.category))
            return false;
        return dateOfTransaction.equals(t.dateOfTransaction);
    }

    public static final String CSV_FIELD_DELIMITER = ";";
    public String toCSVString() {
        return dateOfTransaction.format(ApplicationObjects.dateFormatter)
                + CSV_FIELD_DELIMITER +
                amount + CSV_FIELD_DELIMITER +
                        toAccountNumber + CSV_FIELD_DELIMITER +
                        fromAccountNumber + CSV_FIELD_DELIMITER +
                description + CSV_FIELD_DELIMITER + category + ",";
    }

    public static Transaction fromCSVString(String csvString) {
        String[] fields = csvString.split(CSV_FIELD_DELIMITER);
        String fromAccountNumber   = fields[3];
        String toAccountNumber     = fields[2];
        BigDecimal amount      = new BigDecimal(fields[1]);
        String description     = fields[4];
        LocalDate dateOfTransaction = LocalDate.parse(fields[0], ApplicationObjects.dateFormatter);
        String category = fields[5];
        BudgetCategory budgetCategory = category.endsWith(",") ?
                BudgetCategory.valueOf(category.substring(0, category.length() - 1))
                : BudgetCategory.valueOf(category);

        return new Transaction(fromAccountNumber, toAccountNumber, amount, description, dateOfTransaction, budgetCategory);
    }
}
