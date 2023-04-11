package edu.ntnu.g14;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;

class TestFileManagement {
    Account testAccount = new Account(AccountCategory.SAVINGS_ACCOUNT, BigDecimal.valueOf(100000), "1256.65.56605", "kortkonto");
    Invoice testInvoice = new Invoice(LocalDate.of(2023, 12, 4), BigDecimal.valueOf(100000), "1256.65.56605");
    Transaction testTransaction = new Transaction("1256.65.56605", "1256.65.56605", BigDecimal.valueOf(1000), "brukte penger", LocalDate.of(2023, 12, 4));
    Budget testBudget = new Budget(Byte.parseByte("90"), GenderCategory.FEMALE);
    Login loginInfo = new Login("test", "test", "Test#1");
    User testUser = new User(null, null, loginInfo, null, null, null, null, testBudget);

    @Test
    void write() throws IOException{
        FileManagement.writeNewTransaction("olav#1", testTransaction);
        FileManagement.writeNewUser(testUser);
        FileManagement.writeNewAccount("olav#1", testAccount);
        FileManagement.writeNewInvoice("olav#1", testInvoice);
        FileManagement.writeNewBudget("olav#1", testBudget);

        System.out.println(FileManagement.readAllTransactions("olav#1").toString());
        System.out.println(FileManagement.readLatestTransactions("olav#1", 1).toString());
        System.out.println(FileManagement.getInvoicesForUser("olav#1").toString());
        
    }
    //test methods
}
