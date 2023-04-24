package edu.ntnu.g14.dao;

import edu.ntnu.g14.Transaction;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;

/**
 * This class is an access object for the file-storage of transactions.
 * It uses IndexedDataFile internally to structure the data.
 * The transactions are sorted by time, with the latest transaction at the last index.
 */
public class TransactionDAO {

    private final IndexedDataFile file;
    private final  Charset charset;

    public TransactionDAO(String filePath) throws IOException {
        this(filePath, Charset.defaultCharset());
    }

    public TransactionDAO(String filePath, Charset charset) throws IOException {
        this.file = new IndexedDataFile(filePath, charset);
        this.charset = charset;
    }


    public void addNewTransaction(String userID, Transaction obj) throws IOException {
        if (obj == null)
            throw new IllegalArgumentException("Parameter transaction cannot be null");

        byte[] data = obj.toCSVString().getBytes(charset);

        file.addNewData(userID, data);
    }

    public Transaction[] getAllTransactionsForUser(String userID) throws IOException {
        byte[][] data = file.readAllInIdentifier(userID);
        if (data == null)
            return null;

        Transaction[] transactions = new Transaction[data.length];
        for (int i = 0; i < data.length; i++) {
            String csv = new String(data[i], charset);
            transactions[i] = Transaction.fromCSVString(csv);
        }

        return transactions;
    }

    /**
     * Attempts to read the latest 'amount' number of Transactions, and returns them as
     * @param userID
     * @param amount
     * @return
     * @throws IOException
     */
    public Transaction[] getLatest(String userID, int amount) throws IOException {
        byte[][] data = file.getEntriesBackwards(userID, amount);
        if (data == null)
            return null;

        return Arrays.stream(data)
                .map(arr -> new String(arr, charset))
                .map(Transaction::fromCSVString)
                .toArray(Transaction[]::new);
    }

    public void deleteUser(String userID) throws IOException {
        file.deleteIdentifier(userID);
    }
}
