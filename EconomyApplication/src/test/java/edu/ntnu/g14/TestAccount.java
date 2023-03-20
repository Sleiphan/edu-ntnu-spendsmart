package edu.ntnu.g14;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

public class TestAccount {

    @Test
    public void parseCSV() {
        AccountCategory accountType = AccountCategory.PENSION_ACCOUNT;
        BigDecimal amount = new BigDecimal("987.654");
        String accountNumber = "8356.79.34678";
        String accountName = "Utunga";

        Account t = new Account(accountType, amount, accountNumber, accountName);
        Account t2 = Account.fromCSVString(t.toCSVString());
        assert t.equals(t2);
    }
}
