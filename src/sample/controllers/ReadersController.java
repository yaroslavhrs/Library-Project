package sample.controllers;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import sample.database.DatabaseHandler;
import sample.database.Reader;
import java.io.IOException;
import static sample.Main.*;

public class ReadersController {
    public static Reader selectedReader;
    public static ObservableList<Reader> readersForReport;
    public static String reportName;
    public static String reportAddress;
    public static String reportBirthday;
    public static String reportPhone;

    private boolean booksButtonClicked = false;
    private boolean readersButtonClicked = true;

    @FXML
    private TextField nameSearchTextField;

    @FXML
    private TextField addressSearchTextField;

    @FXML
    private TextField birthdaySearchTextField;

    @FXML
    private TextField phoneSearchTextField;

    @FXML
    private CheckBox anyOfCheckBox;

    @FXML
    private CheckBox fullMatchCheckBox;

    @FXML
    private ImageView searchButton;

    @FXML
    private Button leftPanelBooksButton;

    @FXML
    private Button leftPanelReadersButton;

    @FXML
    private Button addReaderButton;

    @FXML
    private Button getReportButton;

    @FXML
    private TableView<Reader> readersTable;

    @FXML
    private TableColumn<Reader, Integer> idColumn;

    @FXML
    private TableColumn<Reader, String> nameColumn;

    @FXML
    private TableColumn<Reader, String> addressColumn;

    @FXML
    private TableColumn<Reader, String> birthdayColumn;

    @FXML
    private TableColumn<Reader, String> phoneColumn;

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
    void onReaderClicked() {
        selectedReader = readersTable.getSelectionModel().getSelectedItem();
        if (selectedReader != null) {
            setOtherScene("/sample/view/reader_account.fxml");
        }
    }

    @FXML
    void initialize() {
        showReaders();

        // Створення події для кнопки "Додати читача"
        addReaderButton.setOnAction(event -> {
            booksButtonClicked = true;
            setOtherScene("/sample/view/add_reader.fxml");
        });

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

        // Створення події для кнопки "Отримати звіт"
        getReportButton.setOnAction(event -> {
            readersForReport = readersTable.getItems();
            reportName = nameSearchTextField.getText().trim();
            reportAddress = addressSearchTextField.getText().trim();
            reportBirthday = birthdaySearchTextField.getText().trim();
            reportPhone = phoneSearchTextField.getText().trim();
            setOtherScene("/sample/view/readers_report.fxml");
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

        // Створення події для кнопки "Пошук"
        searchButton.setOnMouseClicked(event -> {
            String name = nameSearchTextField.getText().trim();
            String address = addressSearchTextField.getText().trim();
            String birthday = birthdaySearchTextField.getText().trim();
            String phone = phoneSearchTextField.getText().trim();
            boolean anyOf = anyOfCheckBox.isSelected();
            boolean fullMatch = fullMatchCheckBox.isSelected();

            if (!name.equals("") || !address.equals("") || !birthday.equals("") || !phone.equals("")) {
                DatabaseHandler dbHandler = new DatabaseHandler();
                ObservableList<Reader> foundReaders = dbHandler.searchReaders(name,address,birthday,phone,anyOf,fullMatch);

                idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
                nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
                addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
                birthdayColumn.setCellValueFactory(new PropertyValueFactory<>("birthday"));
                phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));

                readersTable.setItems(foundReaders);
            } else {
                showReaders();
            }

        });
    }

    public void showReaders() {
        DatabaseHandler dbHandler = new DatabaseHandler();
        ObservableList<Reader> readersList = dbHandler.getReadersList();
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        birthdayColumn.setCellValueFactory(new PropertyValueFactory<>("birthday"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));

        readersTable.setItems(readersList);
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
