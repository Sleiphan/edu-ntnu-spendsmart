package edu.ntnu.g14.dao;

import edu.ntnu.g14.Budget;
import edu.ntnu.g14.BudgetCategory;
import edu.ntnu.g14.BudgetItem;
import edu.ntnu.g14.GenderCategory;
import org.junit.jupiter.api.*;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BudgetDAOTest {

  private static final String TEST_FILE_PATH = "budgetTestFile.txt";
  private static final String TEMP_FILE_PATH = TEST_FILE_PATH + ".temp";

  @BeforeEach
  public void resetTestData() {
    Budget budget1 = new Budget((byte) 1, GenderCategory.MALE);
    budget1.setSavings(new BigDecimal("1051738"));
    budget1.setSalary(new BigDecimal("670000"));
    budget1.addBudgetItem(
        new BudgetItem(new BigDecimal("250"), "Candy every saturday", BudgetCategory.LEISURE));
    budget1.addBudgetItem(new BudgetItem(new BigDecimal("300.7"), "Bus", BudgetCategory.TRAVEL));

    Budget budget2 = new Budget((byte) 1, GenderCategory.FEMALE);
    budget2.setSavings(new BigDecimal("200000"));
    budget2.setSalary(new BigDecimal("357000"));
    budget2.addBudgetItem(
        new BudgetItem(new BigDecimal("250"), "Candy every saturday", BudgetCategory.LEISURE));
    budget2.addBudgetItem(new BudgetItem(new BigDecimal("300.7"), "Bus", BudgetCategory.TRAVEL));

    try {
      File file = new File(TEST_FILE_PATH);
      boolean exists = !file.createNewFile();
      if (exists) {
        RandomAccessFile fileKiller = new RandomAccessFile(TEST_FILE_PATH, "rw");
        fileKiller.getChannel().truncate(0);
        fileKiller.close();
      }
      BudgetDAO dao = new BudgetDAO(TEST_FILE_PATH);
      dao.setBudget("olav#1", budget1);
      dao.setBudget("kari#1", budget2);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Test
  public void whole_budget_read_and_write() {
    String userID = "Haakon F. Fjellanger";

    Budget budget = new Budget((byte) 1, GenderCategory.MALE);
    budget.setSavings(new BigDecimal("2"));
    budget.setSalary(new BigDecimal("3"));
    budget.addBudgetItem(new BudgetItem(new BigDecimal("4"), "Expense 1", BudgetCategory.LEISURE));
    budget.addBudgetItem(new BudgetItem(new BigDecimal("5"), "Expense 2", BudgetCategory.TRAVEL));

    Budget copy;
    try {
      BudgetDAO dao = new BudgetDAO(TEST_FILE_PATH);
      dao.setBudget(userID, budget);
      copy = dao.getBudget(userID);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    assert (budget.equals(copy));
  }

  @Test
  public void special_norwegian_characters() {
    String userID = "Håkon F. Fjellanger";

    Budget budget = new Budget((byte) 1, GenderCategory.MALE);
    budget.setSavings(new BigDecimal("2"));
    budget.setSalary(new BigDecimal("3"));
    budget.addBudgetItem(new BudgetItem(new BigDecimal("4"), "Expense 1", BudgetCategory.LEISURE));
    budget.addBudgetItem(new BudgetItem(new BigDecimal("5"), "Expense 2", BudgetCategory.TRAVEL));

    Budget copy;
    try {
      BudgetDAO dao = new BudgetDAO(TEST_FILE_PATH);
      dao.setBudget(userID, budget);
      copy = dao.getBudget(userID);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    assert (budget.equals(copy));
  }

  @AfterAll
  public void deleteTestFiles() {
    try {
      Files.delete(Paths.get(TEST_FILE_PATH));
      Files.delete(Paths.get(TEMP_FILE_PATH));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
