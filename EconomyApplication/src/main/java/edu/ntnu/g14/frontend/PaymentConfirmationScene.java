package edu.ntnu.g14.frontend;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import edu.ntnu.g14.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;


public class PaymentConfirmationScene {
    static Stage stage = BankApplication.getStage();
    private static ArrayList<String> paymentInfo;

    private static final DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern("dd.MM.yyyy");

    public static final DateTimeFormatter dateFormatter =
            DateTimeFormatter.ofPattern("dd/MM/yyyy");

    static public Scene scene() throws FileNotFoundException, IOException {
        TableView<ObservableList<Object>> payment = ApplicationObjects.newTableView(new String[]{"Payment", "Information"}, 100, 100, 605, 224);
        ObservableList<ObservableList<Object>> paymentData = initializePaymentData(getPaymentInfo());
        payment.setItems(paymentData);
        payment.getColumns().forEach(colum -> colum.setMinWidth(300));
        payment.getColumns().forEach(column -> column.setSortable(false));


        Button confirm = ApplicationObjects.newButton("Confirm", 250, 350, 100, 20, 15);
        Button cancel = ApplicationObjects.newButton("Cancel", 400, 350, 100, 20, 15);


        confirm.setOnAction(e -> {
            try {
                User loggedInUser = BankApplication.loggedInUser;
                LocalDate dueDate = LocalDate.parse(getPaymentInfo().get(3), formatter); //Funker ikke!!! TODO: FIIIIIX
                dueDate = LocalDate.parse(dueDate.format(dateFormatter), dateFormatter);

                LocalDate dateOfTransaction = LocalDate.now();
                Payment paymentObject = new Payment(getPaymentInfo().get(0), new BigDecimal(getPaymentInfo().get(1)),
                        getPaymentInfo().get(2), getPaymentInfo().get(4), dueDate, getPaymentInfo().get(5),
                        dateOfTransaction,
                        BudgetCategory.valueOf(getPaymentInfo()
                                .get(6).replaceAll(" ", "_").toUpperCase()));
                System.out.println(paymentObject.toString());
                
                FileManagement.writeTransaction(loggedInUser.getLoginInfo().getUserId(), paymentObject);

                stage.setScene(MainPageScene.scene());
            } catch (IOException e1) {

                e1.printStackTrace();
            }
        });
        cancel.setOnAction(e -> {
            try {
                stage.setScene(PaymentScene.scene());
            } catch (IOException e1) {
                
                e1.printStackTrace();
            }
        });


    
        ImageView homeButton = ApplicationObjects.newImage("home.png", 10, 10, 20, 20);
        homeButton.setOnMouseClicked(e -> {
            try {
                stage.setScene(MainPageScene.scene());
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });
        Button dropDownButton = ApplicationObjects.newButton("test", 676, 10, 10, 10, 10);
        Group dropDown = ApplicationObjects.dropDownMenu();
        ImageView manageUserButton = ApplicationObjects.newImage("user.png", 646, 10, 20, 20);
        Group root = new Group(payment, confirm, cancel, dropDownButton, homeButton, manageUserButton);
        dropDownButton.setOnAction(e -> {
            root.getChildren().add(dropDown);
        });
        root.getStylesheets().add("StyleSheet.css"); 
        Scene scene = new Scene(root, 728, 567, ApplicationObjects.getSceneColor());
        
        
        Group userButtons = ApplicationObjects.userMenu();
        manageUserButton.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event){
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

    public static ObservableList<ObservableList<Object>> initializePaymentData(ArrayList<String> paymentInfo) {
        String fromAccount = paymentInfo.get(0);
        String amount = paymentInfo.get(1);
        String description = paymentInfo.get(2);
        String dueDate = paymentInfo.get(3);
        String toAccount = paymentInfo.get(4);
        String cid = paymentInfo.get(5);

        ObservableList<ObservableList<Object>> paymentData = FXCollections.observableArrayList();
        paymentData.add(FXCollections.observableArrayList("From account:", fromAccount));
        paymentData.add(FXCollections.observableArrayList("Amount:", new BigDecimal(amount) + "kr"));
        paymentData.add(FXCollections.observableArrayList("Description:", description));
        paymentData.add(FXCollections.observableArrayList("Due date:", dueDate));
        paymentData.add(FXCollections.observableArrayList("To account:", toAccount));
        paymentData.add(FXCollections.observableArrayList("CID:", cid));
        return paymentData;
    }

    public static ArrayList<String> getPaymentInfo() {
        return paymentInfo;
    }

    public static void setPaymentInfo(ArrayList<String> paymentInfo) {
        PaymentConfirmationScene.paymentInfo = paymentInfo;
    }
}
