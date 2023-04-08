package edu.ntnu.g14;

import java.math.BigDecimal;
import java.time.LocalDate;

public class TestFileManagement {
    Account testAccount = new Account(AccountCategory.SAVINGS_ACCOUNT, BigDecimal.valueOf(100000), "1232.232.1232", "kortkonto");
    Invoice testInvoice = new Invoice(LocalDate.of(2023, 12, 4), BigDecimal.valueOf(100000), "1232.232.1232");
    Transaction testTransaction = new Transaction("1232.232.1232", "1244.232.1232", Short.parseShort("344"), "brukte penger", LocalDate.of(2023, 12, 4));
    Budget testBudget = new Budget(Byte.parseByte("90"), GenderCategory.FEMALE);
    User testUser = new User(null, null, null, null, null, null, null, testBudget);



    //test methods
}
