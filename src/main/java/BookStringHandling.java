package main.java;

import javax.swing.*;
import java.time.Year;
import java.util.HashMap;
import java.util.Map;

public class BookStringHandling
{
    /**
     * This method is useful to check all the given input
     * and if it is compliant with the rules
     * @param title String of the title of the book
     * @param author String of the author of the book
     * @param isbn String of the isbn of the book
     * @param year String of the year of the book
     * @param freeText String of the freeText of the book
     * @param checkFreeText should the freeText string be checked
     * @param warnings show warnings to the user using message boxes
     * @return false if any of the input was incorrect
     */
    public boolean checkBookInput(String title, String author, String isbn, String year, String freeText, boolean checkFreeText, boolean warnings)
    {
        if (title.isEmpty())
        {
            if (warnings)
            {
                JOptionPane.showMessageDialog(Library.window, "Error: Title field empty", "Error", JOptionPane.ERROR_MESSAGE);
            }
            return false;
        }
        if (author.isEmpty())
        {
            if (warnings)
            {
                JOptionPane.showMessageDialog(Library.window, "Error: Author field empty", "Error", JOptionPane.ERROR_MESSAGE);
            }
            return false;
        }
        if (!isLong(isbn))
        {
            if (warnings)
            {
                JOptionPane.showMessageDialog(Library.window, "Error: ISBN not numeric", "Error", JOptionPane.ERROR_MESSAGE);
            }
            return false;
        }
        if (isbn.length() != 13)
        {
            if (warnings)
            {
                JOptionPane.showMessageDialog(Library.window, "Error: Invalid ISBN length", "Error", JOptionPane.ERROR_MESSAGE);
            }
            return false;
        }
        if (!isbn.startsWith("978") && !isbn.startsWith("979"))
        {
            if (warnings)
            {
                JOptionPane.showMessageDialog(Library.window, "Error: ISBN starts with wrong number", "Error", JOptionPane.ERROR_MESSAGE);
            }
            return false;
        }
        for (int i = 0; i < Library.getBookISBNArray().length; i++)
        {
            if (Library.getBookISBNArray()[i].equals(isbn))
            {
                if (warnings)
                {
                    JOptionPane.showMessageDialog(Library.window, "Error: ISBN already exists", "Error", JOptionPane.ERROR_MESSAGE);
                }
                return false;
            }
        }
        if (!isShort(year))
        {
            if (warnings)
            {
                JOptionPane.showMessageDialog(Library.window, "Error: Year is not numeric", "Error", JOptionPane.ERROR_MESSAGE);
            }
            return false;
        }
        if (Short.parseShort(year) > Year.now().getValue() || Short.parseShort(year) < 1967)
        {
            if (warnings)
            {
                JOptionPane.showMessageDialog(Library.window, "Error: Invalid Year", "Error", JOptionPane.ERROR_MESSAGE);
            }
            return false;
        }
        if (checkFreeText)
        {
            if (freeText.isEmpty())
            {
                if (warnings)
                {
                    JOptionPane.showMessageDialog(Library.window, "Error: No extra text", "ERROR", JOptionPane.ERROR_MESSAGE);
                }
                return false;
            }
        }
        return true;
    }

    /**
     * @param string the String that should be checked
     * @return true if the String can be parsed as a long
     */
    private boolean isLong(String string)
    {
        try
        {
            // Variable is useless, it just stops an annoying warning from IntelliJ IDEA that Long.parseLong is not used
            long l = Long.parseLong(string);
            return true;
        }
        catch (NumberFormatException exception)
        {
            return false;
        }
    }

    /**
     * @param string the String that should be checked
     * @return true if the String can be parsed as a short
     */
    private boolean isShort(String string)
    {
        try
        {
            // Variable is useless, it just stops an annoying warning from IntelliJ IDEA that Short.parseShort is not used
            short s = Short.parseShort(string);
            return true;
        }
        catch (NumberFormatException exception)
        {
            return false;
        }
    }

    // This is a hashmap of the greek characters that have accents and what they should be replaced with
    private static final Map<Character, Character> MAP_ACCENT;
    static
    {
        MAP_ACCENT = new HashMap<>();
        MAP_ACCENT.put('Ά', 'Α');
        MAP_ACCENT.put('ά', 'α');

        MAP_ACCENT.put('Έ', 'Ε');
        MAP_ACCENT.put('έ', 'ε');

        MAP_ACCENT.put('Ή', 'Η');
        MAP_ACCENT.put('ή', 'η');

        MAP_ACCENT.put('Ί', 'Ι');
        MAP_ACCENT.put('ί', 'ι');

        MAP_ACCENT.put('Ό', 'Ο');
        MAP_ACCENT.put('ό', 'ο');

        MAP_ACCENT.put('Ύ', 'Υ');
        MAP_ACCENT.put('ύ', 'υ');

        MAP_ACCENT.put('Ώ', 'Ω');
        MAP_ACCENT.put('ώ', 'ω');
    }

