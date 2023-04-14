package edu.ntnu.g14.dao;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;

public class DAOTools {

    public static void insertIntoFile(byte[] data, long offset, RandomAccessFile file, RandomAccessFile tempFile) throws IOException {
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

    public static void replace(byte[] data, long start, long count, RandomAccessFile file, RandomAccessFile tempFile) throws IOException {
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

    public static void appendToFile(byte[] data, RandomAccessFile fileStream) throws IOException {
        fileStream.seek(fileStream.length());
        fileStream.write(data);
    }

    public static void deleteData(long start, long count, RandomAccessFile file, RandomAccessFile tempFile) throws IOException {
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
