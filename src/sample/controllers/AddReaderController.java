package sample.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import sample.confirmBoxes.OKConfirmBox;
import sample.database.DatabaseHandler;
import sample.database.Reader;

import java.io.IOException;

import static sample.Main.window;

public class AddReaderController {
    private boolean booksButtonClicked = false;
    private boolean readersButtonClicked = true;
    String style1 = "-fx-background-color: #F6ECD3; -fx-background-radius :  10 0 0 10;";
    String style2 = "-fx-background-color: #ECCFA5; -fx-background-radius :  10 0 0 10;";
    String readersInitViewFileName = "/sample/view/readers_init.fxml";

    @FXML
    private Button saveReaderButton;

    @FXML
    private TextField nameTextField;

    @FXML
    private TextField addressTextField;

    @FXML
    private TextField birthdayTextField;

    @FXML
    private TextField phoneTextField;

    @FXML
    private Button leftPanelBooksButton;

    @FXML
    private Button leftPanelReadersButton;

    @FXML
    private ImageView backButton;

    @FXML
    void onMouseClickedBooks() {
        leftPanelBooksButton.setStyle(style1);
        leftPanelReadersButton.setStyle(style2);
        booksButtonClicked = true;
        readersButtonClicked = false;
    }

    @FXML
    void onMouseClickedReaders() {
        leftPanelReadersButton.setStyle(style1);
        leftPanelBooksButton.setStyle(style2);
        booksButtonClicked = false;
        readersButtonClicked = true;
    }

    @FXML
    void onMouseEnteredBooks() {
        leftPanelBooksButton.setStyle(style1);
    }

    @FXML
    void onMouseExitedBooks() {
        if (!booksButtonClicked) {
            leftPanelBooksButton.setStyle(style2);
        }
    }

    @FXML
    void onMouseEnteredReaders() {
        leftPanelReadersButton.setStyle(style1);
    }

    @FXML
    void onMouseExitedReaders() {
        if (!readersButtonClicked) {
            leftPanelReadersButton.setStyle(style2);
        }
    }

    @FXML
    void initialize() {

        // Створення події для кнопки "Облік книг"
        leftPanelBooksButton.setOnAction(event -> {
            booksButtonClicked = true;
            setOtherScene("/sample/view/books_init.fxml");
        });

        // Створення події для кнопки "Облік читачів"
        leftPanelReadersButton.setOnAction(event -> {
            readersButtonClicked = true;
            setOtherScene(readersInitViewFileName);
        });

        // Створення події для кнопки "Назад"
        backButton.setOnMouseClicked(event -> {
            readersButtonClicked = true;
            setOtherScene(readersInitViewFileName);
        });

        // Створення події для кнопки "Зберегти"
        saveReaderButton.setOnAction(event -> {
            String name = nameTextField.getText().trim();
            String address = addressTextField.getText().trim();
            String birthday = birthdayTextField.getText().trim();
            String phone = phoneTextField.getText().trim();

            if (!name.equals("") && !address.equals("") && !birthday.equals("") &&
                    !phone.equals("")) {
                Reader newReader = new Reader(name, address, birthday, phone);
                DatabaseHandler dbHandler = new DatabaseHandler();
                dbHandler.addReader(newReader);

                readersButtonClicked = true;
                setOtherScene(readersInitViewFileName);
                OKConfirmBox.display("OK", "Читача було успішно додано!");
            } else {
                OKConfirmBox.display("Помилка", "Заповніть всі поля правильно!");
            }
        });
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
