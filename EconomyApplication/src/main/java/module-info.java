module edu.ntnu.g14 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires commons.validator;
    requires org.json;
    requires java.mail;


    opens edu.ntnu.g14 to javafx.fxml;
    exports edu.ntnu.g14;
}