module EconomyApplication {
    requires transitive javafx.controls;
    requires transitive javafx.graphics;
    requires java.sql;
    requires commons.validator;
    requires org.json;
    requires java.mail;
    exports edu.ntnu.g14;
    opens edu.ntnu.g14;
}