package sample.controllers;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import sample.confirmBoxes.*;
import sample.database.*;

import java.io.IOException;

import static sample.controllers.BooksController.selectedBook;
import static sample.Main.window;
import static sample.database.Status.*;

public class BookCopiesController {
    public static CopyOfBook selectedCopyOfBook;
    public static ObservableList<CopyOfBook> bookCopiesForReport;
    public static String reportStatus;
    public static String reportCode;
    public static String reportReaderID;
    public static String numOfCopiesReport;
    public static String availableCopiesReport;
    private boolean booksButtonClicked = true;
    private boolean readersButtonClicked = false;
    private String style1 = "-fx-background-color: #F6ECD3; -fx-background-radius :  10 0 0 10;";
    private String style2 = "-fx-background-color: #ECCFA5; -fx-background-radius :  10 0 0 10;";
    String errorMessage = "Помилка";
    String issuedMessage = "Видано";
    String notIssuedMessage = "Не видано";


    @FXML
    private ChoiceBox< String > statusSearchChoiceBox;

    @FXML
    private TextField codeSearchTextField;

    @FXML
    private TextField readerIDSearchTextField;

    @FXML
    private ImageView searchButton;

    @FXML
    private CheckBox anyOfCheckBox;

    @FXML
    private CheckBox fullMatchCheckBox;

    @FXML
    private ImageView backButton;

    @FXML
    private Button editBookButton;

    @FXML
    private Button deleteBookButton;

    @FXML
    private Button issueCopyOfBookButton;

    @FXML
    private Button changeStatusButton;

    @FXML
    private Button changeCodeButton;

    @FXML
    private Button changeDeadlineButton;

    @FXML
    private Button deleteCopyOfBookButton;

    @FXML
    private Button getReportButton;

    @FXML
    private TextField bookNumberTextField;

    @FXML
    private TextField bookAuthorTextField;

    @FXML
    private TextField bookNameTextField;

    @FXML
    private TextField bookCategoryTextField;

    @FXML
    private TextField bookYearTextField;

    @FXML
    private ChoiceBox< String > bookCategoryChoiceBox;

    @FXML
    private TableView< CopyOfBook > copyOfBooksTable;

    @FXML
    private TableColumn< CopyOfBook, Integer > idColumn;

    @FXML
    private TableColumn< CopyOfBook, String > statusColumn;

    @FXML
    private TableColumn< CopyOfBook, String > codeColumn;

    @FXML
    private TableColumn< CopyOfBook, Integer > readerIDColumn;

    @FXML
    private TableColumn< CopyOfBook, String > issueDateColumn;

    @FXML
    private TableColumn< CopyOfBook, String > deadlineColumn;

    @FXML
    private Button saveBookButton;

    @FXML
    private TextField bookEditionTextField;

    @FXML
    private TextField numOfCopiesTextField;

    @FXML
    private TextField availableCopiesTextField;

    @FXML
    private Button addNewBookCopyButton;

    @FXML
    private Button leftPanelReadersButton;

    @FXML
    private Button leftPanelBooksButton;

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
    void onCopyOfBookClicked() {
        selectedCopyOfBook = copyOfBooksTable.getSelectionModel().getSelectedItem();
    }

