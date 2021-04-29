package sample.database;

public class Book {
    private int id;
    private String author;
    private String name;
    private String category;
    private int year;
    private String edition;

    public Book(String author, String name, String category, int year, String edition) {
        this.author = author;
        this.name = name;
        this.category = category;
        this.year = year;
        this.edition = edition;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
}
