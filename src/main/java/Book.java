package main.java;

public class Book
{
    private final byte type;
    private final String title;
    private final String author;
    private final long isbn;
    private final short year;
    private final String category;
    private String freeText;

    public Book(String title, String author, long isbn, short year, String category)
    {
        this.type = 0;
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.year = year;
        this.category = category;
    }
    public Book(String title, String author, long isbn, short year, String category, String freeText)
    {
        this.type = 1;
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.year = year;
        this.category = category;
        this.freeText = freeText;
    }

    /**
     * @return a String array of the book properties
     */
    public String[] toStringArray()
    {
        String[] strings;

        if (this.type == 0)
        {
            strings = new String[6];
            strings[0] = BookTypesAndCategories.TYPE_LIT.toString();
            strings[1] = title;
            strings[2] = author;
            strings[3] = String.valueOf(isbn);
            strings[4] = String.valueOf(year);
            strings[5] = category;
        }
        else
        {
            strings = new String[7];
            strings[0] = BookTypesAndCategories.TYPE_SCI.toString();
            strings[1] = title;
            strings[2] = author;
            strings[3] = String.valueOf(isbn);
            strings[4] = String.valueOf(year);
            strings[5] = category;
            strings[6] = freeText;
        }
        return strings;
    }
}
