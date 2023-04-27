package edu.ntnu.g14.model;

import edu.ntnu.g14.dao.AccountDAO;
import edu.ntnu.g14.dao.BudgetDAO;
import edu.ntnu.g14.dao.InvoiceDAO;
import edu.ntnu.g14.dao.TransactionDAO;
import edu.ntnu.g14.dao.UserDAO;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.math.BigDecimal;
import java.nio.channels.FileChannel;
import java.time.LocalDate;
import java.time.Month;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class FileManagementTest {

  private final String[] testPaths = new String[]{
      "transactionsTest.txt",
      "accountsTest.txt",
      "invoicesTest.txt",
      "budgetsTest.txt",
      "usersTest.txt"
  };

  @Test
  public void deleteUser() {
    User u1 = createTestUser1();
    User u2 = createTestUser2();

    User copy = null;

    try {
      FileManagement.writeNewUser(u1);
      FileManagement.writeNewUser(u2);
      FileManagement.deleteUser(u1.getLoginInfo().getUserId());

      copy = FileManagement.readUser(u2.getLoginInfo().getUserId());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    assert u2.equals(copy);
  }


  private User createTestUser1() {
    String email = "some@mail.biz";
    String lastName = "Doe";
    String firstName = "John";
    String name = "J. Doe";
    String pass = "Password123";
    String id = "User10001";

    Account account1 = new Account(AccountCategory.SAVINGS_ACCOUNT, new BigDecimal("450709.71"),
        "1203.45.70098", "Savings");
    Account account2 = new Account(AccountCategory.SAVINGS_ACCOUNT, new BigDecimal("2073.01"),
        "1040.45.71014", "Spending");

    Invoice invoice = new Invoice(LocalDate.of(3023, Month.AUGUST, 14), new BigDecimal("459.97"),
        "1501.45.31121", "Stuff");

    Transaction t1 = new Transaction(account1.getAccountNumber(), account2.getAccountNumber(),
        new BigDecimal("250.00"), "Top up for groceries", LocalDate.of(2023, Month.APRIL, 10),
        BudgetCategory.FOOD_AND_DRINK);
    Transaction t2 = new Transaction(account2.getAccountNumber(), "2404.43.55015",
        new BigDecimal("212.78"), "Groceries", LocalDate.of(2023, Month.APRIL, 10),
        BudgetCategory.FOOD_AND_DRINK);
    Transaction t3 = new Transaction(account1.getAccountNumber(), "2404.43.55015",
        new BigDecimal("13.00"), "Forgot a pack of chewing gum",
        LocalDate.of(2023, Month.APRIL, 10), BudgetCategory.LEISURE);

    Budget budget = new Budget((byte) 23, GenderCategory.MALE);
    budget.addBudgetItem(
        new BudgetItem(new BigDecimal("3100"), "Food", BudgetCategory.FOOD_AND_DRINK));
    budget.addBudgetItem(new BudgetItem(new BigDecimal("800"), "Leisure", BudgetCategory.LEISURE));

    User u = new User(new Account[]{account1, account2},
        new Invoice[]{invoice},
        new Login(name, pass, id),
        email,
        lastName,
        firstName,
        new Transaction[]{t1, t2, t3},
        budget
    );

    return u;
  }

  private User createTestUser2() {
    String email = "perrivuar@gmail.com";
    String lastName = "Baldwin";
    String firstName = "Perry";
    String name = "Perry";
    String pass = "Password123";
    String id = "User10002";

    Account account1 = new Account(AccountCategory.SAVINGS_ACCOUNT, new BigDecimal("450709.71"),
        "1203.45.70098", "Savings");
    Account account2 = new Account(AccountCategory.SAVINGS_ACCOUNT, new BigDecimal("2073.01"),
        "1040.45.71014", "Spending");

    Invoice invoice = new Invoice(LocalDate.of(3023, Month.AUGUST, 14), new BigDecimal("459.97"),
        "1501.45.31121", "Stuff");

    Transaction t1 = new Transaction(account1.getAccountNumber(), account2.getAccountNumber(),
        new BigDecimal("250.00"), "Top up for groceries", LocalDate.of(2023, Month.APRIL, 10),
        BudgetCategory.FOOD_AND_DRINK);
    Transaction t2 = new Transaction(account2.getAccountNumber(), "2404.43.55015",
        new BigDecimal("212.78"), "Groceries", LocalDate.of(2023, Month.APRIL, 10),
        BudgetCategory.FOOD_AND_DRINK);
    Transaction t3 = new Transaction(account1.getAccountNumber(), "2404.43.55015",
        new BigDecimal("13.00"), "Forgot a pack of chewing gum",
        LocalDate.of(2023, Month.APRIL, 10), BudgetCategory.LEISURE);

    Budget budget = new Budget((byte) 23, GenderCategory.MALE);
    budget.addBudgetItem(
        new BudgetItem(new BigDecimal("3100"), "Food", BudgetCategory.FOOD_AND_DRINK));
    budget.addBudgetItem(new BudgetItem(new BigDecimal("800"), "Leisure", BudgetCategory.LEISURE));

    User u = new User(new Account[]{account1, account2},
        new Invoice[]{invoice},
        new Login(name, pass, id),
        email,
        lastName,
        firstName,
        new Transaction[]{t1, t2, t3},
        budget
    );

    return u;
  }


  @BeforeAll
  public void initialize() {
    try {
      for (String path : testPaths) {
        File f = new File(path);
        f.createNewFile();
        f.deleteOnExit();
      }

      // Initialise data access objects
      FileManagement.TRANSACTION_DAO = new TransactionDAO(testPaths[0],
          FileManagement.DATA_CHARSET);
      FileManagement.ACCOUNT_DAO = new AccountDAO(testPaths[1], FileManagement.DATA_CHARSET);
      FileManagement.INVOICE_DAO = new InvoiceDAO(testPaths[2], FileManagement.DATA_CHARSET);
      FileManagement.BUDGET_DAO = new BudgetDAO(testPaths[3], FileManagement.DATA_CHARSET);
      FileManagement.USER_DAO = new UserDAO(testPaths[4], FileManagement.DATA_CHARSET);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @BeforeEach
  public void resetFiles() {
    for (String path : testPaths) {
      try {
        RandomAccessFile raf = new RandomAccessFile(path, "rw");
        FileChannel chn = raf.getChannel();
        chn.truncate(0);
        chn.close();
        raf.close();
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
  }
}
