package sample.confirmBoxes;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import sample.database.CopyOfBook;
import sample.database.DatabaseHandler;
import java.io.IOException;
import static sample.Main.window;
import static sample.database.Status.*;

public class ChangeStatusConfirmBox {

    public void display(CopyOfBook copyOfBook) {
        Stage popWindow = new Stage();
        popWindow.setTitle("Зміна статусу примірника книги");
        popWindow.setMinWidth(450);
        popWindow.setMinHeight(170);
        popWindow.setResizable(false);
        popWindow.getIcons().add(new Image("sample/assets/icon.png"));

        Label label = new Label("Оберіть новий статус примірника:");
        ChoiceBox statusChangeChoiceBox = new ChoiceBox();
        statusChangeChoiceBox.setItems(statusChangeList);
        statusChangeChoiceBox.getStylesheets().add("sample/css/choiceBoxStyle.css");

        Button saveButton = new Button("Зберегти");

        label.setStyle("-fx-font-family: 'Times New Roman'; -fx-font-size: 16; -fx-alignment: 'center'");
        statusChangeChoiceBox.setStyle("-fx-background-color : #ECCFA5; -fx-background-radius : 10 10 10 10;" +
                "-fx-font-family: 'Times New Roman'; -fx-font-size: 15; -fx-alignment: 'center'");
        saveButton.setStyle("-fx-background-color : #ECCFA5; -fx-background-radius : 10 10 10 10;" +
                "-fx-font-family: 'Times New Roman'; -fx-font-size: 15;");
        statusChangeChoiceBox.setMaxWidth(200);
        statusChangeChoiceBox.setPrefHeight(30);
        saveButton.setMinWidth(90);
        saveButton.setMinHeight(30);

        saveButton.setOnAction(event -> {
            if (copyOfBook != null) {
                DatabaseHandler dbHandler = new DatabaseHandler();
                dbHandler.changeStatus(copyOfBook, statusChangeChoiceBox.getValue().toString());
                popWindow.close();
                setOtherScene("/sample/view/book_copies.fxml");
                OKConfirmBox.display("OK", "Статус примірника успішно змінено!");
            }
        });

        VBox layout = new VBox(20);
        layout.getChildren().addAll(label, statusChangeChoiceBox ,saveButton);
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-background-color : #F6ECD3;");

        Scene scene = new Scene(layout);
        popWindow.setScene(scene);
        popWindow.showAndWait();
    }

    public void setOtherScene(String path) {
        FXMLLoader loader = new FXMLLoader();
        // вказуємо місце розташування потрібного нам файлу
        loader.setLocation(getClass().getResource(path));
        // обробка виключень для випадку невдалого завантаження
        try {
            // завантажуємо цей файл для подальшого відображення
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // створення змінної класу Parent, значення якої буде шлях до файлу який необхідно завантажити
        Parent root = loader.getRoot();
        // встановлюємо необхідну сцену, у якості параметру - шлях до необхідного файлу - root
        window.setScene(new Scene(root));
        window.show();
    }
}
