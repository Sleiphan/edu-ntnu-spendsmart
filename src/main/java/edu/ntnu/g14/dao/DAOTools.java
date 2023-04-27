package edu.ntnu.g14.dao;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;

/**
 * The class provides methods for manipulating files using random access
 */
public class DAOTools {

  /**
   * Inserts the given byte array into the specified file at the specified offset. Any data after
   * the offset is shifted to the right to accommodate the new data.
   *
   * @param data     the byte array to be inserted
   * @param offset   the offset at which to insert the data
   * @param file     the RandomAccessFile object representing the file to be modified
   * @param tempFile the RandomAccessFile object representing a temporary file used for the
   *                 insertion
   * @throws IOException if an I/O error occurs
   */
  public static void insertIntoFile(byte[] data, long offset, RandomAccessFile file,
      RandomAccessFile tempFile) throws IOException {
    FileChannel fileChannel = file.getChannel();
    FileChannel tempChannel = tempFile.getChannel();

    long transferCount = fileChannel.size() - offset;

    tempChannel.position(0);
    fileChannel.transferTo(offset, transferCount, tempChannel);
    file.seek(offset);
    file.write(data);
    tempChannel.position(0);
    fileChannel.transferFrom(tempChannel, offset + data.length, transferCount);
  }

  /**
   * Replaces a range of bytes in the specified file with the given byte array.
   *
   * @param data     the byte array to replace the existing data with
   * @param start    the starting offset of the data to be replaced
   * @param count    the number of bytes to be replaced
   * @param file     the RandomAccessFile object representing the file to be modified
   * @param tempFile the RandomAccessFile object representing a temporary file used for the
   *                 replacement
   * @throws IOException if an I/O error occurs
   */
  public static void replace(byte[] data, long start, long count, RandomAccessFile file,
      RandomAccessFile tempFile) throws IOException {
    FileChannel fileChannel = file.getChannel();
    FileChannel tempChannel = tempFile.getChannel();

    long transferCount = fileChannel.size() - (start + count);

    tempChannel.position(0);
    fileChannel.transferTo(start + count, transferCount, tempChannel);
    file.seek(start);
    file.write(data);
    tempChannel.position(0);
    fileChannel.transferFrom(tempChannel, start + data.length, transferCount);
    fileChannel.truncate(start + data.length + transferCount);
  }

  /**
   * Appends the given byte array to the end of the specified file.
   *
   * @param data       the byte array to be appended
   * @param fileStream the RandomAccessFile object representing the file to be appended to
   * @throws IOException if an I/O error occurs
   */
  public static void appendToFile(byte[] data, RandomAccessFile fileStream) throws IOException {
    fileStream.seek(fileStream.length());
    fileStream.write(data);
  }

  /**
   * Deletes a range of bytes from the specified file.
   *
   * @param start    the starting offset of the data to be deleted
   * @param count    the number of bytes to be deleted
   * @param file     the RandomAccessFile object representing the file to be modified
   * @param tempFile the RandomAccessFile object representing a temporary file used for the
   *                 deletion
   * @throws IOException if an I/O error occurs
   */
  public static void deleteData(long start, long count, RandomAccessFile file,
      RandomAccessFile tempFile) throws IOException {
    FileChannel fileChannel = file.getChannel();
    FileChannel tempChannel = tempFile.getChannel();

    long transferCount = fileChannel.size() - (start + count);

    tempChannel.position(0);
    fileChannel.transferTo(start + count, transferCount, tempChannel);
    file.seek(start);
    tempChannel.position(0);
    fileChannel.transferFrom(tempChannel, start, transferCount);
    fileChannel.truncate(start + transferCount);
  }
}
