package edu.ntnu.g14.dao;

import edu.ntnu.g14.Account;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;

public class AccountDAO {

    private IndexedDataFile file;
    private final Charset charset;

    public AccountDAO(String filePath) throws IOException {
        this(filePath, Charset.defaultCharset());
    }

    public AccountDAO(String filePath, Charset charset) throws IOException {
        this.file = new IndexedDataFile(filePath);
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
        int index = -1;

        boolean found = false;
        for (int i = 0; i < accounts.length; i++)
            if (accounts[i].getAccountNumber().equals(account.getAccountNumber())) {
                index = i;
                break;
            }

        if (index == -1)
            return;

        accounts[index] = account;
        replaceAllAccounts(userID, accounts);
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
}
