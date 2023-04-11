package edu.ntnu.g14.dao;

import edu.ntnu.g14.Budget;
import edu.ntnu.g14.BudgetItem;
import edu.ntnu.g14.ExpenditureCategory;
import edu.ntnu.g14.GenderCategory;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;

public class TestBudgetDAO {

    private static final String TEST_FILES_DIRECTORY = "testfiles/";

    @Test
    public void whole_budget_read_and_write() {
        String filepath = TEST_FILES_DIRECTORY + "accounts.txt";
        String userID = "Haakon F. Fjellanger";

        Budget budget = new Budget((byte) 1, GenderCategory.MALE);
        budget.setSavings(new BigDecimal("2"));
        budget.setSalary(new BigDecimal("3"));
        budget.addBudgetItem(new BudgetItem(new BigDecimal("4"), "Expense 1", ExpenditureCategory.LEISURE));
        budget.addBudgetItem(new BudgetItem(new BigDecimal("5"), "Expense 2", ExpenditureCategory.TRAVEL));

        Budget copy;
        try {
            BudgetDAO dao = new BudgetDAO(filepath);
            dao.setBudget(userID, budget);
            copy = dao.getBudget(userID);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        new File(filepath).delete();

        assert(budget.equals(copy));
    }
}
