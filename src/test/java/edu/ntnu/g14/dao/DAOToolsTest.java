package edu.ntnu.g14.dao;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DAOToolsTest {

  private final String filePath = "DAOToolsTest.txt";
  private final String tempPath = filePath + ".temp";
  private static final Charset charset = StandardCharsets.UTF_8;
  RandomAccessFile testFile;
  RandomAccessFile tempFile;

  private String initialTestData = "0123456789";

  @BeforeAll
  public void beforeAll() {
    Path file_p = Paths.get(filePath);

    if (Files.notExists(file_p)) {
      try {
        Files.createFile(file_p);
      } catch (FileAlreadyExistsException e) {
        // Do nothing
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }

    try {
      testFile = new RandomAccessFile(filePath, "rw");
      tempFile = new RandomAccessFile(tempPath, "rw");
    } catch (FileNotFoundException e) {
      throw new RuntimeException(e);
    }


  }

  @BeforeEach
  public void resetFileContent() {
    try {
      testFile.seek(0);
      testFile.write(initialTestData.getBytes());
      testFile.getChannel().truncate(initialTestData.length());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public void insert_into_file_test_common(int position) {
    String insertionString = "Hello World!";
    String expectedResult =
        initialTestData.substring(0, position) + insertionString + initialTestData.substring(
            position);

    String result = null;
    try {
      DAOTools.insertIntoFile(insertionString.getBytes(charset), position, testFile, tempFile);
      testFile.seek(0);
      result = testFile.readLine();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    assert result != null;
    assert result.equals(expectedResult);
  }

  @Test
  public void insert_into_file() {
    insert_into_file_test_common(3);
  }

  @Test
  public void insert_into_start_of_file() {
    insert_into_file_test_common(0);
  }

  @Test
  public void insert_into_end_of_file() {
    insert_into_file_test_common(initialTestData.length());
  }

  public void replace_common(int start, int count) {
    String insertionString = "Hello World!";
    String expectedResult =
        initialTestData.substring(0, start) + insertionString + initialTestData.substring(
            start + count);

    String result = null;
    try {
      DAOTools.replace(insertionString.getBytes(charset), start, count, testFile, tempFile);
      testFile.seek(0);
      result = testFile.readLine();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    assert result != null;
    assert result.equals(expectedResult);
  }

  @Test
  public void replace() {
    replace_common(2, 5);
  }

  @Test
  public void replace_zero_length() {
    replace_common(4, 0);
  }

  @Test
  public void replace_all() {
    replace_common(0, initialTestData.length());
  }

  @Test
  public void replace_start_of_file() {
    replace_common(0, 2);
  }

  @Test
  public void replace_end_of_file() {
    replace_common(initialTestData.length(), 0);
  }

  @Test
  public void append_to_file() {
    String insertionString = "Hello World!";
    String expectedResult = initialTestData + insertionString;

    String result = null;
    try {
      DAOTools.appendToFile(insertionString.getBytes(charset), testFile);
      testFile.seek(0);
      result = testFile.readLine();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    assert result != null;
    assert result.equals(expectedResult);
  }

  @Test
  public void delete_data() {
    int start = 4;
    int count = 2;
    String expectedResult =
        initialTestData.substring(0, start) + initialTestData.substring(start + count);

    String result = null;
    try {
      DAOTools.deleteData(start, count, testFile, tempFile);
      testFile.seek(0);
      result = testFile.readLine();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    assert result != null;
    assert result.equals(expectedResult);
  }

  @AfterAll
  public void close() {
    try {
      testFile.close();
      tempFile.close();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    try {
      Files.delete(Paths.get(filePath));
      Files.delete(Paths.get(tempPath));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }


}
