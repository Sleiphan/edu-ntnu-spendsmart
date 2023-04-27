package edu.ntnu.g14.frontend;

import edu.ntnu.g14.model.Account;
import edu.ntnu.g14.model.AccountCategory;
import edu.ntnu.g14.BankApplication;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;


public class ApplicationObjects {

  public static String borderColor = "#000000";
  public static String backgroundColor = "#b6bbbf";
  public static Color sceneColor = Color.valueOf("#e4eff7");

  static Stage stage = BankApplication.getStage();

  public static final DateTimeFormatter dateFormatter =
      DateTimeFormatter.ofPattern("dd/MM/yyyy");


  public static boolean sounds;

  public static boolean soundOn() {
    return sounds;
  }

  public static void setSound(boolean onOff) {
    sounds = onOff;
  }

  public static Color getSceneColor() {
    return sceneColor;
  }

  public static Group userMenuGeneralOverview() {
    Rectangle rectangle = newRectangle(683 + 20, 10, 130 + 26, 110);

    Button logOut = newButton("Log Out", 696 + 20, 15, 130, 40, 15);
    logOut.setOnAction(e -> {
      try {
        stage.setScene(LoginChooseUserScene.scene());
      } catch (IOException e1) {

        e1.printStackTrace();
      }
    });
    Button manageUser = newButton("Manage User", 696 + 20, 70, 130, 40, 15);
    manageUser.setOnAction(e -> {
      try {
        stage.setScene(UserManagementScene.scene());
      } catch (IOException e1) {

        e1.printStackTrace();
      }
    });
    return new Group(rectangle, logOut, manageUser);
  }

  public static MediaPlayer playing;

  public static void setPlaying(MediaPlayer play){
    playing = play;
  }

  public static MediaPlayer getPlaying(){
    return playing;
  }

  public static Group userMenu() {
    Rectangle rectangle = newRectangle(553, 10, 145, 110);

    Button logOut = newButton("Log Out", 563, 15, 130, 40, 15);
    logOut.setOnAction(e -> {
      try {
        stage.setScene(LoginChooseUserScene.scene());
      } catch (IOException e1) {

        e1.printStackTrace();
      }
    });
    Button manageUser = newButton("Manage User", 563, 70, 130, 40, 15);
    manageUser.setOnAction(e -> {
      try {
        playing.stop();
        MediaPlayer sound = ApplicationObjects.newSound("userManagementScene");
        setPlaying(sound);
        if (ApplicationObjects.soundOn()) {
          sound.play();
        }
        stage.setScene(UserManagementScene.scene());
      } catch (IOException e1) {

        e1.printStackTrace();
      }
    });
    return new Group(rectangle, logOut, manageUser);
  }

