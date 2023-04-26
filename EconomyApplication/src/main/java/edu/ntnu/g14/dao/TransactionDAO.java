package edu.ntnu.g14.dao;

import edu.ntnu.g14.Transaction;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;

/**
 * This class is an access object for the file-storage of transactions. It uses IndexedDataFile
 * internally to structure the data. The transactions are sorted by time, with the latest
 * transaction at the last index.
 */
public class TransactionDAO {

  private final IndexedDataFile file;
  private final Charset charset;

  /**
   * Constructs a new TransactionDAO object with the given file path and default charset.
   *
   * @param filePath the file path of the IndexedDataFile
   * @throws IOException if there is an error creating the IndexedDataFile
   */
  public TransactionDAO(String filePath) throws IOException {
    this(filePath, Charset.defaultCharset());
  }

  /**
   * Constructs a new TransactionDAO object with the given file path and charset.
   *
   * @param filePath the file path of the IndexedDataFile
   * @param charset  the charset used for encoding/decoding transaction data
   * @throws IOException if there is an error creating the IndexedDataFile
   */
  public TransactionDAO(String filePath, Charset charset) throws IOException {
    this.file = new IndexedDataFile(filePath, charset);
    this.charset = charset;
  }

  /**
   * Adds a new transaction for the given user.
   *
   * @param userID the user ID
   * @param obj    the Transaction object to add
   * @throws IOException              if there is an error adding the transaction to the
   *                                  IndexedDataFile
   * @throws IllegalArgumentException if the transaction object is null
   */
  public void addNewTransaction(String userID, Transaction obj) throws IOException {
    if (obj == null) {
      throw new IllegalArgumentException("Parameter transaction cannot be null");
    }

    byte[] data = obj.toCSVString().getBytes(charset);

    file.addNewData(userID, data);
  }

  /**
   * Retrieves all transactions for the given user.
   *
   * @param userID the user ID
   * @return an array of Transaction objects for the user, sorted by time with the latest
   * transaction at the last index, or null if no transactions were found for the user
   * @throws IOException if there is an error reading the transactions from the IndexedDataFile
   */
  public Transaction[] getAllTransactionsForUser(String userID) throws IOException {
    byte[][] data = file.readAllInIdentifier(userID);
    if (data == null) {
      return null;
    }

    Transaction[] transactions = new Transaction[data.length];
    for (int i = 0; i < data.length; i++) {
      String csv = new String(data[i], charset);
      transactions[i] = Transaction.fromCSVString(csv);
    }

    return transactions;
  }

  /**
   * Retrieves the latest 'amount' number of transactions for the given user.
   *
   * @param userID the user ID
   * @param amount the number of transactions to retrieve
   * @return an array of the latest 'amount' number of Transaction objects for the user, sorted by
   * time with the latest transaction at the last index, or null if no transactions were found for
   * the user
   * @throws IOException if there is an error reading the transactions from the IndexedDataFile
   */
  public Transaction[] getLatest(String userID, int amount) throws IOException {
    byte[][] data = file.getEntriesBackwards(userID, amount);
    if (data == null) {
      return null;
    }

    return Arrays.stream(data)
        .map(arr -> new String(arr, charset))
        .map(Transaction::fromCSVString)
        .toArray(Transaction[]::new);
  }

  /**
   * Deletes all transactions associated with the specified user from the data file.
   *
   * @param userID the identifier of the user whose transactions should be deleted
   * @throws IOException if an I/O error occurs while attempting to delete the transactions
   */
  public void deleteUser(String userID) throws IOException {
    file.deleteIdentifier(userID);
  }
}
