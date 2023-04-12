package edu.ntnu.g14.dao;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;

public class DAOTools {

    public static void insertIntoFile(byte[] data, int offset, RandomAccessFile file, RandomAccessFile tempFile) throws IOException {
        FileChannel fileChannel = file.getChannel();
        FileChannel tempChannel = tempFile.getChannel();

        fileChannel.transferTo(offset, fileChannel.size() - offset, tempChannel);
        file.seek(offset);
        file.write(data);
        tempChannel.position(0);
        fileChannel.transferFrom(tempChannel, fileChannel.size(), tempChannel.size());

        fileChannel.close();
        tempChannel.close();
    }

    public static void replace(byte[] data, long start, long count, RandomAccessFile file, RandomAccessFile tempFile) throws IOException {
        FileChannel fileChannel = file.getChannel();
        FileChannel tempChannel = tempFile.getChannel();

        long transferCount = fileChannel.size() - (start + count);

        fileChannel.transferTo(start + count, transferCount, tempChannel);
        tempChannel.truncate(transferCount);
        file.seek(start);
        file.write(data);
        fileChannel.truncate(start + data.length);
        tempChannel.position(0);
        fileChannel.transferFrom(tempChannel, fileChannel.size(), tempChannel.size());

        fileChannel.close();
        tempChannel.close();
    }

    public static void appendToFile(byte[] data, RandomAccessFile fileStream) throws IOException {
        fileStream.seek(fileStream.length());
        fileStream.write(data);
    }
}
