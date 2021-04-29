package sample.database;

public class ReaderAccountBookItem {
    private int id;
    private int book_id;
    private String author;
    private String name;
    private String category;
    private int year;
    private String edition;
    private String code;
    private int reader_id;
    private String issue_date;
    private String deadline;

    public ReaderAccountBookItem(int id, int book_id, String author, String name, String category, int year,
                                 String edition, String code, String issue_date, String deadline) {
        this.id = id;
        this.book_id = book_id;
        this.author = author;
        this.name = name;
        this.category = category;
        this.year = year;
        this.edition = edition;
        this.code = code;
        this.issue_date = issue_date;
        this.deadline = deadline;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBook_id() {
        return book_id;
    }

    public void setBook_id(int book_id) {
        this.book_id = book_id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getEdition() {
        return edition;
    }

    public void setEdition(String edition) {
        this.edition = edition;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getReader_id() {
        return reader_id;
    }

    public void setReader_id(int reader_id) {
        this.reader_id = reader_id;
    }

    public String getIssue_date() {
        return issue_date;
    }

    public void setIssue_date(String issue_date) {
        this.issue_date = issue_date;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }
}
