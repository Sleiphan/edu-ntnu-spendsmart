package edu.ntnu.g14.dao;

import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * The class provides a way to read and manipulate data stored in files.
 */
public class IndexedDataFile {

    private static final char NEW_LINE = '\n';
    private static final char ESCAPE_CHAR = '\\';
    private static final char IDENTIFIER_SEPARATOR = ':';
    private static final char INDEX_SEPARATOR = ',';
    private static final char DATA_SEPARATOR = NEW_LINE;
    private static final char DATA_SEPARATOR_REPLACEMENT = 'n';

    private final File fileFile;
    private final File tempFile;

    private RandomAccessFile fileStream;
    private RandomAccessFile tempStream;
    private FileChannel fileChannel;
    private FileChannel tempChannel;
    private int chainedOpens;

    private long dataStartPosition;
    
    /**
     * This class uses an internal charset to read and write the index-section of the file.
     */
    private final Charset charset;

    /**
    * Constructs a new IndexedDataFile object given a file path and default charset.
    * @param filePath the path of the file to be used
    * @throws IOException if an error occurs while creating the temp file or opening the streams
    */
    public IndexedDataFile(String filePath) throws IOException {
        this(filePath, Charset.defaultCharset());
    }

    /**
    * Constructs a new IndexedDataFile object given a file path and charset.
    * @param filePath the path of the file to be used
    * @param charset the charset to be used for reading and writing the index-section of the file
    * @throws IOException if an error occurs while creating the temp file or opening the streams
    */
    public IndexedDataFile(String filePath, Charset charset) throws IOException {
        this.charset = charset;

        fileFile = new File(filePath);
        tempFile = new File(filePath + ".temp");
        tempFile.createNewFile();
        tempFile.deleteOnExit();

        openStreams();
        updateStartPositionIndex();
        closeStreams();
    }

    /**
    * Reads all the data stored under a given identifier.
    * @param identifier the identifier of the data to be read
    * @return a byte array containing all the data stored under the given identifier, null if no data is found
    * @throws IOException if an error occurs while reading the data
    */
    public byte[][] readAllInIdentifier(String identifier) throws IOException {
        openStreams();

        long[] indices = getIndices(identifier);
        if (indices == null) {
            closeStreams();
            return null;
        }

        
        indices = Arrays.stream(indices)
                .map(l -> l + dataStartPosition)
                .toArray();

        byte[][] result = new byte[indices.length][];
        for (int i = 0; i < result.length; i++) {
            fileStream.seek(indices[i]);
            result[i] = readLine();
        }

        closeStreams();

        return result;
    }

    /**
    * Deletes data at the specified index for a given identifier.
    * @param identifier the identifier for the data
    * @param index the index of the data to delete
    * @return true if the data was successfully deleted, false otherwise
    * @throws IOException if an I/O error occurs
    */
    public boolean deleteData(String identifier, int index) throws IOException {
        openStreams();
        long dataPos = getDataPosition(identifier, index);
        if (dataPos == -1) {
            closeStreams();
            return false;
        }

        fileStream.seek(dataPos); 
        long dataEnd = skipLine();
        if (dataEnd != fileStream.length())
            dataEnd--;


        dataPos--; 
        long dataLength = dataEnd - dataPos;

        DAOTools.deleteData(dataPos, dataLength, fileStream, tempStream);
        deleteIndex(identifier, index);
        updateIndices(-dataLength, dataPos);
        closeStreams();
        return true;
    }

    /**
    * Deletes all data associated with a given identifier.
    * @param identifier the identifier for the data to delete
    * @throws IOException if an I/O error occurs
    */
    public void deleteIdentifier(String identifier) throws IOException {
        int index = getNumEntries(identifier) - 1;
        if (index < 0)
            return;

        openStreams();

        for (; index >= 0; index--)
            deleteData(identifier, index);

        closeStreams();
    }

