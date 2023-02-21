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
