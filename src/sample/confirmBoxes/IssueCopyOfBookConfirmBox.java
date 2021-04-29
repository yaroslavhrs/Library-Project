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
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import sample.database.CopyOfBook;
import sample.database.DatabaseHandler;
import sample.database.Reader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import static sample.Main.window;

public class IssueCopyOfBookConfirmBox {

    public void display(CopyOfBook copyOfBook) {
        Stage popWindow = new Stage();
        popWindow.setTitle("Видача примірника книги");
        popWindow.setMinWidth(500);
        popWindow.setMinHeight(170);
        popWindow.setResizable(false);
        popWindow.getIcons().add(new Image("sample/assets/icon.png"));

        Label label1 = new Label("ID читача:");
        Label label2 = new Label("Термін повернення:");
        label1.setStyle("-fx-font-family: 'Times New Roman'; -fx-font-size: 16; -fx-alignment: 'center'");
        label2.setStyle("-fx-font-family: 'Times New Roman'; -fx-font-size: 16; -fx-alignment: 'center'");

        TextField readerIDTextField = new TextField();
        TextField deadlineTextField = new TextField();

        readerIDTextField.setStyle("-fx-background-color : #ECCFA5; -fx-background-radius : 10 10 10 10;" +
                "-fx-font-family: 'Times New Roman'; -fx-font-size: 15; -fx-alignment: 'center';" +
                "-fx-prompt-text-fill:  derive(-fx-control-inner-background, -30%);");
        deadlineTextField.setStyle("-fx-background-color : #ECCFA5; -fx-background-radius : 10 10 10 10;" +
                "-fx-font-family: 'Times New Roman'; -fx-font-size: 15; -fx-alignment: 'center';" +
                "-fx-prompt-text-fill:  derive(-fx-control-inner-background, -30%);");

        readerIDTextField.setPromptText("ID");
        deadlineTextField.setPromptText("ДД.ММ.РРРР");
        deadlineTextField.setFocusTraversable(false);
        readerIDTextField.setMaxWidth(200);
        readerIDTextField.setPrefHeight(30);
        deadlineTextField.setMaxWidth(200);
        deadlineTextField.setPrefHeight(30);

        Button saveButton = new Button("Зберегти");
        saveButton.setStyle("-fx-background-color : #ECCFA5; -fx-background-radius : 10 10 10 10;" +
                "-fx-font-family: 'Times New Roman'; -fx-font-size: 15;");
        saveButton.setMinWidth(90);
        saveButton.setMinHeight(30);

        saveButton.setOnAction(event -> {
            if (copyOfBook != null && !readerIDTextField.getText().trim().equals("") &&
                    !deadlineTextField.getText().trim().equals("")) {
                DatabaseHandler dbHandler = new DatabaseHandler();
                ObservableList<Reader> readersList = dbHandler.getReadersList();
                int readerID = Integer.valueOf(readerIDTextField.getText().trim());
                String deadline = deadlineTextField.getText().trim();

                SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
                Date deadlineDate = new Date();
                boolean parsed = false;
                try {
                    deadlineDate = sdf.parse(deadline);
                    parsed = true;

                } catch (Exception e) {
                    e.printStackTrace();
                    OKConfirmBox.display("Помилка", "Введіть дату у форматі дд.мм.рррр");
                }

                if (parsed) {
                    Reader chosenReader = null;
                    for (Reader reader : readersList) {
                        if (reader.getId() == readerID) {
                            chosenReader = reader;
                            dbHandler.issueCopyOfBook(copyOfBook, readerID, deadline);
                            popWindow.close();
                            setOtherScene("/sample/view/book_copies.fxml");
                            OKConfirmBox.display("OK", "Примірник успішно видано читачу " + chosenReader.getName() + "!");
                            break;
                        }
                    }

                    if (chosenReader == null) {
                        OKConfirmBox.display("Помилка", "Читача з ID " + readerID + " не знайдено!");
                    }
                }
            }
        });

        VBox layout = new VBox(20);
        HBox innerBox = new HBox(30);

        VBox IDBox = new VBox(10);
        VBox deadlineBox = new VBox(10);

        IDBox.getChildren().addAll(label1, readerIDTextField);
        deadlineBox.getChildren().addAll(label2, deadlineTextField);

        innerBox.getChildren().addAll(IDBox, deadlineBox);
        innerBox.setPrefWidth(500);

        IDBox.setAlignment(Pos.CENTER);
        deadlineBox.setAlignment(Pos.CENTER);
        innerBox.setAlignment(Pos.CENTER);

        layout.getChildren().addAll(innerBox, saveButton);
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
