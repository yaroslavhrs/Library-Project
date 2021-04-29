package sample.asciitable;

public class Column {
    private String header;
    private String footer;
    private HorizontalAlign headerAlign = HorizontalAlign.LEFT;
    private HorizontalAlign dataAlign = HorizontalAlign.RIGHT;
    private HorizontalAlign footerAlign = HorizontalAlign.LEFT;
    private int maxColumnWidth = 80;

    public Column() { }

    public String getHeader() {
        return header;
    }

    public String getFooter() {
        return footer;
    }

    public HorizontalAlign getHeaderAlign() {
        return headerAlign;
    }

    public HorizontalAlign getDataAlign() {
        return dataAlign;
    }

    public HorizontalAlign getFooterAlign() {
        return footerAlign;
    }

    public int getMaxColumnWidth() {
        return maxColumnWidth;
    }

    public int getHeaderWidth() {
        return header != null ? header.length() : 0;
    }

    public int getFooterWidth() {
        return footer != null ? footer.length() : 0;
    }


    public Column header(String header) {
        this.header = header;
        return this;
    }

    public Column footer(String footer) {
        this.footer = footer;
        return this;
    }

}