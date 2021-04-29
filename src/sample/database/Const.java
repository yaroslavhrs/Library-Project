package sample.database;

public class Const {
    public static final String READERS_TABLE = "readers";
    public static final String BOOKS_TABLE = "books";
    public static final String COPIES_TABLE = "copies";
    public static final String HISTORY_TABLE = "history";
    public static final String CATEGORIES_TABLE = "categories";

    public static final String READERS_ID = "id";
    public static final String READERS_NAME = "name";
    public static final String READERS_ADDRESS = "address";
    public static final String READERS_BIRTHDAY = "birthday";
    public static final String READERS_PHONE = "phone";

    public static final String BOOKS_ID = "id";
    public static final String BOOKS_AUTHOR = "author";
    public static final String BOOKS_NAME = "name";
    public static final String BOOKS_CATEGORY = "category";
    public static final String BOOKS_YEAR = "year";
    public static final String BOOKS_EDITION = "edition";

    public static final String COPIES_ID = "id";
    public static final String COPIES_BOOK_ID = "book_id";
    public static final String COPIES_STATUS = "status";
    public static final String COPIES_CODE = "code";
    public static final String COPIES_READER_ID = "reader_id";
    public static final String COPIES_ISSUE_DATE = "issue_date";
    public static final String COPIES_DEADLINE = "deadline";

    public static final String HISTORY_ID = "id";
    public static final String HISTORY_READER_ID = "reader_id";
    public static final String HISTORY_BOOK_ID = "book_id";
    public static final String HISTORY_CODE = "code";
    public static final String HISTORY_ISSUE_DATE = "issue_date";
    public static final String HISTORY_DEADLINE = "deadline";

    public static final String CATEGORIES_ID = "id";
    public static final String CATEGORIES_CATEGORY = "category";
    public static final String CATEGORIES_CATEGORY_CODE = "category_code";

}
