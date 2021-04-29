package sample.controllers;

import sample.asciitable.AsciiTable;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import sample.database.CopyOfBook;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import static sample.controllers.BookCopiesController.bookCopiesForReport;
import static sample.controllers.BookCopiesController.reportCode;
import static sample.controllers.BookCopiesController.reportStatus;
import static sample.controllers.BookCopiesController.reportReaderID;
import static sample.controllers.BookCopiesController.numOfCopiesReport;
import static sample.controllers.BookCopiesController.availableCopiesReport;
import static sample.controllers.BooksController.selectedBook;
import static sample.Main.window;

public class ReportBookCopiesController {
    private boolean booksButtonClicked = true;
    private boolean readersButtonClicked = false;

    @FXML
    private Button leftPanelBooksButton;

    @FXML
    private Button leftPanelReadersButton;

    @FXML
    private ImageView backButton;

    @FXML
    private TextArea reportTextArea;

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
        getReport();

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
            setOtherScene("/sample/view/book_copies.fxml");
        });
    }

    public void getReport() {
        String[] columnNames = {
                "#" ,"Статус видачі", "Штрихкод", "Кому видано (ID)", "Дата видачі", "Термін повернення"};

        String[][] data = new String[bookCopiesForReport.size()][];

        int i = 0;
        for (CopyOfBook copy : bookCopiesForReport) {
            String[] itemString = {String.valueOf(copy.getVisual_id()), copy.getStatus(), copy.getCode(),
                    String.valueOf(copy.getReader_id()), copy.getIssue_date(), copy.getDeadline()};
            data[i] = itemString;
            i += 1;
        }
        String report = AsciiTable.getTable(columnNames, data);
        Date todayDate = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");

        reportTextArea.appendText("Звіт сформовано " + sdf.format(todayDate) +"\n\n");
        reportTextArea.appendText("Примірники\n");
        reportTextArea.appendText("Номер книги: " + selectedBook.getId() + "\n");
        reportTextArea.appendText("Автор: " + selectedBook.getAuthor() + "\n");
        reportTextArea.appendText("Назва: " + selectedBook.getName() + "\n");
        reportTextArea.appendText("Розділ: " + selectedBook.getCategory() + "\n");
        reportTextArea.appendText("Рік: " + selectedBook.getYear() + "\n");
        reportTextArea.appendText("Видавництво: " + selectedBook.getEdition() + "\n\n");
        reportTextArea.appendText("Загальна кількість примірників: " + numOfCopiesReport + ";\t\t");
        reportTextArea.appendText("Доступно до видачі: " + availableCopiesReport + "\n\n");

        if ((reportStatus != null && !reportStatus.equals("")) || !reportCode.equals("") ||!reportReaderID.equals("")) {
            reportTextArea.appendText("Параметри вибірки:\n");
        }
        if (reportStatus != null && !reportStatus.equals("")) {
            reportTextArea.appendText("Статус: " + reportStatus + "\n");
        }
        if (!reportCode.equals("")) {
            reportTextArea.appendText("Штрихкод: " + reportCode + "\n");
        }
        if (!reportReaderID.equals("")) {
            reportTextArea.appendText("Кому видано (ID): " + reportReaderID + "\n");
        }

        reportTextArea.appendText("\n");
        reportTextArea.appendText(report);
        reportTextArea.setStyle("-fx-font-family: monospace");
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
