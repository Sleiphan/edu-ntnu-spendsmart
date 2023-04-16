package edu.ntnu.g14;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class TestAccount {
    private Account account;

    @BeforeEach
    public void setUp() {
        account = new Account(AccountCategory.CHECKING_ACCOUNT, new BigDecimal(1000), "1234.56.78910", "My Account");
    }

    @Test
    public void testRemoveAmount() {
        account.removeAmount(new BigDecimal(500));
        assertEquals(new BigDecimal(500), account.getAmount());
        assertThrows(IllegalArgumentException.class, () -> account.removeAmount(new BigDecimal(1000)));
    }

    @Test
    public void testAddAmount() {
        account.addAmount(new BigDecimal(500));
        assertEquals(new BigDecimal(1500), account.getAmount());
    }

    @Test
    public void testGetAccountType() {
        assertEquals(AccountCategory.CHECKING_ACCOUNT, account.getAccountType());
    }

    @Test
    public void testSetAccountName() {
        account.setAccountName("My New Account");
        assertEquals("My New Account", account.getAccountName());
        assertThrows(IllegalArgumentException.class, () -> account.setAccountName(""));
    }

    @Test
    public void testGetAmount() {
        assertEquals(new BigDecimal(1000), account.getAmount());
    }

    @Test
    public void testGetAccountNumber() {
        assertEquals("1234.56.78910", account.getAccountNumber());
    }

    @Test
    public void testGetAccountName() {
        assertEquals("My Account", account.getAccountName());
    }

    @Test
    public void testEquals() {
        Account account2 = new Account(AccountCategory.CHECKING_ACCOUNT, new BigDecimal(1000), "1234.56.78910", "My Account");
        assertEquals(account, account2);

        Account account3 = new Account(AccountCategory.SAVINGS_ACCOUNT, new BigDecimal(1000), "1234.56.78910", "My Account");
        assertNotEquals(account, account3);
    }

    @Test
    public void testToCSVString() {
        String expected = "CHECKING_ACCOUNT;1000;1234.56.78910;My Account,";
        assertEquals(expected, account.toCSVString());
    }

    @Test
    public void testFromCSVString() {
        String csvString = "CHECKING_ACCOUNT;1000;1234.56.78910;My Account,";
        Account account2 = Account.fromCSVString(csvString);
        assertEquals(account, account2);
    }

    @Test
    public void parseCSV() {
        AccountCategory accountType = AccountCategory.PENSION_ACCOUNT;
        BigDecimal amount = new BigDecimal("987.654");
        String accountNumber = "8356.79.34678";
        String accountName = "Utunga";

        Account t = new Account.AccountBuilder().amount(amount)
                .accountCategory(accountType)
                .accountName(accountName)
                .accountNumber(accountNumber).build();
        Account t2 = Account.fromCSVString(t.toCSVString());
        assert t.equals(t2);
    }
}
