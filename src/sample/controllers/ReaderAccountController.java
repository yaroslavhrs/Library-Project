package sample.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import sample.confirmBoxes.DeleteReaderConfirmBox;
import sample.confirmBoxes.OKConfirmBox;
import sample.confirmBoxes.ReturnBookConfirmBox;
import sample.database.*;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import static sample.Main.window;
import static sample.controllers.ReadersController.selectedReader;

public class ReaderAccountController {
    public static ReaderAccountBookItem selectedBookItem;
    public static ObservableList<ReaderAccountBookItem> bookItemsForReport;
    public static String reportAuthor;
    public static String reportName;
    public static String reportYear;
    public static String reportCode;
    public static boolean readingNowButtonClicked = true;
    public static boolean lateBooksButtonClicked = false;
    public static boolean allBooksButtonClicked = false;

    private boolean booksButtonClicked = false;
    private boolean readersButtonClicked = true;

    @FXML
    private TextField authorSearchTextField;

    @FXML
    private TextField codeSearchTextField;

    @FXML
    private TextField yearSearchTextField;

    @FXML
    private TextField nameSearchTextField;

    @FXML
    private ImageView searchButton;

    @FXML
    private CheckBox anyOfCheckBox;

    @FXML
    private CheckBox fullMatchCheckBox;

    @FXML
    private TableView<ReaderAccountBookItem> readerAccountTable;

    @FXML
    private TableColumn<ReaderAccountBookItem, Integer> idColumn;

    @FXML
    private TableColumn<ReaderAccountBookItem, String> authorColumn;

    @FXML
    private TableColumn<ReaderAccountBookItem, String> nameColumn;

    @FXML
    private TableColumn<ReaderAccountBookItem, String> categoryColumn;

    @FXML
    private TableColumn<ReaderAccountBookItem, Integer> yearColumn;

    @FXML
    private TableColumn<ReaderAccountBookItem, String> editionColumn;

    @FXML
    private TableColumn<ReaderAccountBookItem, String> issueDateColumn;

    @FXML
    private TableColumn<ReaderAccountBookItem, String> deadlineColumn;

    @FXML
    private TableColumn<ReaderAccountBookItem, String> codeColumn;

    @FXML
    private ImageView backButton;

    @FXML
    private Button editReaderButton;

    @FXML
    private Button saveReaderButton;

    @FXML
    private Button deleteReaderButton;

    @FXML
    private Button readingNowButton;

    @FXML
    private Button lateBooksButton;

    @FXML
    private Button allBooksButton;

    @FXML
    private Button returnBookButton;

    @FXML
    private Button getReportButton;

    @FXML
    private TextField accountNumberTextField;

    @FXML
    private TextField nameTextField;

    @FXML
    private TextField addressTextField;

    @FXML
    private TextField birthdayTextField;

    @FXML
    private TextField phoneTextField;

    @FXML
    private Button leftPanelReadersButton;

    @FXML
    private Button leftPanelBooksButton;

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
    void onBookItemClicked() {
        selectedBookItem = readerAccountTable.getSelectionModel().getSelectedItem();
    }