    @FXML
    void initialize() {
        selectedCopyOfBook = null;
        showCopyOfBooks();
        getCategories();

        bookNumberTextField.setText(String.valueOf(selectedBook.getId()));
        bookAuthorTextField.setText(String.valueOf(selectedBook.getAuthor()));
        bookNameTextField.setText(String.valueOf(selectedBook.getName()));
        bookCategoryTextField.setText(String.valueOf(selectedBook.getCategory()));
        bookYearTextField.setText(String.valueOf(selectedBook.getYear()));
        bookEditionTextField.setText(String.valueOf(selectedBook.getEdition()));

        statusSearchChoiceBox.setItems(statusSearchList);

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

        // Створення події для кнопки "Редагувати"
        editBookButton.setOnAction(event -> {
            bookAuthorTextField.setEditable(true);
            bookNameTextField.setEditable(true);
            bookCategoryChoiceBox.setDisable(false);
            bookYearTextField.setEditable(true);
            bookEditionTextField.setEditable(true);
            String fieldStyle = "-fx-border-color: #1A73E8; -fx-border-radius: 5 5 5 5;";
            bookAuthorTextField.setStyle(fieldStyle);
            bookNameTextField.setStyle(fieldStyle);
            bookCategoryChoiceBox.setStyle(fieldStyle + "-fx-background-color: white;");
            bookYearTextField.setStyle(fieldStyle);
            bookEditionTextField.setStyle(fieldStyle);

            editBookButton.setDisable(true);
            saveBookButton.setDisable(false);
        });

        // Створення події для кнопки "Зберегти"
        saveBookButton.setOnAction(event -> {
            String editedCategory = bookCategoryChoiceBox.getValue();
            String editedAuthor = bookAuthorTextField.getText().trim();
            String editedName = bookNameTextField.getText().trim();
            String editedEdition = bookEditionTextField.getText().trim();

            if (editedCategory == null || editedCategory.equals("")) {
                editedCategory = selectedBook.getCategory();
            }
            if (!editedCategory.equals("") && !editedAuthor.equals("") && !editedName.equals("") && !editedEdition.equals("")) {
                try {
                    int editedYear = Integer.valueOf(bookYearTextField.getText().trim());

                    Book editedBook = new Book(editedAuthor, editedName, editedCategory, editedYear, editedEdition);
                    editedBook.setId(selectedBook.getId());
                    selectedBook = editedBook;
                    DatabaseHandler dbHandler = new DatabaseHandler();
                    dbHandler.editBook(editedBook);
                    setOtherScene("/sample/view/book_copies.fxml");
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    OKConfirmBox.display(errorMessage, "Введіть правильно значення 'Рік'");
                }
            } else {
                OKConfirmBox.display(errorMessage, "Заповніть всі поля!");
            }

        });

        // Створення події для кнопки "Видалити"
        deleteBookButton.setOnAction(event -> {
            if (selectedBook != null) {
                DatabaseHandler dbHandler = new DatabaseHandler();
                ObservableList< CopyOfBook > copyOfBooksList = dbHandler.getCopyOfBooksList(selectedBook);
                boolean issuedBooks = false;

                for (CopyOfBook copy : copyOfBooksList) {
                    if (copy.getStatus().equals(issuedMessage)) {
                        issuedBooks = true;
                        break;
                    }
                }

                if (!issuedBooks) {
                    DeleteBookConfirmBox deleteBook = new DeleteBookConfirmBox();
                    deleteBook.display("Підтвердження", "Ви справді хочете видалити книгу та всі її примірники?", selectedBook);
                } else {
                    OKConfirmBox.display("Повідомлення", "Неможливо видалити книгу. Є видані примірники.");
                }
            }
        });

        // Створення події для кнопки "Додати примірник"
        addNewBookCopyButton.setOnAction(event -> {
            DatabaseHandler dbHandler = new DatabaseHandler();
            String category = selectedBook.getCategory();
            String categoryCode = dbHandler.getCategoryCode(category);
            String bookID = String.valueOf(selectedBook.getId());
            String copyNumber = "";
            String resultCode = "";
            ObservableList< CopyOfBook > allCopiesList = dbHandler.getAllCopiesList();
            boolean isUnique = false;
            int k = 1;

            OUTER:
            while (!isUnique) {
                copyNumber = String.valueOf((Integer.valueOf(numOfCopiesReport) + k));
                resultCode = categoryCode + "." + bookID + "." + copyNumber;
                for (CopyOfBook copy : allCopiesList) {
                    if (copy.getCode().equals(resultCode)) {
                        k += 1;
                        continue OUTER;
                    }
                }
                isUnique = true;
            }

            if (!categoryCode.equals("") && isUnique) {
                CopyOfBook newCopyOfBook = new CopyOfBook(selectedBook.getId(), notIssuedMessage, resultCode, 0, "-", "-");
                dbHandler.addCopyOfBook(newCopyOfBook);
                setOtherScene("/sample/view/book_copies.fxml");
                OKConfirmBox.display("OK", "Примірник книги " + resultCode + " успішно додано!");
            } else {
                OKConfirmBox.display(errorMessage, "Відсутній розділ у таблиці розділів книг!");
            }
        });

        // Створення події для кнопки "Змінити статус"
        changeStatusButton.setOnAction(event -> {
            if (selectedCopyOfBook != null && !selectedCopyOfBook.getStatus().equals(issuedMessage)) {
                ChangeStatusConfirmBox confirmBox = new ChangeStatusConfirmBox();
                confirmBox.display(selectedCopyOfBook);
                selectedCopyOfBook = null;
            }
        });

        // Створення події для кнопки "Видалити примірник"
        deleteCopyOfBookButton.setOnAction(event -> {
            if (selectedCopyOfBook != null && !selectedCopyOfBook.getStatus().equals(issuedMessage)) {
                DeleteCopyOfBookConfirmBox confirmBox = new DeleteCopyOfBookConfirmBox();
                confirmBox.display(selectedCopyOfBook);
                selectedCopyOfBook = null;
            }
        });

        // Створення події для кнопки "Змінити штрихкод"
        changeCodeButton.setOnAction(event -> {
            if (selectedCopyOfBook != null) {
                ChangeCodeConfirmBox confirmBox = new ChangeCodeConfirmBox();
                confirmBox.display(selectedCopyOfBook);
                selectedCopyOfBook = null;
            }
        });

        // Створення події для кнопки "Видати примірник"
        issueCopyOfBookButton.setOnAction(event -> {
            if (selectedCopyOfBook != null && selectedCopyOfBook.getStatus().equals(notIssuedMessage)) {
                IssueCopyOfBookConfirmBox confirmBox = new IssueCopyOfBookConfirmBox();
                confirmBox.display(selectedCopyOfBook);
                selectedCopyOfBook = null;
            }
        });

        // Створення події для кнопки "Змінити термін"
        changeDeadlineButton.setOnAction(event -> {
            if (selectedCopyOfBook != null && selectedCopyOfBook.getStatus().equals(issuedMessage)) {
                ChangeDeadlineConfirmBox confirmBox = new ChangeDeadlineConfirmBox();
                confirmBox.display(selectedCopyOfBook);
                selectedCopyOfBook = null;
            }
        });

        // Створення події для кнопки "Отримати звіт"
        getReportButton.setOnMouseClicked(event -> {
            bookCopiesForReport = copyOfBooksTable.getItems();
            reportStatus = statusSearchChoiceBox.getValue();
            reportCode = codeSearchTextField.getText().trim();
            reportReaderID = readerIDSearchTextField.getText().trim();
            setOtherScene("/sample/view/book_copies_report.fxml");
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
            String status = "";
            if (statusSearchChoiceBox.getValue() != null && !statusSearchChoiceBox.getValue().equals("")) {
                status = statusSearchChoiceBox.getValue();
            }
            String code = codeSearchTextField.getText().trim();
            boolean anyOf = anyOfCheckBox.isSelected();
            boolean fullMatch = fullMatchCheckBox.isSelected();
            int readerID = 0;
            if (!readerIDSearchTextField.getText().trim().equals("")) {
                try {
                    readerID = Integer.valueOf(readerIDSearchTextField.getText().trim());
                } catch (NumberFormatException e) {
                    OKConfirmBox.display(errorMessage, "Вкажіть значення 'Кому видано (ID)' правильно");
                }
            }

            if (!status.equals("") || !code.equals("") || readerID != 0) {
                DatabaseHandler dbHandler = new DatabaseHandler();
                int bookID = selectedBook.getId();
                ObservableList< CopyOfBook > foundCopies = dbHandler.searchCopies(status, code, readerID, bookID, anyOf, fullMatch);

                idColumn.setCellValueFactory(new PropertyValueFactory<>("visual_id"));
                statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
                codeColumn.setCellValueFactory(new PropertyValueFactory<>("code"));
                readerIDColumn.setCellValueFactory(new PropertyValueFactory<>("reader_id"));
                issueDateColumn.setCellValueFactory(new PropertyValueFactory<>("issue_date"));
                deadlineColumn.setCellValueFactory(new PropertyValueFactory<>("deadline"));

                copyOfBooksTable.setItems(foundCopies);
            } else {
                showCopyOfBooks();
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

    public void getCategories() {
        DatabaseHandler dbHandler = new DatabaseHandler();
        ObservableList< String > categoriesList = dbHandler.getCategoriesStringList();
        bookCategoryChoiceBox.setItems(categoriesList);
    }

    public void showCopyOfBooks() {
        DatabaseHandler dbHandler = new DatabaseHandler();
        ObservableList< CopyOfBook > copyOfBooksList = dbHandler.getCopyOfBooksList(selectedBook);
        idColumn.setCellValueFactory(new PropertyValueFactory<>("visual_id"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        codeColumn.setCellValueFactory(new PropertyValueFactory<>("code"));
        readerIDColumn.setCellValueFactory(new PropertyValueFactory<>("reader_id"));
        issueDateColumn.setCellValueFactory(new PropertyValueFactory<>("issue_date"));
        deadlineColumn.setCellValueFactory(new PropertyValueFactory<>("deadline"));

        copyOfBooksTable.setItems(copyOfBooksList);

        int numOfCopies = copyOfBooksList.size();
        numOfCopiesReport = String.valueOf(numOfCopies);
        numOfCopiesTextField.setText(String.valueOf(numOfCopies));

        int availableCopies = 0;
        for (CopyOfBook copy : copyOfBooksList) {
            if (copy.getStatus().equals(notIssuedMessage)) {
                availableCopies += 1;
            }
        }
        availableCopiesReport = String.valueOf(availableCopies);
        availableCopiesTextField.setText(String.valueOf(availableCopies));
    }

}
