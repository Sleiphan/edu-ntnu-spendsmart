package edu.ntnu.g14.dao;

import edu.ntnu.g14.Invoice;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Arrays;

public class InvoiceDAO {

    private final Charset charset;
    private final IndexedDataFile idf;

    public InvoiceDAO(String filePath) throws IOException {
        this(filePath, Charset.defaultCharset());
    }

    public InvoiceDAO(String filePath, Charset charset) throws IOException {
        this.idf = new IndexedDataFile(filePath);
        this.charset = charset;
    }

    public boolean addNewInvoice(String userID, Invoice budget) {
        try {
            idf.addNewData(userID, budget.toCSVString().getBytes(charset));
        } catch (IOException e) {
            return false;
        }

        return true;
    }

    public boolean deleteInvoice(String userID, int invoiceIndex) {
        try {
            idf.deleteData(userID, invoiceIndex);
        } catch (IOException e) {
            return false;
        }

        return true;
    }

    public Invoice getInvoice(String userID, int invoiceIndex) {
        byte[] data;
        try {
            data = idf.getData(userID, invoiceIndex);
        } catch (IOException e) {
            return null;
        }

        String csvString = new String(data, charset);
        return Invoice.fromCSVString(csvString);
    }

    public Invoice[] getAllInvoices(String userID) {
        byte[][] data;

        try {
            data = idf.readAllInIdentifier(userID);
        } catch (IOException e) {
            return null;
        }

        if (data == null)
            return null;

        return Arrays.stream(data)
                .map(b -> new String(b, charset))
                .map(Invoice::fromCSVString)
                .toArray(Invoice[]::new);
    }
}