    /**
    * Adds new data for a given identifier.
    * @param identifier the identifier for the data
    * @param data the data to add
    * @throws IOException if an I/O error occurs
    * @throws IllegalArgumentException if the identifier contains the separator characters
    */
    public void addNewData(String identifier, byte[] data) throws IOException {
        if (identifier.contains(String.valueOf(IDENTIFIER_SEPARATOR)))
            throw new IllegalArgumentException("Identifier cannot contain " + IDENTIFIER_SEPARATOR + ". The submitted invalid identifier was '" + identifier + "'");
        if (identifier.contains(String.valueOf(DATA_SEPARATOR)))
            throw new IllegalArgumentException("Identifier cannot contain " + DATA_SEPARATOR + ". The submitted invalid identifier was '" + identifier + "'");

        data = convertToStorageFormat(data);

        openStreams();

        DAOTools.appendToFile(data, fileStream);
        DAOTools.appendToFile("\n".getBytes(charset), fileStream);

        long dataIndex = fileStream.length() - data.length - 1;
        if (dataStartPosition == -1)
            DAOTools.insertIntoFile("\n".getBytes(charset), 0, fileStream, tempStream);
        else
            dataIndex -= dataStartPosition;

        appendIndex(identifier, dataIndex);
        closeStreams();
    }

    /**
    * Sets the data at a specified index for a given identifier.
    * @param identifier the identifier for the data
    * @param index the index of the data to set
    * @param data the new data to set
    * @throws IOException if an I/O error occurs
    * @throws IllegalArgumentException if the index is out of bounds or the identifier does not exist
    */
    public void setData(String identifier, int index, byte[] data) throws IOException {
        data = convertToStorageFormat(data);

        openStreams();

        long dataStart = getDataPosition(identifier, index);
        if (dataStart == -1)
            throw new IllegalArgumentException("Could not find any data at index " + index + ". Either the index is out of bounds, or the identifier " + identifier + " does not exist.");

        fileStream.seek(dataStart);

        long dataEnd = skipLine() - 1;
        long dataLength = dataEnd - dataStart;
        DAOTools.replace(data, dataStart, dataLength, fileStream, tempStream);

        long dataSizeChange = data.length - dataLength;
        updateIndices(dataSizeChange, dataStart);

        closeStreams();
    }

    /**
    * Gets the data at a specified index for a given identifier.
    * @param identifier the identifier for the data
    * @param index the index of the data to retrieve
    * @return the data at the specified index, or null if it does not exist
    * @throws IOException if an I/O error occurs
    */
    public byte[] getData(String identifier, int index) throws IOException {
        openStreams();
        long dataPos = getDataPosition(identifier, index);
        if (dataPos == -1) {
            closeStreams();
            return null;
        }

        fileStream.seek(dataPos);
        byte[] result = readLine();
        closeStreams();

        result = convertFromStorageFormat(result);

        return result;
    }

    /**
    * Gets the number of entries for a given identifier.
    * @param identifier the identifier for the data
    * @return the number of entries for the specified identifier, or -1 if it does not exist
    * @throws IOException if an I/O error occurs
    */
    public int getNumEntries(String identifier) throws IOException {
        openStreams();
        long position = getPositionOfIndices(identifier);
        if (position == -1) {
            closeStreams();
            return -1;
        }
        fileStream.seek(position);

        int numEntries = readLineS().split(String.valueOf(INDEX_SEPARATOR)).length;

        closeStreams();
        return numEntries;
    }

    /**
    * Gets a range of entries for a given identifier.
    * @param identifier the identifier for the data
    * @param startIndex the starting index of the range (inclusive)
    * @param endIndex the ending index of the range (exclusive)
    * @return an array of the data within the specified range, or null if the range is invalid
    * @throws IOException if an I/O error occurs
    */
    public byte[][] getRangeOfEntries(String identifier, int startIndex, int endIndex) throws IOException {
        int numEntries = getNumEntries(identifier);
        if (numEntries == -1 || startIndex >= numEntries || endIndex <= 0 || endIndex <= startIndex)
            return null;

        if (startIndex < 0)
            startIndex = 0;
        if (endIndex >= numEntries)
            endIndex = numEntries - 1;

        int length = endIndex - startIndex;
        if (length <= 0)
            return null;

        byte[][] data = new byte[length][];

        for (int i = 0; i < length; i++)
            data[i] = getData(identifier, startIndex + i);

        return data;
    }

