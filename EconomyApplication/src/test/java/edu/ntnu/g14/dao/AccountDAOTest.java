package edu.ntnu.g14.dao;

import edu.ntnu.g14.model.Account;
import edu.ntnu.g14.model.AccountCategory;
import org.junit.jupiter.api.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.math.BigDecimal;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AccountDAOTest {

    private String testFilePath = "accountDAOTest.txt";
    private String tempFilePath = testFilePath + ".temp";

    private Path file;
    private Path tempFile;

    private AccountDAO dao;



    private Account getTestAccount() {
        String userID = "User_1";
        String accountName = "Savings";
        String accountNumber = "1234.56.78910";
        BigDecimal amount = new BigDecimal("1234.5678");

        return new Account.AccountBuilder()
                .accountCategory(AccountCategory.SAVINGS_ACCOUNT)
                .accountName(accountName)
                .amount(amount)
                .accountNumber(accountNumber)
                .build();
    }

    @Test
    public void addNewAccount() {
        String userID = "User_1";
        String accountName = "Savings";
        String accountNumber = "1234.56.78910";
        BigDecimal amount = new BigDecimal("1234.5678");

        Account account = new Account.AccountBuilder()
                .accountCategory(AccountCategory.SAVINGS_ACCOUNT)
                .accountName(accountName)
                .amount(amount)
                .accountNumber(accountNumber)
                .build();

        Account copy = null;
        try {
            dao.addNewAccount(userID, account);
            copy = dao.getAccount(userID, accountNumber);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        assert account.equals(copy);
    }

    @Test
    public void setAccount() {
        String userID = "User_1";
        String accountName1 = "Savings";
        String accountNumber1 = "1234.56.78910";
        BigDecimal amount1 = new BigDecimal("1234.5678");
        AccountCategory category1 = AccountCategory.SAVINGS_ACCOUNT;

        Account account1 = new Account(category1, amount1, accountNumber1, accountName1);



        String accountName2 = "Pensions";
        String accountNumber2 = "5580.45.99011";
        BigDecimal amount2 = new BigDecimal("70.98");
        AccountCategory category2 = AccountCategory.PENSION_ACCOUNT;

        Account account2 = new Account(category2, amount2, accountNumber2, accountName2);
        Account account3 = new Account(category2, amount2, accountNumber1, accountName2);



        Account copy1 = null;
        Account copy2 = null;
        Account copy3 = null;
        try {
            dao.setAccount(userID, account1);
            dao.setAccount(userID, account2);
            copy1 = dao.getAccount(userID, accountNumber1);
            copy2 = dao.getAccount(userID, accountNumber2);
            dao.setAccount(userID, account3);
            copy3 = dao.getAccount(userID, accountNumber1);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        assert account1.equals(copy1);
        assert account2.equals(copy2);
        assert account3.equals(copy3);
    }

    @Test
    public void deleteUser() {
        String userID = "User_1";
        String accountName1 = "Savings";
        String accountNumber1 = "1234.56.78910";
        BigDecimal amount1 = new BigDecimal("1234.5678");
        AccountCategory category1 = AccountCategory.SAVINGS_ACCOUNT;

        Account account1 = new Account(category1, amount1, accountNumber1, accountName1);



        String accountName2 = "Pensions";
        String accountNumber2 = "5580.45.99011";
        BigDecimal amount2 = new BigDecimal("70.98");
        AccountCategory category2 = AccountCategory.PENSION_ACCOUNT;

        Account account2 = new Account(category2, amount2, accountNumber2, accountName2);
        Account account3 = new Account(category2, amount2, accountNumber1, accountName2);



        Account read1 = null;
        Account read2 = null;
        Account read3 = null;
        Account read4 = null;

        try {
            dao.addNewAccount(userID + "1", account1);
            dao.addNewAccount(userID, account2);
            dao.addNewAccount(userID, account3);
            dao.addNewAccount(userID + "2", account3);
            dao.deleteUser(userID);

            read1 = dao.getAccount(userID, account2.getAccountNumber());
            read2 = dao.getAccount(userID, account3.getAccountNumber());
            read3 = dao.getAccount(userID + "1", account1.getAccountNumber());
            read4 = dao.getAccount(userID + "2", account3.getAccountNumber());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        assert read1 == null;
        assert read2 == null;
        assert read3.equals(account1);
        assert read4.equals(account3);
    }



    @BeforeAll
    public void initialize() {
        try {
            dao = new AccountDAO(testFilePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @BeforeEach
    public void resetFile() {
        try {
            RandomAccessFile raf = new RandomAccessFile(testFilePath, "rw");
            FileChannel chn = raf.getChannel();
            chn.truncate(0);
            chn.close();
            raf.close();

            dao = new AccountDAO(testFilePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @AfterAll
    public void cleanup() {
        try {
            Files.delete(Path.of(testFilePath));
            Files.delete(Path.of(tempFilePath));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
