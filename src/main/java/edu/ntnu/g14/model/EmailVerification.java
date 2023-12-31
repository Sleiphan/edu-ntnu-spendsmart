package edu.ntnu.g14.model;

import java.util.Properties;
import java.util.Random;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * The EmailVertification class provides functionality to generate a vertification key and send it
 * to a specified email adress using Gmail's SMTP server.
 */
public class EmailVerification {

  /**
   * Generates a verification key and sends it to the provided email address using the Gmail SMTP
   * server.
   *
   * @param toMail The email address to send the verification key to.
   * @return The generated verification key.
   */
  public static String sendVerificationKey(String toMail) {
    int leftLimit = 97;
    int rightLimit = 122;
    int length = 16;
    Random random = new Random();

    String key = random.ints(leftLimit, rightLimit + 1)
        .limit(length)
        .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
        .toString();

    final String username = "economy.application@gmail.com";
    final String password = "xntgxpogksdgdjuc";

    Properties prop = new Properties();
    prop.put("mail.smtp.host", "smtp.gmail.com");
    prop.put("mail.smtp.port", "587");
    prop.put("mail.smtp.auth", "true");
    prop.put("mail.smtp.starttls.enable", "true");

    Session session = Session.getInstance(prop,
        new javax.mail.Authenticator() {
          protected PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(username, password);
          }
        });

    StringBuilder displayString = new StringBuilder();
    displayString.append(key.substring(0, 4) + " ")
        .append(key.substring(4, 8) + " ")
        .append(key.substring(8, 12) + " ")
        .append(key.substring(12, 16));

    try {
      Message message = new MimeMessage(session);
      message.setFrom(new InternetAddress(username));
      message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toMail));
      message.setSubject("Vertification key");
      message.setText("Hello,"
          + "\nYour vertification key is: "
          + "\n" + displayString
          + "\n\n Use this code to register in the economy application");

      Transport.send(message);
    } catch (MessagingException e) {
      e.printStackTrace();
    }
    return key;
  }
}