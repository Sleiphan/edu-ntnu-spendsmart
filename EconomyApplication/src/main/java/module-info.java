module edu.ntnu.g14 {
  requires transitive javafx.controls;
  requires transitive javafx.graphics;
  requires transitive javafx.media;
  requires java.sql;
  requires commons.validator;
  requires org.json;
  requires java.mail;

  exports edu.ntnu.g14;
  opens edu.ntnu.g14 to javafx.graphics;
  exports edu.ntnu.g14.frontend;
  exports edu.ntnu.g14.model;
  opens edu.ntnu.g14.model to javafx.graphics;
}