package edu.ntnu.g14;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;

class TestFileManagement {
    Account testAccount = new Account.AccountBuilder().accountCategory(AccountCategory.SAVINGS_ACCOUNT).amount(BigDecimal.valueOf(100000)).accountNumber("1256.65.56605").accountName("kortkonto").build();
    Invoice testInvoice = new Invoice(LocalDate.of(2023, 12, 4), BigDecimal.valueOf(100000), "1256.65.56605", "Comment");
    Transaction testTransaction = new Transaction("1256.65.56605", "1256.65.56605", BigDecimal.valueOf(1000), "brukte penger", LocalDate.of(2023, 12, 4), BudgetCategory.ALCOHOL_AND_TOBACCO);
    Budget testBudget = new Budget(Byte.parseByte("90"), GenderCategory.FEMALE);
    Login loginInfo = new Login("test", "test", "Test#1");
    User testUser = new User(null, null, loginInfo, null, null, null, null, testBudget);

    @Test
    void write() throws IOException{
        FileManagement.writeTransaction("olav#1", testTransaction);
        FileManagement.writeNewUser(testUser);
        FileManagement.writeAccount("olav#1", testAccount);
        FileManagement.writeNewInvoice("olav#1", testInvoice);
        FileManagement.writeNewBudget("olav#1", testBudget);
        

        System.out.println(FileManagement.readAllTransactions("olav#1").toString());
        System.out.println(FileManagement.readLatestTransactions("olav#1", 1).toString());
        System.out.println(FileManagement.getInvoicesForUser("olav#1").toString());
        
    }
    //test methods
}
