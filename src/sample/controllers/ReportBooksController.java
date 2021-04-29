package sample.controllers;

import sample.asciitable.AsciiTable;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import sample.database.Book;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import static sample.controllers.BooksController.reportAuthor;
import static sample.controllers.BooksController.reportCategory;
import static sample.controllers.BooksController.reportName;
import static sample.controllers.BooksController.reportYear;
import static sample.controllers.BooksController.booksForReport;
import static sample.Main.window;

public class ReportBooksController {
    private boolean booksButtonClicked = true;
    private boolean readersButtonClicked = false;
    private String style1 = "-fx-background-color: #F6ECD3; -fx-background-radius :  10 0 0 10;";
    private String style2 = "-fx-background-color: #ECCFA5; -fx-background-radius :  10 0 0 10;";

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
        backButton.setOnMouseClicked(event -> setOtherScene("/sample/view/books_init.fxml"));
    }

    public void getReport() {
        String[] columnNames = {
                "#" ,"Автор", "Назва", "Розділ", "Рік", "Видавництво"};

        String[][] data = new String[booksForReport.size()][];

        int i = 0;
        for (Book book : booksForReport) {
            String[] itemString = {String.valueOf(book.getId()), book.getAuthor(), book.getName(), book.getCategory(),
                    String.valueOf(book.getYear()), book.getEdition()};
            data[i] = itemString;
            i += 1;
        }
        String report = AsciiTable.getTable(columnNames, data);
        Date todayDate = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");

        reportTextArea.appendText("Звіт сформовано " + sdf.format(todayDate) +"\n\n");
        reportTextArea.appendText("Книги у бібліотеці\n");

        if ((reportCategory != null && !reportCategory.equals("")) || !reportAuthor.equals("") ||
                !reportName.equals("") || !reportYear.equals("")) {
            reportTextArea.appendText("\nПараметри вибірки: \n");
        }
        if (reportCategory != null && !reportCategory.equals("")) {
            reportTextArea.appendText("Розділ: " + reportCategory + "\n");
        }
        if (!reportAuthor.equals("")) {
            reportTextArea.appendText("Автор: " + reportAuthor + "\n");
        }
        if (!reportName.equals("")) {
            reportTextArea.appendText("Назва: " + reportName + "\n");
        }
        if (!reportYear.equals("")) {
            reportTextArea.appendText("Рік: " + reportYear + "\n");
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
