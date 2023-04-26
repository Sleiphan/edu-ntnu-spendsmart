package edu.ntnu.g14.model;

/**
 * A class that represents a person's personal information including login credentials and personal
 * details.
 */
public class Personalia {

  private final Login loginInfo;
  private String email;
  private String lastName;
  private String firstName;

  /**
   * Constructs a new Personalia object with the specified login information and personal details
   *
   * @param loginInfo the login information associated with this person
   * @param email     the email address of this person
   * @param lastName  the last name of this person
   * @param firstName the first name of this person
   */
  public Personalia(Login loginInfo, String email, String lastName, String firstName) {
    this.loginInfo = loginInfo;
    this.email = email;
    this.lastName = lastName;
    this.firstName = firstName;
  }

  /**
   * Returns the login information associated with this person
   *
   * @return the login information associated with this person
   */
  public Login getLoginInfo() {
    return loginInfo;
  }

  /**
   * Returns the email address of this person
   *
   * @return the email address of this person
   */
  public String getEmail() {
    return email;
  }

  /**
   * Returns a blurred version of the email address, where only the first and last characters of the
   * handle are visible and the rest of the characters are replaced with asterisks. If the email
   * address is null or less than 3 characters long, it will return the original email address.
   *
   * @return a blurred version of the email address
   */
  public String getBlurredEmail() {
    if (this.email == null || this.email.length() < 3) {
      return email;
    }
    String handle = email.substring(0, email.lastIndexOf("@"));
    char[] handleArray = handle.toCharArray();
    for (int i = 1; i < handle.length() - 1; i++) {
      handleArray[i] = '*';
    }
    return new String(handleArray) + email.substring(email.lastIndexOf("@"));
  }

  /**
   * Sets the email address of this person
   *
   * @param email the new email address to set
   */
  public void setEmail(String email) {
    this.email = email;
  }

  /**
   * Returns the last name of this person
   *
   * @return the last name of this person
   */
  public String getLastName() {
    return lastName;
  }

  /**
   * Sets the last name of this person
   *
   * @param lastName the new last name to set
   */
  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  /**
   * Returns the first name of this person
   *
   * @return the first name of this person
   */
  public String getFirstName() {
    return firstName;
  }

  /**
   * Sets the first name of this person
   *
   * @param firstName the new first name to set
   */
  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  /**
   * Returns the full name of this person
   *
   * @return the full name of this person
   */
  public String getFullName() {
    return getFirstName() + " " + getLastName();
  }
}
