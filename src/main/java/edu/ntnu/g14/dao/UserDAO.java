package edu.ntnu.g14.dao;

import edu.ntnu.g14.model.Login;
import edu.ntnu.g14.model.User;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;

/**
 * The class provides methods for accessing and modifying user data stored in users.txt.
 */
public class UserDAO {

  private static final char LOGIN_DATA_SEPARATOR = ',';
  private static final char USER_DATA_SEPARATOR = ';';

  private final IndexedDataFile file;
  private final Charset charset;


  /**
   * Creates a new UserDAO object with the specified file path and default character set.
   *
   * @param filePath the path to the user data file
   * @throws IOException if an I/O error occurs while accessing the file
   */
  public UserDAO(String filePath) throws IOException {
    this(filePath, Charset.defaultCharset());
  }

  /**
   * Creates a new UserDAO object with the specified file path and character set.
   *
   * @param filePath the path to the user data file
   * @param charset  the character set used to read and write data to and from the file
   * @throws IOException if an I/O error occurs while accessing the file
   */
  public UserDAO(String filePath, Charset charset) throws IOException {
    this.file = new IndexedDataFile(filePath, charset);
    this.charset = charset;
  }

  /**
   * Returns an array of Login objects representing all logins stored in the user data file.
   *
   * @return an array of Login objects representing all logins stored in the user data file, or null
   * if an I/O error occurs while reading data from the file
   * @throws IOException if an I/O error occurs while accessing the file
   */
  public Login[] getAllLogins() throws IOException {
    String[] ids = file.readAllIdentifiers();
    if (ids == null) {
      return null;
    }

    byte[][] data = new byte[ids.length][];
    for (int i = 0; i < ids.length; i++) {
      data[i] = file.getData(ids[i], 0);
    }

    Login[] logins = null;

    try {
      logins = Arrays.stream(data)
          .map(bytes -> new String(bytes, charset))
          .map(UserDAO::parseLogin)
          .toArray(Login[]::new);
    } catch (Exception e) {
      // Skip. If something went wrong during parsing, return null.
    }

    return logins;
  }

  /**
   * Returns the User object with the specified user ID, or null if the ID is not found in the
   * file.
   *
   * @param userID the ID of the user to retrieve
   * @return the User object with the specified user ID, or null if the ID is not found in the file
   * @throws IOException if an I/O error occurs while accessing the file
   */
  public User getUser(String userID) throws IOException {
    byte[] data = file.getData(userID, 0);
    if (data == null) {
      return null;
    }

    String dataString = new String(data, charset);
    return parseUser(dataString);
  }

  /**
   * Sets the user data in the data file. If the file already contains data for the given user ID,
   * the existing data is replaced; otherwise, a new entry is added to the data file.
   *
   * @param newUser the User object to be added to the data file
   * @throws IOException if there is an error reading or writing to the data file
   */
  public void setUser(User newUser) throws IOException {
    String userID = newUser.getLoginInfo().getUserId();
    byte[] data = parse(newUser).getBytes(charset);

    if (file.containsIdentifier(userID)) {
      file.setData(userID, 0, data);
    } else {
      file.addNewData(userID, data);
    }
  }

  /**
   * Parses the given User object into a CSV string.
   *
   * @param user the User object to parse
   * @return a CSV string representing the User object
   */
  static String parse(User user) {
    return parse(user.getLoginInfo()) + USER_DATA_SEPARATOR +
        "\"" + user.getEmail() + "\"" + USER_DATA_SEPARATOR +
        "\"" + user.getLastName() + "\"" + USER_DATA_SEPARATOR +
        "\"" + user.getFirstName() + "\"";
  }

  /**
   * Parses the given CSV string into a User object.
   *
   * @param csv the CSV string to parse
   * @return a User object representing the CSV string
   */
  static User parseUser(String csv) {
    String[] fields = csv.split(USER_DATA_SEPARATOR + "(?!\\s)");

    Login login = parseLogin(fields[0]);
    String email = fields[1].substring(1, fields[1].length() - 1);
    String lastName = fields[2].substring(1, fields[2].length() - 1);
    String firstName = fields[3].substring(1, fields[3].length() - 1);

    return new User(null, null, login, email, lastName, firstName, null, null);
  }

  /**
   * Parses the given Login object into a CSV string.
   *
   * @param login the Login object to parse
   * @return a CSV string representing the Login object
   */
  static String parse(Login login) {
    return "\"" + login.getUserId() + "\"" + LOGIN_DATA_SEPARATOR +
        "\"" + login.getUserName() + "\"" + LOGIN_DATA_SEPARATOR +
        "\"" + login.getPassword() + "\"";
  }

  /**
   * Parses the given CSV string into a Login object.
   *
   * @param csv the CSV string to parse
   * @return a Login object representing the CSV string
   */
  static Login parseLogin(String csv) {
    String[] fields = Arrays.stream(csv.split(LOGIN_DATA_SEPARATOR + "(?!\\s)"))
        .map(s -> s.substring(1, s.length() - 1))
        .toArray(String[]::new);

    String id = fields[0];
    String name = fields[1];
    String pass = fields[2];
    return new Login(name, pass, id);
  }

  /**
   * Deletes the user data with the given user ID from the data file.
   *
   * @param userID the ID of the user to be deleted
   * @throws IOException if there is an error reading or writing to the data file
   */
  public void deleteUser(String userID) throws IOException {
    file.deleteIdentifier(userID);
  }
}
