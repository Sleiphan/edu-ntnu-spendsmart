package edu.ntnu.g14.dao;

import edu.ntnu.g14.Budget;
import java.io.IOException;
import java.nio.charset.Charset;

/**
 * The class represents a data acess object for managing budgets stored in budgets.txt
 */
public class BudgetDAO {

    private final IndexedDataFile file;
    private final Charset charset;

    /**
    * Constructs a BudgetDAO object with the specified file path.
    *
    * @param filePath the path to the file containing the budget data
    * @throws IOException if an I/O error occurs while opening the file
    */
    public BudgetDAO(String filePath) throws IOException {
        this(filePath, Charset.defaultCharset());
    }

    /**
    * Constructs a BudgetDAO object with the specified file path and character set.
    *
    * @param filePath the path to the file containing the budget data
    * @param charset  the character set used to encode the budget data
    * @throws IOException if an I/O error occurs while opening the file
    */
    public BudgetDAO(String filePath, Charset charset) throws IOException {
        this.file = new IndexedDataFile(filePath, charset);
        this.charset = charset;
    }

    /**
    * Sets the budget for the specified user ID.
    *
    * @param userID the ID of the user whose budget is being set
    * @param budget the budget to set for the user
    * @throws IOException if an I/O error occurs while writing to the file
    */
    public void setBudget(String userID, Budget budget) throws IOException {
        byte[] data = budget.toCSV().getBytes(charset);
        boolean newData = file.containsIdentifier(userID);

        if (newData)
            file.setData(userID, 0, data);
        else
            file.addNewData(userID, data);
    }

    /**
    * Retrieves the budget for the specified user ID.
    *
    * @param userID the ID of the user whose budget is being retrieved
    * @return the budget for the specified user ID, or null if no budget is found
    * @throws IOException if an I/O error occurs while reading from the file
    */
    public Budget getBudget(String userID) throws IOException {
        byte[] data = file.getData(userID, 0);
        if (data == null)
            return null;

        String csv = new String(data, charset);
        return Budget.fromCSV(csv);
    }

    /**
    * Deletes the budget data for the specified user ID.
    *
    * @param userID the ID of the user whose budget data is being deleted
    * @throws IOException if an I/O error occurs while writing to the file
    */
    public void deleteUser(String userID) throws IOException {
        file.deleteIdentifier(userID);
    }
}
