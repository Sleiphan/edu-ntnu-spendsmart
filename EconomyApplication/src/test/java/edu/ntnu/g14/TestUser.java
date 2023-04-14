package edu.ntnu.g14;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

public class TestUser {

    @Test
    public void testUser() {
    Account testAccount = new Account(AccountCategory.SAVINGS_ACCOUNT, BigDecimal.valueOf(100000), "1256.65.56605", "kortkonto");
    Account[] accounts = new Account[1];
    accounts[0] = testAccount;
    Invoice testInvoice = new Invoice(LocalDate.of(2023, 12, 4), BigDecimal.valueOf(100000), "1256.65.56605", "Comment");
    Invoice[] invoices = new Invoice[1];
    invoices[0] = testInvoice;
    Transaction testTransaction = new Transaction("1256.65.56605", "1256.65.56605", BigDecimal.valueOf(1000), "brukte penger", LocalDate.of(2023, 12, 4), BudgetCategory.ALCOHOL_AND_TOBACCO);
    Transaction[] transactions = new Transaction[1];
    transactions[0] = testTransaction;
    Budget testBudget = new Budget(Byte.parseByte("90"), GenderCategory.FEMALE);
    Login loginInfo = new Login("test", "test", "Test#1");
    User testUser = new User(accounts, invoices, loginInfo, "test@mail.com", "knut", "knut", transactions, testBudget);

    System.out.println(testUser.getEmail() + "\n"
    + testUser.getFirstName() + "\n"
    + testUser.getFullName() + "\n"
    + testUser.getLastName() + "\n"
    + testUser.getAccounts() + "\n"
    + testUser.getAccountsAsList() + "\n"
    + testUser.getAllInvoices() + "\n"
    + testUser.getBudget() + "\n"
    + testUser.getLoginInfo().toString() + "\n"
    + testUser.getTransactions() + "\n"
    + testUser.getTransactionsAsList() + "\n");
    }
}
