package sample.controllers;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import sample.confirmBoxes.OKConfirmBox;
import sample.database.Book;
import sample.database.DatabaseHandler;
import java.io.IOException;
import static sample.Main.*;

public class AddBookController {
    private boolean booksButtonClicked = true;
    private boolean readersButtonClicked = false;
    private String highlightedStyle = "-fx-background-color: #F6ECD3; -fx-background-radius :  10 0 0 10;";
    private String normalStyle = "-fx-background-color: #ECCFA5; -fx-background-radius :  10 0 0 10;";
    private String bookInitViewFileName = "/sample/view/books_init.fxml";

    @FXML
    private TextField authorTextField;

    @FXML
    private TextField nameTextField;

    @FXML
    private ChoiceBox<String> categoryChoiceBox;

    @FXML
    private TextField yearTextField;

    @FXML
    private TextField editionTextField;

    @FXML
    private Button saveBookButton;

    @FXML
    private ImageView backButton;

    @FXML
    private Button leftPanelBooksButton;

    @FXML
    private Button leftPanelReadersButton;

    @FXML
    void onMouseClickedBooks() {
        leftPanelBooksButton.setStyle(highlightedStyle);
        leftPanelReadersButton.setStyle(normalStyle);
        booksButtonClicked = true;
        readersButtonClicked = false;
    }

    @FXML
    void onMouseClickedReaders() {
        leftPanelReadersButton.setStyle(highlightedStyle);
        leftPanelBooksButton.setStyle(normalStyle);
        booksButtonClicked = false;
        readersButtonClicked = true;
    }

    @FXML
    void onMouseEnteredBooks() {
        leftPanelBooksButton.setStyle(highlightedStyle);
    }

    @FXML
    void onMouseExitedBooks() {
        if (!booksButtonClicked) {
            leftPanelBooksButton.setStyle(normalStyle);
        }
    }

    @FXML
    void onMouseEnteredReaders() {
        leftPanelReadersButton.setStyle(highlightedStyle);
    }

    @FXML
    void onMouseExitedReaders() {
        if (!readersButtonClicked) {
            leftPanelReadersButton.setStyle(normalStyle);
        }
    }

    @FXML
    void initialize() {
        getCategories();

        // Створення події для кнопки "Облік книг"
        leftPanelBooksButton.setOnAction(event -> {
            booksButtonClicked = true;
            setOtherScene(bookInitViewFileName);
        });

        // Створення події для кнопки "Облік читачів"
        leftPanelReadersButton.setOnAction(event -> {
            readersButtonClicked = true;
            setOtherScene("/sample/view/readers_init.fxml");
        });

        // Створення події для кнопки "Назад"
        backButton.setOnMouseClicked(event -> {
            booksButtonClicked = true;
            setOtherScene(bookInitViewFileName);
        });

        // Створення події для кнопки "Зберегти"
        saveBookButton.setOnAction(event -> {
            String author = authorTextField.getText().trim();
            String name = nameTextField.getText().trim();
            String category = categoryChoiceBox.getValue();
            String edition = editionTextField.getText().trim();
            int year = 0;
            try {
                year = Integer.parseInt(yearTextField.getText().trim());
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }

            if (!author.equals("") && !name.equals("") && !category.equals("") &&
                    !edition.equals("") && year != 0) {
                Book newBook = new Book(author, name, category, year, edition);
                DatabaseHandler dbHandler = new DatabaseHandler();
                dbHandler.addBook(newBook);

                booksButtonClicked = true;
                setOtherScene(bookInitViewFileName);
                OKConfirmBox.display("OK", "Книга була успішно додана!");
            }
            else {
                OKConfirmBox.display("Помилка", "Заповніть всі поля правильно!");
            }
        });
    }

    public void getCategories() {
        DatabaseHandler dbHandler = new DatabaseHandler();
        ObservableList<String> categoriesList = dbHandler.getCategoriesStringList();
        categoryChoiceBox.setItems(categoriesList);
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
