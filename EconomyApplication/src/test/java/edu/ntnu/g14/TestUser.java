package edu.ntnu.g14;

import static org.junit.jupiter.api.Assertions.*;

import edu.ntnu.g14.model.Account;
import edu.ntnu.g14.model.AccountCategory;
import edu.ntnu.g14.model.Budget;
import edu.ntnu.g14.model.BudgetCategory;
import edu.ntnu.g14.model.GenderCategory;
import edu.ntnu.g14.model.Invoice;
import edu.ntnu.g14.model.Login;
import edu.ntnu.g14.model.Transaction;
import edu.ntnu.g14.model.User;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TestUser {
  User testUser;
  Account testAccount;
  Invoice testInvoice;
  Transaction testTransaction;
  Budget testBudget;
  Login loginInfo;

  @BeforeEach
  public void setUp() {
    testAccount = new Account.AccountBuilder().accountCategory(
            AccountCategory.SAVINGS_ACCOUNT).amount(BigDecimal.valueOf(100000))
        .accountNumber("1256.65.56605").accountName("kortkonto").build();
    Account[] accounts = new Account[1];
    accounts[0] = testAccount;
    testInvoice = new Invoice(LocalDate.of(2023, 12, 4), BigDecimal.valueOf(100000),
        "1256.65.56605", "Comment");
    Invoice[] invoices = new Invoice[1];
    invoices[0] = testInvoice;
    testTransaction = new Transaction("1256.65.56605", "1256.65.56605",
        BigDecimal.valueOf(1000), "brukte penger", LocalDate.now().minusDays(15),
        BudgetCategory.ALCOHOL_AND_TOBACCO);
    Transaction[] transactions = new Transaction[1];
    transactions[0] = testTransaction;
    testBudget = new Budget(Byte.parseByte("90"), GenderCategory.FEMALE);
    loginInfo = new Login("test", "test", "Test#1");
    testUser = new User(accounts, invoices, loginInfo, "test@mail.com", "knut", "knut",
        transactions, testBudget);
  }

  @Test
  public void testGetEmail() {
    assertEquals("test@mail.com", testUser.getEmail());
  }

  @Test
  public void testGetFirstName() {
    assertEquals("knut", testUser.getFirstName());
  }
  @Test
  public void testGetLastName() {
    assertEquals("knut", testUser.getLastName());
  }

  @Test
  public void testGetFullName() {
    assertEquals("knut knut", testUser.getFullName());
  }

  @Test
  public void testGetAccounts() {
    Account[] accounts = testUser.getAccounts();
    assertEquals(1, accounts.length);
    assertEquals(testAccount, accounts[0]);
  }

  @Test
  public void testGetAccountsAsList() {
    List<Account> accounts = testUser.getAccountsAsList();
    assertEquals(1, accounts.size());
    assertEquals(testAccount, accounts.get(0));
  }

  @Test
  public void testGetAllInvoices() {
    List<Invoice> invoices = testUser.getAllInvoices();
    assertEquals(1, invoices.size());
    assertEquals(testInvoice, invoices.get(0));
  }

  @Test
  public void testGetBudget() {
    assertEquals(testBudget, testUser.getBudget());
  }

  @Test
  public void testGetLoginInfo() {
    assertEquals(loginInfo, testUser.getLoginInfo());
  }

  @Test
  public void testGetTransactions() {
    Transaction[] transactions = testUser.getTransactions();
    assertEquals(1, transactions.length);
    assertEquals(testTransaction, transactions[0]);
  }

  @Test
  public void testGetTransactionsAsList() {
    List<Transaction> transactions = testUser.getTransactionsAsList();
    assertEquals(1, transactions.size());
    assertEquals(testTransaction, transactions.get(0));
  }

  @Test
  public void testAddAccount() {
    Account newAccount = new Account.AccountBuilder().accountCategory(
            AccountCategory.CHECKING_ACCOUNT).amount(BigDecimal.valueOf(50000))
        .accountNumber("1256.65.56606").accountName("newAccount").build();
    testUser.addAccount(newAccount);
    assertEquals(2, testUser.getAccounts().length);
    assertTrue(testUser.getAccountsAsList().contains(newAccount));
  }

  @Test
  public void testRemoveAccount() {
    testUser.removeAccount(testAccount);
    assertEquals(0, testUser.getAccounts().length);
    assertFalse(testUser.getAccountsAsList().contains(testAccount));
  }

  @Test
  public void testAddTransaction() {
    Transaction newTransaction = new Transaction("1256.65.56605", "1256.65.56606",
        BigDecimal.valueOf(500), "newTransaction", LocalDate.of(2023, 12, 5),
        BudgetCategory.ALCOHOL_AND_TOBACCO);
    testUser.addTransaction(newTransaction);
    assertEquals(2, testUser.getTransactions().length);
    assertTrue(testUser.getTransactionsAsList().contains(newTransaction));
  }
  // Add the following test methods to your TestUser class

  @Test
  public void testCheckIfAccountNameIsOccupied() {
    assertTrue(testUser.checkIfAccountNameIsOccupied("kortkonto"));
    assertFalse(testUser.checkIfAccountNameIsOccupied("nonexistentAccountName"));
  }

  @Test
  public void testCheckIfAccountNumberIsOccupied() {
    assertTrue(testUser.checkIfAccountNumberIsOccupied("1256.65.56605"));
    assertFalse(testUser.checkIfAccountNumberIsOccupied("1234.56.78901"));
  }

  @Test
  public void testGetAccountWithAccountName() {
    Account retrievedAccount = testUser.getAccountWithAccountName("kortkonto");
    assertEquals(testAccount, retrievedAccount);
  }

  @Test
  public void testGetAccountWithAccountNumber() {
    Account retrievedAccount = testUser.getAccountWithAccountNumber("1256.65.56605");
    assertEquals(testAccount, retrievedAccount);
  }

  @Test
  public void testAmountAllAccounts() {
    String amount = testUser.amountAllAccounts();
    assertEquals("100 000.00 kr", amount);
  }

  @Test
  public void testExpensesLastYear() {
    String expenses = testUser.expensesLastYear();
    assertEquals("0.00 kr", expenses);
  }

  @Test
  public void testIncomeLastYear() {
    String income = testUser.incomeLastYear();
    assertEquals("0.00 kr", income);
  }

  @Test
  public void testExpensesLast30Days() {
    String expenses = testUser.expensesLast30Days();
    assertEquals("1 000.00 kr", expenses);
  }

  @Test
  public void testIncomeLast30Days() {
    String income = testUser.incomeLast30Days();
    assertEquals("1 000.00 kr", income);
  }

  @Test
  public void testGetTotalExpenseOfCategoryLast30Days() {
    double totalExpense = testUser.getTotalExpenseOfCategoryLast30Days("Alcohol and Tobacco");
    assertEquals(1000.0, totalExpense, 0.001);
  }

  @Test
  public void testGetTotalIncomeOfCategoryLast30Days() {
    double totalIncome = testUser.getTotalIncomeOfCategoryLast30Days("Alcohol and Tobacco");
    assertEquals(1000, totalIncome, 0.001);
  }

  @Test
  public void testGetTotalExpenseOfCategoryLastYear() {
    double totalExpense = testUser.getTotalExpenseOfCategoryLastYear("Alcohol and Tobacco");
    assertEquals(0, totalExpense, 0.001);
  }

  @Test
  public void testGetTotalIncomeOfCategoryLastYear() {
    double totalIncome = testUser.getTotalIncomeOfCategoryLastYear("Alcohol and Tobacco");
    assertEquals(0, totalIncome, 0.001);
  }

}