    /**
    * Gets a specified amount of entries for a given identifier, starting from the most recent.
    * @param identifier the identifier for the data
    * @param amount the number of entries to retrieve
    * @return an array of the most recent data entries, or null if there are none
    * @throws IOException if an I/O error occurs
    */
    public byte[][] getEntriesBackwards(String identifier, int amount) throws IOException {
        openStreams();
        int lastIndex = getNumEntries(identifier) - 1;
        if (lastIndex < 0) {
            closeStreams();
            return null;
        }

        if (lastIndex >= amount)
            amount = lastIndex + 1;

        byte[][] data = new byte[amount][];
        for (int i = 0; i < amount; i++)
            data[i] = getData(identifier, amount - 1 - i);

        closeStreams();
        return data;
    }

    /**
    * Checks if the specified identifier is present in the stored data.
    * This method opens the required streams, reads all identifiers, and then closes the streams.
    *
    * @param identifier The identifier to search for.
    * @return true if the identifier is present in the stored data, false otherwise.
    * @throws IOException if there is an error reading the data from the stream.
    */
    public boolean containsIdentifier(String identifier) throws IOException {
        openStreams();
        String[] ids = readAllIdentifiers();
        closeStreams();

        boolean found = false;

        for (int i = 0; i < ids.length && !found; i++)
            if (ids[i].equals(identifier))
                found = true;

        return found;
    }

    /**
    * Converts the specified data to the storage format.
    *
    * @param data The data to convert.
    * @return The converted data.
    */ 
    private byte[] convertToStorageFormat(byte[] data) {
        ByteArrayOutputStream out = new ByteArrayOutputStream(data.length);
        for (byte b : data) {
            if (b == DATA_SEPARATOR) {
                out.write(ESCAPE_CHAR);
                out.write(DATA_SEPARATOR_REPLACEMENT);
            } else
                out.write(b);
        }

        if (out.size() == data.length)
            return data;
        else
            return out.toByteArray();
    }

    /**
    * Converts the specified data from the storage format.
    *
    * @param data The data to convert.
    * @return The converted data.
    */
    private byte[] convertFromStorageFormat(byte[] data) {
        ByteArrayOutputStream out = new ByteArrayOutputStream(data.length);

        for (int i = 0; i < data.length; i++) {
            if (data[i] == ESCAPE_CHAR && i + 1 < data.length && data[i+1] == DATA_SEPARATOR_REPLACEMENT){
                out.write(DATA_SEPARATOR);
                i++;
            }
            else
                out.write(data[i]);
        }

        if (out.size() == data.length)
            return data;
        else
            return out.toByteArray();
    }

    /**
    * Deletes the data at the specified index for the given identifier.
    * This method updates the start position index after deleting the data.
    *
    * @param identifier The identifier to delete data for.
    * @param index The index of the data to delete.
    * @return true if the data was deleted successfully, false otherwise.
    * @throws IOException if there is an error reading or writing the data to the stream.
    */
    private boolean deleteIndex(String identifier, int index) throws IOException {
        long dataPos = getIndexDataPosition(identifier, index);
        if (dataPos < 0)
            return false;

        long dataEnd = skipToNextIndex();
        if (dataEnd == -1)
            return false;
        if (dataEnd == -2) {
            
            dataEnd = fileStream.getFilePointer();

            
            if (index == 0) {
              
                fileStream.seek(fileStream.getFilePointer() - 2);
                byte b;
                while ((b = fileStream.readByte()) != DATA_SEPARATOR) { 
                    long newPos = fileStream.getFilePointer() - 2;
                    fileStream.seek(newPos);
                    if (newPos == 0)
                        break;
                }
                dataPos = fileStream.getFilePointer();
            }
        }

        long dataLength = dataEnd - dataPos;

        DAOTools.deleteData(dataPos, dataLength, fileStream, tempStream);
        updateStartPositionIndex();
        return true;
    }

    /**
    * Returns the position of the specified index for the given identifier.
    *
    * @param identifier The identifier to get the index position for.
    * @param index The index to get the position for.
    * @return The position of the specified index for the given identifier, or -1 if the identifier or index is not found.
    * @throws IOException if there is an error reading the data from the stream.
    */
    private long getIndexDataPosition(String identifier, int index) throws IOException {
        long pos = getPositionOfIndices(identifier);
        if (pos == -1)
            return -1;

        for (int i = 0; i < index; i++)
            if (skipToNextIndex() < 0)
                return -1;

        return fileStream.getFilePointer();
    }

