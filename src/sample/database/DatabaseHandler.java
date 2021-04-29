package sample.database;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import sample.confirmBoxes.OKConfirmBox;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DatabaseHandler extends Configs {
    Connection dbConnection;

    public Connection getDbConnection()
            throws ClassNotFoundException, SQLException {
        String connectionString = "jdbc:mysql://" + dbHost + ":"
                + dbPort + "/" + dbName;
        dbConnection = DriverManager.getConnection(connectionString,
                dbUser, dbPass);
        return dbConnection;
    }

    // Створення і виконання запиту для додавання нового читача у базу даних
    public void addReader(Reader reader) {
        String insert = "INSERT INTO " + Const.READERS_TABLE + "(" +
                Const.READERS_NAME + "," + Const.READERS_ADDRESS + "," +
                Const.READERS_BIRTHDAY + "," + Const.READERS_PHONE + ")" +
                " VALUES(?,?,?,?);";
        try {
            PreparedStatement prSt = getDbConnection().prepareStatement(insert);
            prSt.setString(1, reader.getName());
            prSt.setString(2, reader.getAddress());
            prSt.setString(3, reader.getBirthday());
            prSt.setString(4, reader.getPhone());

            prSt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            OKConfirmBox.display("Помилка","Немає з'єднання з базою даних");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    // Створення і виконання запиту для додавання нової книги у базу даних
    public void addBook(Book book) {
        String insert = "INSERT INTO " + Const.BOOKS_TABLE + "(" +
                Const.BOOKS_AUTHOR + "," + Const.BOOKS_NAME + "," +
                Const.BOOKS_CATEGORY + "," + Const.BOOKS_YEAR + "," +
                Const.BOOKS_EDITION + ")" + " VALUES(?,?,?,?,?);";
        try {
            PreparedStatement prSt = getDbConnection().prepareStatement(insert);
            prSt.setString(1, book.getAuthor());
            prSt.setString(2, book.getName());
            prSt.setString(3, book.getCategory());
            prSt.setInt(4, book.getYear());
            prSt.setString(5, book.getEdition());

            prSt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            OKConfirmBox.display("Помилка","Немає з'єднання з базою даних");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    // Створення запиту для додавання нового примірника книги у базу даних
    public void addCopyOfBook(CopyOfBook copyOfBook) {
        String insert = "INSERT INTO " + Const.COPIES_TABLE + "(" +
                Const.COPIES_BOOK_ID + "," + Const.COPIES_STATUS + "," +
                Const.COPIES_CODE + "," + Const.COPIES_READER_ID + "," +
                Const.COPIES_ISSUE_DATE + "," + Const.COPIES_DEADLINE + ")" +
                " VALUES(?,?,?,?,?,?);";
        try {
            PreparedStatement prSt = getDbConnection().prepareStatement(insert);
            prSt.setInt(1, copyOfBook.getBook_id());
            prSt.setString(2, copyOfBook.getStatus());
            prSt.setString(3, copyOfBook.getCode());
            prSt.setInt(4, copyOfBook.getReader_id());
            prSt.setString(5, copyOfBook.getIssue_date());
            prSt.setString(6, copyOfBook.getDeadline());

            prSt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            OKConfirmBox.display("Помилка","Немає з'єднання з базою даних");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    // Створення і виконання запиту для додавання нового розділу книги у базу даних
    public void addCategory(String category, String category_code) {
        String insert = "INSERT INTO " + Const.CATEGORIES_TABLE + "(" +
                Const.CATEGORIES_CATEGORY + "," + Const.CATEGORIES_CATEGORY_CODE + ")" + " VALUES(?,?);";
        try {
            PreparedStatement prSt = getDbConnection().prepareStatement(insert);
            prSt.setString(1, category);
            prSt.setString(2, category_code);

            prSt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            OKConfirmBox.display("Помилка","Немає з'єднання з базою даних");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    // Створення і виконання запиту для внесення змін про читача у базі даних
    public void editReader(Reader reader) {
        String query = "UPDATE " + Const.READERS_TABLE + " SET " +
                Const.READERS_NAME + "= '" + reader.getName() + "'," +
                Const.READERS_ADDRESS + "= '" + reader.getAddress() + "'," +
                Const.READERS_BIRTHDAY + "= '" + reader.getBirthday() + "'," +
                Const.READERS_PHONE + "= '" + reader.getPhone() + "' WHERE id = " +
                reader.getId() + ";";
        executeUpdateFunction(query);
    }

    // Створення і виконання запиту для внесення змін про книгу у базі даних
    public void editBook(Book book) {
        String query = "UPDATE " + Const.BOOKS_TABLE + " SET " +
                Const.BOOKS_AUTHOR + "= '" + book.getAuthor() + "'," +
                Const.BOOKS_NAME + "= '" + book.getName() + "'," +
                Const.BOOKS_CATEGORY + "= \"" + book.getCategory() + "\"," +
                Const.BOOKS_YEAR + "= " + book.getYear() + "," +
                Const.BOOKS_EDITION + "= '" + book.getEdition() + "' WHERE id = " +
                book.getId() + ";";
        executeUpdateFunction(query);
    }

    // Створення і виконання запиту для видалення читача з бази даних
    public void deleteReader(Reader reader) {
        String query = "DELETE FROM " + Const.READERS_TABLE +
                " WHERE id = " + reader.getId() + ";";
        executeUpdateFunction(query);
    }

    // Створення і виконання запиту для видалення книги та всіх її примірників з бази даних
    public void deleteBook(Book book) {
        String query = "DELETE FROM " + Const.BOOKS_TABLE +
                " WHERE id = " + book.getId() + ";";
        executeUpdateFunction(query);

        String query2 = "DELETE FROM " + Const.COPIES_TABLE +
                " WHERE " + Const.COPIES_BOOK_ID + " = " + book.getId() + ";";
        executeUpdateFunction(query2);
    }

    // Створення і виконання запиту для видалення примірника книги з бази даних
    public void deleteCopyOfBook(CopyOfBook copyOfBook) {
        String query = "DELETE FROM " + Const.COPIES_TABLE +
                " WHERE id = " + copyOfBook.getId() + ";";
        executeUpdateFunction(query);
    }

    // Створення і виконання запиту для зміни статусу примірника книги у базі даних
    public void changeStatus(CopyOfBook copyOfBook, String newStatus) {
        String query = "UPDATE " + Const.COPIES_TABLE + " SET " +
                Const.COPIES_STATUS + " = '" + newStatus + "' WHERE " +
                Const.COPIES_ID + " = " + copyOfBook.getId() + ";";
        executeUpdateFunction(query);
    }

    // Створення і виконання запиту для видалення розділу з бази даних
    public void deleteCategory(Category category) {
        String query = "DELETE FROM " + Const.CATEGORIES_TABLE +
                " WHERE id = " + category.getId() + ";";
        executeUpdateFunction(query);
    }

    // Створення і виконання запиту для зміни штрихкоду примірника книги у базі даних
    public void changeCode(CopyOfBook copyOfBook, String newCode) {
        String query = "UPDATE " + Const.COPIES_TABLE + " SET " +
                Const.COPIES_CODE + " = '" + newCode + "' WHERE " +
                Const.COPIES_ID + " = " + copyOfBook.getId() + ";";
        executeUpdateFunction(query);

        String query2 = "UPDATE " + Const.HISTORY_TABLE + " SET " +
                Const.HISTORY_CODE + " = '" + newCode + "' WHERE " +
                Const.HISTORY_READER_ID + " = " + copyOfBook.getReader_id() + " AND " +
                Const.HISTORY_BOOK_ID + " = " + copyOfBook.getBook_id() + " AND " +
                Const.HISTORY_CODE + " = '" + copyOfBook.getCode() + "';";
        executeUpdateFunction(query2);
    }

    // Створення і виконання запиту для зміни терміну повернення примірника книги у базі даних
    public void changeDeadline(CopyOfBook copyOfBook, String newDeadline) {
        String query = "UPDATE " + Const.COPIES_TABLE + " SET " +
                Const.COPIES_DEADLINE + " = '" + newDeadline + "' WHERE " +
                Const.COPIES_ID + " = " + copyOfBook.getId() + ";";
        executeUpdateFunction(query);

        String query2 = "UPDATE " + Const.HISTORY_TABLE + " SET " +
                Const.HISTORY_DEADLINE + " = '" + newDeadline + "' WHERE " +
                Const.HISTORY_READER_ID + " = " + copyOfBook.getReader_id() + " AND " +
                Const.HISTORY_BOOK_ID + " = " + copyOfBook.getBook_id() + " AND " +
                Const.HISTORY_CODE + " = '" + copyOfBook.getCode() + "';";
        executeUpdateFunction(query2);
    }

    // Створення і виконання запиту для видачі примірника книги
    public void issueCopyOfBook(CopyOfBook copyOfBook, int readerID, String deadline) {
        Date issueDate = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");

        String query = "UPDATE " + Const.COPIES_TABLE + " SET " +
                Const.COPIES_READER_ID + "=" + readerID + ", " +
                Const.COPIES_STATUS + " = 'Видано', " +
                Const.COPIES_ISSUE_DATE + " = '" + sdf.format(issueDate) + "', " +
                Const.COPIES_DEADLINE + " = '" + deadline + "'" +
                " WHERE " + Const.COPIES_ID + " = " + copyOfBook.getId() + ";";
        executeUpdateFunction(query);

        String insert = "INSERT INTO " + Const.HISTORY_TABLE + "(" +
                Const.HISTORY_READER_ID + "," + Const.HISTORY_BOOK_ID + "," +
                Const.HISTORY_CODE + "," + Const.HISTORY_ISSUE_DATE + "," + Const.HISTORY_DEADLINE + ")" +
                " VALUES ('" + readerID + "', '" + copyOfBook.getBook_id() + "', '" +
                copyOfBook.getCode() + "', '" + sdf.format(issueDate) + "', '" + deadline + "');";
        executeUpdateFunction(insert);
    }

    // Створення і виконання запиту для повернення примірника книги
    public void returnBook(ReaderAccountBookItem bookItem, int readerID) {
        String query = "UPDATE " + Const.COPIES_TABLE + " SET " +
                Const.COPIES_READER_ID + " = 0, " +
                Const.COPIES_STATUS + " = 'Не видано', " +
                Const.COPIES_ISSUE_DATE + " = '-', " +
                Const.COPIES_DEADLINE + " = '-'" +
                " WHERE " + Const.COPIES_READER_ID + " = " + readerID + " AND " +
                Const.COPIES_BOOK_ID + " = " + bookItem.getBook_id() + " AND " +
                Const.COPIES_CODE + " = '" + bookItem.getCode() + "';";
        executeUpdateFunction(query);
    }

    // Метод для отримання списку всіх книг з бази даних
    public ObservableList<Book> getBooksList() {
        ObservableList<Book> booksList = FXCollections.observableArrayList();
        String query = "SELECT * FROM " + Const.BOOKS_TABLE + ";";
        try {
            ResultSet resSet = executeQueryFunction(query);
            Book book;
            while (resSet.next()) {
                book = new Book(resSet.getString("author"), resSet.getString("name"),
                        resSet.getString("category"), resSet.getInt("year"),
                        resSet.getString("edition"));
                book.setId(resSet.getInt("id"));
                booksList.add(book);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            OKConfirmBox.display("Помилка","Немає з'єднання з базою даних");
        }
        return booksList;
    }

    // Метод для отримання списку всіх читачів з бази даних
    public ObservableList<Reader> getReadersList() {
        ObservableList<Reader> readersList = FXCollections.observableArrayList();
        String query = "SELECT * FROM " + Const.READERS_TABLE + ";";
        try {
            ResultSet resSet = executeQueryFunction(query);
            Reader reader;
            while (resSet.next()) {
                reader = new Reader(resSet.getString("name"), resSet.getString("address"),
                        resSet.getString("birthday"), resSet.getString("phone"));
                reader.setId(resSet.getInt("id"));
                readersList.add(reader);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            OKConfirmBox.display("Помилка","Немає з'єднання з базою даних");
        }
        return readersList;
    }

    // Метод для отримання списку всіх примірників книги з бази даних
    public ObservableList<CopyOfBook> getCopyOfBooksList(Book book) {
        ObservableList<CopyOfBook> copyOfBooksList = FXCollections.observableArrayList();
        String query = "SELECT * FROM " + Const.COPIES_TABLE + " WHERE " +
                Const.COPIES_BOOK_ID + " = " + book.getId() + ";";
        try {
            ResultSet resSet = executeQueryFunction(query);
            CopyOfBook copyOfBook;
            int visual_id = 1;
            while (resSet.next()) {
                copyOfBook = new CopyOfBook(resSet.getInt("book_id"), resSet.getString("status"),
                        resSet.getString("code"), resSet.getInt("reader_id"), resSet.getString("issue_date"),
                        resSet.getString("deadline"));
                copyOfBook.setId(resSet.getInt("id"));
                copyOfBook.setVisual_id(visual_id);
                copyOfBooksList.add(copyOfBook);
                visual_id += 1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            OKConfirmBox.display("Помилка","Немає з'єднання з базою даних");
        }
        return copyOfBooksList;
    }

    // Метод для отримання списку всіх примірників з бази даних
    public ObservableList<CopyOfBook> getAllCopiesList() {
        ObservableList<CopyOfBook> allCopiesList = FXCollections.observableArrayList();
        String query = "SELECT * FROM " + Const.COPIES_TABLE + ";";
        try {
            ResultSet resSet = executeQueryFunction(query);
            CopyOfBook copyOfBook;
            int visual_id = 1;
            while (resSet.next()) {
                copyOfBook = new CopyOfBook(resSet.getInt("book_id"), resSet.getString("status"),
                        resSet.getString("code"), resSet.getInt("reader_id"), resSet.getString("issue_date"),
                        resSet.getString("deadline"));
                copyOfBook.setId(resSet.getInt("id"));
                copyOfBook.setVisual_id(visual_id);
                allCopiesList.add(copyOfBook);
                visual_id += 1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            OKConfirmBox.display("Помилка","Немає з'єднання з базою даних");
        }
        return allCopiesList;
    }

    // Метод для отримання списку всіх примірників книги, які зараз на руках в читача
    public ObservableList<CopyOfBook> getReadingNowCopies(int readerID) {
        ObservableList<CopyOfBook> readingNowList = FXCollections.observableArrayList();
        String query = "SELECT * FROM " + Const.COPIES_TABLE + " WHERE " +
                Const.COPIES_READER_ID + " = " + readerID + ";";
        try {
            ResultSet resSet = executeQueryFunction(query);
            CopyOfBook copyOfBook;
            while (resSet.next()) {
                copyOfBook = new CopyOfBook(resSet.getInt("book_id"), resSet.getString("status"),
                        resSet.getString("code"), resSet.getInt("reader_id"), resSet.getString("issue_date"),
                        resSet.getString("deadline"));
                copyOfBook.setId(resSet.getInt("id"));
                readingNowList.add(copyOfBook);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            OKConfirmBox.display("Помилка","Немає з'єднання з базою даних");
        }
        return readingNowList;
    }

    // Метод для отримання списку всіх розділів книг з бази даних
    public ObservableList<Category> getCategoriesList() {
        ObservableList<Category> categoriesList = FXCollections.observableArrayList();
        String query = "SELECT * FROM " + Const.CATEGORIES_TABLE +
                " ORDER BY " + Const.CATEGORIES_ID + ";";
        try {
            ResultSet resSet = executeQueryFunction(query);
            int visualId = 0;
            Category category;
            while (resSet.next()) {
                category = new Category(resSet.getInt("id"), resSet.getString("category"),
                        resSet.getString("category_code"));
                category.setVisual_id(visualId);
                categoriesList.add(category);
                visualId += 1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            OKConfirmBox.display("Помилка","Немає з'єднання з базою даних");
        }
        return categoriesList;
    }

    // Метод для отримання списку всіх розділів книг з бази даних у форматі String
    public ObservableList<String> getCategoriesStringList() {
        ObservableList<String> categoriesStringList = FXCollections.observableArrayList();
        String query = "SELECT * FROM " + Const.CATEGORIES_TABLE +
                " ORDER BY " + Const.CATEGORIES_ID + ";";
        try {
            ResultSet resSet = executeQueryFunction(query);
            String category;
            while (resSet.next()) {
                category = resSet.getString("category");
                categoriesStringList.add(category);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            OKConfirmBox.display("Помилка","Немає з'єднання з базою даних");
        }
        return categoriesStringList;
    }

    // Метод для отримання списку всіх розділів книг з бази даних
    public String getCategoryCode(String category) {
        String categoryCode = "";
        String query = "SELECT * FROM " + Const.CATEGORIES_TABLE +
                " WHERE " + Const.CATEGORIES_CATEGORY + "=\"" + category + "\";";
        try {
            ResultSet resSet = executeQueryFunction(query);
            while (resSet.next()) {
                categoryCode = resSet.getString("category_code");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            OKConfirmBox.display("Помилка","Немає з'єднання з базою даних");
        }
        return categoryCode;
    }

    // Метод для отримання списку всіх виданих примірників книг
    public ObservableList<ReaderAccountBookItem> getIssuedItemsList() {
        ObservableList<ReaderAccountBookItem> issuedItemsList = FXCollections.observableArrayList();

        String query = "SELECT * FROM " + Const.BOOKS_TABLE + " RIGHT JOIN " + Const.COPIES_TABLE + " ON " +
                Const.BOOKS_TABLE + "." + Const.BOOKS_ID + "=" + Const.COPIES_TABLE + "." + Const.COPIES_BOOK_ID +
                " WHERE " + Const.COPIES_READER_ID + "<>" + 0 + ";";
        int item_id = 1;
        try {
            ResultSet resSet = executeQueryFunction(query);
            ReaderAccountBookItem bookItem;
            while (resSet.next()) {
                bookItem = new ReaderAccountBookItem(item_id, resSet.getInt("book_id"), resSet.getString("author"),
                        resSet.getString("name"), resSet.getString("category"), resSet.getInt("year"),
                        resSet.getString("edition"), resSet.getString("code"), resSet.getString("issue_date"),
                        resSet.getString("deadline"));
                bookItem.setReader_id(resSet.getInt("reader_id"));
                issuedItemsList.add(bookItem);
                item_id += 1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            OKConfirmBox.display("Помилка","Немає з'єднання з базою даних");
        }
        return issuedItemsList;
    }

    // Метод для отримання списку книг, які на руках у читача
    public ObservableList<ReaderAccountBookItem> getReadingNowItemsList(int readerID) {
        ObservableList<ReaderAccountBookItem> readingNowItemsList = FXCollections.observableArrayList();

        String query = "SELECT * FROM " + Const.BOOKS_TABLE + " RIGHT JOIN " + Const.COPIES_TABLE + " ON " +
                Const.BOOKS_TABLE + "." + Const.BOOKS_ID + "=" + Const.COPIES_TABLE + "." + Const.COPIES_BOOK_ID +
                " WHERE " + Const.COPIES_READER_ID + "=" + readerID + ";";
        int item_id = 1;
        try {
            ResultSet resSet = executeQueryFunction(query);
            ReaderAccountBookItem bookItem;
            while (resSet.next()) {
                bookItem = new ReaderAccountBookItem(item_id, resSet.getInt("book_id"), resSet.getString("author"),
                        resSet.getString("name"), resSet.getString("category"), resSet.getInt("year"),
                        resSet.getString("edition"), resSet.getString("code"), resSet.getString("issue_date"),
                        resSet.getString("deadline"));
                readingNowItemsList.add(bookItem);
                item_id += 1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            OKConfirmBox.display("Помилка","Немає з'єднання з базою даних");
        }
        return readingNowItemsList;
    }

    // Метод для отримання списку книг, які читач брав за весь час
    public ObservableList<ReaderAccountBookItem> getHistoryItemsList(int readerID) {
        ObservableList<ReaderAccountBookItem> historyItemsList = FXCollections.observableArrayList();

        String query = "SELECT * FROM " + Const.BOOKS_TABLE + " RIGHT JOIN " + Const.HISTORY_TABLE + " ON " +
                Const.BOOKS_TABLE + "." + Const.BOOKS_ID + "=" + Const.HISTORY_TABLE + "." + Const.HISTORY_BOOK_ID +
                " WHERE " + Const.HISTORY_READER_ID + "=" + readerID + ";";
        int item_id = 1;
        try {
            ResultSet resSet = executeQueryFunction(query);
            ReaderAccountBookItem bookItem;
            while (resSet.next()) {
                bookItem = new ReaderAccountBookItem(item_id, resSet.getInt("book_id"), resSet.getString("author"),
                        resSet.getString("name"), resSet.getString("category"), resSet.getInt("year"),
                        resSet.getString("edition"), resSet.getString("code"), resSet.getString("issue_date"),
                        resSet.getString("deadline"));
                historyItemsList.add(bookItem);
                item_id += 1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            OKConfirmBox.display("Помилка","Немає з'єднання з базою даних");
        }
        return historyItemsList;
    }

    // Метод для пошуку списку книг за параметрами з бази даних
    public ObservableList<Book> searchBooks(String category, String author, String name, int year, boolean anyOf,
                                            boolean fullMatch) {
        ObservableList<Book> booksList = FXCollections.observableArrayList();
        String query = "";
        String categoryQueryAND = "";
        String authorQueryAND = "";
        String nameQueryAND = "";
        String yearQueryAND = "";
        String categoryQueryOR = "";
        String authorQueryOR = "";
        String nameQueryOR = "";
        String yearQueryOR = "";
        String constantQueryAND = Const.BOOKS_ID + " <> " + 0;
        String constantQueryOR = Const.BOOKS_ID + " = " + 0;

        if (!category.equals("")) {
            categoryQueryAND = " AND " + Const.BOOKS_CATEGORY + " = \"" + category + "\"";
            categoryQueryOR = " OR " + Const.BOOKS_CATEGORY + " = \"" + category + "\"";
        }

        if (!author.equals("")) {
            authorQueryAND = " AND " + Const.BOOKS_AUTHOR + " LIKE \"%" + author + "%\"";
            authorQueryOR = " OR " + Const.BOOKS_AUTHOR + " LIKE \"%" + author + "%\"";
        }

        if (!name.equals("")) {
            nameQueryAND = " AND " + Const.BOOKS_NAME + " LIKE \"%" + name + "%\"";
            nameQueryOR = " OR " + Const.BOOKS_NAME + " LIKE \"%" + name + "%\"";
        }

        if (year != 0) {
            yearQueryAND = " AND " + Const.BOOKS_YEAR + " = " + year;
            yearQueryOR = " OR " + Const.BOOKS_YEAR + " = " + year;
        }

        // пошук "повне співпадіння"
        if (fullMatch && !anyOf) {
            query = "SELECT * FROM " + Const.BOOKS_TABLE + " WHERE " +
                    constantQueryAND + categoryQueryAND + authorQueryAND + nameQueryAND + yearQueryAND + ";";
        }

        // пошук "будь-який з"
        if (anyOf && !fullMatch) {
            query = "SELECT * FROM " + Const.BOOKS_TABLE + " WHERE " +
                    constantQueryOR + categoryQueryOR + authorQueryOR + nameQueryOR + yearQueryOR + ";";
        }

        try {
            ResultSet resSet = executeQueryFunction(query);
            Book book;
            while (resSet.next()) {
                book = new Book(resSet.getString("author"), resSet.getString("name"),
                        resSet.getString("category"), resSet.getInt("year"),
                        resSet.getString("edition"));
                book.setId(resSet.getInt("id"));
                booksList.add(book);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            OKConfirmBox.display("Помилка","Немає з'єднання з базою даних");
        }
        return booksList;
    }

    // Метод для отримання списку книг, які на руках у читача за параметрами книги
    public ObservableList<ReaderAccountBookItem> searchReadingNowItemsList(int readerID, String author, String name,
                                                                           int year, String code, boolean anyOf, boolean fullMatch) {
        ObservableList<ReaderAccountBookItem> foundReadingNowItemsList = FXCollections.observableArrayList();
        String query = "";
        String authorQueryAND = "";
        String nameQueryAND = "";
        String yearQueryAND = "";
        String codeQueryAND = "";
        String authorQueryOR = "";
        String nameQueryOR = "";
        String yearQueryOR = "";
        String codeQueryOR = "";
        String constantQueryAND = Const.COPIES_TABLE + "." + Const.COPIES_ID + " <> " + 0;
        String constantQueryOR = Const.COPIES_TABLE + "." + Const.COPIES_ID + " = " + 0;

        if (!author.equals("")) {
            authorQueryAND = " AND " + Const.BOOKS_AUTHOR + " LIKE \"%" + author + "%\"";
            authorQueryOR = " OR " + Const.BOOKS_AUTHOR + " LIKE \"%" + author + "%\"";
        }

        if (!name.equals("")) {
            nameQueryAND = " AND " + Const.BOOKS_NAME + " LIKE \"%" + name + "%\"";
            nameQueryOR = " OR " + Const.BOOKS_NAME + " LIKE \"%" + name + "%\"";
        }

        if (year != 0) {
            yearQueryAND = " AND " + Const.BOOKS_YEAR + " = " + year;
            yearQueryOR = " OR " + Const.BOOKS_YEAR + " = " + year;
        }

        if (!code.equals("")) {
            codeQueryAND = " AND " + Const.COPIES_CODE + " = \"" + code + "\"";
            codeQueryOR = " OR " + Const.COPIES_CODE + " = \"" + code + "\"";
        }

        // пошук "повне співпадіння"
        if (fullMatch && !anyOf) {
            query = "SELECT * FROM " + Const.BOOKS_TABLE + " RIGHT JOIN " + Const.COPIES_TABLE + " ON " +
                    Const.BOOKS_TABLE + "." + Const.BOOKS_ID + "=" + Const.COPIES_TABLE + "." + Const.COPIES_BOOK_ID +
                    " WHERE " + Const.COPIES_READER_ID + "=" + readerID + " AND " + constantQueryAND +
                    authorQueryAND + nameQueryAND + yearQueryAND + codeQueryAND + ";";
        }

        // пошук "будь-який з"
        if (anyOf && !fullMatch) {
            query = "SELECT * FROM " + Const.BOOKS_TABLE + " RIGHT JOIN " + Const.COPIES_TABLE + " ON " +
                    Const.BOOKS_TABLE + "." + Const.BOOKS_ID + "=" + Const.COPIES_TABLE + "." + Const.COPIES_BOOK_ID +
                    " WHERE " + Const.COPIES_READER_ID + "=" + readerID + " AND (" + constantQueryOR +
                    authorQueryOR + nameQueryOR + yearQueryOR + codeQueryOR + ");";
        }

        int item_id = 1;
        try {
            ResultSet resSet = executeQueryFunction(query);
            ReaderAccountBookItem bookItem;
            while (resSet.next()) {
                bookItem = new ReaderAccountBookItem(item_id, resSet.getInt("book_id"), resSet.getString("author"),
                        resSet.getString("name"), resSet.getString("category"), resSet.getInt("year"),
                        resSet.getString("edition"), resSet.getString("code"), resSet.getString("issue_date"),
                        resSet.getString("deadline"));
                foundReadingNowItemsList.add(bookItem);
                item_id += 1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            OKConfirmBox.display("Помилка","Немає з'єднання з базою даних");
        }
        return foundReadingNowItemsList;
    }

    // Метод для отримання списку книг, які читач брав за весь час
    public ObservableList<ReaderAccountBookItem> searchHistoryItemsList(int readerID, String author, String name,
                                                                           int year, String code, boolean anyOf, boolean fullMatch) {
        ObservableList<ReaderAccountBookItem> foundHistoryItemsList = FXCollections.observableArrayList();
        String query = "";
        String authorQueryAND = "";
        String nameQueryAND = "";
        String yearQueryAND = "";
        String codeQueryAND = "";
        String authorQueryOR = "";
        String nameQueryOR = "";
        String yearQueryOR = "";
        String codeQueryOR = "";
        String constantQueryAND = Const.HISTORY_TABLE + "." + Const.HISTORY_ID + " <> " + 0;
        String constantQueryOR = Const.HISTORY_TABLE + "." + Const.HISTORY_ID + " = " + 0;

        if (!author.equals("")) {
            authorQueryAND = " AND " + Const.BOOKS_AUTHOR + " LIKE \"%" + author + "%\"";
            authorQueryOR = " OR " + Const.BOOKS_AUTHOR + " LIKE \"%" + author + "%\"";
        }

        if (!name.equals("")) {
            nameQueryAND = " AND " + Const.BOOKS_NAME + " LIKE \"%" + name + "%\"";
            nameQueryOR = " OR " + Const.BOOKS_NAME + " LIKE \"%" + name + "%\"";
        }

        if (year != 0) {
            yearQueryAND = " AND " + Const.BOOKS_YEAR + " = " + year;
            yearQueryOR = " OR " + Const.BOOKS_YEAR + " = " + year;
        }

        if (!code.equals("")) {
            codeQueryAND = " AND " + Const.HISTORY_CODE + " = \"" + code + "\"";
            codeQueryOR = " OR " + Const.HISTORY_CODE + " = \"" + code + "\"";
        }

        // пошук "повне співпадіння"
        if (fullMatch && !anyOf) {
            query = "SELECT * FROM " + Const.BOOKS_TABLE + " RIGHT JOIN " + Const.HISTORY_TABLE + " ON " +
                    Const.BOOKS_TABLE + "." + Const.BOOKS_ID + "=" + Const.HISTORY_TABLE + "." + Const.HISTORY_BOOK_ID +
                    " WHERE " + Const.HISTORY_READER_ID + "=" + readerID + " AND " + constantQueryAND +
                    authorQueryAND + nameQueryAND + yearQueryAND + codeQueryAND + ";";
        }

        // пошук "будь-який з"
        if (anyOf && !fullMatch) {
            query = "SELECT * FROM " + Const.BOOKS_TABLE + " RIGHT JOIN " + Const.HISTORY_TABLE + " ON " +
                    Const.BOOKS_TABLE + "." + Const.BOOKS_ID + "=" + Const.HISTORY_TABLE + "." + Const.HISTORY_BOOK_ID +
                    " WHERE " + Const.HISTORY_READER_ID + "=" + readerID + " AND (" + constantQueryOR +
                    authorQueryOR + nameQueryOR + yearQueryOR + codeQueryOR + ");";
        }

        int item_id = 1;
        try {
            ResultSet resSet = executeQueryFunction(query);
            ReaderAccountBookItem bookItem;
            while (resSet.next()) {
                bookItem = new ReaderAccountBookItem(item_id, resSet.getInt("book_id"), resSet.getString("author"),
                        resSet.getString("name"), resSet.getString("category"), resSet.getInt("year"),
                        resSet.getString("edition"), resSet.getString("code"), resSet.getString("issue_date"),
                        resSet.getString("deadline"));
                foundHistoryItemsList.add(bookItem);
                item_id += 1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            OKConfirmBox.display("Помилка","Немає з'єднання з базою даних");
        }
        return foundHistoryItemsList;
    }

    // Метод для пошуку списку книг за параметрами з бази даних
    public ObservableList<CopyOfBook> searchCopies(String status, String code, int readerID, int bookID, boolean anyOf,
                                                   boolean fullMatch) {
        ObservableList<CopyOfBook> copiesList = FXCollections.observableArrayList();
        String query = "";
        String statusQueryAND = "";
        String codeQueryAND = "";
        String readerIDQueryAND = "";
        String statusQueryOR = "";
        String codeQueryOR = "";
        String readerIDQueryOR = "";
        String constantQueryAND = Const.COPIES_ID + " <> " + 0;
        String constantQueryOR = Const.COPIES_ID + " = " + 0;
        String bookIDQuery = " AND " + Const.COPIES_BOOK_ID + " = " + bookID;

        if (!status.equals("")) {
            statusQueryAND = " AND " + Const.COPIES_STATUS + " = \"" + status + "\"";
            statusQueryOR = " OR " + Const.COPIES_STATUS + " = \"" + status + "\"";
        }

        if (!code.equals("")) {
            codeQueryAND = " AND " + Const.COPIES_CODE + " = \"" + code + "\"";
            codeQueryOR = " OR " + Const.COPIES_CODE + " = \"" + code + "\"";
        }

        if (readerID != 0) {
            readerIDQueryAND = " AND " + Const.COPIES_READER_ID + " = " + readerID;
            readerIDQueryOR = " OR " + Const.COPIES_READER_ID + " = " + readerID;
        }

        // пошук "повне співпадіння"
        if (fullMatch && !anyOf) {
            query = "SELECT * FROM " + Const.COPIES_TABLE + " WHERE " +
                    constantQueryAND + statusQueryAND + codeQueryAND + readerIDQueryAND + bookIDQuery + ";";
        }

        // пошук "будь-який з"
        if (anyOf && !fullMatch) {
            query = "SELECT * FROM " + Const.COPIES_TABLE + " WHERE " + "(" +
                    constantQueryOR + statusQueryOR + codeQueryOR + readerIDQueryOR + ")" + bookIDQuery + ";";
        }

        try {
            ResultSet resSet = executeQueryFunction(query);
            CopyOfBook copy;
            int visual_id = 1;
            while (resSet.next()) {
                copy = new CopyOfBook(resSet.getInt("book_id"), resSet.getString("status"),
                        resSet.getString("code"), resSet.getInt("reader_id"), resSet.getString("issue_date"),
                        resSet.getString("deadline"));
                copy.setId(resSet.getInt("id"));
                copy.setVisual_id(visual_id);
                copiesList.add(copy);
                visual_id += 1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            OKConfirmBox.display("Помилка","Немає з'єднання з базою даних");
        }
        return copiesList;
    }

    // Метод для пошуку списку книг за параметрами з бази даних
    public ObservableList<Reader> searchReaders(String name, String address, String birthday, String phone, boolean anyOf,
                                                boolean fullMatch) {
        ObservableList<Reader> readersList = FXCollections.observableArrayList();
        String query = "";
        String nameQueryAND = "";
        String addressQueryAND = "";
        String birthdayQueryAND = "";
        String phoneQueryAND = "";
        String nameQueryOR = "";
        String addressQueryOR = "";
        String birthdayQueryOR = "";
        String phoneQueryOR = "";
        String constantQueryAND = Const.READERS_ID + " <> " + 0;
        String constantQueryOR = Const.READERS_ID + " = " + 0;

        if (!name.equals("")) {
            nameQueryAND = " AND " + Const.READERS_NAME + " LIKE \"%" + name + "%\"";
            nameQueryOR = " OR " + Const.READERS_NAME + " LIKE \"%" + name + "%\"";
        }

        if (!address.equals("")) {
            addressQueryAND = " AND " + Const.READERS_ADDRESS + " LIKE \"%" + address + "%\"";
            addressQueryOR = " OR " + Const.READERS_ADDRESS + " LIKE \"%" + address + "%\"";
        }

        if (!birthday.equals("")) {
            birthdayQueryAND = " AND " + Const.READERS_BIRTHDAY + " = \"" + birthday + "\"";
            birthdayQueryOR = " OR " + Const.READERS_BIRTHDAY + " = \"" + birthday + "\"";
        }

        if (!phone.equals("")) {
            phoneQueryAND = " AND " + Const.READERS_PHONE + " = \"" + phone + "\"";
            phoneQueryOR = " OR " + Const.READERS_PHONE + " = \"" + phone + "\"";
        }

        // пошук "повне співпадіння"
        if (fullMatch && !anyOf) {
            query = "SELECT * FROM " + Const.READERS_TABLE + " WHERE " +
                    constantQueryAND + nameQueryAND + addressQueryAND + birthdayQueryAND + phoneQueryAND + ";";
        }

        // пошук "будь-який з"
        if (anyOf && !fullMatch) {
            query = "SELECT * FROM " + Const.READERS_TABLE + " WHERE " +
                    constantQueryOR + nameQueryOR + addressQueryOR + birthdayQueryOR + phoneQueryOR + ";";
        }

        try {
            ResultSet resSet = executeQueryFunction(query);
            Reader reader;
            while (resSet.next()) {
                reader = new Reader(resSet.getString("name"), resSet.getString("address"),
                        resSet.getString("birthday"), resSet.getString("phone"));
                reader.setId(resSet.getInt("id"));
                readersList.add(reader);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            OKConfirmBox.display("Помилка","Немає з'єднання з базою даних");
        }
        return readersList;
    }

    // Метод для пошуку виданих примірників книг за параметрами
    public ObservableList<ReaderAccountBookItem> searchIssuedCopies(String category, String author, String name, int year, boolean anyOf,
                                                                    boolean fullMatch) {
        ObservableList<ReaderAccountBookItem> allItemsList = getIssuedItemsList();
        ObservableList<Book> foundBooks = searchBooks(category, author, name, year, anyOf, fullMatch);
        ObservableList<ReaderAccountBookItem> foundCopies = FXCollections.observableArrayList();

        for (Book book : foundBooks) {
            for (ReaderAccountBookItem item : allItemsList) {
                if (book.getId() == item.getBook_id()) {
                    foundCopies.add(item);
                }
            }
        }
        return foundCopies;
    }

    private void executeUpdateFunction(String query) {
        try {
            Connection connection = getDbConnection();
            Statement st = connection.createStatement();
            st.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
            OKConfirmBox.display("Помилка","Немає з'єднання з базою даних");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private ResultSet executeQueryFunction(String query) {
        ResultSet resultSet = null;
        try {
            Connection connection = getDbConnection();
            Statement st = connection.createStatement();
            resultSet = st.executeQuery(query);
        } catch (SQLException e) {
            e.printStackTrace();
            OKConfirmBox.display("Помилка","Немає з'єднання з базою даних");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return resultSet;
    }

}
