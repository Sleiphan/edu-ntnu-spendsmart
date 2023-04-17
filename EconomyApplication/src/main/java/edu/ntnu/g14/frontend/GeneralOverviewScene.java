package edu.ntnu.g14.frontend;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.io.IOException;

import edu.ntnu.g14.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Side;
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
    private static ObservableList<PieChart.Data> incomePieData;
    private static Text totalOfAllAccountsCombinedText;
    private static Text bigSumText;
    private static Text totalIncomeText;
    private static Text totalExpensesText;
    static public Scene scene() throws IOException {
        String monthlyExpensesPieChartTitle    = "Monthly Expenses";
        String yearlyExpensesPieChartTitle     = "Expenses Last Year";
        String monthlyIncomePieChartTitle      = "Monthly Income";
        String yearlyIncomePieChartTitle       = "Income Last Year";
        String[] columnTitlesTransactionsTable = {"Date", "Transaction", "Amount", "Account"};
        totalOfAllAccountsCombinedText = ApplicationObjects.newText("Total of all Accounts Combined (excl. Pension)", 16, false, 900/2 - 319/2, 50);
        setBigSumText();

        totalIncomeText = ApplicationObjects.newText("Income: " + loggedInUser
                .incomeLast30Days() + " kr", 20, false, 0, 170);
        totalIncomeText.setX(205 - totalIncomeText.getLayoutBounds().getWidth()/2);

        totalExpensesText = ApplicationObjects.newText("Expenses: " + loggedInUser
                .expensesLast30Days() + " kr", 20, false, 0, 170);
        totalExpensesText.setX(630 - totalExpensesText.getLayoutBounds().getWidth()/2);

        int expenditureDataWidth = 450;
        int expenditureDataHeight = 342;
        setPiesDataLast30Days();

        PieChart expenditurePieChart = new PieChart(expenditurePieData);
        expenditurePieChart.setLabelsVisible(false);
        expenditurePieChart.setAnimated(false);
        expenditurePieChart.setTitle(monthlyExpensesPieChartTitle);
        expenditurePieChart.setLegendVisible(true);
        expenditurePieChart.setLayoutX(400);
        expenditurePieChart.setLayoutY(567/2 - 120 + 20);
        expenditurePieChart.setStyle("-fx-pref-width: " + expenditureDataWidth +";  \n" +
                                     "-fx-pref-height: " + expenditureDataHeight +"; \n" +
                                     "-fx-min-width: " + expenditureDataWidth + ";   \n" +
                                     "-fx-min-height: " + expenditureDataHeight +";  \n" +
                                     "-fx-max-width: " + expenditureDataWidth +";   \n" +
                                     "-fx-max-height: " + expenditureDataHeight+";   ");


        PieChart incomePieChart = new PieChart(incomePieData);
        incomePieChart.setAnimated(false);
        incomePieChart.setLegendSide(Side.BOTTOM);
        incomePieChart.setTitle(monthlyIncomePieChartTitle);
        incomePieChart.setLayoutX(-50);
        incomePieChart.setLayoutY((float) 567/2 - 120 + 20);
        incomePieChart.setLegendVisible(true);
        incomePieChart.setLabelsVisible(false);
        incomePieChart.setStyle("-fx-pref-width: 500; \n" +
                "-fx-pref-height: 300; \n" +
                "-fx-min-width: 500;   \n" +
                "-fx-min-height: 300;  \n" +
                "-fx-max-width: 500;   \n" +
                "-fx-max-height: 300;  ");

        Button intervalToggle = ApplicationObjects.newButton("Yearly", 70, 75, 100, 20, 16);

        TableView<ObservableList<Object>> transactionsTables = ApplicationObjects.newTableView(columnTitlesTransactionsTable, 40, 230, 658, 300);
        transactionsTables.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);

        intervalToggle.setOnMouseClicked(actionEvent -> {
            if (!expenditurePieChart.getTitle().equals(yearlyExpensesPieChartTitle)) {
                totalIncomeText.setVisible(true);
                totalExpensesText.setVisible(true);
                expenditurePieChart.setVisible(true);
                incomePieChart.setVisible(true);
                setPiesDataLastYear();
                setTotalIncomeText(false);
                setTotalExpensesText(false);
                expenditurePieChart.setData(expenditurePieData);
                expenditurePieChart.setVisible(expenditurePieData.stream().anyMatch(pieData -> pieData.getPieValue() != 0));
                expenditurePieChart.setTitle(yearlyExpensesPieChartTitle);
                incomePieChart.setTitle(yearlyIncomePieChartTitle);
                incomePieChart.setData(incomePieData);
                incomePieChart.setVisible(incomePieData.stream().anyMatch(pieData -> pieData.getPieValue() != 0));
                intervalToggle.setText("Monthly");
            }
            else {
                totalIncomeText.setVisible(true);
                totalExpensesText.setVisible(true);
                expenditurePieChart.setVisible(true);
                incomePieChart.setVisible(true);
                setPiesDataLast30Days();
                setTotalIncomeText(true);
                setTotalExpensesText(true);
                expenditurePieChart.setTitle(monthlyExpensesPieChartTitle);
                expenditurePieChart.setData(expenditurePieData);
                expenditurePieChart.setVisible(expenditurePieData.stream().anyMatch(pieData -> pieData.getPieValue() != 0));
                incomePieChart.setTitle(monthlyIncomePieChartTitle);
                incomePieChart.setData(incomePieData);
                incomePieChart.setVisible(incomePieData.stream().anyMatch(pieData -> pieData.getPieValue() != 0));
                intervalToggle.setText("Yearly");
            }
        });







        ImageView homeButton = ApplicationObjects.newImage("home.png", 24, 12, 20, 20);
        homeButton.setOnMouseClicked(e -> {
            try {
                stage.setScene(MainPageScene.scene());
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });
        Button dropDownButton = ApplicationObjects.newButton("test", 835, 12, 10, 10, 10);
        Group dropDown = ApplicationObjects.dropDownMenuGeneralOverview();
        ImageView manageUserButton = ApplicationObjects.newImage("user.png", 798, 12, 20, 20);
        Group root = new Group(totalOfAllAccountsCombinedText, bigSumText,
                totalIncomeText, totalExpensesText,
                dropDownButton, homeButton, manageUserButton, expenditurePieChart, incomePieChart, intervalToggle);
        dropDownButton.setOnAction(e -> {
            root.getChildren().add(dropDown);
        });
        root.getStylesheets().addAll("StyleSheet.css");
        Scene scene = new Scene(root, 900, 567, ApplicationObjects.getSceneColor());


        Group userButtons = ApplicationObjects.userMenuGeneralOverview();

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
                        .getTotalExpenseOfCategoryLast30Days(category))).collect(Collectors.toList());
        expenditurePieData = FXCollections.observableArrayList(pieChartExpenditureData);

        List<PieChart.Data> pieChartIncomeData = Arrays.stream(ApplicationObjects.getBudgetIncomeCategories())
                .map(category -> new PieChart.Data(category, loggedInUser
                .getTotalIncomeOfCategoryLast30Days(category))).collect(Collectors.toList());
        incomePieData = FXCollections.observableArrayList(pieChartIncomeData);
    }

    private static void setBigSumText() {
        bigSumText = ApplicationObjects.newText(loggedInUser
                .amountAllAccounts() + " kr", 35, false, 346, 100);
        bigSumText.setX((float) 900/2 - bigSumText.getLayoutBounds().getWidth()/2);
    }

    private static void setTotalIncomeText(Boolean monthOrYear) {
        if (monthOrYear) {
            totalIncomeText.setText("Income: " + loggedInUser
                    .incomeLast30Days() + " kr");
        }
        else {
            totalIncomeText.setText("Income: " + loggedInUser
                    .incomeLastYear() + " kr");
        }
        if (loggedInUser.incomeLastYear().replaceAll(" ","").equals("0"))
            totalIncomeText.setVisible(false);
        totalIncomeText.setX(205 - totalIncomeText.getLayoutBounds().getWidth()/2);
    }

    public static void setTotalExpensesText(Boolean monthOrYear) {
        if (monthOrYear) {
            totalExpensesText.setText("Expenses: " + loggedInUser
                    .expensesLast30Days() + " kr");
        } else {
            totalExpensesText.setText("Expenses: " + loggedInUser
                    .expensesLastYear() + " kr");
        }
        if (loggedInUser.incomeLastYear().replaceAll(" ","").equals("0"))
            totalExpensesText.setVisible(false);
        totalExpensesText.setX(630 - totalExpensesText.getLayoutBounds().getWidth()/2);
    }

    private static void setPiesDataLastYear() {
        List<PieChart.Data> pieChartExpenditureData = Arrays.stream(ApplicationObjects.getBudgetExpenditureCategories())
                .map(category -> new PieChart.Data(category, loggedInUser
                        .getTotalExpenseOfCategoryLastYear(category))).collect(Collectors.toList());
        expenditurePieData = FXCollections.observableArrayList(pieChartExpenditureData);

        List<PieChart.Data> pieChartIncomeData = Arrays.stream(ApplicationObjects.getBudgetIncomeCategories())
                .map(category -> new PieChart.Data(category, loggedInUser
                        .getTotalIncomeOfCategoryLastYear(category))).collect(Collectors.toList());
        incomePieData = FXCollections.observableArrayList(pieChartIncomeData);
    }
}
