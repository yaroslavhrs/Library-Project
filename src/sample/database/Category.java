package sample.database;

public class Category {
    private int id;
    private int visual_id;
    private String category;
    private String category_code;

    public Category(int id, String category, String category_code) {
        this.id = id;
        this.category = category;
        this.category_code = category_code;
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCategory_code() {
        return category_code;
    }

    public void setCategory_code(String category_code) {
        this.category_code = category_code;
    }
}
