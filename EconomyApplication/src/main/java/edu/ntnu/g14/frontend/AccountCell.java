package edu.ntnu.g14.frontend;

import edu.ntnu.g14.AccountCategory;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class AccountCell extends ListCell<String> {

    public static final int FIT_HEIGHT = 20;
    public static final int FIT_WIDTH = 22;
    private final HBox hBox = new HBox(10);

    public AccountCell(String labelName, AccountCategory iconType) {
        super();
        Pane pane = new Pane();
        ImageView icon = createIcon(iconType);

        hBox.getChildren().addAll(icon, new Label(labelName), pane);
        hBox.setAlignment(Pos.CENTER);
        HBox.setHgrow(pane, Priority.ALWAYS);
    }
    private static ImageView createIcon(AccountCategory category) {
        ImageView icon;
        try {
            switch (category) {
                case PENSION_ACCOUNT:
                    icon = new ImageView(new Image(new FileInputStream("src/main/resources/images/pension_account.png")));
                    icon.setFitWidth(FIT_WIDTH);
                    icon.setFitHeight(FIT_HEIGHT);
                    return icon;
                case SAVINGS_ACCOUNT:
                    icon = new ImageView(new Image(new FileInputStream("src/main/resources/images/savings_account.png")));
                    icon.setFitWidth(FIT_WIDTH);
                    icon.setFitHeight(FIT_HEIGHT);
                    return icon;
                case CHECKING_ACCOUNT:
                    icon = new ImageView(new Image(new FileInputStream("src/main/resources/images/checking_account.png")));
                    icon.setFitWidth(FIT_WIDTH);
                    icon.setFitHeight(FIT_HEIGHT);
                    return icon;
                case OTHER:
                    icon = new ImageView(new Image(new FileInputStream("src/main/resources/images/other_account.png")));
                    icon.setFitWidth(FIT_WIDTH);
                    icon.setFitHeight(FIT_HEIGHT);
                    return icon;
                default: throw new IllegalArgumentException("Icon name does not correspond to icon");
            }
        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException("Icon not found");
        }
    }

    public HBox getHBox() {
        return hBox;
    }
}
