package edu.ntnu.g14.dao;

import edu.ntnu.g14.Account;
import edu.ntnu.g14.Transaction;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class AccountDAO {

    private final IndexedDataFile file;
    private final Charset charset;

    public AccountDAO(String filePath) throws IOException {
        this(filePath, Charset.defaultCharset());
    }

    public AccountDAO(String filePath, Charset charset) throws IOException {
        this.file = new IndexedDataFile(filePath, charset);
        this.charset = charset;
    }

    public void replaceAllAccounts(String userID, Account[] accounts) throws IOException {
        file.deleteIdentifier(userID);

        for (Account account : accounts) {
            byte[] data = account.toCSVString().getBytes(charset);
            file.addNewData(userID, data);
        }
    }

    public void setAccount(String userID, Account account) throws IOException {
        Account[] accounts = getAllAccountsForUser(userID);

        int size = -1;
        if (accounts == null)
            size = 0;
        else
            size = accounts.length;

        int index = -1;

        for (int i = 0; i < size; i++)
            if (accounts[i].getAccountNumber().equals(account.getAccountNumber())) {
                index = i;
                break;
            }

        if (index == -1) {
            addNewAccount(userID, account);
            return;
        }

        accounts[index] = account;

        replaceAllAccounts(userID, accounts);
    }

    public void addNewAccount(String userID, Account obj) throws IOException {
        if (obj == null)
            throw new IllegalArgumentException("Parameter transaction cannot be null");

        byte[] data = obj.toCSVString().getBytes(charset);

        file.addNewData(userID, data);
    }

    public Account getAccount(String userID, String accountNumber) throws IOException {
        Account[] accounts = getAllAccountsForUser(userID);

        for (Account account : accounts)
            if (account.getAccountNumber().equals(accountNumber))
                return account;

        return null;
    }

    public Account[] getAllAccountsForUser(String userID) throws IOException {
        byte[][] data = file.readAllInIdentifier(userID);
        if (data == null)
            return null;

        return Arrays.stream(data)
                .map(arr -> new String(arr, charset))
                .map(Account::fromCSVString)
                .toArray(Account[]::new);
    }

    public void deleteUser(String userID) throws IOException {
        file.deleteIdentifier(userID);
    }
}
