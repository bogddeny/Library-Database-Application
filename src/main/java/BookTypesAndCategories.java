package main.java;

/**
 * This is just an enum of all the book types
 * and categories, it is useful because we
 * don't have to write these strings over and
 * over again in the program and for showing
 * the user message boxes with options
 */
public enum BookTypesAndCategories
{
    TYPE_LIT("ΛΟΓΟΤΕΧΝΙΚΟ"),
    TYPE_SCI("ΕΠΙΣΤΗΜΟΝΙΚΟ"),
    CAT_LIT_1("ΜΥΘΙΣΤΟΡΗΜΑ"),
    CAT_LIT_2("ΝΟΥΒΕΛΑ"),
    CAT_LIT_3("ΔΙΗΓΗΜΑ"),
    CAT_LIT_4("ΠΟΙΗΣΗ"),
    CAT_SCI_1("ΠΕΡΙΟΔΙΚΟ"),
    CAT_SCI_2("ΒΙΒΛΙΟ"),
    CAT_SCI_3("ΠΡΑΚΤΙΚΑ ΣΥΝΕΔΡΙΩΝ");

    private final String category;

    BookTypesAndCategories(String string)
    {
        category = string;
    }
    @Override
    public String toString()
    {
        return category;
    }
}
