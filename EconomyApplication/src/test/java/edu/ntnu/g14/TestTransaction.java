package edu.ntnu.g14;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Date;

class TestTransaction {

    @Test
    void csvParsing() {
        String fromAccountId   = "From account";
        String toAccountId     = "To account";
        short amount           = 560;
        String description     = "Desc";
        LocalDate dateOfTransaction = LocalDate.now();

        Transaction t = new Transaction(fromAccountId, toAccountId, amount, description, dateOfTransaction);
        Transaction t2 = Transaction.fromCSVString(t.toCSVString());
        assert t.equals(t2);
    }
}