    /**
    * Skips past any bytes in the fileStream until either INDEX_SEPARATOR or DATA_SEPARATOR is found.
    * @return The file pointer position pointing to the next INDEX_SEPARATOR or DATA_SEPARATOR, or -1 if end of file is reached before finding either separator, or -2 if the DATA_SEPARATOR is found instead of the INDEX_SEPARATOR.
    * @throws IOException
    */
    private long skipToNextIndex() throws IOException {
        byte b;
        try {
            b = fileStream.readByte();
            while (b != INDEX_SEPARATOR && b != DATA_SEPARATOR)
                b = fileStream.readByte();
        } catch (EOFException e) {
            return -1;
        }
        if (b == DATA_SEPARATOR)
            return -2;
        else
            return fileStream.getFilePointer();
    }

    /**
    * Appends the given data index to the entry with the specified identifier in the index section of the file. If the entry does not exist, creates a new entry for the identifier.
    * @param identifier The identifier of the entry to append the data index to.
    * @param dataIndex The data index to append.
    * @throws IOException
    */
    private void appendIndex(String identifier, long dataIndex) throws IOException {
        long indicesPos = getPositionOfIndices(identifier);

        boolean newIdentifier = indicesPos == -1;
        if (newIdentifier)
            indicesPos = createNewIdentifierEntry(identifier);

        fileStream.seek(indicesPos);
        skipLine();
        long start = fileStream.getFilePointer() - 1;

        String data = newIdentifier ? "" : ",";
        data += dataIndex;

        DAOTools.insertIntoFile(data.getBytes(charset), start, fileStream, tempStream);
        updateStartPositionIndex();
    }

    /**
    * Creates a new entry for the given identifier in the index section of the file.
    * @param identifier The identifier for the new entry.
    * @return The position in the file where the new entry starts.
    * @throws IOException
    */
    private long createNewIdentifierEntry(String identifier) throws IOException {
        String text = identifier + IDENTIFIER_SEPARATOR;
        byte[] data = (text + '\n').getBytes(charset);

        long offset = 0; 
        DAOTools.insertIntoFile(data, offset, fileStream, tempStream);
        updateStartPositionIndex();
        return offset + text.length() - 1;
    }

    /**
    * Returns the byte position of the data identified by the given identifier and index.
    * @param identifier The identifier of the data.
    * @param index The index of the data.
    * @return The byte position of the data, or -1 if the identifier or index is not found.
    * @throws IOException
    */
    private long getDataPosition(String identifier, int index) throws IOException {
        long[] indices = getIndices(identifier);
        if (indices == null || index >= indices.length)
            return -1;
        return indices[index] + dataStartPosition;
    }

    /**
    * Reads bytes from the given stream until an IDENTIFIER_SEPARATOR is found, and returns the bytes as a string.
    * @param stream The stream to read from.
    * @return The bytes read as a string, or null if a DATA_SEPARATOR is found before an IDENTIFIER_SEPARATOR, or end of file is reached before finding an IDENTIFIER_SEPARATOR.
    * @throws IOException
    */
    private String readUntil_breakNewLine(RandomAccessFile stream) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        char current;
        try {
            while ((current = (char) stream.readByte()) != IndexedDataFile.IDENTIFIER_SEPARATOR) {
                if (current == DATA_SEPARATOR)
                    return null;
                out.write(current);
            }
        } catch (EOFException e) {
            return null;
        }

