package edu.ntnu.g14.dao;


import org.junit.jupiter.api.*;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class IndexedDataFileTest {

  private static final Charset CHARSET = Charset.defaultCharset();
  private static final String TEST_FILE_PATH = "IndexedDataFileTest.txt";
  private static final String TEMP_FILE_PATH = TEST_FILE_PATH + ".temp";
  private static final String user1ID = "User 1";
  private static final String user2ID = "User 2";


  private IndexedDataFile dao;

  @BeforeEach
  public void reset() {
    try {
      RandomAccessFile file = new RandomAccessFile(TEST_FILE_PATH, "rw");
      file.getChannel().truncate(0);
      file.close();
      dao = new IndexedDataFile(TEST_FILE_PATH);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Test
  public void empty_then_fill_up_again() {
    String expected =
        "User 2:12\n" +
            "User 1:0\n" +
            "\n" +
            "Test data 1\n" +
            "Test data 2\n";

    try {
      dao.addNewData(user1ID, "Test data 1".getBytes(CHARSET));
      dao.addNewData(user2ID, "Test data 2".getBytes(CHARSET));
      dao.deleteData(user1ID, 0);
      dao.deleteData(user2ID, 0);
      dao.addNewData(user1ID, "Test data 1".getBytes(CHARSET));
      dao.addNewData(user2ID, "Test data 2".getBytes(CHARSET));

      String wholeFile = Files.readString(Path.of(TEST_FILE_PATH));
      assert expected.equals(wholeFile);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

  }

  @Test
  public void storing_data_containing_the_data_separator() {
    String testString1 = "This data contains the \n-character which is a data separator";
    String testString2 = "This also contains the \n data separator";
    try {
      dao.addNewData(user1ID, testString1.getBytes(CHARSET));
      assert testString1.equals(new String(dao.getData(user1ID, 0), CHARSET));

      dao.setData(user1ID, 0, testString2.getBytes(CHARSET));
      assert testString2.equals(new String(dao.getData(user1ID, 0), CHARSET));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

  }

  @Test
  public void setData() {
    try {
      dao.addNewData(user1ID, "Test data 1".getBytes(CHARSET));
      dao.addNewData(user1ID, "Test data 2".getBytes(CHARSET));
      dao.addNewData(user2ID, "Test data 3".getBytes(CHARSET));
      dao.addNewData(user2ID, "Test data 4".getBytes(CHARSET));
      dao.setData(user1ID, 0, "New data which is longer than source".getBytes(CHARSET));
      dao.setData(user2ID, 1, "Shorter".getBytes(CHARSET));
      dao.setData(user2ID, 0, "Changed some data in the middle".getBytes(CHARSET));

      String s = new String(dao.getData(user1ID, 0), CHARSET);
      assert s.equals("New data which is longer than source");

      s = new String(dao.getData(user2ID, 1), CHARSET);
      assert s.equals("Shorter");

      s = new String(dao.getData(user2ID, 0), CHARSET);
      assert s.equals("Changed some data in the middle");

    } catch (IOException e) {
      throw new RuntimeException(e);
    }

  }

  @Test
  public void delete() {
    try {
      dao.addNewData(user1ID, "Test data 1".getBytes(CHARSET));
      dao.addNewData(user1ID, "Test data 2".getBytes(CHARSET));
      dao.addNewData(user2ID, "Test data 3".getBytes(CHARSET));
      dao.addNewData(user2ID, "Test data 4".getBytes(CHARSET));

      dao.deleteData(user2ID, 0);
      byte[] data = dao.getData(user2ID, 1);
      assert data == null;

      data = dao.getData(user2ID, 0);
      assert data != null;
      String s = new String(data, CHARSET);
      assert s.equals("Test data 4");

      assert dao.deleteData(user2ID, 0);
      assert !dao.deleteData(user2ID, 0);
      assert dao.deleteData(user1ID, 0);
      data = dao.getData(user1ID, 0);
      assert data != null;
      s = new String(data, CHARSET);
      assert s.equals("Test data 2");
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Order(0)
  @Test
  public void write_and_read() {
    final String datapoint11 = "Some data";
    final String datapoint12 = "Some more data";
    final String datapoint21 = "Data for user2";
    final String datapoint13 = "Again for user1";
    final String datapoint22 = "Holy damn, user2 once again!";

    final String u1 = "User1";
    final String u2 = "User2";

    String read_datapoint11;
    String read_datapoint12;
    String read_datapoint21;
    String read_datapoint13;
    String read_datapoint22;

    try {
      dao.addNewData(u1, datapoint11.getBytes(CHARSET));
      dao.addNewData(u1, datapoint12.getBytes(CHARSET));
      dao.addNewData(u2, datapoint21.getBytes(CHARSET));
      dao.addNewData(u1, datapoint13.getBytes(CHARSET));
      dao.addNewData(u2, datapoint22.getBytes(CHARSET));

      read_datapoint11 = new String(dao.getData(u1, 0), CHARSET);
      read_datapoint12 = new String(dao.getData(u1, 1), CHARSET);
      read_datapoint21 = new String(dao.getData(u2, 0), CHARSET);
      read_datapoint13 = new String(dao.getData(u1, 2), CHARSET);
      read_datapoint22 = new String(dao.getData(u2, 1), CHARSET);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    assert datapoint11.equals(read_datapoint11);
    assert datapoint12.equals(read_datapoint12);
    assert datapoint21.equals(read_datapoint21);
    assert datapoint13.equals(read_datapoint13);
    assert datapoint22.equals(read_datapoint22);
  }

  @Test
  public void indices_write_and_read() {
    long[] readIndices = null;

    try {
      readIndices = dao._getIndices(user1ID + "not");
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    assert readIndices == null;
  }

  @Test
  public void get_all_indices() {
    long[][] allIndices = null;

    try {
      dao.addNewData(user1ID, "Test data 1".getBytes(CHARSET));
      dao.addNewData(user1ID, "Test data 2".getBytes(CHARSET));
      dao.addNewData(user1ID, "Test data 3".getBytes(CHARSET));
      dao.addNewData(user2ID, "Test data 4".getBytes(CHARSET));
      dao.addNewData(user2ID, "Test data 5".getBytes(CHARSET));
      dao.addNewData(user2ID, "Test data 6".getBytes(CHARSET));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    try {
      allIndices = dao._getAllIndices();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    assert allIndices.length == 2;
    assert allIndices[0].length == 3;
    assert allIndices[1].length == 3;
  }

  @Test
  public void read_all_identifiers() {
    String[] ids;
    try {
      dao.addNewData(user1ID, "Test data 1".getBytes(CHARSET));
      dao.addNewData(user2ID, "Test data 2".getBytes(CHARSET));
      ids = dao._readAllIdentifiers();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    assert ids[0].equals(user1ID);
    assert ids[1].equals(user2ID);
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