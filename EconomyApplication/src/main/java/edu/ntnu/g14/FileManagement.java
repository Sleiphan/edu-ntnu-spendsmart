package edu.ntnu.g14;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;


//TODO: create exceptions

public class FileManagement {
     
    public static Login[] readUsers() throws IOException {
        
        List<String> lines = new ArrayList<>();
        InputStream input = FileManagement.class.getResourceAsStream("/resources/textfiles/users.txt");
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        reader.readLine();
        String line;
        while ((line = reader.readLine()) != null) {
            lines.add(line);
        }
        String[] linesArray = lines.toArray(new String[0]);


        Login[] logins = new Login[linesArray.length];
        for(int i = 0; i < linesArray.length; i++){
            String[] user = linesArray[i].split(",");
            logins[i] = new Login(user[1], user[3], user[0]);
        }
        reader.close();
        return logins;
    }


    public static Transaction[] readAllTransactions(String userID) throws IOException {
        InputStream input = FileManagement.class.getResourceAsStream("/resources/textfiles/transactions.txt");
        assert input != null;
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        Stream<String> userTrans = reader.lines() 
                .filter(line -> line.startsWith(userID + ","));
        
        Transaction[] transactions = userTrans
                .flatMap(s -> Arrays.stream(s.split(",")) 
                        .skip(1)) 
                .map(Transaction::fromCSVString) 
                .toArray(Transaction[]::new);
        reader.close();
        return transactions;
    }

   
    public static Transaction[] findTransactionsToFromDate(LocalDate from, LocalDate to, String userId) throws IOException {
        return (Transaction[]) Arrays.stream(readAllTransactions(userId)) //TODO: Bytt med parameter med readAllTransactions(userId), slett foregående linje
                .filter(transaction -> transaction.getDateOfTransaction().isAfter(from) && transaction.getDateOfTransaction().isBefore(to))
                .toArray();
    }

    public static User readUser(String userId) throws IOException {
        List<String> lines = new ArrayList<>();
        InputStream input = FileManagement.class.getResourceAsStream("/resources/textfiles/users.txt");
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        reader.readLine();

        String line;
        while ((line = reader.readLine()) != null) {
            lines.add(line);
        }
        String[] linesArray = lines.toArray(new String[0]);

        String userInfoString = "";
        for(int i = 0; i < linesArray.length; i++){
           String[] user= linesArray[i].split(",");
           if(user[0].equals(userId)){
            userInfoString = linesArray[i];
           }
        }
        
        Transaction[] transactions = readAllTransactions(userId);
        if (userInfoString != null) {
            String[] userInfoArray = userInfoString.split(",");

            String username = userInfoArray[1];
            String email = userInfoArray[2];
            String password = userInfoArray[3];
            String firstName = userInfoArray[4];
            String lastName = userInfoArray[5];

            //TODO: make users.txt contain accounts, innvoices, and Budget
            Account[] accounts = getAccountsForUser(userId);
            Invoice[] invoices = getInvoicesForUser(userId);
            Budget budget = getBudgetForUser(userId);

            Login loginInfo = new Login(username,password, userId);

            return new User(accounts, invoices, loginInfo, email, lastName, firstName, transactions, budget);
        } else {
            return null;
        }
    }

    public static Account[] getAccountsForUser(String userId){
        return null;
    }

    public static Invoice[] getInvoicesForUser(String userId){
        return null;
    }

    public static Budget getBudgetForUser(String userId){
        return null;
    }


    public static Transaction[] findTransactionsOfUserAndAccountNumber(String userId, String accountNumber) throws IOException {
        return (Transaction[]) Arrays.stream(readAllTransactions(userId)) //TODO: Bytt med parameter med readAllTransactions(userId), slett foregående linje
                .filter(transaction -> transaction.getFromAccountId().equals(accountNumber) || transaction.getToAccountId().equals(accountNumber))
                .toArray();
    }


    public static Transaction[] readLatestTransactions(String userId, int amount) throws IOException{
        Transaction[] allTransactions = readAllTransactions(userId);
        if (allTransactions.length >= amount) {
            Transaction[] latestTransaction = Arrays.copyOfRange(allTransactions, allTransactions.length - amount, allTransactions.length);
            return latestTransaction;
        } else {
            return null;
        }
    }

    public static void main(String[] args) throws IOException {
        User a = readUser("olav#1");
        System.out.println(a.getEmail());
    }

}