        return out.toString(charset);
    }

    /**
     * Forwards the cursor of the fileStream past the next DATA_SEPARATOR delimiter.
     * @return The data index pointing past the next line shift. If end of file was reached, returns the position at the end of the file.
     * @throws IOException
     */
    private long skipLine() throws IOException {
        try {
            while (fileStream.readByte() != DATA_SEPARATOR) continue;
        } catch (EOFException e) {
            return fileStream.length();
        }

        return fileStream.getFilePointer();
    }

    /**
    * Reads a line from the fileStream until a new line character is found.
    * @return The bytes read as a byte array, or an empty byte array if end of file is reached before finding a new line character.
    * @throws IOException
    */
    private byte[] readLine() throws IOException {
        final byte newLine = '\n';
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            byte b;
            while ((b = fileStream.readByte()) != newLine)
                out.write(b);
        } catch (EOFException e) {
            // Ignore
        }

        return out.toByteArray();
    }

    /**
    * Reads a line from the fileStream until a new line character is found, and returns the line as a string.
    * @return The line read as a string, or an empty string if end of file is reached before finding a new line character.
    * @throws IOException
    */
    private String readLineS() throws IOException {
        return new String(readLine(), charset);
    }

    /**
    * Finds the position in the file where the index entry for the given identifier starts.
    * @param identifier The identifier to search for.
    * @return The byte position where the index entry for the identifier starts, or -1 if the identifier is not found.
    * @throws IOException
    */
    private long getPositionOfIndices(String identifier) throws IOException {
        fileStream.seek(0); // Read from the beginning of the file.

        String currID = "";
        while (!currID.equals(identifier)) {
            currID = readUntil_breakNewLine(fileStream);
            if (currID == null)
                return -1;
            if (currID.equals(identifier))
                break;
            else
                skipLine();
        }

        long position = fileStream.getFilePointer();
        return position;
    }

    /**
     * Reads the index line of the submitted identifier, and returns all indices as an array.
     * An index points to the start of the data.
     * @return A long[] containing all indices under the submitted identifier. Returns null if no such identifier exists.
     * @throws IOException
     */
    private long[] getIndices(String identifier) throws IOException {
        long position = getPositionOfIndices(identifier);
        if (position == -1)
            return null;
        fileStream.seek(position);
        String line = readLineS();

        return Arrays.stream(line.split(String.valueOf(INDEX_SEPARATOR)))
                .map(s->s.replaceAll("\\s+",""))
                .mapToLong(Long::parseLong)
                .toArray();
    }

    /**
    * Updates all index values that come after the specified position with the given data amount change.
    * If the data amount change is 0 or if there is no data start position set, no updates will be made.
    * @param dataAmountChange the amount to change the indices by
    * @param positionOfChange the position to start changing indices from
    * @throws IOException if there is an issue accessing the file
    */
    private void updateIndices(long dataAmountChange, long positionOfChange) throws IOException {
        if (dataAmountChange == 0)
            return;

        if (dataStartPosition == -1)
            return;

        positionOfChange -= dataStartPosition;

        long[][] allIndices = getAllIndices();

        for (long[] indexGroup : allIndices)
            for (int i = 0; i < indexGroup.length; i++)
                indexGroup[i] = indexGroup[i] > positionOfChange ? indexGroup[i] + dataAmountChange : indexGroup[i];

        setAllIndices(allIndices);
    }

    /**
    * Updates the data start position to the current position in the file.
    * @throws IOException if there is an issue accessing the file
    */
    private void updateStartPositionIndex() throws IOException {
        dataStartPosition = readStartPositionOfData();
    }

    /**
    * Reads the start position of the data in the file by searching for the double line shift that marks the beginning of the data.
    * If no double line shift is found, returns -1.
    * @return the start position of the data, or -1 if not found
    * @throws IOException if there is an issue accessing the file
    */
    private long readStartPositionOfData() throws IOException {
        fileStream.seek(0);

        final byte newLine = DATA_SEPARATOR;
        byte current = 0;
        byte lastByte = 0;

        try {
            while (!((current = fileStream.readByte()) == newLine && lastByte == newLine))
                lastByte = current;
        } catch (EOFException e) {
            return -1;
        }

        return fileStream.getFilePointer();
    }

    /**
    * Reads all data in the index part of the file, including the double line shift marking the beginning of the data.
    * @return the index data as a string
    * @throws IOException if there is an issue accessing the file
    */
    private String readAllIndexData() throws IOException {
        int dataStart = (int) dataStartPosition;
        byte[] data = new byte[dataStart];
        fileStream.read(data, 0, dataStart);
        return new String(data, charset);
    }

    /**
    * Reads all present identifiers from the file. If the file does not contain any identifiers, returns an empty array.
    * @return an array of all identifiers in the file
    * @throws IOException if there is an issue accessing the file
    */
    public String[] readAllIdentifiers() throws IOException {
        List<String> identifiers = new ArrayList<>();

        openStreams();

        fileStream.seek(0);
        String currID;
        while ((currID = readUntil_breakNewLine(fileStream)) != null) {
            identifiers.add(currID);
            skipLine();
        }

        closeStreams();

        return identifiers.toArray(String[]::new);
    }

    /**
    * Reads all index groups from the file and returns them as a two-dimensional array.
    * @return a two-dimensional array of all index groups in the file
    * @throws IOException if there is an issue accessing the file
    */
    private long[][] getAllIndices() throws IOException {
        List<long[]> indexGroups = new ArrayList<>();

        fileStream.seek(0);
        String line;
        while (!(line = readLineS()).isEmpty())
            indexGroups.add(Arrays.stream(line.split(String.valueOf(IDENTIFIER_SEPARATOR))[1].split(String.valueOf(INDEX_SEPARATOR)))
                    .map(s->s.replaceAll("\\s+",""))
                    .mapToLong(Long::parseLong)
                    .toArray());

        return indexGroups.toArray(long[][]::new);
    }

    /**
    * Sets all index values in the file to the given array.
    * @param allIndices a two-dimensional array of index values to set in the file
    * @throws IOException if there is an issue accessing the file
    */
    private void setAllIndices(long[][] allIndices) throws IOException {
        String[] identifiers = readAllIdentifiers();

        StringBuilder sb = new StringBuilder();
        for (int indexGroup = 0; indexGroup < allIndices.length; indexGroup++) {
            sb.append(identifiers[indexGroup]).append(IDENTIFIER_SEPARATOR);
            for (int i = 0; i < allIndices[indexGroup].length; i++) {
                sb.append(allIndices[indexGroup][i]).append(INDEX_SEPARATOR);
            }
            sb.deleteCharAt(sb.length() - 1);
            sb.append("\n");
        }
        sb.append("\n");

        DAOTools.replace(sb.toString().getBytes(charset), 0, dataStartPosition, fileStream, tempStream);
        updateStartPositionIndex();
    }

    /**
    * Opens the file and temporary streams if they are not already open.
    * If the streams are already open, increments a counter instead of reopening the streams.
    * @throws IOException if there is an issue accessing the file
    */
    private void openStreams() throws IOException {
        if (chainedOpens > 0) {
            chainedOpens++;
            return;
        }

        fileStream = new RandomAccessFile(fileFile, "rw");
        tempStream = new RandomAccessFile(tempFile, "rw");
        updateStartPositionIndex();
        fileChannel = fileStream.getChannel();
        tempChannel = tempStream.getChannel();
        chainedOpens++;
    }

    /**
    * Closes the file and temporary streams if the number of chained open calls is 1.
    * If the number of chained open calls is greater than 1, decrements a counter instead of closing the streams.
    * @throws IOException if there is an issue accessing the file
    */
    private void closeStreams() throws IOException {
        if (chainedOpens > 1) {
            chainedOpens--;
            return;
        }

        fileStream.close();
        tempStream.close();
        fileStream = null;
        tempStream = null;
        fileChannel = null;
        tempChannel = null;
        chainedOpens--;
    }

    /**
     * Accessor method for testing purposes.
     * @throws IOException
     */
    long[] _getIndices(String userID) throws IOException {
        openStreams();
        long[] result = getIndices(userID);
        closeStreams();
        return result;
    }

    /**
     * Accessor method for testing purposes.
     * @throws IOException
     */
    long[][] _getAllIndices() throws IOException {
        openStreams();
        long[][] result = getAllIndices();
        closeStreams();
        return result;
    }

    /**
     * Accessor method for testing purposes.
     * @throws IOException
     */
    String[] _readAllIdentifiers() throws IOException {
        openStreams();
        String[] ids = readAllIdentifiers();
        closeStreams();

        // As we are adding the identifiers in a backwards fashion, we need to flip the array before returning it.
        String[] result = new String[ids.length];
        int i = 0;
        int i2 = ids.length - 1;
        while (i < ids.length)
            result[i++] = ids[i2--];

        return result;
    }
}
