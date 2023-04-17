package edu.ntnu.g14.frontend;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import edu.ntnu.g14.*;
import javafx.beans.property.*;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.StringConverter;


public class ApplicationObjects {

    public static String borderColor = "#071E22";
    public static String backgroundColor = "#dba87d";
    public static Color sceneColor = Color.valueOf("#F4C095");

    static Stage stage = ApplicationFront.getStage();

    public static final DateTimeFormatter dateFormatter =
            DateTimeFormatter.ofPattern("dd/MM/yyyy");


    public static Color getSceneColor(){
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
                stage.setScene(InvoiceScene.scene());
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });
        Button transfer = newButton("transfer", 696 + 20, 70, 130, 40, 20);
        transfer.setOnAction(e -> {
            try {
                stage.setScene(TransferScene.scene());
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });
        Button payment = newButton("payment", 696 + 20, 125, 130, 40, 20);
        payment.setOnAction(e -> {
            try {
                stage.setScene(PaymentScene.scene());
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });
        Button accounts = newButton("accounts", 696 + 20, 180, 130, 40, 20);
        accounts.setOnAction(e -> {
            try {
                stage.setScene(AccountOverviewScene.scene(null));
            } catch (IOException e1) {

                e1.printStackTrace();
            }
        });
        Button overview = newButton("overview", 696 + 20, 235, 130, 40, 20);
        overview.setOnAction(e -> {
            try {
                stage.setScene(GeneralOverviewScene.scene());
            } catch (IOException e1) {

                e1.printStackTrace();
            }
        });
        Button budgetting = newButton("budgetting", 696 + 20, 290, 130, 40, 20);
        budgetting.setOnAction(e -> {
            try {
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
                stage.setScene(InvoiceScene.scene());
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });
        Button transfer = newButton("transfer", 563, 70, 130, 40, 20);
        transfer.setOnAction(e -> {
            try {
                stage.setScene(TransferScene.scene());
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });
        Button payment = newButton("payment", 563, 125, 130, 40, 20);
        payment.setOnAction(e -> {
            try {
                stage.setScene(PaymentScene.scene());
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });
        Button accounts = newButton("accounts", 563, 180, 130, 40, 20);
        accounts.setOnAction(e -> {
            try {
                stage.setScene(AccountOverviewScene.scene(null));
            } catch (IOException e1) {
                
                e1.printStackTrace();
            }
        });
        Button overview = newButton("overview", 563, 235, 130, 40, 20);
        overview.setOnAction(e -> {
            try {
                stage.setScene(GeneralOverviewScene.scene());
            } catch (IOException e1) {
                
                e1.printStackTrace();
            }
        });
        Button budgetting = newButton("budgetting", 563, 290, 130, 40, 20);
        budgetting.setOnAction(e -> {
            try {
                stage.setScene(BudgetingScene.scene());
            } catch (IOException e1) {
                
                e1.printStackTrace();
            }
        });
        return new Group(rectangle, invoice, transfer, payment, accounts, overview, budgetting);
    }
    public static Button newButton(String text, int x, int y, int width, int height, int fontSize){
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

    public static Group newButtonWithIcon(String text, int x, int y, int width, int height, int fontSize, String iconname, Scene scene) throws FileNotFoundException {
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
            public void handle(MouseEvent event){
                stage.setScene(scene);
                event.consume();
            }
        }); 
        button.setOnAction(e -> {
            stage.setScene(scene);
        });
        Group group = new Group(button, icon);
        return group;
    }
    
    public static ToggleButton newToggleButton(String text, int x, int y, int width, int height, int fontSize) {
        ToggleButton toggleButton = new ToggleButton(text);
        toggleButton.setLayoutX(x);
        toggleButton.setLayoutY(y);
        toggleButton.setStyle(setStyleString(borderColor, backgroundColor, width, height, fontSize));
        toggleButton.setOnMouseEntered(e -> toggleButton.setStyle(setStyleString(borderColor, "grey", width, height, fontSize)));
        toggleButton.setOnMouseExited(e -> toggleButton.setStyle(setStyleString(borderColor, backgroundColor, width, height, fontSize)));
        return toggleButton;
    }

    public static TextField newTextField(String promptText, int x, int y, int width, int height, int fontSize) {
        TextField textField = new TextField();
        textField.setPromptText(promptText);
        textField.setLayoutX(x);
        textField.setLayoutY(y);
        textField.setStyle(setStyleString(borderColor, backgroundColor, width, height, fontSize));
        return textField;
    }

    public static ImageView newImage(String imagename,
     int x, int y, int width, int height) throws FileNotFoundException{
        ImageView imageview = new ImageView();
        Image image = new Image(new FileInputStream("src/main/resources/images/" + imagename));
        imageview.setImage(image);
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

    public static TableView<ObservableList<Object>> newTableView(String[] columnTitles, double x, double y,
                                                                 double width, double height) {
        TableView<ObservableList<Object>> tableView = new TableView<>();
        tableView.setLayoutX(x);
        tableView.setLayoutY(y);
        tableView.setPrefWidth(width);
        tableView.setPrefHeight(height);

        int colum = columnTitles.length;

        // create columns
         for (String title : columnTitles) {
         TableColumn<ObservableList<Object>, Object> column = new TableColumn<>(title);
         column.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(
         param.getValue().get(tableView.getColumns().indexOf(column))));
         tableView.getColumns().add(column);
         }


        return tableView;
    }

    public static TableView<ObservableList<Object>> newTableView1(String[] columnTitles, double x, double y,
                                                                 double width, double height, List<String> accountNumbers) {
        TableView<ObservableList<Object>> tableView = new TableView<>();
        tableView.setLayoutX(x);
        tableView.setLayoutY(y);
        tableView.setPrefWidth(width);
        tableView.setPrefHeight(height);

        int colum = columnTitles.length;

        // create columns
        for (String title : columnTitles) {
            TableColumn<ObservableList<Object>, Object> column = new TableColumn<>(title);

            // set cell factory for second column (index 1)
            if (tableView.getColumns().size() == 1 && columnTitles.length < 3 || tableView.getColumns().size() == 2) {
                column.setCellFactory(param -> new TableCell<>() {
                    @Override
                    protected void updateItem(Object item, boolean empty) {
                        super.updateItem(item, empty);

                        if (item == null || empty) {
                            setText("");
                            setStyle("");
                            return;
                        }
                        setStyle("-fx-font-weight: 700");
                        setStyle("-fx-font-family: 'Arial'");
                        ObservableList<Object> rowData = getTableView().getItems().get(getIndex());
                        Object prevCellData = rowData.get(0); // get data from previous cell
                        if (accountNumbers.contains(prevCellData)) {
                            setTextFill(Paint.valueOf("#3477eb")); // set text color to blue
                        } else {
                            setTextFill(Paint.valueOf("#eb344c")); // set text color to red
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






    public static ListView<String> newListView(String[] elements, double x, double y, double width, double height) {
        ListView<String> listView = new ListView<>();
        listView.setLayoutX(x);
        listView.setLayoutY(y);
        listView.setPrefWidth(width);
        listView.setPrefHeight(height);

        for (String element: elements) {
            listView.getItems().add(element);
        }
        return listView;
    }
    
    public static Rectangle newRectangle(int x, int y, int width, int height){
        Rectangle rectangle = new Rectangle();
        rectangle.setX(x);
        rectangle.setY(y);
        rectangle.setWidth(width);
        rectangle.setHeight(height);
        rectangle.setStroke(Color.BLACK);
        rectangle.setFill(Color.WHITE);
        return rectangle;
    }

    public static ChoiceBox<String> newChoiceBox(String[] choices, int width, int height, int fontSize, int x, int y) {
        ChoiceBox<String> choiceBox = new ChoiceBox<>();
        choiceBox.getItems().addAll(choices);
        choiceBox.setStyle(setStyleString(borderColor, backgroundColor, width, height, fontSize));
        choiceBox.setLayoutX(x);
        choiceBox.setLayoutY(y);
        return choiceBox;
    }
    public static ComboBox<String> newComboBox(String[] choices, int width, int height, int fontSize, int x, int y) {
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
        return new String[] { "Checking Account", "Savings Account", "Pension Account", "Other"};
    }
    public static String[] getBudgetCategories() {
        return new String[] {"Food and Drink", "Clothes and Shoes", "Personal Care", "Leisure", "Travel"
                , "Alcohol and Tobacco", "Other", "Salary", "Payment", "Income", "Business"};

    }
    public static String[] getBudgetExpenditureCategories() {
        return new String[] {"Food and Drink", "Clothes and Shoes", "Personal Care", "Leisure", "Travel"
                , "Alcohol and Tobacco", "Payment", "Other"};
    }

    public static String numberRegex(String number) {
        
        return number.replaceAll("(\\d)(?=(\\d{3})+$)", "$1 ").trim().concat(" kr");
    }

    public static String[] getBudgetIncomeCategories() {
        return new String[] {"Salary", "Income", "Business"};
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
