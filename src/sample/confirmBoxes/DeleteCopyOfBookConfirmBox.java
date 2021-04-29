package sample.confirmBoxes;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import sample.database.CopyOfBook;
import sample.database.DatabaseHandler;
import java.io.IOException;
import static sample.Main.window;

public class DeleteCopyOfBookConfirmBox {

    public void display(CopyOfBook copyOfBook) {
        Stage popWindow = new Stage();
        popWindow.setTitle("Підтвердження");
        popWindow.setMinWidth(450);
        popWindow.setMinHeight(170);
        popWindow.setResizable(false);
        popWindow.getIcons().add(new Image("sample/assets/icon.png"));

        Label label = new Label();
        label.setText("Ви справді хочете видалити примірник книги " + copyOfBook.getCode() + "?");
        label.setStyle("-fx-font-family: 'Times New Roman'; -fx-font-size: 16;");

        Button yesButton = new Button("Видалити");
        Button noButton = new Button("Скасувати");
        yesButton.setStyle("-fx-background-color : #ECCFA5; -fx-background-radius : 10 10 10 10;" +
                "-fx-font-family: 'Times New Roman'; -fx-font-size: 15;");
        noButton.setStyle("-fx-background-color : #ECCFA5; -fx-background-radius : 10 10 10 10;" +
                "-fx-font-family: 'Times New Roman'; -fx-font-size: 15;");
        yesButton.setMinWidth(70);
        yesButton.setMinHeight(30);
        noButton.setMinWidth(70);
        noButton.setMinHeight(30);

        noButton.setOnAction(event -> popWindow.close());
        yesButton.setOnAction(event -> {
            DatabaseHandler dbHandler = new DatabaseHandler();
            dbHandler.deleteCopyOfBook(copyOfBook);
            popWindow.close();
            setOtherScene("/sample/view/book_copies.fxml");
            OKConfirmBox.display("OK", "Примірник книги було успішно видалено!");
        });

        VBox layout = new VBox(10);
        HBox buttons = new HBox(30);
        buttons.getChildren().addAll(yesButton, noButton);
        buttons.setAlignment(Pos.CENTER);
        layout.getChildren().addAll(label, buttons);
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