    @FXML
    void initialize() {
        selectedBookItem = null;
        accountNumberTextField.setText(String.valueOf(selectedReader.getId()));
        nameTextField.setText(selectedReader.getName());
        addressTextField.setText(selectedReader.getAddress());
        birthdayTextField.setText(selectedReader.getBirthday());
        phoneTextField.setText(selectedReader.getPhone());

        showReadingNowBooks();

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
            setOtherScene("/sample/view/readers_init.fxml");
        });

        // Створення події для кнопки "Редагувати"
        editReaderButton.setOnAction(event -> {
            nameTextField.setEditable(true);
            addressTextField.setEditable(true);
            birthdayTextField.setEditable(true);
            phoneTextField.setEditable(true);

            nameTextField.setStyle("-fx-border-color: #1A73E8; -fx-border-radius: 5 5 5 5;");
            addressTextField.setStyle("-fx-border-color: #1A73E8; -fx-border-radius: 5 5 5 5;");
            birthdayTextField.setStyle("-fx-border-color: #1A73E8; -fx-border-radius: 5 5 5 5;" +
                    "-fx-prompt-text-fill:  derive(-fx-control-inner-background, -30%);");
            phoneTextField.setStyle("-fx-border-color: #1A73E8; -fx-border-radius: 5 5 5 5;" +
                    "-fx-prompt-text-fill:  derive(-fx-control-inner-background, -30%);");

            birthdayTextField.setPromptText("ДД.ММ.РРРР");
            phoneTextField.setPromptText("0ХХ-ХХХ-ХХХХ");

            editReaderButton.setDisable(true);
            saveReaderButton.setDisable(false);
        });

        // Створення події для кнопки "Зберегти"
        saveReaderButton.setOnAction(event -> {
            String name = nameTextField.getText().trim();
            String address = addressTextField.getText().trim();
            String birthday = birthdayTextField.getText().trim();
            String phone = phoneTextField.getText().trim();

            if (!name.equals("") && !address.equals("") && !birthday.equals("") && !phone.equals("")) {
                Reader editedReader = new Reader(name, address, birthday, phone);
                editedReader.setId(selectedReader.getId());
                selectedReader = editedReader;
                DatabaseHandler dbHandler = new DatabaseHandler();
                dbHandler.editReader(editedReader);
                setOtherScene("/sample/view/reader_account.fxml");
            } else {
                OKConfirmBox.display("Помилка","Заповніть всі поля!");
            }
        });

        // Створення події для кнопки "Видалити"
        deleteReaderButton.setOnAction(event -> {
            DatabaseHandler dbHandler = new DatabaseHandler();
            ObservableList<CopyOfBook> readingCopiesList = dbHandler.getReadingNowCopies(selectedReader.getId());
            if (readingCopiesList.size() == 0) {
                DeleteReaderConfirmBox deleteReader = new DeleteReaderConfirmBox();
                deleteReader.display("Підтвердження", "Ви справді хочете видалити читача?", selectedReader);
            } else {
                OKConfirmBox.display("Помилка", "Неможливо видалити читача. У читача є книги на руках!");
            }

        });

        // Створення події для кнопки "Книги на руках"
        readingNowButton.setOnAction(event -> {
            clearStylesheets();
            readingNowButton.getStylesheets().add("/sample/css/readerAccountButtonStyle1.css");
            lateBooksButton.getStylesheets().add("/sample/css/readerAccountButtonStyle2.css");
            allBooksButton.getStylesheets().add("/sample/css/readerAccountButtonStyle2.css");
            readingNowButtonClicked = true;
            lateBooksButtonClicked = false;
            allBooksButtonClicked = false;
            clearSearchLine();
            showReadingNowBooks();
        });

        // Створення події для кнопки "Просрочені книги"
        lateBooksButton.setOnAction(event -> {
            clearStylesheets();
            readingNowButton.getStylesheets().add("/sample/css/readerAccountButtonStyle2.css");
            lateBooksButton.getStylesheets().add("/sample/css/readerAccountButtonStyle1.css");
            allBooksButton.getStylesheets().add("/sample/css/readerAccountButtonStyle2.css");
            readingNowButtonClicked = false;
            lateBooksButtonClicked = true;
            allBooksButtonClicked = false;
            clearSearchLine();
            showLateBooks();
        });

        // Створення події для кнопки "Всі книги"
        allBooksButton.setOnAction(event -> {
            clearStylesheets();
            readingNowButton.getStylesheets().add("/sample/css/readerAccountButtonStyle2.css");
            lateBooksButton.getStylesheets().add("/sample/css/readerAccountButtonStyle2.css");
            allBooksButton.getStylesheets().add("/sample/css/readerAccountButtonStyle1.css");
            readingNowButtonClicked = false;
            lateBooksButtonClicked = false;
            allBooksButtonClicked = true;
            clearSearchLine();
            showAllBooks();
        });

        // Створення події для кнопки "Повернути книгу"
        returnBookButton.setOnAction(event -> {
            if (selectedBookItem != null && readingNowButtonClicked) {
                ReturnBookConfirmBox confirmBox = new ReturnBookConfirmBox();
                confirmBox.display(selectedBookItem, selectedReader.getId());
                selectedBookItem = null;
            }
        });

        // Створення події для кнопки "Отримати звіт"
        getReportButton.setOnMouseClicked(event -> {
            bookItemsForReport = readerAccountTable.getItems();
            reportAuthor = authorSearchTextField.getText().trim();
            reportName = nameSearchTextField.getText().trim();
            reportYear = yearSearchTextField.getText().trim();
            reportCode = codeSearchTextField.getText().trim();
            setOtherScene("/sample/view/reader_account_report.fxml");
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
            DatabaseHandler dbHandler = new DatabaseHandler();
            String author = authorSearchTextField.getText().trim();
            String name = nameSearchTextField.getText().trim();
            String code = codeSearchTextField.getText().trim();
            int year = 0;
            if (!yearSearchTextField.getText().trim().equals("")) {
                try {
                    year = Integer.valueOf(yearSearchTextField.getText().trim());
                } catch (NumberFormatException e) {
                    OKConfirmBox.display("Помилка", "Введіть значення 'Рік' правильно!");
                }
            }

            idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
            authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
            nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
            categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
            yearColumn.setCellValueFactory(new PropertyValueFactory<>("year"));
            editionColumn.setCellValueFactory(new PropertyValueFactory<>("edition"));
            codeColumn.setCellValueFactory(new PropertyValueFactory<>("code"));
            issueDateColumn.setCellValueFactory(new PropertyValueFactory<>("issue_date"));
            deadlineColumn.setCellValueFactory(new PropertyValueFactory<>("deadline"));

            if (readingNowButtonClicked || lateBooksButtonClicked) {
                if (!author.equals("") || !name.equals("") || year != 0 || !code.equals("")) {
                    ObservableList<ReaderAccountBookItem> foundReadingNowItemsList = dbHandler.searchReadingNowItemsList(
                            selectedReader.getId(), author, name, year, code, anyOfCheckBox.isSelected(), fullMatchCheckBox.isSelected());

                    if (readingNowButtonClicked) {
                        readerAccountTable.setItems(foundReadingNowItemsList);
                    }

                    if (lateBooksButtonClicked) {
                        ObservableList<ReaderAccountBookItem> foundLateItemsList = FXCollections.observableArrayList();
                        Date todayDate = new Date();
                        for (ReaderAccountBookItem item : foundReadingNowItemsList) {
                            String deadline = item.getDeadline();
                            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
                            Date deadlineDate = new Date();
                            try {
                                deadlineDate = sdf.parse(deadline);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                            int daysDifference = calculateDaysDiff(todayDate, deadlineDate);

                            if (daysDifference < 0) {
                                foundLateItemsList.add(item);
                            }
                        }
                        readerAccountTable.setItems(foundLateItemsList);
                    }

                } else {
                    if (readingNowButtonClicked) {
                        showReadingNowBooks();
                    }
                    if (lateBooksButtonClicked) {
                        showLateBooks();
                    }
                }
            } else {
                if (!author.equals("") || !name.equals("") || year != 0 || !code.equals("")) {
                    ObservableList<ReaderAccountBookItem> foundHistoryItemsList = dbHandler.searchHistoryItemsList(
                            selectedReader.getId(), author, name, year, code, anyOfCheckBox.isSelected(), fullMatchCheckBox.isSelected());

                    readerAccountTable.setItems(foundHistoryItemsList);
                } else {
                    showAllBooks();
                }
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

    public void showReadingNowBooks() {
        DatabaseHandler dbHandler = new DatabaseHandler();
        ObservableList<ReaderAccountBookItem> readingNowItemsList = dbHandler.getReadingNowItemsList(selectedReader.getId());

        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        yearColumn.setCellValueFactory(new PropertyValueFactory<>("year"));
        editionColumn.setCellValueFactory(new PropertyValueFactory<>("edition"));
        codeColumn.setCellValueFactory(new PropertyValueFactory<>("code"));
        issueDateColumn.setCellValueFactory(new PropertyValueFactory<>("issue_date"));
        deadlineColumn.setCellValueFactory(new PropertyValueFactory<>("deadline"));

        readerAccountTable.setItems(readingNowItemsList);
    }

    public void showLateBooks() {
        DatabaseHandler dbHandler = new DatabaseHandler();
        ObservableList<ReaderAccountBookItem> readingNowItemsList = dbHandler.getReadingNowItemsList(selectedReader.getId());
        ObservableList<ReaderAccountBookItem> lateItemsList = FXCollections.observableArrayList();
        Date todayDate = new Date();
        for (ReaderAccountBookItem item : readingNowItemsList) {
            String deadline = item.getDeadline();
            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
            Date deadlineDate = new Date();
            try {
                deadlineDate = sdf.parse(deadline);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            int daysDifference = calculateDaysDiff(todayDate, deadlineDate);

            if (daysDifference < 0) {
                lateItemsList.add(item);
            }
        }

        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        yearColumn.setCellValueFactory(new PropertyValueFactory<>("year"));
        editionColumn.setCellValueFactory(new PropertyValueFactory<>("edition"));
        codeColumn.setCellValueFactory(new PropertyValueFactory<>("code"));
        issueDateColumn.setCellValueFactory(new PropertyValueFactory<>("issue_date"));
        deadlineColumn.setCellValueFactory(new PropertyValueFactory<>("deadline"));

        readerAccountTable.setItems(lateItemsList);
    }

    public int calculateDaysDiff(Date today, Date deadline) {
        return (int) ((deadline.getTime() - today.getTime()) / (1000 * 60 * 60 * 24));
    }

    public void showAllBooks() {
        DatabaseHandler dbHandler = new DatabaseHandler();
        ObservableList<ReaderAccountBookItem> allBookItemsList = dbHandler.getHistoryItemsList(selectedReader.getId());

        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        yearColumn.setCellValueFactory(new PropertyValueFactory<>("year"));
        editionColumn.setCellValueFactory(new PropertyValueFactory<>("edition"));
        codeColumn.setCellValueFactory(new PropertyValueFactory<>("code"));
        issueDateColumn.setCellValueFactory(new PropertyValueFactory<>("issue_date"));
        deadlineColumn.setCellValueFactory(new PropertyValueFactory<>("deadline"));

        readerAccountTable.setItems(allBookItemsList);
    }

    public void clearSearchLine() {
        authorSearchTextField.clear();
        nameSearchTextField.clear();
        yearSearchTextField.clear();
        codeSearchTextField.clear();
    }

    public void clearStylesheets() {
        readingNowButton.getStylesheets().clear();
        lateBooksButton.getStylesheets().clear();
        allBooksButton.getStylesheets().clear();
    }

}
