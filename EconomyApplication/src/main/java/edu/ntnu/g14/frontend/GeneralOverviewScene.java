package edu.ntnu.g14.frontend;

import java.io.FileNotFoundException;

import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class GeneralOverviewScene {
    static Stage stage = ApplicationFront.getStage();

    static public Scene scene() throws FileNotFoundException {

        String [] columnTitlesTransactionsTable = {"Date", "Transaction", "Amount", "Account"};
        Text totalOfAllAccountsCombinedText = ApplicationObjects.newText("Total of all Accounts Combined (excl. Pension)", 16, false, 200, 30);
        Text bigSumText = ApplicationObjects.newText("736 000 kr", 35, false,280, 90);
        Text totalIncomeText = ApplicationObjects.newText("Income all accounts: 19 929 kr", 20, false, 40, 150);
        Text totalExpensesText = ApplicationObjects.newText("Expenses all account: 10 320 kr", 20, false, (728/2 + 40), 150);
        ToggleGroup intervalToggles = new ToggleGroup();

        Button back_bt = ApplicationObjects.newButton("Back", 40, 30, "black", "white", 60, 20, 14);
        back_bt.setOnAction(e -> {
            try {
                stage.setScene(MainPageScene.scene());
            } catch (FileNotFoundException e1) {
                
                e1.printStackTrace();
            }
        });
        ToggleButton yearlyToggle = ApplicationObjects.newToggleButton("Yearly", 40, 190, "black", "white", 80,20, 16);
        yearlyToggle.setToggleGroup(intervalToggles);
        ToggleButton monthlyToggle = ApplicationObjects.newToggleButton("Monthly", 120, 190, "black", "white", 80,20, 16);
        monthlyToggle.setToggleGroup(intervalToggles);
        TableView<ObservableList<Object>> transactionsTables = ApplicationObjects.newTableView(columnTitlesTransactionsTable, 40, 230, 658, 300);
        transactionsTables.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

      
        ImageView homeButton = ApplicationObjects.newImage("home.png", 10, 10, 20, 20);
        homeButton.setOnMouseClicked(e -> {
            try {
                stage.setScene(MainPageScene.scene());
            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
            }
        });
        Button dropDownButton = ApplicationObjects.newButton("test", 676, 10, "black", "white", 10, 10, 10);
        Group dropDown = ApplicationObjects.dropDownMenu();
        ImageView manageUserButton = ApplicationObjects.newImage("user.png", 646, 10, 20, 20);
        Group root = new Group(back_bt, totalOfAllAccountsCombinedText, bigSumText,
         totalIncomeText, totalExpensesText, yearlyToggle, monthlyToggle, transactionsTables, dropDownButton, homeButton, manageUserButton);
        dropDownButton.setOnAction(e -> {
            root.getChildren().add(dropDown);
        });

        Scene scene = new Scene(root, 728, 567, Color.WHITE);
        
        
        Group userButtons = ApplicationObjects.userMenu();
        manageUserButton.setOnMouseEntered(e -> {
            root.getChildren().add(userButtons);
        });
        scene.setOnMouseClicked(e -> {
            root.getChildren().remove(userButtons);
            root.getChildren().remove(dropDown);
        });
        return scene;
    }
    
}
