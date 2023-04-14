package edu.ntnu.g14;


import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import edu.ntnu.g14.dao.BudgetDAO;
import edu.ntnu.g14.dao.InvoiceDAO;


//TODO: create exceptions

public class FileManagement {

    public static final String filePath          = "saves/accounts.txt";
    public static final String PATH_BUDGETS      = "saves/budgets.txt";
    public static final String PATH_INVOICES     = "saves/invoices.txt";
    public static final String PATH_TRANSACTIONS = "saves/transactions.txt";
    public static final String PATH_USERS        = "saves/users.txt";
    private static final String PATH_TEMPFILE    = "saves/temp_file.txt";

    // public static void fileContentInsert(String pathToFile, long

    public static Login[] readUsers() throws IOException {
        
        List<String> lines = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(PATH_USERS));
        int count = 0;
        reader.readLine();
        String line;
        while ((line = reader.readLine()) != null) {
            lines.add(line);
            if (!line.startsWith("  ")) {
                count++;
            } 
        }
        String[] linesArray = lines.toArray(new String[0]);
        
        

        Login[] logins = new Login[count];
        for(int i = 0; i < count; i++){
            String[] user = linesArray[i].split(",");
            logins[i] = new Login(user[1], user[3], user[0]);
        }
        reader.close();
        return logins;
    }

    public static Transaction[] readAllTransactions(String userID) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(PATH_TRANSACTIONS));
        Stream<String> userTrans = reader.lines()
                .filter(line -> line.startsWith(userID + ","));
        Transaction[] transactions = userTrans.flatMap(s -> Stream.of(s.split(","))
                .skip(1).map(Transaction::fromCSVString)).toArray(Transaction[]::new);
        reader.close();
        return transactions;
    }

    public static Transaction[] findTransactionsToFromDate(LocalDate from, LocalDate to, String userId) throws IOException {
        return (Transaction[]) Arrays.stream(readAllTransactions(userId)) //TODO: Bytt med parameter med readAllTransactions(userId), slett foregående linje
                .filter(transaction -> transaction.getDateOfTransaction().isAfter(from) && transaction.getDateOfTransaction().isBefore(to))
                .toArray();
    }

    public static User readUser(String userId) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(PATH_USERS));
        Stream<String> userTrans = reader.lines() 
                .filter(line -> line.startsWith(userId + ","));
        System.out.println(userTrans);
        String[] userInfoArray = null;
        String[] userTransArray = userTrans.toArray(String[]::new);
        for (String s : userTransArray) {
            String[] placeHolder = s.split(",");
            userInfoArray = new String[placeHolder.length - 1];
            for(int i =  0; i < placeHolder.length - 1; i++){
                userInfoArray[i] = placeHolder[i];
            }
            
        }
        Transaction[] transactions = readAllTransactions(userId);
        String username = userInfoArray[1];
        String email = userInfoArray[2];
        String password = userInfoArray[3];
        String firstName = userInfoArray[4];
        String lastName = userInfoArray[5];

            
        Account[] accounts = readAccounts(userId);
        Invoice[] invoices = new InvoiceDAO(PATH_INVOICES).getAllInvoices(userId);
        Budget budget = new BudgetDAO(PATH_BUDGETS).getBudget(userId);

        Login loginInfo = new Login(username,password, userId);

        User user = new User(accounts, invoices, loginInfo, email, lastName, firstName, transactions, budget);
        return user;
        
    }

    public static void editUser(User newUser, String userId) throws FileNotFoundException, IOException{
        String addonText = "" + newUser.getLoginInfo().getUserId() + "," + newUser.getLoginInfo().getUserName() + "," + 
            newUser.getEmail() + "," + newUser.getLoginInfo().getPassword() + "," + newUser.getFirstName() + "," + newUser.getLastName() + ",                                     ";


        try (RandomAccessFile file = new RandomAccessFile(PATH_USERS, "rw")) {
            String line;
            long pos = 0;
            while ((line = file.readLine()) != null) {
                if (line.startsWith(userId + ",")) {
                    pos = file.getFilePointer() - line.length() - 2;
                    file.seek(pos);
                    file.write(addonText.getBytes());
                    file.close();
                    break;
                }
            }
        }
    }

    public static void writeNewUser(User newUser) throws IOException{
        String userInfo = "" + newUser.getLoginInfo().getUserId() + "," + newUser.getLoginInfo().getUserName() + "," + 
            newUser.getEmail() + "," + newUser.getLoginInfo().getPassword() + "," + newUser.getFirstName() + "," + newUser.getLastName() + ",";
            
           
        try (RandomAccessFile file = new RandomAccessFile(PATH_USERS, "rw")) {
            String line;
            while ((line = file.readLine()) != null) {
                if (line.startsWith(" ")) {
                    file.seek(file.getFilePointer() - line.length() - 2);
                    file.write(userInfo.getBytes());
                    file.close();
                    break;
                }
            }
        }
        
        String addonTextInvoice = newUser.getLoginInfo().getUserId() + ",";
        try (RandomAccessFile file = new RandomAccessFile(PATH_INVOICES, "rw")) {
            String line;
            while ((line = file.readLine()) != null) {
                if (line.startsWith(" ")) {
                    file.seek(file.getFilePointer() - line.length() - 2);
                    file.write(addonTextInvoice.getBytes());
                    file.close();
                    break;
                }
            }
        }

        String addonTextAccount = newUser.getLoginInfo().getUserId() + ",";
        try (RandomAccessFile file = new RandomAccessFile(filePath, "rw")) {
            String line;
            while ((line = file.readLine()) != null) {
                if (line.startsWith(" ")) {
                    file.seek(file.getFilePointer() - line.length() - 2);
                    file.write(addonTextAccount.getBytes());
                    file.close();
                    break;
                }
            }
        }

        
        String addonTextTransactions = newUser.getLoginInfo().getUserId() + ",";
        try (RandomAccessFile file = new RandomAccessFile(PATH_TRANSACTIONS, "rw")) {
            String line;
            while ((line = file.readLine()) != null) {
                if (line.startsWith(" ")) {
                    file.seek(file.getFilePointer() - line.length() - 2);
                    file.write(addonTextTransactions.getBytes());
                    file.close();
                    break;
                }
            }
        }

    }

    public static Account[] readAccounts(String userId) throws IOException{
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        Stream<String> userTrans = reader.lines() 
                .filter(line -> line.startsWith(userId + ","));
        Account[] accounts = userTrans.flatMap(s -> Stream.of(s.split(","))
                .skip(1).map(Account::fromCSVString)).toArray(Account[]::new);
        reader.close();
        return accounts;
    }
    public static Account getAccountForUser(String userId, String accountNumber) throws IOException{
        Account[] accounts = readAccounts(userId);
        Account account = null;
        for(int i = 0; i < accounts.length; i++){
            if(accounts[i].getAccountNumber().equals(accountNumber)){
                account = accounts[0];
            }
        }
        return account;
    }

    public static Invoice[] getInvoicesForUser(String userID) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(PATH_INVOICES));
        Stream<String> userTrans = reader.lines() 
                .filter(line -> line.startsWith(userID + ","));
        System.out.println(userTrans);

        String[] userTransArray = userTrans.toArray(String[]::new);
        List<Invoice> transactionList = new ArrayList<>();
        for (String s : userTransArray) {
            String[] placeHolder = s.split(",");
            String[] transactionArray = new String[placeHolder.length - 1];
            for(int i =  0; i < placeHolder.length - 1; i++){
                transactionArray[i] = placeHolder[i];
            }
            
            for (int i = 1; i < transactionArray.length; i++) {
                Invoice transaction = Invoice.fromCSVString(transactionArray[i]);
                transactionList.add(transaction);
            }
        }
        Invoice[] invoices = transactionList.toArray(Invoice[]::new);




        reader.close();
        return invoices;
    }

    
    public static void writeAccount(String userId, Account account) {
        writeTransactionOrAccount(userId, account.toCSVString(), filePath);
    }
    public static void writeTransaction(String userId, Transaction transaction) {
        writeTransactionOrAccount(userId, transaction.toCSVString(), PATH_TRANSACTIONS);
    }
    public static void deleteOrEditAccount(User loggedInUser) {
        File oldFile    = new File(filePath);
        File newFile    = new File(PATH_TEMPFILE);
        try {
            FileWriter fileWriter         = new FileWriter(PATH_TEMPFILE, true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            PrintWriter printWriter       = new PrintWriter(bufferedWriter);

            FileInputStream inputStream   = new FileInputStream(oldFile);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            int lines = (int) bufferedReader.lines().count();
            inputStream.getChannel().position(0);
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String accountStrings = loggedInUser.getAccountsAsList().stream()
                    .map(Account::toCSVString).reduce("", String::concat);
            String userId = loggedInUser.getLoginInfo().getUserId();

            String newLine = userId + "," + accountStrings;

            String currentLine;
            for (int i = 1; i < lines; i++) {
                currentLine = bufferedReader.readLine();
                if (currentLine.startsWith(userId)) {
                    printWriter.println(newLine);
                }
                else {
                    printWriter.println(currentLine);
                }
            }
            currentLine = bufferedReader.readLine();
            if (currentLine.startsWith(userId)) {
                printWriter.print(newLine);
            }
            else {
                printWriter.print(currentLine);
            }

            inputStream.close();
            bufferedReader.close();
            printWriter.flush();
            printWriter.close();
            oldFile.delete();
            File dump = new File(filePath);
            newFile.renameTo(dump);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void writeTransactionOrAccount(String userId, String toCSVString, String filePath) {
        File oldFile    = new File(filePath);
        File newFile    = new File(PATH_TEMPFILE);
        try {
            FileWriter fileWriter         = new FileWriter(PATH_TEMPFILE, true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            PrintWriter printWriter       = new PrintWriter(bufferedWriter);

            FileInputStream inputStream   = new FileInputStream(oldFile);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            int lines = (int) bufferedReader.lines().count();
            inputStream.getChannel().position(0);
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String currentLine;
            for (int i = 1; i < lines; i++) {
                currentLine = bufferedReader.readLine();
                if (currentLine.startsWith(userId)) {
                    printWriter.println(currentLine + toCSVString);
                }
                else {
                    printWriter.println(currentLine);
                }
            }
            currentLine = bufferedReader.readLine();
            if (currentLine.startsWith(userId)) {
                printWriter.print(currentLine + toCSVString);
            }
            else {
                printWriter.print(currentLine);
            }

            inputStream.close();
            bufferedReader.close();
            printWriter.flush();
            printWriter.close();
            oldFile.delete();
            File dump = new File(filePath);
            newFile.renameTo(dump);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void writeNewInvoice(String userId, Invoice invoice) throws IOException{
        String addonText = "" + invoice.getDueDate() + "." + invoice.getAmount() + "." + invoice.getRecipientAccountNumber() + ",";
        try (RandomAccessFile file = new RandomAccessFile(PATH_INVOICES, "rw")) {
            String line;
            long pos = 0;
            while ((line = file.readLine()) != null) {
                if (line.startsWith(userId + ",")) {
                    pos = file.getFilePointer() - line.length() + line.indexOf("   ") - 1;
                    file.seek(pos);
                    file.write(addonText.getBytes());
                    file.close();
                    break;
                }
            }
        }
    }

    public static void writeNewBudget(String userId, Budget budget) throws IOException {
        String addonText = ""; //TODO: make in format of budget
        try (RandomAccessFile file = new RandomAccessFile(PATH_BUDGETS, "rw")) {
            String line;
            long pos = 0;
            while ((line = file.readLine()) != null) {
                if (line.startsWith(userId + ",")) {
                    pos = file.getFilePointer() - line.length() + line.indexOf("   ") - 1;
                    file.seek(pos);
                    file.write(addonText.getBytes());
                    file.close();
                    break;
                }
            }
        }
    }

    public static Transaction[] findTransactionsOfUserAndAccountNumber(String userId, String accountNumber) throws IOException {
        return (Transaction[]) Arrays.stream(readAllTransactions(userId)) 
                .filter(transaction -> transaction.getFromAccountNumber().equals(accountNumber) || transaction.getToAccountNumber().equals(accountNumber))
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

    public static void deleteInvoice(Invoice invoice, String userId) throws IOException{
        
        try (RandomAccessFile file = new RandomAccessFile(PATH_INVOICES, "rw")) {
            String line;
            long pos = 0;
            while ((line = file.readLine()) != null) {
                if (line.startsWith(userId + ",")) {
                    String addonText = "";
                    for(int i = 0; i < line.length(); i++){
                        addonText = addonText + " ";
                    }
                    pos = file.getFilePointer() - line.length() - 2;
                    file.seek(pos);
                    file.write(addonText.getBytes());
                    file.close();
                    break;
                }
            }
        }
    }

}
