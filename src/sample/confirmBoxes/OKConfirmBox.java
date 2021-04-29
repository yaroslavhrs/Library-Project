package sample.confirmBoxes;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class OKConfirmBox {

    public static void display(String title, String message) {
        Stage popWindow = new Stage();
        popWindow.setTitle(title);
        popWindow.setMinWidth(380);
        popWindow.setMinHeight(150);
        popWindow.setResizable(false);
        popWindow.getIcons().add(new Image("sample/assets/icon.png"));

        Label label = new Label();
        label.setText(message);
        label.setStyle("-fx-font-family: 'Times New Roman'; -fx-font-size: 16;");

        Button okButton = new Button("OK");
        okButton.setStyle("-fx-background-color : #ECCFA5; -fx-background-radius : 10 10 10 10;" +
                "-fx-font-family: 'Times New Roman'; -fx-font-size: 15;");
        okButton.setMinWidth(70);
        okButton.setMinHeight(30);
        okButton.setOnAction(event -> popWindow.close());
        okButton.setOnAction(event -> popWindow.close());

        VBox layout = new VBox(20);
        layout.getChildren().addAll(label, okButton);
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-background-color : #F6ECD3;");

        Scene scene = new Scene(layout);
        popWindow.setScene(scene);
        popWindow.showAndWait();
    }
}
