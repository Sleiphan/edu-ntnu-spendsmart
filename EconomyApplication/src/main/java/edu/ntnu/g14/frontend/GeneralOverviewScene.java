package edu.ntnu.g14.frontend;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.io.IOException;

import edu.ntnu.g14.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class GeneralOverviewScene {
    static Stage stage = ApplicationFront.getStage();
    private static final User loggedInUser = ApplicationFront.loggedInUser;
    private static ObservableList<PieChart.Data> expenditurePieData;
    private static ObservableList<PieChart.Data> pieIncomeData;
    private static Text totalOfAllAccountsCombinedText;
    private static Text bigSumText;
    private static Text totalIncomeText;
    private static Text totalExpensesText;
    static public Scene scene() throws IOException {

        String[] columnTitlesTransactionsTable = {"Date", "Transaction", "Amount", "Account"};
        totalOfAllAccountsCombinedText = ApplicationObjects.newText("Total of all Accounts Combined (excl. Pension)", 16, false, 728/2 - 319/2, 30);
        setBigSumText();
        setTotalIncomeText();
        setTotalExpensesText();
        ToggleGroup intervalToggles = new ToggleGroup();

        setPiesDataLast30Days();
        PieChart expenditurePieChart = new PieChart(expenditurePieData);
        expenditurePieChart.setLayoutX((float) 728/2 - 255);
        expenditurePieChart.setLayoutY((float) 567/2 - 130);
        expenditurePieChart.setLegendVisible(false);
        expenditurePieChart.setStartAngle(90);

        Button back_bt = ApplicationObjects.newButton("Back", 40, 30, 60, 20, 14);
        back_bt.setOnAction(e -> {
            try {
                stage.setScene(MainPageScene.scene());
            } catch (IOException e1) {

                e1.printStackTrace();
            }
        });
        ToggleButton yearlyToggle = ApplicationObjects.newToggleButton("Yearly", 40, 190, 100, 20, 16);
        yearlyToggle.setToggleGroup(intervalToggles);
        ToggleButton monthlyToggle = ApplicationObjects.newToggleButton("Monthly", 140, 190, 100, 20, 16);
        monthlyToggle.setToggleGroup(intervalToggles);
        TableView<ObservableList<Object>> transactionsTables = ApplicationObjects.newTableView(columnTitlesTransactionsTable, 40, 230, 658, 300);
        transactionsTables.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
        monthlyToggle.setOnAction(actionEvent -> {
            setPiesDataLast30Days();
            setBigSumText();
            setTotalIncomeText();
            setTotalExpensesText();
            expenditurePieChart.setData(expenditurePieData);
        });
        yearlyToggle.setOnAction(actionEvent -> {
            setPiesDataThisYear();
            setBigSumText();
            setTotalIncomeText();
            setTotalExpensesText();
            expenditurePieChart.setData(expenditurePieData);
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
        Group root = new Group(totalOfAllAccountsCombinedText, bigSumText,
                totalIncomeText, totalExpensesText, yearlyToggle, monthlyToggle,
                dropDownButton, homeButton, manageUserButton, expenditurePieChart);
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
    private static void setPiesDataLast30Days() {
        List<PieChart.Data> pieChartExpenditureData = Arrays.stream(ApplicationObjects.getBudgetExpenditureCategories())
                .map(category -> new PieChart.Data(category, loggedInUser
                        .getAmountSpentOnCategoryLast30Days(category))).collect(Collectors.toList());
        expenditurePieData = FXCollections.observableArrayList(pieChartExpenditureData);

        List<PieChart.Data> pieChartIncomeData = Arrays.stream(ApplicationObjects.getBudgetIncomeCategories())
                .map(category -> new PieChart.Data(category, loggedInUser
                .getAmountSpentOnCategoryLast30Days(category))).collect(Collectors.toList());
        pieIncomeData = FXCollections.observableArrayList(pieChartIncomeData);
    }

    private static void setBigSumText() {
        bigSumText = ApplicationObjects.newText(loggedInUser
                .amountAllAccounts() + " kr", 35, false, 280, 90);
        bigSumText.setX((float) 728/2 - bigSumText.getLayoutBounds().getWidth()/2);
    }

    private static void setTotalIncomeText() {
        totalIncomeText = ApplicationObjects.newText("Income: " + loggedInUser.
                incomeLast30Days() + " kr", 20, false, 0, 150);

        totalIncomeText.setX(totalOfAllAccountsCombinedText.getLayoutBounds().getMinX()
                - totalIncomeText.getLayoutBounds().getWidth()/2);
    }

    public static void setTotalExpensesText() {
        totalExpensesText = ApplicationObjects.newText("Expenses: " + loggedInUser
                .expensesLast30Days() + " kr", 20, false, 0, 150);
        totalExpensesText.setX(totalOfAllAccountsCombinedText.getLayoutBounds().getMaxX()
                - totalExpensesText.getLayoutBounds().getWidth()/2);
    }

    private static void setPiesDataThisYear() {
        List<PieChart.Data> pieChartExpenditureData = Arrays.stream(ApplicationObjects.getBudgetExpenditureCategories())
                .map(category -> new PieChart.Data(category, loggedInUser
                        .getAmountSpentOnCategoryLastYear(category))).collect(Collectors.toList());
        expenditurePieData = FXCollections.observableArrayList(pieChartExpenditureData);

        List<PieChart.Data> pieChartIncomeData = Arrays.stream(ApplicationObjects.getBudgetIncomeCategories())
                .map(category -> new PieChart.Data(category, loggedInUser
                        .getAmountSpentOnCategoryLastYear(category))).collect(Collectors.toList());
        pieIncomeData = FXCollections.observableArrayList(pieChartIncomeData);
    }
}
