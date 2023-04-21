package edu.ntnu.g14;


import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import edu.ntnu.g14.dao.BudgetDAO;
import edu.ntnu.g14.dao.DAOTools;
import edu.ntnu.g14.dao.InvoiceDAO;


//TODO: create exceptions

public class FileManagement {

    public static final String PATH_ACCOUNTS     = "saves/accounts.txt";
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
        Stream<String> userInfoLine = reader.lines()
                .filter(line -> line.startsWith(userId + ","));

        Transaction[] transactions = readAllTransactions(userId);
        Account[] accounts = readAccounts(userId);
        Invoice[] invoices = new InvoiceDAO(PATH_INVOICES).getAllInvoices(userId);
        Budget budget = new BudgetDAO(PATH_BUDGETS).getBudget(userId);

        String[] userInfo = userInfoLine.flatMap(s -> Stream.of(s.split(","))
                        .skip(1)
                        .map(String::toString))
                .toArray(String[]::new);

        String username = userInfo[0];
        String email = userInfo[1];
        String password = userInfo[2];
        String firstName = userInfo[3];
        String lastName = userInfo[4];



        Login loginInfo = new Login(username,password, userId);

        return new User(accounts, invoices, loginInfo, email, lastName, firstName, transactions, budget);
        
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
        newEditUser(newUser);
        editAccount(newUser);


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
        BufferedReader reader = new BufferedReader(new FileReader(PATH_ACCOUNTS));
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
        writeTransactionOrAccount(userId, account.toCSVString(), PATH_ACCOUNTS);
    }
    public static void writeTransaction(String userId, Transaction transaction) {
        writeTransactionOrAccount(userId, transaction.toCSVString(), PATH_TRANSACTIONS);
    }
    public static void editAccount(User loggedInUser) {
        String accountStrings = loggedInUser.getAccountsAsList().stream()
                .map(Account::toCSVString).reduce("", String::concat);
        String userId = loggedInUser.getLoginInfo().getUserId();

        String newLine = userId + "," + accountStrings;
        editAccountOrUser(newLine, userId, PATH_ACCOUNTS);
    }
    public static void newEditUser(User loggedInUser) {

        String userId = loggedInUser.getLoginInfo().getUserId();

        String newLine = loggedInUser.toCSVString();
        editAccountOrUser(newLine, userId, PATH_USERS);

    }
    private static void editAccountOrUser(String newLine, String userId, String filePath) {
        final String LINE_DELIMITER = "\n";
        final Path accountOrUserFilePath = Path.of(filePath);
        final Path directory = Path.of(filePath.substring(0, filePath.lastIndexOf("/")));
        String content = "";

        if (newLine.startsWith(LINE_DELIMITER))
            newLine = newLine.substring(1);
        if (newLine.endsWith(LINE_DELIMITER))
            newLine = newLine.substring(0, newLine.length() - 1);

        try {
            content = Files.readString(accountOrUserFilePath);
        } catch (FileNotFoundException e) {
            try {
                Files.createDirectories(directory);
                Files.createFile(accountOrUserFilePath);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        boolean entryFound = false;

        String[] lines = content.split(LINE_DELIMITER);
        for (int i = 0; i < lines.length; i++) {
            if (lines[i].startsWith(userId)) {
                lines[i] = newLine;
                entryFound = true;
                break;
            }
        }

        StringBuilder sb = new StringBuilder();
        if (entryFound) {
            for (String line : lines)
                sb.append(line).append(LINE_DELIMITER);
        } else {
            sb.append(newLine);
        }

        try {
            Files.writeString(accountOrUserFilePath, sb.toString());
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
