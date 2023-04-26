package edu.ntnu.g14.frontend;

import edu.ntnu.g14.AccountCategory;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class AccountCell extends ListCell<String> {

  public static final int FIT_HEIGHT = 20;
  public static final int FIT_WIDTH = 22;
  private final HBox hBox = new HBox(10);

  public AccountCell(String labelName, AccountCategory iconType, String amount) {
    super();
    Pane pane = new Pane();
    ImageView icon = createIcon(iconType);
    Label amountLabel = new Label("Balance: " + amount);
    amountLabel.setStyle("-fx-text-fill: #3477eb;" +
        "-fx-font-size: 12px;" +
        "-fx-font-weight: 700;");
    VBox vBox = new VBox();
    vBox.getChildren().addAll(new Label(labelName), amountLabel, pane);
    VBox.setMargin(amountLabel, new Insets(0, 0, 2, 0));

    hBox.getChildren().addAll(icon, vBox, pane);
    hBox.setAlignment(Pos.CENTER);
    HBox.setHgrow(pane, Priority.ALWAYS);
  }

  private static ImageView createIcon(AccountCategory category) {
    ImageView icon;
    try {
      switch (category) {
        case PENSION_ACCOUNT:
          icon = new ImageView(
              new Image(new FileInputStream("src/main/resources/images/pension_account.png")));
          icon.setFitWidth(FIT_WIDTH);
          icon.setFitHeight(FIT_HEIGHT);
          return icon;
        case SAVINGS_ACCOUNT:
          icon = new ImageView(
              new Image(new FileInputStream("src/main/resources/images/savings_account.png")));
          icon.setFitWidth(FIT_WIDTH);
          icon.setFitHeight(FIT_HEIGHT);
          return icon;
        case CHECKING_ACCOUNT:
          icon = new ImageView(
              new Image(new FileInputStream("src/main/resources/images/checking_account.png")));
          icon.setFitWidth(FIT_WIDTH);
          icon.setFitHeight(FIT_HEIGHT);
          return icon;
        case OTHER:
          icon = new ImageView(
              new Image(new FileInputStream("src/main/resources/images/other_account.png")));
          icon.setFitWidth(FIT_WIDTH);
          icon.setFitHeight(FIT_HEIGHT);
          return icon;
        default:
          throw new IllegalArgumentException("Icon name does not correspond to icon");
      }
    } catch (FileNotFoundException e) {
      throw new IllegalArgumentException("Icon not found");
    }
  }

  public HBox getHBox() {
    return hBox;
  }
}