  public static Group dropDownMenuGeneralOverview() {
    Rectangle rectangle = newRectangle(683 + 20, 10, 130 + 26, 340);
    Button invoice = newButton("invoice", 696 + 20, 15, 130, 40, 20);
    invoice.setOnAction(e -> {
      try {
        playing.stop();
        MediaPlayer sound = ApplicationObjects.newSound("invoiceScene");
        setPlaying(sound);
        if (ApplicationObjects.soundOn()) {
          sound.play();
        }
        stage.setScene(InvoiceScene.scene());
      } catch (IOException e1) {
        e1.printStackTrace();
      }
    });
    Button transfer = newButton("transfer", 696 + 20, 70, 130, 40, 20);
    transfer.setOnAction(e -> {
      try {
        playing.stop();
        MediaPlayer sound = ApplicationObjects.newSound("transferScene");
        setPlaying(sound);
        if (ApplicationObjects.soundOn()) {
          sound.play();
        }
        stage.setScene(TransferScene.scene());
      } catch (IOException e1) {
        e1.printStackTrace();
      }
    });
    Button payment = newButton("payment", 696 + 20, 125, 130, 40, 20);
    payment.setOnAction(e -> {
      try {
        playing.stop();
        MediaPlayer sound = ApplicationObjects.newSound("transferScene");
        setPlaying(sound);
        if (ApplicationObjects.soundOn()) {
          sound.play();
        }
        stage.setScene(PaymentScene.scene());
      } catch (IOException e1) {
        e1.printStackTrace();
      }
    });
    Button accounts = newButton("accounts", 696 + 20, 180, 130, 40, 20);
    accounts.setOnAction(e -> {
      try {
        playing.stop();
        MediaPlayer sound = ApplicationObjects.newSound("accountOverviewScene");
        setPlaying(sound);
        if (ApplicationObjects.soundOn()) {
          sound.play();
        }
        stage.setScene(AccountOverviewScene.scene(null));
      } catch (IOException e1) {

        e1.printStackTrace();
      }
    });
    Button overview = newButton("overview", 696 + 20, 235, 130, 40, 20);
    overview.setOnAction(e -> {
      try {
        playing.stop();
        MediaPlayer sound = ApplicationObjects.newSound("generalOverviewScene");
        setPlaying(sound);
        if (ApplicationObjects.soundOn()) {
          sound.play();
        }
        stage.setScene(GeneralOverviewScene.scene());
      } catch (IOException e1) {

        e1.printStackTrace();
      }
    });
    Button budgetting = newButton("budgeting", 696 + 20, 290, 130, 40, 20);
    budgetting.setOnAction(e -> {
      try {
        playing.stop();
        MediaPlayer sound = ApplicationObjects.newSound("budgetingScene");
        setPlaying(sound);
        if (ApplicationObjects.soundOn()) {
          sound.play();
        }
        stage.setScene(BudgetingScene.scene());
      } catch (IOException e1) {

        e1.printStackTrace();
      }
    });
    return new Group(rectangle, invoice, transfer, payment, accounts, overview, budgetting);
  }

  public static Group dropDownMenu() {
    Rectangle rectangle = newRectangle(553, 10, 145, 340);
    Button invoice = newButton("invoice", 563, 15, 130, 40, 20);
    invoice.setOnAction(e -> {
      try {
        playing.stop();
        MediaPlayer sound = ApplicationObjects.newSound("invoiceScene");
        setPlaying(sound);
        if (ApplicationObjects.soundOn()) {
          sound.play();
        }
        stage.setScene(InvoiceScene.scene());
      } catch (IOException e1) {
        e1.printStackTrace();
      }
    });
    Button transfer = newButton("transfer", 563, 70, 130, 40, 20);
    transfer.setOnAction(e -> {
      try {
        playing.stop();
        MediaPlayer sound = ApplicationObjects.newSound("transferScene");
        setPlaying(sound);
        if (ApplicationObjects.soundOn()) {
          sound.play();
        }
        stage.setScene(TransferScene.scene());
      } catch (IOException e1) {
        e1.printStackTrace();
      }
    });
    Button payment = newButton("payment", 563, 125, 130, 40, 20);
    payment.setOnAction(e -> {
      try {
        playing.stop();
        MediaPlayer sound = ApplicationObjects.newSound("transferScene");
        setPlaying(sound);
        if (ApplicationObjects.soundOn()) {
          sound.play();
        }
        stage.setScene(PaymentScene.scene());
      } catch (IOException e1) {
        e1.printStackTrace();
      }
    });
    Button accounts = newButton("accounts", 563, 180, 130, 40, 20);
    accounts.setOnAction(e -> {
      try {
        playing.stop();
        MediaPlayer sound = ApplicationObjects.newSound("accountOverviewScene");
        setPlaying(sound);
        if (ApplicationObjects.soundOn()) {
          sound.play();
        }
        stage.setScene(AccountOverviewScene.scene(null));
      } catch (IOException e1) {

        e1.printStackTrace();
      }
    });
    Button overview = newButton("overview", 563, 235, 130, 40, 20);
    overview.setOnAction(e -> {
      try {
        playing.stop();
        MediaPlayer sound = ApplicationObjects.newSound("generalOverviewScene");
        setPlaying(sound);
        if (ApplicationObjects.soundOn()) {
          sound.play();
        }
        stage.setScene(GeneralOverviewScene.scene());
      } catch (IOException e1) {

        e1.printStackTrace();
      }
    });
    Button budgetting = newButton("budgeting", 563, 290, 130, 40, 20);
    budgetting.setOnAction(e -> {
      try {
        playing.stop();
        MediaPlayer sound = ApplicationObjects.newSound("budgetingScene");
        setPlaying(sound);
        if (ApplicationObjects.soundOn()) {
          sound.play();
        }
        stage.setScene(BudgetingScene.scene());
      } catch (IOException e1) {

        e1.printStackTrace();
      }
    });
    return new Group(rectangle, invoice, transfer, payment, accounts, overview, budgetting);
  }

