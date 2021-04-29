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
import sample.database.Category;
import sample.database.DatabaseHandler;
import java.io.IOException;
import static sample.Main.window;

public class AddCategoryConfirmBox {

    public void display() {
        Stage popWindow = new Stage();
        popWindow.setTitle("Додавання нового розділу");
        popWindow.setMinWidth(450);
        popWindow.setMinHeight(170);
        popWindow.setResizable(false);
        popWindow.getIcons().add(new Image("sample/assets/icon.png"));
        String style = "-fx-background-color : #ECCFA5; -fx-background-radius : 10 10 10 10;";

        Label label1 = new Label("Назва нового розділу:");
        Label label2 = new Label("Код розділу:");
        label1.setStyle("-fx-font-family: 'Times New Roman'; -fx-font-size: 16; -fx-alignment: 'center'");
        label2.setStyle("-fx-font-family: 'Times New Roman'; -fx-font-size: 16; -fx-alignment: 'center'");

        TextField categoryTextField = new TextField();
        TextField categoryCodeTextField = new TextField();

        Button saveButton = new Button("Зберегти");

        categoryTextField.setStyle(style +
                "-fx-font-family: 'Times New Roman'; -fx-font-size: 15; -fx-alignment: 'center';" +
                "-fx-prompt-text-fill:  derive(-fx-control-inner-background, -30%);");
        categoryCodeTextField.setStyle(style +
                "-fx-font-family: 'Times New Roman'; -fx-font-size: 15; -fx-alignment: 'center';" +
                "-fx-prompt-text-fill:  derive(-fx-control-inner-background, -30%);");
        saveButton.setStyle(style + "-fx-font-family: 'Times New Roman'; -fx-font-size: 15;");

        categoryTextField.setPromptText("Приклад: Фантастика");
        categoryCodeTextField.setPromptText("Приклад: ФА");

        categoryTextField.setMaxWidth(200);
        categoryTextField.setPrefHeight(30);
        categoryCodeTextField.setMaxWidth(200);
        categoryCodeTextField.setPrefHeight(30);
        saveButton.setMinWidth(90);
        saveButton.setMinHeight(30);

        saveButton.setOnAction(event -> {
            if (!categoryTextField.getText().trim().equals("")) {
                String newCategory = categoryTextField.getText().trim();
                String newCategoryCode = categoryCodeTextField.getText().trim();
                DatabaseHandler dbHandler = new DatabaseHandler();
                ObservableList<Category> categoriesList = dbHandler.getCategoriesList();
                boolean isUnique = true;

                for (Category category : categoriesList) {
                    if (category.getCategory().equals(newCategory) || category.getCategory_code().equals(newCategoryCode)) {
                        isUnique = false;
                        break;
                    }
                }

                if (isUnique) {
                    dbHandler.addCategory(newCategory, newCategoryCode);
                    popWindow.close();
                    setOtherScene("/sample/view/books_categories.fxml");
                    OKConfirmBox.display("OK", "Розділ успішно додано!");
                } else {
                    OKConfirmBox.display("Помилка", "Даний розділ або код розділу вже існує!");
                }

            }
        });

        VBox layout = new VBox(20);
        HBox innerBox = new HBox(30);

        VBox categoryBox = new VBox(10);
        VBox categoryCodeBox = new VBox(10);

        categoryBox.getChildren().addAll(label1, categoryTextField);
        categoryCodeBox.getChildren().addAll(label2, categoryCodeTextField);

        innerBox.getChildren().addAll(categoryBox, categoryCodeBox);
        innerBox.setPrefWidth(500);

        categoryBox.setAlignment(Pos.CENTER);
        categoryCodeBox.setAlignment(Pos.CENTER);
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
