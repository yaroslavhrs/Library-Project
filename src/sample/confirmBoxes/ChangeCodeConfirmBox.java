package sample.confirmBoxes;

import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import sample.database.CopyOfBook;
import sample.database.DatabaseHandler;
import java.io.IOException;
import static sample.Main.window;

public class ChangeCodeConfirmBox {

    public void display(CopyOfBook copyOfBook) {
        Stage popWindow = new Stage();
        popWindow.setTitle("Зміна штрихкоду примірника книги");
        popWindow.setMinWidth(450);
        popWindow.setMinHeight(170);
        popWindow.setResizable(false);
        popWindow.getIcons().add(new Image("sample/assets/icon.png"));

        Label label = new Label("Введіть новий штрихкод примірника:");
        TextField codeTextField = new TextField();
        Button saveButton = new Button("Зберегти");

        label.setStyle("-fx-font-family: 'Times New Roman'; -fx-font-size: 16; -fx-alignment: 'center'");
        codeTextField.setStyle("-fx-background-color : #ECCFA5; -fx-background-radius : 10 10 10 10;" +
                "-fx-font-family: 'Times New Roman'; -fx-font-size: 15; -fx-alignment: 'center'");
        saveButton.setStyle("-fx-background-color : #ECCFA5; -fx-background-radius : 10 10 10 10;" +
                "-fx-font-family: 'Times New Roman'; -fx-font-size: 15;");
        codeTextField.setMaxWidth(200);
        codeTextField.setPrefHeight(30);
        saveButton.setMinWidth(90);
        saveButton.setMinHeight(30);

        saveButton.setOnAction(event -> {
            String code = codeTextField.getText().trim();
            DatabaseHandler dbHandler = new DatabaseHandler();
            ObservableList<CopyOfBook> allCopiesList = dbHandler.getAllCopiesList();
            if (copyOfBook != null && !code.equals("")) {
                boolean isUnique = true;
                for (CopyOfBook copy : allCopiesList) {
                    if (copy.getCode().equals(code)) {
                        isUnique = false;
                        break;
                    }
                }
                if (isUnique) {
                    dbHandler.changeCode(copyOfBook, code);
                    popWindow.close();
                    setOtherScene("/sample/view/book_copies.fxml");
                    OKConfirmBox.display("OK", "Штрихкод примірника успішно змінено!");
                } else {
                    OKConfirmBox.display("Помилка", "Примірник з даним штрихкодом вже існує!");
                }
            }
        });

        VBox layout = new VBox(20);
        layout.getChildren().addAll(label, codeTextField ,saveButton);
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
