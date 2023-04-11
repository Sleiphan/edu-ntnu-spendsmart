package edu.ntnu.g14;

import javafx.beans.property.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class TransactionWithProperty {
    private static final DateTimeFormatter dateFormatter =
            DateTimeFormatter.ofPattern("dd/MM/yyyy");
    //These private fields should maybe be public instead?
    private final StringProperty fromAccountId;
    private final StringProperty toAccountId;
    private final StringProperty amount;
    private final StringProperty description;

    private final ObjectProperty<LocalDate> dateOfTransaction = new SimpleObjectProperty<>();

    public TransactionWithProperty(String fromAccountId, String toAccountId, String amount, String description, LocalDate dateOfTransaction, ObjectProperty<LocalDate> dateOfTransaction1) {
        this.fromAccountId = new SimpleStringProperty(fromAccountId);
        this.toAccountId = new SimpleStringProperty(toAccountId);
        this.amount = new SimpleStringProperty(amount);
        this.description = new SimpleStringProperty(description);
        this.dateOfTransaction.set(dateOfTransaction); {
        };
    }

    public StringProperty getFromAccountIdProperty() {
        return fromAccountId;
    }

    public String getFromAccountId() {
        return fromAccountId.get();
    }

    public StringProperty getToAccountIdProperty() {
        return toAccountId;
    }

    public String getToAccountId() {
        return toAccountId.get();
    }

    public StringProperty getAmountProperty() {
        return amount;
    }
    public String getAmount() {
        return amount.get();
    }

    public StringProperty getDescriptionProperty() {
        return description;
    }

    public String getDescription() {
        return description.get();
    }

    public ObjectProperty<LocalDate> getDateOfTransactionProperty() {
        return dateOfTransaction;
    }

    public LocalDate getDateOfTransaction() {
        return dateOfTransaction.get();
    }

    public void setFromAccountId(String fromAccountId) {
        this.fromAccountId.set(fromAccountId);
    }

    public void setToAccountId(String toAccountId) {
        this.toAccountId.set(toAccountId);
    }

    public void setAmount(String amount) {
        this.amount.set(amount);
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

    public void setDateOfTransaction(LocalDate dateOfTransaction) {
        this.dateOfTransaction.set(dateOfTransaction);
    }

}
