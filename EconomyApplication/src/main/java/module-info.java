module edu.ntnu.g14 {
    requires transitive javafx.controls;
    requires transitive javafx.fxml;
    requires transitive javafx.graphics;
    requires java.sql;
    requires commons.validator;
    requires org.json;
    requires java.mail;

    opens edu.ntnu.g14.frontend to javafx.fxml;
    exports edu.ntnu.g14.frontend;
}