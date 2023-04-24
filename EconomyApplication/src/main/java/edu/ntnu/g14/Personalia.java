package edu.ntnu.g14;
public class Personalia {
    private final Login loginInfo;
    private String email;
    private String lastName;
    private String firstName;

    public Personalia(Login loginInfo, String email, String lastName, String firstName) {
        this.loginInfo = loginInfo;
        this.email = email;
        this.lastName = lastName;
        this.firstName = firstName;
    }

    public Login getLoginInfo() {
        return loginInfo;
    }

    public String getEmail() {
        return email;
    }

    public String getBlurredEmail() {
        if (this.email == null || this.email.length() < 3) {
            return email;
        }
        String handle = email.substring(0, email.lastIndexOf("@"));
        char[] handleArray = handle.toCharArray();
        for (int i = 1; i<handle.length() - 1; i++) {
            handleArray[i] = '*';
        }
        return new String(handleArray) + email.substring(email.lastIndexOf("@"));
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getFullName(){
        return getFirstName() + " " + getLastName();
    }
}
