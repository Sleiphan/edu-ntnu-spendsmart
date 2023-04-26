package edu.ntnu.g14.dao;

import edu.ntnu.g14.model.Account;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;

/**
 * The class represents a data acess object for managing accounts stored in accounts.txt
 */
public class AccountDAO {

  private final IndexedDataFile file;
  private final Charset charset;

  /**
   * Constructs an AccountDAO object with the specified file path and the default character
   * encoding.
   *
   * @param filePath The path to the IndexedDataFile that will be used to persist the account data.
   * @throws IOException If an I/O error occurs while initializing the IndexedDataFile.
   */
  public AccountDAO(String filePath) throws IOException {
    this(filePath, Charset.defaultCharset());
  }

  /**
   * Constructs an AccountDAO object with the specified file path and character encoding.
   *
   * @param filePath The path to the IndexedDataFile that will be used to persist the account data.
   * @param charset  The character encoding that will be used to encode and decode the account
   *                 data.
   * @throws IOException If an I/O error occurs while initializing the IndexedDataFile.
   */
  public AccountDAO(String filePath, Charset charset) throws IOException {
    this.file = new IndexedDataFile(filePath, charset);
    this.charset = charset;
  }

  /**
   * Replaces all existing accounts associated with a user with the specified array of Account
   * objects. If the specified array is empty or null, all existing accounts associated with the
   * user will be deleted.
   *
   * @param userID   The ID of the user whose accounts should be replaced.
   * @param accounts The array of Account objects that should replace the existing accounts.
   * @throws IOException If an I/O error occurs while updating the IndexedDataFile.
   */
  public void replaceAllAccounts(String userID, Account[] accounts) throws IOException {
    file.deleteIdentifier(userID);

    for (Account account : accounts) {
      byte[] data = account.toCSVString().getBytes(charset);
      file.addNewData(userID, data);
    }
  }

  /**
   * Updates an existing Account object associated with a user or adds a new Account object if one
   * does not exist.
   *
   * @param userID  The ID of the user whose account should be updated or added.
   * @param account The Account object that should be updated or added.
   * @throws IOException              If an I/O error occurs while updating the IndexedDataFile.
   * @throws IllegalArgumentException If the specified Account object is null.
   */
  public void setAccount(String userID, Account account) throws IOException {
    Account[] accounts = getAllAccountsForUser(userID);

    int size = -1;
    if (accounts == null) {
      size = 0;
    } else {
      size = accounts.length;
    }

    int index = -1;

    for (int i = 0; i < size; i++) {
      if (accounts[i].getAccountNumber().equals(account.getAccountNumber())) {
        index = i;
        break;
      }
    }

    if (index == -1) {
      addNewAccount(userID, account);
      return;
    }

    accounts[index] = account;

    replaceAllAccounts(userID, accounts);
  }

  /**
   * Adds a new account for the given user ID.
   *
   * @param userID The user ID to add the account to.
   * @param obj    The account object to add.
   * @throws IOException              If there is an I/O error while adding the account.
   * @throws IllegalArgumentException If the account object is null.
   */
  public void addNewAccount(String userID, Account obj) throws IOException {
    if (obj == null) {
      throw new IllegalArgumentException("Parameter account cannot be null");
    }

    byte[] data = obj.toCSVString().getBytes(charset);

    file.addNewData(userID, data);
  }

  /**
   * Retrieves the account with the specified account number for the given user ID.
   *
   * @param userID        The user ID to search for the account.
   * @param accountNumber The account number of the account to retrieve.
   * @return The account object with the specified account number, or null if the account is not
   * found.
   * @throws IOException If there is an I/O error while retrieving the account.
   */
  public Account getAccount(String userID, String accountNumber) throws IOException {
    Account[] accounts = getAllAccountsForUser(userID);

    if (accounts == null)
      return null;

    for (Account account : accounts)
      if (account.getAccountNumber().equals(accountNumber))
        return account;

    return null;
  }

  /**
   * Retrieves all accounts for the specified user ID.
   *
   * @param userID The user ID to retrieve accounts for.
   * @return An array of all account objects for the specified user ID, or null if the user has no
   * accounts.
   * @throws IOException If there is an I/O error while retrieving the accounts.
   */
  public Account[] getAllAccountsForUser(String userID) throws IOException {
    byte[][] data = file.readAllInIdentifier(userID);
    if (data == null) {
      return null;
    }

    return Arrays.stream(data)
        .map(arr -> new String(arr, charset))
        .map(Account::fromCSVString)
        .toArray(Account[]::new);
  }

  /**
   * Deletes the user with the specified user ID, along with all associated accounts.
   *
   * @param userID The user ID to delete.
   * @throws IOException If there is an I/O error while deleting the user and accounts.
   */
  public void deleteUser(String userID) throws IOException {
    file.deleteIdentifier(userID);
  }
}
