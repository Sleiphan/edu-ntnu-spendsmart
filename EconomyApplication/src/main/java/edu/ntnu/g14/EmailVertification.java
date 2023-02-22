//String key = EmailVertification.sendVertificationKey("olavsie@hotmail.no");

package edu.ntnu.g14;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.nio.charset.Charset;
import java.util.Properties;
import java.util.Random;

public class EmailVertification {

    public static String sendVertificationKey(String toMail){
        byte[] array = new byte[16];
        new Random().nextBytes(array);
        String key = new String(array, Charset.forName("UTF-8"));

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

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toMail));
            message.setSubject("Vertification key");
            message.setText("Hello,"
                + "\nYour vertification key is: "
                + "\n" + key
                + "\n\n Use this code to register in the economy application");
    
            Transport.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return key;
    }
}