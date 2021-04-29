package sample.asciitable;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class AsciiTable {
    private static final int MIN_PADDING = 1;

    public static final Character[] NO_BORDERS = new Character[29];

    public static final Character[] FANCY_ASCII = {'╔', '═', '╤', '╗', '║', '│', '║',  '╠', '═',
            '╪', '╣', '║', '│', '║', '╟', '─', '┼', '╢', '╠', '═', '╪', '╣', '║', '│', '║', '╚', '═', '╧', '╝'};

    public static String getTable(String[] header, String[][] data) {
        return getTable(FANCY_ASCII, header, null, data);
    }

    public static String getTable(Character[] borderChars, String[] header, String[] footer, String[][] data) {
        String[] nonNullHeader = header != null ? header : new String[0];
        String[] nonNullFooter = footer != null ? footer : new String[0];

        Column[] headerCol = IntStream.range(0, Math.max(nonNullHeader.length, nonNullFooter.length))
                .mapToObj(index -> new Column()
                        .header(index < nonNullHeader.length ? nonNullHeader[index] : null)
                        .footer(index < nonNullFooter.length ? nonNullFooter[index] : null))
                .toArray(Column[]::new);

        return getTable(borderChars, headerCol, data);
    }

    public static String getTable(Character[] borderChars, Column[] rawColumns, String[][] data) {
        if (borderChars.length != NO_BORDERS.length) {
            throw new IllegalArgumentException("Border characters array must be exactly " + NO_BORDERS.length + " elements long");
        }

        final int numColumns = getNumColumns(rawColumns, data);
        final Column[] columns = IntStream.range(0, numColumns)
                .mapToObj(index -> index < rawColumns.length ? rawColumns[index] : new Column())
                .toArray(Column[]::new);
        final int[] colWidths = getColWidths(columns, data);

        final HorizontalAlign[] headerAligns = Arrays.stream(columns).map(Column::getHeaderAlign).toArray(HorizontalAlign[]::new);
        final HorizontalAlign[] dataAligns = Arrays.stream(columns).map(Column::getDataAlign).toArray(HorizontalAlign[]::new);
        final HorizontalAlign[] footerAligns = Arrays.stream(columns).map(Column::getFooterAlign).toArray(HorizontalAlign[]::new);

        final String[] header = Arrays.stream(columns).map(Column::getHeader).toArray(String[]::new);
        final String[] footer = Arrays.stream(columns).map(Column::getFooter).toArray(String[]::new);

        List<String> tableRows = getTableRows(colWidths, headerAligns, dataAligns, footerAligns, borderChars, header, data, footer);

        return tableRows.stream()
                .filter(line -> !line.isEmpty())
                .collect(Collectors.joining(System.lineSeparator()));
    }

    private static List<String> getTableRows(int[] colWidths, HorizontalAlign[] headerAligns,
                                             HorizontalAlign[] dataAligns, HorizontalAlign[] footerAligns,
                                             Character[] borderChars, String[] header, String[][] data, String[] footer) {
        final LinkedList<String> lines = new LinkedList<>();
        lines.add(lineRow(colWidths, borderChars[0], borderChars[1], borderChars[2], borderChars[3]));

        if (! Arrays.stream(header).allMatch(Objects::isNull)) {
            lines.addAll(dataRow(colWidths, headerAligns, header, borderChars[4], borderChars[5], borderChars[6]));
            lines.add(lineRow(colWidths, borderChars[7], borderChars[8], borderChars[9], borderChars[10]));
        }

        String contentRowBorder = lineRow(colWidths, borderChars[14], borderChars[15], borderChars[16], borderChars[17]);
        for (String[] dataRow : data) {
            lines.addAll(dataRow(colWidths, dataAligns, dataRow, borderChars[11], borderChars[12], borderChars[13]));
            lines.add(contentRowBorder);
        }
        if (data.length > 0) lines.removeLast();
        if (! Arrays.stream(footer).allMatch(Objects::isNull)) {
            lines.add(lineRow(colWidths, borderChars[18], borderChars[19], borderChars[20], borderChars[21]));
            lines.addAll(dataRow(colWidths, footerAligns, footer, borderChars[22], borderChars[23], borderChars[24]));
        }

        lines.add(lineRow(colWidths, borderChars[25], borderChars[26], borderChars[27], borderChars[28]));

        return lines;
    }

    /**
     * Повертає рядок лінії/рамки у таблиці
     */
    private static String lineRow(int[] colWidths, Character left, Character middle, Character columnSeparator, Character right) {
        final StringBuilder row = new StringBuilder(getTableWidth(colWidths));
        if (left != null) row.append((char) left);
        for (int col = 0; col < colWidths.length; col++) {
            if (middle != null) row.append(repeat(middle, colWidths[col]));
            if (columnSeparator != null && col != colWidths.length - 1) row.append((char) columnSeparator);
        }
        if (right != null) row.append((char) right);
        return row.toString();
    }

    /**
     * Повертає масив рядків у таблиці для заданих рядків заголовка і даних. Певний рядок заголовку/даних може займати
     * декілька рядків у таблиці, якщо:
     *  - Вміст рядка перевищує значення maxCharInLine для даного рядка
     *  - Зміст рядка вже багаторядковий
     */
    private static List<String> dataRow(int[] colWidths, HorizontalAlign[] horizontalAligns, String[] contents,
                                 Character left, Character columnSeparator, Character right) {
        final List<List<String>> linesContents = IntStream.range(0, colWidths.length)
                .mapToObj(i -> {
                    String text = i < contents.length && contents[i] != null ? contents[i] : "";
                    String[] paragraphs = text.split(System.lineSeparator());
                    return Arrays.stream(paragraphs)
                            .flatMap(paragraph -> splitTextIntoLinesOfMaxLength(paragraph, colWidths[i] - 2* MIN_PADDING).stream())
                            .collect(Collectors.toList());
                })
                .collect(Collectors.toList());
        final int numLines = linesContents.stream()
                .mapToInt(List::size)
                .max().orElse(0);

        final StringBuilder row = new StringBuilder(getTableWidth(colWidths));
        final List<String> lines = new LinkedList<>();
        for (int line = 0; line < numLines; line++) {
            if (left != null) row.append((char) left);
            for (int col = 0; col < colWidths.length; col++) {
                String item = linesContents.get(col).size() <= line ? "" : linesContents.get(col).get(line);
                row.append(justify(item, horizontalAligns[col], colWidths[col], MIN_PADDING));
                if (columnSeparator != null && col != colWidths.length - 1) row.append((char) columnSeparator);
            }
            if (right != null) row.append((char) right);
            lines.add(row.toString());
            row.setLength(0);
        }
        return lines;
    }

    /**
     * Повертає ширину кожного стовпця у таблиці
     */
    private static int[] getColWidths(Column[] columns, String[][] data) {
        final int[] result = new int[columns.length];

        for (String[] dataRow : data) {
            for (int col = 0; col < dataRow.length; col++) {
                result[col] = Math.max(result[col], dataRow[col] == null ? 0 : dataRow[col].length());
            }
        }

        for (int col = 0; col < columns.length; col++) {
            int length = Math.max(Math.max(columns[col].getHeaderWidth(), columns[col].getFooterWidth()), result[col]);
            result[col] = Math.min(columns[col].getMaxColumnWidth(), length + 2 * MIN_PADDING);
        }
        return result;
    }

    /**
     * Повертає максимальну кількість стовпців між рядком заголовку або будь-яким рядком даних
     */
    private static int getNumColumns(Column[] columns, String[][] data) {
        final int maxDataColumns = Arrays.stream(data)
                .mapToInt(cols -> cols.length)
                .max().orElse(0);

        return Math.max(columns.length, maxDataColumns);
    }

    /**
     * Повертає ширину кожної лінії в таблиці не враховуючи символ(и) розриву рядка
     */
    private static int getTableWidth(int[] colWidths) {
        return Arrays.stream(colWidths).sum() + MIN_PADDING * (colWidths.length + 1) - 1;
    }

    /**
     * Розділяє рядок у декілька рядків, довжина кожного з яких <= maxCharInLine. Розділ виконується через символ
     * пробілу, якщо можливо, інакше слово розривається саме на maxCharInLine.
     * Цей метод зберігає всі пробіли, окрім тих, за якими відбувається розділ, наприклад:
     * рядок "пристрої у    техніці  курс лекцій" розділяється до: ["пристрої у ", "  техніці ", "курс", "лекцій"]
     *
     * @param str Рядок для розділу
     * @param maxCharInLine Максимальна довжина кожної частини розділу
     * @return Масив рядків, що формує початковий рядок, але кожен рядок <= maxCharInLine
     */
    static List<String> splitTextIntoLinesOfMaxLength(String str, int maxCharInLine) {
        final List<String> lines = new LinkedList<>();
        final StringBuilder line = new StringBuilder(maxCharInLine);
        int offset = 0;

        while (offset < str.length() && maxCharInLine < str.length() - offset) {
            final int spaceToWrapAt = str.lastIndexOf(' ', offset + maxCharInLine);

            if (offset < spaceToWrapAt) {
                line.append(str.substring(offset, spaceToWrapAt));
                offset = spaceToWrapAt + 1;
            } else {
                line.append(str.substring(offset, offset + maxCharInLine));
                offset += maxCharInLine;
            }

            lines.add(line.toString());
            line.setLength(0);
        }

        line.append(str.substring(offset));
        lines.add(line.toString());

        return lines;
    }

    /**
     * Вирівнювання рядка по заданому горизонтальному вирівнюванню заповнюючи відступи пробілами. Перед вирівнюванням рядка,
     * мінімальний відступ додається до обох сторін. Нова довжина це повна довжина включаючи мінімальні відступи. Якщо
     * переданий рядок має максимальну довжину або більшу за неї, рядок повертається без змін.
     *
     * @param str Рядок для вирівнювання
     * @param align Горизонтальне вирівнювання
     * @param length Нова повна довжина
     * @param minPadding Довжина відступу, що додається з лівого та правого боку перед вирівнюванням
     * @return Вирівнений масив символів рядка
     */
    static char[] justify(String str, HorizontalAlign align, int length, int minPadding) {
        if (str.length() < length) {
            final char[] justified = new char[length];
            Arrays.fill(justified, ' ');
            switch (align) {
                case LEFT:
                    System.arraycopy(str.toCharArray(), 0, justified, minPadding, str.length());
                    break;

                case CENTER:
                    System.arraycopy(str.toCharArray(), 0, justified, (length - str.length()) / 2, str.length());
                    break;

                case RIGHT:
                    System.arraycopy(str.toCharArray(), 0, justified, length - str.length() - minPadding, str.length());
                    break;
            }

            return justified;
        }
        return str.toCharArray();
    }

    private static char[] repeat(char c, int num) {
        char[] repeat = new char[num];
        Arrays.fill(repeat, c);
        return repeat;
    }
}