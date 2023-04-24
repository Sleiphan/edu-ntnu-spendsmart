package edu.ntnu.g14.dao;

import edu.ntnu.g14.Login;
import edu.ntnu.g14.User;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;

public class UserDAO {

    private static final String LOGINS_IDENTIFIER = "ALL_LOGINS";
    private static final char LOGIN_DATA_SEPARATOR = ',';
    private static final char USER_DATA_SEPARATOR = ';';

    private final IndexedDataFile file;
    private final Charset charset;



    public UserDAO(String filePath) throws IOException {
        this(filePath, Charset.defaultCharset());
    }

    public UserDAO(String filePath, Charset charset) throws IOException {
        this.file = new IndexedDataFile(filePath);
        this.charset = charset;
    }

    public Login[] getAllLogins() throws IOException {
        String[] ids = file.readAllIdentifiers();
        if (ids == null)
            return null;

        byte[][] data = new byte[ids.length][];
        for (int i = 0; i < ids.length; i++)
            data[i] = file.getData(ids[i], 0);

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

    public User getUser(String userID) throws IOException {
        byte[] data = file.getData(userID, 0);
        if (data == null)
            return null;

        String dataString = new String(data, charset);
        return parseUser(dataString);
    }

    public void setUser(User newUser) throws IOException {
        String userID = newUser.getLoginInfo().getUserId();
        byte[] data = parse(newUser).getBytes(charset);

        if (file.containsIdentifier(userID))
            file.setData(userID, 0, data);
        else
            file.addNewData(userID, data);
    }



    static String parse(User user) {
        return         parse(user.getLoginInfo())        + USER_DATA_SEPARATOR +
                "\"" + user.getEmail()            + "\"" + USER_DATA_SEPARATOR +
                "\"" + user.getLastName()         + "\"" + USER_DATA_SEPARATOR +
                "\"" + user.getFirstName()        + "\"";
    }

    static User parseUser(String csv) {
        String[] fields = csv.split(USER_DATA_SEPARATOR + "(?!\\s)");

        Login login      = parseLogin(fields[0]);
        String email     = fields[1].substring(1, fields[1].length() - 1);
        String lastName  = fields[2].substring(1, fields[2].length() - 1);
        String firstName = fields[3].substring(1, fields[3].length() - 1);

        return new User(null, null, login, email, lastName, firstName, null, null);
    }

    static String parse(Login login) {
        return  "\"" + login.getUserId()   + "\"" + LOGIN_DATA_SEPARATOR +
                "\"" + login.getUserName() + "\"" + LOGIN_DATA_SEPARATOR +
                "\"" + login.getPassword() + "\"";
    }

    static Login parseLogin(String csv) {
        String[] fields = Arrays.stream(csv.split(LOGIN_DATA_SEPARATOR + "(?!\\s)"))
                .map(s -> s.substring(1, s.length() - 1))
                .toArray(String[]::new);

        String id   = fields[0];
        String name = fields[1];
        String pass = fields[2];
        return new Login(name, pass, id);
    }
}
