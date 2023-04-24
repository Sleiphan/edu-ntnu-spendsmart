package edu.ntnu.g14.dao;

import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class IndexedDataFile {
    /**
     * This class uses an internal charset to read and write the index-section of the file.
     */
    private static final Charset CHARSET = Charset.defaultCharset();

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

    public IndexedDataFile(String filePath) throws IOException {
        fileFile = new File(filePath);
        tempFile = new File(filePath + ".temp");
        tempFile.createNewFile();
        tempFile.deleteOnExit();

        openStreams();
        updateStartPositionIndex();
        closeStreams();
    }

    public byte[][] readAllInIdentifier(String identifier) throws IOException {
        openStreams();

        // Read all indices connected to the specified identifier.
        long[] indices = getIndices(identifier);
        if (indices == null) {
            closeStreams();
            return null;
        }

        // Additionally add the variable dataStartPosition to each element.
        // This is necessary since all the indices describe the data-positions relative to this variable.
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

    public boolean deleteData(String identifier, int index) throws IOException {
        openStreams();
        long dataPos = getDataPosition(identifier, index);
        if (dataPos == -1) {
            closeStreams();
            return false;
        }

        fileStream.seek(dataPos); // Seek to the start of the data
        long dataEnd = skipLine(); // Find the end of the data
        if (dataEnd != fileStream.length())
            dataEnd--; // Make sure we do not remove the appended DATA_SEPARATOR


        dataPos--; // We delete the prepending DATA_SEPARATOR
        long dataLength = dataEnd - dataPos;

        DAOTools.deleteData(dataPos, dataLength, fileStream, tempStream);
        deleteIndex(identifier, index);
        updateIndices(-dataLength, dataPos);
        closeStreams();
        return true;
    }

    public void deleteIdentifier(String identifier) throws IOException {
        int index = getNumEntries(identifier) - 1;
        if (index < 0)
            return;

        openStreams();

        for (; index >= 0; index--)
            deleteData(identifier, index);

        closeStreams();
    }

    public void addNewData(String identifier, byte[] data) throws IOException {
        if (identifier.contains(String.valueOf(IDENTIFIER_SEPARATOR)))
            throw new IllegalArgumentException("Identifier cannot contain " + IDENTIFIER_SEPARATOR + ". The submitted invalid identifier was '" + identifier + "'");
        if (identifier.contains(String.valueOf(DATA_SEPARATOR)))
            throw new IllegalArgumentException("Identifier cannot contain " + DATA_SEPARATOR + ". The submitted invalid identifier was '" + identifier + "'");

        data = convertToStorageFormat(data);

        openStreams();

        DAOTools.appendToFile(data, fileStream);
        DAOTools.appendToFile("\n".getBytes(CHARSET), fileStream);

        long dataIndex = fileStream.length() - data.length - 1;
        if (dataStartPosition == -1)
            DAOTools.insertIntoFile("\n".getBytes(CHARSET), 0, fileStream, tempStream);
        else
            dataIndex -= dataStartPosition;

        appendIndex(identifier, dataIndex);
        closeStreams();
    }

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

    // ##### HELPER METHODS #####
    // From this point on, we define helper methods.

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

    private boolean deleteIndex(String identifier, int index) throws IOException {
        long dataPos = getIndexDataPosition(identifier, index);
        if (dataPos < 0)
            return false;

        long dataEnd = skipToNextIndex();
        if (dataEnd == -1)
            return false;
        if (dataEnd == -2) {
            // We reached a DATA_SEPARATOR while searching for the next INDEX_SEPARATOR
            dataEnd = fileStream.getFilePointer();

            // Is this the last index of this identifier entry?
            if (index == 0) {
                // Remove the entire entry
                fileStream.seek(fileStream.getFilePointer() - 2);
                byte b;
                while ((b = fileStream.readByte()) != DATA_SEPARATOR) { // Move backwards until we find the beginning of the entry.
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
     *
     * @return The position in the file of the next index. -1 if end of file is reached, and -2 if end of line is reached.
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
     *
     * @param identifier
     * @param dataIndex The position in the file where the new data was written, relative to the variable dataStartPosition.
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

        DAOTools.insertIntoFile(data.getBytes(CHARSET), start, fileStream, tempStream);
        updateStartPositionIndex();
    }

    /**
     * Creates a new identifier entry able to hold indices in the index-section of the file.
     * @param identifier
     * @return The index of the new identifier's index-area.
     * @throws IOException
     */
    private long createNewIdentifierEntry(String identifier) throws IOException {
        String text = identifier + IDENTIFIER_SEPARATOR;
        byte[] data = (text + '\n').getBytes(CHARSET);

        long offset = 0; // Add the new identifier entry to the beginning of the file.
        DAOTools.insertIntoFile(data, offset, fileStream, tempStream);
        updateStartPositionIndex();
        return offset + text.length() - 1;
    }

    private long getDataPosition(String identifier, int index) throws IOException {
        long[] indices = getIndices(identifier);
        if (indices == null || index >= indices.length)
            return -1;
        return indices[index] + dataStartPosition;
    }

    /**
     * Reads and returns all characters from the current position of the RandomAccessFile to the submitted marker.
     * If a line delimiter is found, or end of file is reached, returns null;
     * @throws IOException
     */
    private static String readUntil_breakNewLine(RandomAccessFile stream) throws IOException {
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

        return out.toString(CHARSET);
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


    private String readLineS() throws IOException {
        return new String(readLine(), CHARSET);
    }

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
     * Call this method when changing the data-part of the file, e.g. adding a new entry, editing data, and removing an entry.
     * @param dataAmountChange
     * @param positionOfChange
     * @throws IOException
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

    private void updateStartPositionIndex() throws IOException {
        dataStartPosition = readStartPositionOfData();
    }

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
     * @return
     * @throws IOException
     */
    private String readAllIndexData() throws IOException {
        int dataStart = (int) dataStartPosition;
        byte[] data = new byte[dataStart];
        fileStream.read(data, 0, dataStart);
        return new String(data, CHARSET);
    }

    /**
     * Reads all present identifiers from the file. If the file does not contain any identifiers, returns an empty array.
     * @throws IOException
     */
    public String[] readAllIdentifiers() throws IOException {
        List<String> identifiers = new ArrayList<>();

        openStreams();

        fileStream.seek(0); // Read from the beginning of the file.
        String currID;
        while ((currID = readUntil_breakNewLine(fileStream)) != null) {
            identifiers.add(currID);
            skipLine();
        }

        closeStreams();

        return identifiers.toArray(String[]::new);
    }

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

        DAOTools.replace(sb.toString().getBytes(CHARSET), 0, dataStartPosition, fileStream, tempStream);
        updateStartPositionIndex();
    }

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


    // ##### ACCESSOR-METHODS FOR TESTING-PURPOSES #####

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
