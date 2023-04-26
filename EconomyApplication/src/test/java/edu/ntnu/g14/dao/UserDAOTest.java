package edu.ntnu.g14.dao;

import edu.ntnu.g14.Login;
import edu.ntnu.g14.User;
import org.junit.jupiter.api.Test;

public class UserDAOTest {

  @Test
  public void parseLogin() {
    String name = "Username";
    String pass = "Password123";
    String id = "#grg4";
    Login l = new Login(name, pass, id);

    Login copy = UserDAO.parseLogin(UserDAO.parse(l));

    assert l.getUserName().equals(copy.getUserName());
    assert l.getPassword().equals(copy.getPassword());
    assert l.getUserId().equals(copy.getUserId());
  }

  @Test
  public void parseUser() {
    String email = "some@mail.biz";
    String lastName = "Lastname";
    String firstName = "Firstname";
    String name = "Username";
    String pass = "Password123";
    String id = "#grg4";
    User u = new User(null, null, new Login(name, pass, id), email, lastName, firstName, null,
        null);

    String text = UserDAO.parse(u);
    User copy = UserDAO.parseUser(text);

    assert u.getEmail().equals(copy.getEmail());
    assert u.getLastName().equals(copy.getLastName());
    assert u.getFirstName().equals(copy.getFirstName());
    assert u.getLoginInfo().getUserName().equals(copy.getLoginInfo().getUserName());
    assert u.getLoginInfo().getPassword().equals(copy.getLoginInfo().getPassword());
    assert u.getLoginInfo().getUserId().equals(copy.getLoginInfo().getUserId());
  }
}
