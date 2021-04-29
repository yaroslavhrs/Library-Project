package sample.database;

public class CopyOfBook {

    private int id;
    private int visual_id;
    private int book_id;
    private String status;
    private String code;
    private int reader_id;
    private String issue_date;
    private String deadline;

    public CopyOfBook(int book_id, String status, String code, int reader_id, String issue_date, String deadline) {
        this.book_id = book_id;
        this.status = status;
        this.code = code;
        this.reader_id = reader_id;
        this.issue_date = issue_date;
        this.deadline = deadline;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getVisual_id() {
        return visual_id;
    }

    public void setVisual_id(int visual_id) {
        this.visual_id = visual_id;
    }

    public int getBook_id() {
        return book_id;
    }

    public void setBook_id(int book_id) {
        this.book_id = book_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
