package edu.ntnu.g14;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


//TODO: create exceptions

public class FileManagement {
     
    public static Login[] readUsers() throws IOException {
        InputStream input = FileManagement.class.getResourceAsStream("/resources/textfiles/users.txt");
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        int lines = 0;
        while (reader.readLine() != null) lines++;
        reader.close();
        String[] users = new String[lines];
        for(int i = 0; i < lines; i++){
           users[i] = reader.readLine();
        }
        Login[] logins = new Login[lines];
        for(int i = 0; i < lines; i++){
            String[] user = users[i].split(",");
            logins[i] = new Login(user[1], user[3], user[0]);
        }
        reader.close();
        return logins;
    }


    public static Transaction[] readAllTransactions(String userID) throws IOException {
        InputStream input = FileManagement.class.getResourceAsStream("/resources/textfiles/transactions.txt");
        assert input != null;
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));

        Stream<String> userTrans = reader.lines() // Find the entry with the submitted userID.
                .filter(line -> line.startsWith(userID + ","));
        reader.close();

        if (userTrans.findAny().isEmpty()) // If we could not find an entry with the requested userID, ...
            return null; // ... and return null.

        Transaction[] transactions = userTrans
                .flatMap(s -> Arrays.stream(s.split(",")) // Perform the String::split-operation on every entry, and explode the results into a common one-dimensional array.
                        .skip(1)) // Skip the first element in every line, as it contains the userID and not a parsable transaction.
                .map(Transaction::fromCSVString) // Create a Transaction object for every string.
                .toArray(Transaction[]::new);

        return transactions;
    }

    //TODO: gjør om til typen transaction[]
    public static String findTransactions(LocalDate from, LocalDate to, String userID) throws IOException { 
        String allTransactions = readAllTransactions(userID);
        String[] transactions = allTransactions.split(",");
        
        return Arrays.stream(transactions)
                .map(String::trim)
                .filter(line -> !line.isEmpty())
                .map(transaction -> {
                    String[] info = transaction.split(";");
                    LocalDate date = LocalDate.parse(info[0], DateTimeFormatter.ofPattern("dd.MM.yyyy"));
                    if (date.compareTo(from) >= 0 && date.compareTo(to) <= 0) {
                        return transaction + ",";
                    }
                    return "";
                })
                .collect(Collectors.joining());
    }

    public static User readUser(String userId) throws IOException {
        String userInfoString = readUsers(userId);
        List<Transaction> transactions = readAllTransactions(userId);

        if (userInfoString != null) {
            String[] userInfoArray = userInfoString.split(",");

            String username = userInfoArray[1];
            String email = userInfoArray[2];
            String password = userInfoArray[3];
            String firstName = userInfoArray[4];
            String lastName = userInfoArray[5];
            String[] accountNames = userInfoArray[6].split("\\.");

            //TODO: make users.txt contain accounts, innvoices, and Budget
            HashMap<String, Account> accounts = getAccountsForUser(userId, accountNames);
            HashMap<String, Invoice> invoices = getInvoicesForUser(userId);
            Budget budget = getBudgetForUser(userId);

            Login loginInfo = new Login(username,password, userId);

            return new User(accounts, invoices, loginInfo, email, lastName, firstName, transactions, budget);
        } else {
            return null;
        }
    }


    public static Transaction readLatestTransactions(String userId, int amount){
        
    }

   

    public static void main(String[] args) {
        LocalDate from = LocalDate.of(2023, 03, 12);
        LocalDate to = LocalDate.of(2024, 01, 13);
        try {
            String title = findTransaction(from, to, "olav1");
            System.out.println(title);
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
    }
}

