package edu.ntnu.g14;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.stream.Collectors;



//TODO: create exceptions

public class FileManagement {
     
    public static String readUsers(String userID) throws IOException {
        InputStream input = FileManagement.class.getResourceAsStream("/resources/textfiles/users.txt");
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        
        String userInfoString = reader.lines()
            .map(String::trim)
            .filter(line -> line.startsWith(userID + ","))
            .findFirst()
            .orElse(null);
        
        reader.close();
        return userInfoString;
    }

    public static String readAllTransactions(String userID) throws IOException {
        InputStream input = FileManagement.class.getResourceAsStream("/resources/textfiles/transactions.txt");
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        
        String transactionsInfoString = reader.lines()
            .map(String::trim)
            .filter(line -> line.startsWith(userID + ","))
            .findFirst()
            .orElse(null);
        
        reader.close();
        return transactionsInfoString;
    }

    public static String findTransaction(LocalDate from, LocalDate to, String userID) throws IOException {
        String allTransactions = readAllTransactions(userID);
        String[] transactions = allTransactions.split(",");
        
        return Arrays.stream(transactions)
                .skip(1)
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
    

    public static void main(String[] args) {
        LocalDate from = LocalDate.of(2023, 03, 12);
        LocalDate to = LocalDate.of(2024, 01, 13);
        try {
            String title = findTransaction(from, to, "kkk");
            System.out.println(title);
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
    }
}

