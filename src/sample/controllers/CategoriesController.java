package sample.controllers;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import sample.confirmBoxes.AddCategoryConfirmBox;
import sample.confirmBoxes.DeleteCategoryConfirmBox;
import sample.confirmBoxes.OKConfirmBox;
import sample.database.Category;
import sample.database.DatabaseHandler;
import java.io.IOException;
import static sample.Main.window;

public class CategoriesController {
    private boolean booksButtonClicked = true;
    private boolean readersButtonClicked = false;

    public static Category selectedCategory;

    @FXML
    private Button leftPanelBooksButton;

    @FXML
    private Button leftPanelReadersButton;

    @FXML
    private Button addCategoryButton;

    @FXML
    private Button deleteCategoryButton;

    @FXML
    private ImageView backButton;

    @FXML
    private TableView<Category> categoriesTable;

    @FXML
    private TableColumn<Category, Integer> idColumn;

    @FXML
    private TableColumn<Category, String> categoryColumn;

    @FXML
    private TableColumn<Category, String> categoryCodeColumn;

    @FXML
    void onMouseClickedBooks() {
        leftPanelBooksButton.setStyle("-fx-background-color: #F6ECD3; -fx-background-radius :  10 0 0 10;");
        leftPanelReadersButton.setStyle("-fx-background-color: #ECCFA5; -fx-background-radius :  10 0 0 10;");
        booksButtonClicked = true;
        readersButtonClicked = false;
    }

    @FXML
    void onMouseClickedReaders() {
        leftPanelReadersButton.setStyle("-fx-background-color: #F6ECD3; -fx-background-radius :  10 0 0 10;");
        leftPanelBooksButton.setStyle("-fx-background-color: #ECCFA5; -fx-background-radius :  10 0 0 10;");
        booksButtonClicked = false;
        readersButtonClicked = true;
    }

    @FXML
    void onMouseEnteredBooks() {
        leftPanelBooksButton.setStyle("-fx-background-color: #F6ECD3; -fx-background-radius :  10 0 0 10;");
    }

    @FXML
    void onMouseExitedBooks() {
        if (!booksButtonClicked) {
            leftPanelBooksButton.setStyle("-fx-background-color: #ECCFA5; -fx-background-radius :  10 0 0 10;");
        }
    }

    @FXML
    void onMouseEnteredReaders() {
        leftPanelReadersButton.setStyle("-fx-background-color: #F6ECD3; -fx-background-radius :  10 0 0 10;");
    }

    @FXML
    void onMouseExitedReaders() {
        if (!readersButtonClicked) {
            leftPanelReadersButton.setStyle("-fx-background-color: #ECCFA5; -fx-background-radius :  10 0 0 10;");
        }
    }

    @FXML
    void onCategoryClicked() {
        selectedCategory = categoriesTable.getSelectionModel().getSelectedItem();
    }

    @FXML
    void initialize() {
        showCategories();

        // Створення події для кнопки "Облік книг"
        leftPanelBooksButton.setOnAction(event -> {
            booksButtonClicked = true;
            setOtherScene("/sample/view/books_init.fxml");
        });

        // Створення події для кнопки "Облік читачів"
        leftPanelReadersButton.setOnAction(event -> {
            readersButtonClicked = true;
            setOtherScene("/sample/view/readers_init.fxml");
        });

        // Створення події для кнопки "Назад"
        backButton.setOnMouseClicked(event -> {
            booksButtonClicked = true;
            setOtherScene("/sample/view/books_init.fxml");
        });

        // Створення події для кнопки "Додати розділ"
        addCategoryButton.setOnAction(event -> {
            AddCategoryConfirmBox confirmBox = new AddCategoryConfirmBox();
            confirmBox.display();
        });

        // Створення події для кнопки "Видалити розділ"
        deleteCategoryButton.setOnAction(event -> {
            if (selectedCategory != null) {
                if (selectedCategory.getId() != 1) {
                    DeleteCategoryConfirmBox confirmBox = new DeleteCategoryConfirmBox();
                    confirmBox.display(selectedCategory);
                } else {
                    OKConfirmBox.display("Помилка", "Не можна видалити даний розділ!");
                }
            }
        });
    }

    public void showCategories() {
        DatabaseHandler dbHandler = new DatabaseHandler();
        ObservableList<Category> categoriesList = dbHandler.getCategoriesList();
        idColumn.setCellValueFactory(new PropertyValueFactory<>("visual_id"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        categoryCodeColumn.setCellValueFactory(new PropertyValueFactory<>("category_code"));
        categoriesTable.setItems(categoriesList);
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
