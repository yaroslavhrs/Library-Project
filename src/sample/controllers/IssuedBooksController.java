package sample.controllers;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import sample.confirmBoxes.OKConfirmBox;
import sample.database.DatabaseHandler;
import sample.database.ReaderAccountBookItem;
import java.io.IOException;
import static sample.Main.window;

public class IssuedBooksController {
    public static ObservableList<ReaderAccountBookItem> bookItemsForReport;
    public static String reportCategory;
    public static String reportAuthor;
    public static String reportName;
    public static String reportYear;

    private boolean booksButtonClicked = true;
    private boolean readersButtonClicked = false;

    @FXML
    private TextField authorSearchTextField;

    @FXML
    private TextField nameSearchTextField;

    @FXML
    private TextField yearSearchTextField;

    @FXML
    private CheckBox anyOfCheckBox;

    @FXML
    private CheckBox fullMatchCheckBox;

    @FXML
    private ChoiceBox<String> categoryChoiceBox;

    @FXML
    private Button leftPanelBooksButton;

    @FXML
    private Button leftPanelReadersButton;

    @FXML
    private ImageView searchButton;

    @FXML
    private ImageView backButton;

    @FXML
    private Button getReportButton;

    @FXML
    private TableView<ReaderAccountBookItem> issuedBooksTable;

    @FXML
    private TableColumn<ReaderAccountBookItem,Integer> idColumn;

    @FXML
    private TableColumn<ReaderAccountBookItem, String> authorColumn;

    @FXML
    private TableColumn<ReaderAccountBookItem, String> nameColumn;

    @FXML
    private TableColumn<ReaderAccountBookItem, String> categoryColumn;

    @FXML
    private TableColumn<ReaderAccountBookItem,Integer> yearColumn;

    @FXML
    private TableColumn<ReaderAccountBookItem, String> editionColumn;

    @FXML
    private TableColumn<ReaderAccountBookItem, String> readerIDColumn;

    @FXML
    private TableColumn<ReaderAccountBookItem, String> issueDateColumn;

    @FXML
    private TableColumn<ReaderAccountBookItem, String> deadlineColumn;

    @FXML
    private TableColumn<ReaderAccountBookItem, String> codeColumn;

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
    void initialize() {
        showIssuedBooks();
        getCategories();

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

        // Створення події для кнопки "Отримати звіт"
        getReportButton.setOnMouseClicked(event -> {
            bookItemsForReport = issuedBooksTable.getItems();
            reportCategory = categoryChoiceBox.getValue();
            reportAuthor = authorSearchTextField.getText().trim();
            reportName = nameSearchTextField.getText().trim();
            reportYear = yearSearchTextField.getText().trim();

            setOtherScene("/sample/view/issued_books_report.fxml");
        });

        // Створення події для поля "будь-який з"
        anyOfCheckBox.setOnAction(event -> {
            if (anyOfCheckBox.isSelected() && fullMatchCheckBox.isSelected()) {
                anyOfCheckBox.setSelected(true);
                fullMatchCheckBox.setSelected(false);
            }

            if (!anyOfCheckBox.isSelected() && !fullMatchCheckBox.isSelected()) {
                anyOfCheckBox.setSelected(true);
                fullMatchCheckBox.setSelected(false);
            }
        });

        // Створення події для поля "повне співпадіння"
        fullMatchCheckBox.setOnAction(event -> {
            if (fullMatchCheckBox.isSelected() && anyOfCheckBox.isSelected()) {
                fullMatchCheckBox.setSelected(true);
                anyOfCheckBox.setSelected(false);
            }
            if (!fullMatchCheckBox.isSelected() && !anyOfCheckBox.isSelected()) {
                fullMatchCheckBox.setSelected(true);
                anyOfCheckBox.setSelected(false);
            }
        });

        // Створення події для кнопки пошуку
        searchButton.setOnMouseClicked(event -> {
            String category = "";
            if (categoryChoiceBox.getValue() != null) {
                category = categoryChoiceBox.getValue();
            }
            String author = authorSearchTextField.getText().trim();
            String name = nameSearchTextField.getText().trim();
            boolean anyOf = anyOfCheckBox.isSelected();
            boolean fullMatch = fullMatchCheckBox.isSelected();
            int year = 0;
            if (!yearSearchTextField.getText().trim().equals("")) {
                try {
                    year = Integer.valueOf(yearSearchTextField.getText().trim());
                } catch (NumberFormatException e) {
                    OKConfirmBox.display("Помилка", "Вкажіть значення 'Рік' правильно");
                }
            }

            if (!category.equals("") || !author.equals("") || !name.equals("") || year != 0) {
                DatabaseHandler dbHandler = new DatabaseHandler();
                ObservableList<ReaderAccountBookItem> foundBookItems = dbHandler.searchIssuedCopies(category, author,
                        name, year, anyOf, fullMatch);

                idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
                authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
                nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
                categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
                yearColumn.setCellValueFactory(new PropertyValueFactory<>("year"));
                editionColumn.setCellValueFactory(new PropertyValueFactory<>("edition"));
                readerIDColumn.setCellValueFactory(new PropertyValueFactory<>("reader_id"));
                issueDateColumn.setCellValueFactory(new PropertyValueFactory<>("issue_date"));
                deadlineColumn.setCellValueFactory(new PropertyValueFactory<>("deadline"));
                codeColumn.setCellValueFactory(new PropertyValueFactory<>("code"));

                issuedBooksTable.setItems(foundBookItems);
            } else {
                showIssuedBooks();
            }

        });
    }

    public void showIssuedBooks() {
        DatabaseHandler dbHandler = new DatabaseHandler();
        ObservableList<ReaderAccountBookItem> issuedBookItemsList = dbHandler.getIssuedItemsList();

        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        yearColumn.setCellValueFactory(new PropertyValueFactory<>("year"));
        editionColumn.setCellValueFactory(new PropertyValueFactory<>("edition"));
        readerIDColumn.setCellValueFactory(new PropertyValueFactory<>("reader_id"));
        issueDateColumn.setCellValueFactory(new PropertyValueFactory<>("issue_date"));
        deadlineColumn.setCellValueFactory(new PropertyValueFactory<>("deadline"));
        codeColumn.setCellValueFactory(new PropertyValueFactory<>("code"));

        issuedBooksTable.setItems(issuedBookItemsList);
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
