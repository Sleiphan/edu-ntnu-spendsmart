package edu.ntnu.g14.dao;

import edu.ntnu.g14.Budget;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;

public class BudgetDAO {

    private static final String USER_ID_SEPARATOR = ":";

    private final File fileFile;
    private final File tempFile;

    private RandomAccessFile fileStream;
    private RandomAccessFile tempStream;
    private FileChannel fileChannel;
    private FileChannel tempChannel;
    private ByteBuffer tempBuffer = ByteBuffer.allocateDirect(2048);

    public BudgetDAO(String filePath) throws IOException {
        fileFile = new File(filePath);
        fileFile.createNewFile();
        tempFile = new File(filePath + ".temp");
        tempFile.createNewFile();
        tempFile.deleteOnExit();
    }

    /**
     *
     * @param userID
     * @param budget
     * @throws IOException
     */
    public void setBudget(String userID, Budget budget) throws IOException {
        openStreams();

        long[] endpoints = getBudgetEndpoints(userID);
        long beginIndex = endpoints[0];
        long endIndex = endpoints[1];

        String csv = budget.toCSV();


        if (beginIndex != -1) {
            byte[] data = (csv + "\n").getBytes(Charset.defaultCharset());
            long start = beginIndex + userID.length() + USER_ID_SEPARATOR.length();
            long count = endIndex - start;
            replace(data, start, count);
        }
        else {
            appendToFile(((isFileEmpty() ? "" : "\n") + userID + ":" + csv).getBytes(Charset.defaultCharset()));
        }

        closeStreams();
    }

    public Budget getBudget(String userID) throws IOException {
        openStreams();

        long[] endpoints = getBudgetEndpoints(userID);
        long beginIndex = endpoints[0];
        long endIndex = endpoints[1];

        final String prefix = userID + USER_ID_SEPARATOR;

        fileStream.seek(0);
        String line = "";
        while (line != null && !line.startsWith(prefix))
            line = fileStream.readLine();

        closeStreams();

        if (line == null || line.isEmpty())
            return null;

        line = line.substring(prefix.length());

        Budget budget = null;
        try {
            budget = Budget.fromCSV(line); // If parsing somehow fails...
        } catch (Exception e) {
            return null; // ... tell the caller that we could not find any budget for the specified user.
        }
        return budget;
    }

    private boolean isFileEmpty() throws IOException {
        return fileStream.length() <= 0;
    }

    private long[] getBudgetEndpoints(String userID) throws IOException {
        long beginIndex = 0;
        long endIndex = 0;

        fileStream.seek(0);
        String line = null;
        while ((line = fileStream.readLine()) != null) {
            if (line.startsWith(userID + USER_ID_SEPARATOR)) {
                endIndex = fileStream.getFilePointer();
                break;
            }
            beginIndex = fileStream.getFilePointer();
        }

        boolean foundExistingEntry = beginIndex < fileStream.length() - 1;

        if (foundExistingEntry)
            return new long[] { beginIndex, endIndex };
        else
            return new long[] { -1, -1 };
    }

    private void openStreams() throws FileNotFoundException {
         fileStream = new RandomAccessFile(fileFile, "rw");
         tempStream = new RandomAccessFile(tempFile, "rw");
         fileChannel = fileStream.getChannel();
         tempChannel = tempStream.getChannel();
    }

    private void closeStreams() throws IOException {
        fileStream.close();
        tempStream.close();
        fileStream = null;
        tempStream = null;
        fileChannel = null;
        tempChannel = null;
    }

    private void replace(byte[] data, long start, long count) throws IOException {
        DAOTools.replace(data, start, count, fileStream, tempStream);
    }

    private void appendToFile(byte[] data) throws IOException {
        DAOTools.appendToFile(data, fileStream);
    }
}
