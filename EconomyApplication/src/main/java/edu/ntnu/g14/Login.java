package edu.ntnu.g14;
public class Login {
    private String userName;
    private String password;
    private final String userId;

    public Login (String userName, String password, String userId){
        this.userName=userName;
        this.password=password;
        this.userId=userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserId(){
        return userId;
    }
}
