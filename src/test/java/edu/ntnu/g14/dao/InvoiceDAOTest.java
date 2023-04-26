package edu.ntnu.g14.dao;


import org.junit.jupiter.api.*;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Paths;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class InvoiceDAOTest {
    private static final String TEST_FILE_PATH = "invoicesTestFile.txt";
    private static final String TEMP_FILE_PATH = TEST_FILE_PATH + ".temp";
    private static final String user1ID = "User 1";
    private static final String user2ID = "User 2";

    @BeforeEach
    public void resetFileContent() {
        try {
            RandomAccessFile testFile = new RandomAccessFile(TEST_FILE_PATH, "rw");
            testFile.getChannel().truncate(0);
            testFile.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @AfterAll
    public void cleanup() {
        try {
            Files.delete(Paths.get(TEST_FILE_PATH));
            Files.delete(Paths.get(TEMP_FILE_PATH));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}