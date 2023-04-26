package edu.ntnu.g14.model;

/**
 * The login class represents a user's login information, including the user's username, password,
 * and userID
 */
public class Login {

  private String userName;
  private String password;
  private final String userId;

  /**
   * Creates a new Login object with the specified username, password, and user ID.
   *
   * @param userName the username for the login
   * @param password the password for the login
   * @param userId   the user ID for the login
   */
  public Login(String userName, String password, String userId) {
    this.userName = userName;
    this.password = password;
    this.userId = userId;
  }

  /**
   * Returns the username for this Login object.
   *
   * @return the username for this Login object
   */
  public String getUserName() {
    return userName;
  }

  /**
   * Sets the username for this Login object.
   *
   * @param userName the new username for this Login object
   */
  public void setUserName(String userName) {
    this.userName = userName;
  }

  /**
   * Returns the password for this Login object.
   *
   * @return the password for this Login object
   */
  public String getPassword() {
    return password;
  }

  /**
   * Sets the password for this Login object.
   *
   * @param password the new password for this Login object
   */
  public void setPassword(String password) {
    this.password = password;
  }

  /**
   * Returns the user ID for this Login object.
   *
   * @return the user ID for this Login object
   */
  public String getUserId() {
    return userId;
  }
}
