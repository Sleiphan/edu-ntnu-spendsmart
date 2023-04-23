package edu.ntnu.g14.dao;

import edu.ntnu.g14.Budget;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class BudgetDAO {

    private final IndexedDataFile file;
    private final Charset charset;

    public BudgetDAO(String filePath) throws IOException {
        this(filePath, Charset.defaultCharset());
    }

    public BudgetDAO(String filePath, Charset charset) throws IOException {
        this.file = new IndexedDataFile(filePath);
        this.charset = charset;
    }

    /**
     *
     * @param userID
     * @param budget
     * @throws IOException
     */
    public void setBudget(String userID, Budget budget) throws IOException {
        byte[] data = budget.toCSV().getBytes(charset);
        boolean newData = file.containsIdentifier(userID);

        if (newData)
            file.setData(userID, 0, data);
        else
            file.addNewData(userID, data);
    }

    public Budget getBudget(String userID) throws IOException {
        byte[] data = file.getData(userID, 0);
        if (data == null)
            return null;

        String csv = new String(data, charset);
        return Budget.fromCSV(csv);
    }
}
