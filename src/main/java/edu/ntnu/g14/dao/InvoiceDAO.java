package edu.ntnu.g14.dao;

import edu.ntnu.g14.model.Invoice;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;

/**
 * A class that provides methods for interacting with a file to store and retrieve invoice data.
 */
public class InvoiceDAO {

  private final Charset charset;
  private final IndexedDataFile file;

  /**
   * Constructs an InvoiceDAO object with the specified file path using the default character set.
   *
   * @param filePath the path of the file where invoice data is stored
   * @throws IOException if an I/O error occurs
   */
  public InvoiceDAO(String filePath) throws IOException {
    this(filePath, Charset.defaultCharset());
  }

  /**
   * Constructs an InvoiceDAO object with the specified file path and character set.
   *
   * @param filePath the path of the file where invoice data is stored
   * @param charset  the character set used to encode and decode the data
   * @throws IOException if an I/O error occurs
   */
  public InvoiceDAO(String filePath, Charset charset) throws IOException {
    this.file = new IndexedDataFile(filePath, charset);
    this.charset = charset;
  }

  /**
   * Adds a new invoice for the specified user to the data file.
   *
   * @param userID  the ID of the user
   * @param invoice the invoice to add
   * @return true if the invoice was added successfully, false otherwise
   */
  public boolean addNewInvoice(String userID, Invoice budget) {
    try {
      file.addNewData(userID, budget.toCSVString().getBytes(charset));
    } catch (IOException e) {
      return false;
    }

    return true;
  }

  /**
   * Deletes the specified invoice for the specified user from the data file.
   *
   * @param userID       the ID of the user
   * @param invoiceIndex the index of the invoice to delete
   * @return true if the invoice was deleted successfully, false otherwise
   */
  public boolean deleteInvoice(String userID, int invoiceIndex) {
    try {
      file.deleteData(userID, invoiceIndex);
    } catch (IOException e) {
      return false;
    }

    return true;
  }

  /**
   * Retrieves the specified invoice for the specified user from the data file.
   *
   * @param userID       the ID of the user
   * @param invoiceIndex the index of the invoice to retrieve
   * @return the retrieved invoice, or null if the invoice could not be retrieved
   */
  public Invoice getInvoice(String userID, int invoiceIndex) {
    byte[] data;
    try {
      data = file.getData(userID, invoiceIndex);
    } catch (IOException e) {
      return null;
    }

    String csvString = new String(data, charset);
    return Invoice.fromCSVString(csvString);
  }

  /**
   * Retrieves all invoices for the specified user from the data file.
   *
   * @param userID the ID of the user
   * @return an array of all invoices for the specified user, or null if the invoices could not be
   * retrieved
   */
  public Invoice[] getAllInvoices(String userID) {
    byte[][] data;

    try {
      data = file.readAllInIdentifier(userID);
    } catch (IOException e) {
      return null;
    }

    if (data == null) {
      return null;
    }

    return Arrays.stream(data)
        .map(b -> new String(b, charset))
        .map(Invoice::fromCSVString)
        .toArray(Invoice[]::new);
  }

  /**
   * Deletes all invoices associated with the specified user from the data file.
   *
   * @param userID the identifier of the user whose transactions should be deleted
   * @throws IOException if an I/O error occurs while attempting to delete the transactions
   */
  public void deleteUser(String userID) throws IOException {
    file.deleteIdentifier(userID);
  }
}