  public static Button newButton(String text, int x, int y, int width, int height, int fontSize) {
    Button button = new Button(text);
    button.setLayoutX(x);
    button.setLayoutY(y);
    button.setStyle(setStyleString(borderColor, backgroundColor, width, height, fontSize));
    button.setOnMouseEntered(
        e -> button.setStyle(setStyleString(borderColor, "grey", width, height, fontSize)));
    button.setOnMouseExited(e -> button.setStyle(
        setStyleString(borderColor, backgroundColor, width, height, fontSize)));

    return button;
  }

  public static Group newButtonWithIcon(String text, int x, int y, int width, int height,
      int fontSize, String iconname, Scene scene, MediaPlayer playing, MediaPlayer play) throws FileNotFoundException {
    Button button = new Button(text);
    button.setLayoutX(x);
    button.setLayoutY(y);
    button.setStyle(setStyleString(borderColor, backgroundColor, width, height, fontSize));
    button.setOnMouseEntered(
        e -> button.setStyle(setStyleString(borderColor, "grey", width, height, fontSize)));
    button.setOnMouseExited(e -> button.setStyle(
        setStyleString(borderColor, backgroundColor, width, height, fontSize)));

    ImageView icon = newImage(iconname, x + 10, y + 5, height - 2, height - 2);
    icon.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
      @Override
      public void handle(MouseEvent event) {
        stage.setScene(scene);
        event.consume();
      }
    });
    button.setOnAction(e -> {
        playing.stop();
        stage.setScene(scene);
        if (ApplicationObjects.soundOn()) {
          play.play();
        }
    });
    Group group = new Group(button, icon);
    return group;
  }

  public static MediaPlayer newSound(String soundName) throws MalformedURLException {
    File mediaFile = new File("src/main/resources/sounds/" + soundName + ".mp3");
    Media media = new Media(mediaFile.toURI().toURL().toString());
    MediaPlayer player = new MediaPlayer(media);
    return player;
  }

  public static ToggleButton newToggleButton(String text, int x, int y, int width, int height,
      int fontSize) {
    ToggleButton toggleButton = new ToggleButton(text);
    toggleButton.setLayoutX(x);
    toggleButton.setLayoutY(y);
    toggleButton.setStyle(setStyleString(borderColor, backgroundColor, width, height, fontSize));
    toggleButton.setOnMouseEntered(
        e -> toggleButton.setStyle(setStyleString(borderColor, "grey", width, height, fontSize)));
    toggleButton.setOnMouseExited(e -> toggleButton.setStyle(
        setStyleString(borderColor, backgroundColor, width, height, fontSize)));
    return toggleButton;
  }

  public static TextField newTextField(String promptText, int x, int y, int width, int height,
      int fontSize) {
    TextField textField = new TextField();
    textField.setPromptText(promptText);
    textField.setLayoutX(x);
    textField.setLayoutY(y);
    textField.setStyle(setStyleString(borderColor, backgroundColor, width, height, fontSize));
    return textField;
  }

  public static ImageView newImage(String imagename,
      int x, int y, int width, int height) throws FileNotFoundException {
    ImageView imageview = new ImageView(
        new Image(new FileInputStream("src/main/resources/images/" + imagename)));
    imageview.setX(x);
    imageview.setY(y);
    imageview.setFitHeight(width);
    imageview.setFitWidth(height);
    imageview.setPreserveRatio(true);
    return imageview;
  }

  public static Text newText(String title, int size, boolean underline, int x, int y) {
    Text text = new Text(title);
    text.setFont(Font.font("Times New Roman", FontWeight.BOLD, size));
    text.setUnderline(underline);
    text.setX(x);
    text.setY(y);
    return text;
  }

  public static TableView<ObservableList<Object>> newTableView(String[] columnTitles, double x,
      double y,
      double width, double height) {
    TableView<ObservableList<Object>> tableView = new TableView<>();
    tableView.setLayoutX(x);
    tableView.setLayoutY(y);
    tableView.setPrefWidth(width);
    tableView.setPrefHeight(height);

    for (String title : columnTitles) {
      TableColumn<ObservableList<Object>, Object> column = new TableColumn<>(title);
      column.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(
          param.getValue().get(tableView.getColumns().indexOf(column))));
      tableView.getColumns().add(column);
    }

    return tableView;
  }

  public static TableView<ObservableList<Object>> newTableView1(String[] columnTitles, double x,
      double y,
      double width, double height, List<String> accountNumbers) {
    TableView<ObservableList<Object>> tableView = new TableView<>();
    tableView.setLayoutX(x);
    tableView.setLayoutY(y);
    tableView.setPrefWidth(width);
    tableView.setPrefHeight(height);

    int numberOfColumns = columnTitles.length;

    // create columns
    for (String title : columnTitles) {
      TableColumn<ObservableList<Object>, Object> column = new TableColumn<>(title);

      // set cell factory for second or third column (index 1)
      if (tableView.getColumns().size() == 1 && numberOfColumns < 3
          || tableView.getColumns().size() == 2) {
        column.setCellFactory(param -> new TableCell<>() {
          @Override
          protected void updateItem(Object item, boolean empty) {
            super.updateItem(item, empty);

            if (item == null || empty) {
              setText("");
              setStyle("");
              return;
            }
            ObservableList<Object> rowData = getTableView().getItems().get(getIndex());
            Object prevCellData = rowData.get(0); // get data from previous cell
            if (accountNumbers.contains(prevCellData)) {
              setStyle("-fx-font-family: \"Helvetica Neue\";\n" +
                  "    -fx-font-size: 16px;\n" +
                  "    -fx-font-weight: bold;\n" +
                  "    -fx-text-fill: #3477eb;");
            } else {
              setStyle("-fx-font-family: \"Helvetica Neue\";\n" +
                  "    -fx-font-size: 16px;\n" +
                  "    -fx-font-weight: bold;\n" +
                  "    -fx-text-fill: #eb344c;");
            }
            setText(item.toString());
          }
        });
      }

      column.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(
          param.getValue().get(tableView.getColumns().indexOf(column))));
      tableView.getColumns().add(column);
    }

    return tableView;
  }

  public static ListView<String> newListView(String[] elements, double x, double y, double width,
      double height) {
    ListView<String> listView = new ListView<>();
    listView.setLayoutX(x);
    listView.setLayoutY(y);
    listView.setPrefWidth(width);
    listView.setPrefHeight(height);

    ObservableList<String> items = FXCollections.observableArrayList(elements);

    listView.setItems(items);
    listView.setCellFactory(param -> new ListCell<String>() {

      public void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);
        if (empty || item == null) {
          setText(null);
          setGraphic(null);
        } else {
          Account account = BankApplication.loggedInUser.getAccountWithAccountName(item);
          String amount = formatCurrency(account.getAmount());
          AccountCell cell;
          switch (account.getAccountType()) {
            case PENSION_ACCOUNT:
              cell = new AccountCell(item, AccountCategory.PENSION_ACCOUNT, amount);
              setGraphic(cell.getHBox());
              break;
            case SAVINGS_ACCOUNT:
              cell = new AccountCell(item, AccountCategory.SAVINGS_ACCOUNT, amount);
              setGraphic(cell.getHBox());
              break;
            case CHECKING_ACCOUNT:
              cell = new AccountCell(item, AccountCategory.CHECKING_ACCOUNT, amount);
              setGraphic(cell.getHBox());
              break;
            case OTHER:
              cell = new AccountCell(item, AccountCategory.OTHER, amount);
              setGraphic(cell.getHBox());
          }
        }
      }
    });
    return listView;
  }

  public static Rectangle newRectangle(int x, int y, int width, int height) {
    Rectangle rectangle = new Rectangle();
    rectangle.setX(x);
    rectangle.setY(y);
    rectangle.setWidth(width);
    rectangle.setHeight(height);
    rectangle.setStroke(Color.BLACK);
    rectangle.setFill(Color.WHITE);
    return rectangle;
  }

  public static ChoiceBox<String> newChoiceBox(String[] choices, int width, int height,
      int fontSize, int x, int y) {
    ChoiceBox<String> choiceBox = new ChoiceBox<>();
    choiceBox.getItems().addAll(choices);
    choiceBox.setStyle(setStyleString(borderColor, backgroundColor, width, height, fontSize));
    choiceBox.setLayoutX(x);
    choiceBox.setLayoutY(y);
    return choiceBox;
  }

  public static ComboBox<String> newComboBox(String[] choices, int width, int height, int fontSize,
      int x, int y) {
    ComboBox<String> comboBox = new ComboBox<>();
    comboBox.getItems().addAll(choices);
    comboBox.setStyle(setStyleString(borderColor, backgroundColor, width, height, fontSize));
    comboBox.setLayoutX(x);
    comboBox.setLayoutY(y);
    return comboBox;
  }

  public static Alert alertBox(String title, String header, String content) {
    Alert alert = new Alert(AlertType.ERROR);
    alert.setTitle(title);
    alert.setHeaderText(header);
    alert.setContentText(content);
    alert.showAndWait();
    return alert;
  }

  public static String[] getAccountCategories() {
    return new String[]{"Checking Account", "Savings Account", "Pension Account", "Other"};
  }

  public static String[] getBudgetCategories() {
    return new String[]{"Food and Drink", "Clothes and Shoes", "Personal Care", "Leisure", "Travel"
        , "Alcohol and Tobacco", "Other", "Salary", "Payment", "Income", "Business"};

  }

  public static String[] getBudgetExpenditureCategories() {
    return new String[]{"Food and Drink", "Clothes and Shoes", "Personal Care", "Leisure", "Travel"
        , "Alcohol and Tobacco", "Payment", "Other"};
  }

  public static String formatCurrency(BigDecimal amount) {
    final String CURRENCY_SYMBOL = " kr";
    String decimalFormat = decimalFormat(amount);

    return decimalFormat.replaceAll("(?<=\\d)(?=(\\d{3})+(?!\\d))", " ") + CURRENCY_SYMBOL;
  }

  private static String decimalFormat(BigDecimal amount) {
    DecimalFormat df = new DecimalFormat();

    df.setMaximumFractionDigits(2);

    df.setMinimumFractionDigits(2);

    df.setGroupingUsed(false);

    return df.format(amount);
  }

  public static String[] getBudgetIncomeCategories() {
    return new String[]{"Salary", "Income", "Business"};
  }

  public static String setStyleString(String borderColor,
      String backgroundColor, int width, int height, int fontSize) {
    return "-fx-border-color: " + borderColor + ";" +
        "-fx-background-color: " + backgroundColor + ";" +
        "-fx-pref-width: " + width + ";" +
        "-fx-pref-height: " + height + ";" +
        "-fx-font-size: " + fontSize + "px;";
  }

}