    /**
     * @param string String to remove greek accents from
     * @return the given String with greek accent characters replaced with non-accented ones
     */
    public String removeGreekAccents(String string)
    {
        if (string == null)
        {
            return null;
        }
        StringBuilder stringBuilder = new StringBuilder(string);

        for (int i = 0; i < string.length(); i++)
        {
            Character character = MAP_ACCENT.get(stringBuilder.charAt(i));

            if (character != null)
            {
                stringBuilder.setCharAt(i, character);
            }
        }
        return stringBuilder.toString();
    }

    // This is a hashmap of the greek characters that should be replaced with others for use in search
    private static final Map<Character, Character> MAP_SPELL;
    static
    {
        MAP_SPELL = new HashMap<>();
        MAP_SPELL.put('Ι', 'Η');
        MAP_SPELL.put('Υ', 'Η');
        MAP_SPELL.put('Ω', 'Ο');
    }

    /**
     * @param string String to remove greek spelling from
     * @return the given String with greek spelling characters replaced with default ones
     */
    public String removeGreekSpelling(String string)
    {
        if (string == null)
        {
            return null;
        }
        String temp = string.replace("ΑΙ", "Ε").replace("ΕΙ", "Η").replace("ΟΙ", "Η");
        StringBuilder stringBuilder = new StringBuilder(temp);

        for (int i = 0; i < temp.length(); i++)
        {
            Character character = MAP_SPELL.get(stringBuilder.charAt(i));

            if (character != null)
            {
                stringBuilder.setCharAt(i, character);
            }
        }
        return stringBuilder.toString();
    }

    /**
     * This method just returns the same
     * string without the characters that
     * are not to be accepted by the program
     * @param string the String to have all not acceptable characters removed from
     * @param allowSymNum if symbols and numbers allowed by the program are to be kept or replaced
     * @return String with removed unacceptable characters
     */
    public String removeUnacceptedCharacters(String string, boolean allowSymNum)
    {
        if (string == null)
        {
            return null;
        }
        if (allowSymNum)
        {
            return string.replaceAll("[^a-zA-Zα-ωΑ-Ωά-ώΆ-Ώ0-9-_., ]+", "");
        }
        else
        {
            return string.replaceAll("[^a-zA-Zα-ωΑ-Ωά-ώΆ-Ώ ]+", "");
        }
    }

    /**
     * @param string the String that should be checked
     * @return true if the String matches any of the book literature categories
     */
    public boolean isBookLitCategory(String string)
    {
        if (string == null)
        {
            return false;
        }
        return removeGreekAccents(removeUnacceptedCharacters(string.toUpperCase(), false)).equals(BookTypesAndCategories.CAT_LIT_1.toString()) ||
                removeGreekAccents(removeUnacceptedCharacters(string.toUpperCase(), false)).equals(BookTypesAndCategories.CAT_LIT_2.toString()) ||
                removeGreekAccents(removeUnacceptedCharacters(string.toUpperCase(), false)).equals(BookTypesAndCategories.CAT_LIT_3.toString()) ||
                removeGreekAccents(removeUnacceptedCharacters(string.toUpperCase(), false)).equals(BookTypesAndCategories.CAT_LIT_4.toString());
    }

    /**
     * @param string the String that should be checked
     * @return true if the String matches any of the book science categories
     */
    public boolean isBookSciCategory(String string)
    {
        if (string == null)
        {
            return false;
        }
        return removeGreekAccents(removeUnacceptedCharacters(string.toUpperCase(), false)).equals(BookTypesAndCategories.CAT_SCI_1.toString()) ||
                removeGreekAccents(removeUnacceptedCharacters(string.toUpperCase(), false)).equals(BookTypesAndCategories.CAT_SCI_2.toString()) ||
                removeGreekAccents(removeUnacceptedCharacters(string.toUpperCase(), false)).equals(BookTypesAndCategories.CAT_SCI_3.toString());
    }

    /**
     * @param string the String that should be checked
     * @return true if the String matches any of the allowed book types
     */
    public boolean isBookType(String string)
    {
        if (string == null)
        {
            return false;
        }
        return isLiteratureBook(string) || isScienceBook(string);
    }

    /**
     * @param string the String that should be checked
     * @return true if the String matches with the literature category string
     */
    public boolean isLiteratureBook(String string)
    {
        if (string == null)
        {
            return false;
        }
        return removeGreekAccents(removeUnacceptedCharacters(string.toUpperCase(), false)).equals(BookTypesAndCategories.TYPE_LIT.toString());
    }

    /**
     * @param string the String that should be checked
     * @return true if the String matches with the science category string
     */
    public boolean isScienceBook(String string)
    {
        if (string == null)
        {
            return false;
        }
        return removeGreekAccents(removeUnacceptedCharacters(string.toUpperCase(), false)).equals(BookTypesAndCategories.TYPE_SCI.toString());
    }
}
