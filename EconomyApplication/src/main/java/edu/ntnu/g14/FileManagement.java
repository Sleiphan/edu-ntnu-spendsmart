package edu.ntnu.g14;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
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
        InputStream input = FileManagement.class.getResourceAsStream("/textfiles/users.txt");
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
        InputStream input = FileManagement.class.getResourceAsStream("/textfiles/transactions.txt");
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
        InputStream input = FileManagement.class.getResourceAsStream("/textfiles/users.txt");
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

    public static void writeNewUser(User newUser) throws IOException{
        String userInfo = "" + newUser.getLoginInfo().getUserId() + "," + newUser.getLoginInfo().getUserName() + "," + 
            newUser.getEmail() + "," + newUser.getLoginInfo().getPassword() + "," + newUser.getFirstName() + "," + newUser.getLastName();
            
        //write new user to user.txt
        try {
            RandomAccessFile file = new RandomAccessFile("/textfiles/users.txt", "rw");
            String line;
            boolean isFirstEmptyLineFound = false;
            long pos = 0;
            while ((line = file.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    if (!isFirstEmptyLineFound) {
                        pos = file.getFilePointer() - line.length() - 1;
                        file.seek(pos);
                        file.writeBytes(userInfo + "\n");
                        isFirstEmptyLineFound = true;
                    }
                }
            }
            
            
            if (!isFirstEmptyLineFound && pos != 0) {
                file.writeBytes(userInfo + "\n");
            }
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        //write new user to invoices
        try {
            RandomAccessFile file = new RandomAccessFile("/textfiles/invoices.txt", "rw");
            String line;
            boolean isFirstEmptyLineFound = false;
            long pos = 0;
            while ((line = file.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    if (!isFirstEmptyLineFound) {
                        pos = file.getFilePointer() - line.length() - 1;
                        file.seek(pos);
                        file.writeBytes(newUser.getLoginInfo().getPassword() + ", \n");
                        isFirstEmptyLineFound = true;
                    }
                }
            }
            
            
            if (!isFirstEmptyLineFound && pos != 0) {
                file.writeBytes(newUser.getLoginInfo().getPassword() + ", \n");
            }
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //write new user to accounts
        try {
            RandomAccessFile file = new RandomAccessFile("/textfiles/accounts.txt", "rw");
            String line;
            boolean isFirstEmptyLineFound = false;
            long pos = 0;
            while ((line = file.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    if (!isFirstEmptyLineFound) {
                        pos = file.getFilePointer() - line.length() - 1;
                        file.seek(pos);
                        file.writeBytes(newUser.getLoginInfo().getPassword() + ", \n");
                        isFirstEmptyLineFound = true;
                    }
                }
            }
            
            
            if (!isFirstEmptyLineFound && pos != 0) {
                file.writeBytes(newUser.getLoginInfo().getPassword() + ", \n");
            }
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //write new user to budgets
        try {
            RandomAccessFile file = new RandomAccessFile("/textfiles/budgets.txt", "rw");
            String line;
            boolean isFirstEmptyLineFound = false;
            long pos = 0;
            while ((line = file.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    if (!isFirstEmptyLineFound) {
                        pos = file.getFilePointer() - line.length() - 1;
                        file.seek(pos);
                        file.writeBytes(newUser.getLoginInfo().getPassword() + ", \n");
                        isFirstEmptyLineFound = true;
                    }
                }
            }
            
            
            if (!isFirstEmptyLineFound && pos != 0) {
                file.writeBytes(newUser.getLoginInfo().getPassword() + ", \n");
            }
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //write new user to transactions
        try {
            RandomAccessFile file = new RandomAccessFile("/textfiles/transactions.txt", "rw");
            String line;
            boolean isFirstEmptyLineFound = false;
            long pos = 0;
            while ((line = file.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    if (!isFirstEmptyLineFound) {
                        pos = file.getFilePointer() - line.length() - 1;
                        file.seek(pos);
                        file.writeBytes(newUser.getLoginInfo().getPassword() + ", \n");
                        isFirstEmptyLineFound = true;
                    }
                }
            }
            
            
            if (!isFirstEmptyLineFound && pos != 0) {
                file.writeBytes(newUser.getLoginInfo().getPassword() + ", \n");
            }
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static Account[] getAccountsForUser(String userId) throws IOException{
        InputStream input = FileManagement.class.getResourceAsStream("/textfiles/accounts.txt");
        assert input != null;
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        Stream<String> userTrans = reader.lines() 
                .filter(line -> line.startsWith(userId + ","));
        System.out.println(userTrans);

        Account[] accounts = userTrans
        .flatMap(s -> Arrays.stream(s.split(",")) 
                .skip(1)) 
        .map(Account::fromCSVString) 
        .toArray(Account[]::new);
        reader.close();
        return accounts;
    }

    public static Account getAccountForUser(String userId, String accountNumber) throws IOException{
        Account[] accounts = getAccountsForUser(userId);
        Account account = null;
        for(int i = 0; i < accounts.length; i++){
            if(accounts[i].getAccountNumber().equals(accountNumber)){
                account = accounts[0];
            }
        }
        return account;
    }

    public static Invoice[] getInvoicesForUser(String userID) throws IOException {
        InputStream input = FileManagement.class.getResourceAsStream("/textfiles/invoices.txt");
        assert input != null;
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        Stream<String> userTrans = reader.lines() 
                .filter(line -> line.startsWith(userID + ","));
        System.out.println(userTrans);

        Invoice[] invoices = userTrans
        .flatMap(s -> Arrays.stream(s.split(",")) 
                .skip(1)) 
        .map(Invoice::fromCSVString) 
        .toArray(Invoice[]::new);
        reader.close();
        return invoices;
    }

    public static Budget getBudgetForUser(String userId){
        return null;
    }

    public static void writeNewTransaction(String userId, Transaction transaction) throws IOException{
        String addonText = "" + transaction.getDateOfTransaction() + ";" + transaction.getAmount() + ";" +
        transaction.getToAccountId() + ";" + transaction.getFromAccountId() + ";" + transaction.getDescription() + ",";
        RandomAccessFile file = new RandomAccessFile("textfiles/transactions.txt", "rw");
        String line;
        long pos = 0;
        while ((line = file.readLine()) != null) {
            if (line.startsWith(userId + ",")) {
                pos = file.getFilePointer() - line.length();
                file.seek(pos + line.getBytes().length);
                file.writeBytes(addonText);
                break;
            }
        }
        file.close();
    }

    public static void writeNewAccount(String userId, Account account) throws IOException{
        String addonText = "" + account.getAccountType() + "." + account.getAmount() + "." + account.getAccountNumber() + "." + 
        account.getAccountName() + ",";
        RandomAccessFile file = new RandomAccessFile("textfiles/accounts.txt", "rw");
        String line;
        long pos = 0;
        while ((line = file.readLine()) != null) {
            if (line.startsWith(userId + ",")) {
                pos = file.getFilePointer() - line.length();
                file.seek(pos + line.getBytes().length);
                file.writeBytes(addonText);
                break;
            }
        }
        file.close();
    }

    public static void writeNewInvoice(String userId, Invoice invoice) throws IOException{
        String addonText = "" + invoice.getDueDate() + "." + invoice.getAmount() + "." + invoice.getRecipientAccountNumber() + ",";
        RandomAccessFile file = new RandomAccessFile("textfiles/invoices.txt", "rw");
        String line;
        long pos = 0;
        while ((line = file.readLine()) != null) {
            if (line.startsWith(userId + ",")) {
                pos = file.getFilePointer() - line.length();
                file.seek(pos + line.getBytes().length);
                file.writeBytes(addonText);
                break;
            }
        }
        file.close();
    }

    public static void writeNewBudget(String userId, Budget budget) throws IOException{
        String addonText = ""; //TODO: make in format of budget
        RandomAccessFile file = new RandomAccessFile("textfiles/transactions.txt", "rw");
        String line;
        long pos = 0;
        while ((line = file.readLine()) != null) {
            if (line.startsWith(userId + ",")) {
                pos = file.getFilePointer() - line.length();
                file.seek(pos + line.getBytes().length);
                file.writeBytes(addonText);
                break;
            }
        }
        file.close();
    }

    public static Transaction[] findTransactionsOfUserAndAccountNumber(String userId, String accountNumber) throws IOException {
        return (Transaction[]) Arrays.stream(readAllTransactions(userId)) 
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
        
    }

}

