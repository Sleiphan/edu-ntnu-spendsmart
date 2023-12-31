package edu.ntnu.g14.frontend;

import edu.ntnu.g14.BankApplication;
import edu.ntnu.g14.dao.InvoiceDAO;
import edu.ntnu.g14.model.FileManagement;
import edu.ntnu.g14.model.Invoice;
import edu.ntnu.g14.model.User;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.time.LocalDate;
import java.time.ZoneId;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.apache.commons.validator.routines.BigDecimalValidator;

public class InvoiceScene {

  private static final String INVOICE_PATH = FileManagement.PATH_INVOICES;
  static Stage stage = BankApplication.getStage();
  private static InvoiceDAO invoiceFile;

  static public Scene scene() throws IOException {

    final User user = BankApplication.loggedInUser;
    final String userID = user.getLoginInfo().getUserId();
    invoiceFile = new InvoiceDAO(INVOICE_PATH);

    Text amount_t = ApplicationObjects.newText("Amount (kr):", 17, false, 11, 345 - 186);
    Text accountNum_t = ApplicationObjects.newText("Account number:", 17, false, 11, 376 - 186);
    Text cidComment_t = ApplicationObjects.newText("CID / Comment:", 17, false, 11, 407 - 186);
    Text dueDate_t = ApplicationObjects.newText("Due date:", 17, false, 11, 438 - 186);
    TextField amount_tf = ApplicationObjects.newTextField("Amount (kr)", 198, 322 - 186, 206, 20,
        14);
    TextField accountNum_tf = ApplicationObjects.newTextField("Account number", 198, 353 - 186, 206,
        20, 14);
    TextField cidComment_tf = ApplicationObjects.newTextField("CID / Comment", 198, 384 - 186, 206,
        20, 14);

    DatePicker due_dp = new DatePicker();
    due_dp.setLayoutX(527 - 329);
    due_dp.setLayoutY(422 - 186);
    restrictDatePicker(due_dp, LocalDate.now());

    ListView<Invoice> invoices_lv = new ListView<>();
    invoices_lv.setLayoutX(782 - 329);
    invoices_lv.setLayoutY(326 - 186);
    invoices_lv.getItems().setAll(user.getAllInvoices());
    invoices_lv.setEditable(false);

    Button clear_bt = ApplicationObjects.newButton("Clear", 358 - 329, 570 - 186, 159, 61, 16);
    Button register_bt = ApplicationObjects.newButton("Register", 549 - 329, 570 - 186, 159, 61,
        16);
    Button back_bt = ApplicationObjects.newButton("Back", 358 - 329, 637 - 186, 159, 35, 16);
    Button payNow_bt = ApplicationObjects.newButton("Pay now", 817 - 329, 493 - 186, 159, 61, 16);
    Button delete_bt = ApplicationObjects.newButton("Delete", 817 - 329, 570 - 186, 159, 61, 16);

    clear_bt.setOnAction(e -> {
      amount_tf.clear();
      accountNum_tf.clear();
      cidComment_tf.clear();
      due_dp.setValue(null);
    });
    register_bt.setOnAction(e -> {
      if (amount_tf.getText().isBlank()) {
        ApplicationObjects.alertBox("Missing input", "Missing information",
            "Please enter the financial amount in the new invoice first.");
        return;
      }

      if (accountNum_tf.getText().isBlank()) {
        ApplicationObjects.alertBox("Missing input", "Missing information",
            "Please enter the account number this invoice .");
        return;
      }

      BigDecimal amount = BigDecimalValidator.getInstance().validate(amount_tf.getText());
      if (amount == null) {
        ApplicationObjects.alertBox("Invalid input", "Invalid amount",
            "Could not understand the amount specified. Make sure to only enter numbers and use the period-sign instead of comma.");
        return;
      }

      // TODO: Check validity of account number.

      LocalDate due = due_dp.getValue();
      Invoice newInvoice = new Invoice(
          due.atStartOfDay(ZoneId.systemDefault()).toLocalDate(),
          amount,
          accountNum_tf.getText(),
          cidComment_tf.getText());
      user.getAllInvoices().add(newInvoice);

      if (!invoiceFile.addNewInvoice(user.getLoginInfo().getUserId(), newInvoice)) {
        ApplicationObjects.alertBox("Internal error", "Failed to register invoice",
            "The save file could be corrupted.");
      }

      clear_bt.getOnAction().handle(null);
      invoices_lv.getItems().setAll(user.getAllInvoices());
    });
    back_bt.setOnAction(e -> {
      try {
        stage.setScene(MainPageScene.scene());
      } catch (IOException e1) {

        e1.printStackTrace();
      }
    });
    payNow_bt.setOnAction(e -> {
      int index = invoices_lv.getSelectionModel().getSelectedIndex();
      user.getAllInvoices().remove(index);
      invoiceFile.deleteInvoice(user.getLoginInfo().getUserId(), index);
      invoices_lv.getItems().setAll(user.getAllInvoices());
    });
    delete_bt.setOnAction(e -> {
      int index = invoices_lv.getSelectionModel().getSelectedIndex();
      user.getAllInvoices().remove(index);
      invoiceFile.deleteInvoice(userID, index);
      invoices_lv.getItems().setAll(user.getAllInvoices());
    });
    invoices_lv.setOnMouseClicked(e -> {
      Invoice selected = invoices_lv.getSelectionModel().getSelectedItem();

      amount_tf.setText(selected.getAmount().toString());
      accountNum_tf.setText(selected.getRecipientAccountNumber());
      cidComment_tf.setText(selected.getComment());
      due_dp.setValue(selected.getDueDate());
    });

    ImageView homeButton = ApplicationObjects.newImage("home.png", 10, 10, 20, 20);
    homeButton.setOnMouseClicked(e -> {
      MediaPlayer textToSpeachnew;
      try {
        textToSpeachnew = ApplicationObjects.newSound("mainPageScene");
        if (ApplicationObjects.soundOn()) {
          ApplicationObjects.getPlaying().stop();
          textToSpeachnew.play();
          ApplicationObjects.setPlaying(textToSpeachnew);
        }
      } catch (MalformedURLException e1) {
        e1.printStackTrace();
      }
      try {
        stage.setScene(MainPageScene.scene());
      } catch (IOException e1) {
        e1.printStackTrace();
      }
    });
    Button dropDownButton = ApplicationObjects.newButton("test", 676, 10, 10, 10, 10);
    Group dropDown = ApplicationObjects.dropDownMenu();
    ImageView manageUserButton = ApplicationObjects.newImage("user.png", 646, 10, 20, 20);
    Group root = new Group(amount_t, accountNum_t, cidComment_t, dueDate_t,
        amount_tf, accountNum_tf, cidComment_tf,
        invoices_lv, due_dp,
        clear_bt, register_bt, payNow_bt, delete_bt, dropDownButton, homeButton, manageUserButton);
    dropDownButton.setOnAction(e -> {
      root.getChildren().add(dropDown);
    });
    root.getStylesheets().add("StyleSheet.css");
    Scene scene = new Scene(root, 728, 567, ApplicationObjects.getSceneColor());

    Group userButtons = ApplicationObjects.userMenu();
    manageUserButton.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
      @Override
      public void handle(MouseEvent event) {
        root.getChildren().add(userButtons);
        event.consume();
      }
    });
    scene.setOnMouseClicked(e -> {
      root.getChildren().remove(userButtons);
      root.getChildren().remove(dropDown);
    });

    return scene;
  }

  public static void restrictDatePicker(DatePicker datePicker, LocalDate minDate) {
    final Callback<DatePicker, DateCell> dayCellFactory = new Callback<>() {
      @Override
      public DateCell call(final DatePicker datePicker) {
        return new DateCell() {
          @Override
          public void updateItem(LocalDate item, boolean empty) {
            super.updateItem(item, empty);
            if (item.isBefore(minDate)) {
              setDisable(true);
              setStyle("-fx-background-color: #ffc0cb;");
            }
          }
        };
      }
    };
    datePicker.setDayCellFactory(dayCellFactory);
  }

}
