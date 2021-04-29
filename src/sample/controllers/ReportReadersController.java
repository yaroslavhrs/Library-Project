package sample.controllers;

import sample.asciitable.AsciiTable;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import sample.database.Reader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import static sample.controllers.ReadersController.reportName;
import static sample.controllers.ReadersController.reportAddress;
import static sample.controllers.ReadersController.reportBirthday;
import static sample.controllers.ReadersController.reportPhone;
import static sample.controllers.ReadersController.readersForReport;
import static sample.Main.window;

public class ReportReadersController {
    private boolean booksButtonClicked = false;
    private boolean readersButtonClicked = true;
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
        backButton.setOnMouseClicked(event -> {
            setOtherScene("/sample/view/readers_init.fxml");
        });
    }

    public void getReport() {
        String[] columnNames = {
                "ID" ,"ПІБ", "Адреса", "Дата народження", "Номер телефону"};

        String[][] data = new String[readersForReport.size()][];

        int i = 0;
        for (Reader reader : readersForReport) {
            String[] itemString = {String.valueOf(reader.getId()), reader.getName(), reader.getAddress(),
                    reader.getBirthday(), reader.getPhone()};
            data[i] = itemString;
            i += 1;
        }
        String report = AsciiTable.getTable(columnNames, data);
        Date todayDate = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");

        reportTextArea.appendText("Звіт сформовано " + sdf.format(todayDate) +"\n\n");
        reportTextArea.appendText("Читачі бібліотеки\n");

        if (!reportName.equals("") || !reportAddress.equals("") || !reportBirthday.equals("") || !reportPhone.equals("")) {
            reportTextArea.appendText("\nПараметри вибірки: \n");
        }
        if (!reportName.equals("")) {
            reportTextArea.appendText("ПІБ: " + reportName + "\n");
        }
        if (!reportAddress.equals("")) {
            reportTextArea.appendText("Адреса: " + reportAddress + "\n");
        }
        if (!reportBirthday.equals("")) {
            reportTextArea.appendText("Дата народження: " + reportBirthday + "\n");
        }
        if (!reportPhone.equals("")) {
            reportTextArea.appendText("Номер телефону: " + reportPhone + "\n");
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
