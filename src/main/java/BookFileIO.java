package main.java;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This class is used purely for
 * handling book file IO
 */
public class BookFileIO
{
    private static final BookStringHandling stringHandling = new BookStringHandling();

    /**
     * This method uses the list of
     * books to create a new file
     * OR write to an existing file
     * It uses a StringBuffer to
     * create the desired file
     * structure (all book elements
     * on separate lines + empty
     * line after each book)
     * @param fileName is a String name of the file that will be written
     */
    public void writeFile(String fileName)
    {
        try
        {
            StringBuilder buffer = new StringBuilder();

            for (int i = 0; i < Library.books.size(); i++)
            {
                for (int j = 0; j < Library.books.get(i).toStringArray().length; j++)
                {
                    buffer.append(Library.books.get(i).toStringArray()[j]);
                    buffer.append("\n");

                    if (j == Library.books.get(i).toStringArray().length - 1)
                    {
                        buffer.append("\n");
                    }
                }
            }
            FileWriter fileWriter = new FileWriter(fileName);
            fileWriter.write(buffer.toString());
            fileWriter.close();
        }
        catch (IOException exception)
        {
            JOptionPane.showMessageDialog(Library.window, "An error occurred while trying to write to file...", "ERROR", JOptionPane.ERROR_MESSAGE);
            exception.printStackTrace();
        }
    }

    /**
     * This method is used for reading
     * books from a file, it collects all
     * lines of the file to a list and then
     * makes a table based on the amount
     * of books in the file for the ease of
     * error checking then it adds new books
     * to the books list in the Library class
     * @param fileName is a String name of the file that will be read and if it doesn't exist created
     */
    public void readFile(String fileName)
    {
        // This prevents a exception that happens when the file does not exist, it just creates that file
        try
        {
            File file = new File(fileName);

            if (file.createNewFile())
            {
                JOptionPane.showMessageDialog(Library.window, "Books.txt doesn't exist so we created it...", "Warning", JOptionPane.WARNING_MESSAGE);
            }
        }
        catch (IOException exception)
        {
            JOptionPane.showMessageDialog(Library.window, "An error occurred while creating Books.txt file...", "ERROR", JOptionPane.ERROR_MESSAGE);
            exception.printStackTrace();
        }
        try
        {
            // Create a new BufferedReader and uses the built in java Collectors to add each line of the file to a list
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            List<String> readList = reader.lines().collect(Collectors.toList());
            reader.close();

            int booksCount = 0;

            // Counts how many books there are in the file (based only on the book type)
            for (String str : readList)
            {
                if (stringHandling.isBookType(str))
                {
                    booksCount++;
                }
            }
            // Creates a new 2d array based on the amount of book counted above
            String[][] bookStringArray = new String[booksCount][7];
            //System.out.println(booksCount); // DEBUG

            boolean book = false;
            int id = -1;
            int line = 0;

            /*
            Fills up the bookStringArray based on the data from the readList
            Uses book flag to know if we are reading book data or the book has stopped (empty line)
            Uses id to know which book are we on at the moment (first dimension of the array)
            Uses line to know which part of the book data are we on (second dimension of the array)
             */
            for (String str : readList)
            {
                if (stringHandling.isBookType(str))
                {
                    book = true;
                    id++;
                    line = 0;
                }
                if (str.isEmpty())
                {
                    book = false;
                }
                if (book)
                {
                    if (line < 7)
                    {
                        //System.out.println(id + "-" + line + "\n"); // DEBUG
                        bookStringArray[id][line] = str;
                        line++;
                    }
                }
            }
            readList.clear(); // Clears the list, since we no longer need it
            //System.out.println(Arrays.deepToString(readTable)); // DEBUG

            int invalidBooks = 0;

            /*
            Adds new books to the books list of the class Library
            while checking for errors in each book, if there are
            errors, a message will be displayed to the user about how
            many invalid books there were
             */
            for (int i = 0; i < booksCount; i++)
            {
                if (stringHandling.isLiteratureBook(bookStringArray[i][0]))
                {
                    if (bookStringArray[i][1] == null || bookStringArray[i][2] == null || bookStringArray[i][3] == null || bookStringArray[i][4] == null || bookStringArray[i][5] == null)
                    {
                        invalidBooks++;
                    }
                    else
                    {
                        String title = stringHandling.removeGreekAccents(stringHandling.removeUnacceptedCharacters(bookStringArray[i][1].toUpperCase(), true));
                        String author = stringHandling.removeGreekAccents(stringHandling.removeUnacceptedCharacters(bookStringArray[i][2].toUpperCase(), false));
                        String isbn = stringHandling.removeUnacceptedCharacters(bookStringArray[i][3], true);
                        String year = stringHandling.removeUnacceptedCharacters(bookStringArray[i][4], true);
                        String category = stringHandling.removeGreekAccents(stringHandling.removeUnacceptedCharacters(bookStringArray[i][5].toUpperCase(), false));

                        if (title.length() > 50)
                        {
                            title = title.substring(0, 50);
                        }
                        if (author.length() > 50)
                        {
                            author = author.substring(0, 50);
                        }

                        if (stringHandling.checkBookInput(title, author, isbn, year, null, false, false))
                        {
                            if (stringHandling.isBookLitCategory(category))
                            {
                                Library.books.add(new Book(title, author, Long.parseLong(isbn), Short.parseShort(year), category));
                            }
                            else
                            {
                                invalidBooks++;
                            }
                        }
                        else
                        {
                            invalidBooks++;
                        }
                    }
                }
                else if (stringHandling.isScienceBook(bookStringArray[i][0]))
                {
                    if (bookStringArray[i][1] == null || bookStringArray[i][2] == null || bookStringArray[i][3] == null || bookStringArray[i][4] == null || bookStringArray[i][5] == null || bookStringArray[i][6] == null)
                    {
                        invalidBooks++;
                    }
                    else
                    {
                        String title = stringHandling.removeGreekAccents(stringHandling.removeUnacceptedCharacters(bookStringArray[i][1].toUpperCase(), true));
                        String author = stringHandling.removeGreekAccents(stringHandling.removeUnacceptedCharacters(bookStringArray[i][2].toUpperCase(), false));
                        String isbn = stringHandling.removeUnacceptedCharacters(bookStringArray[i][3], true);
                        String year = stringHandling.removeUnacceptedCharacters(bookStringArray[i][4], true);
                        String category = stringHandling.removeGreekAccents(stringHandling.removeUnacceptedCharacters(bookStringArray[i][5].toUpperCase(), false));
                        String freeText = bookStringArray[i][6].replaceAll("\\R", "");

                        if (title.length() > 50)
                        {
                            title = title.substring(0, 50);
                        }
                        if (author.length() > 50)
                        {
                            author = author.substring(0, 50);
                        }

                        if (stringHandling.checkBookInput(title, author, isbn, year, freeText, true, false))
                        {
                            if (stringHandling.isBookSciCategory(category))
                            {
                                Library.books.add(new Book(title, author, Long.parseLong(isbn), Short.parseShort(year), category, freeText));
                            }
                            else
                            {
                                invalidBooks++;
                            }
                        }
                        else
                        {
                            invalidBooks++;
                        }
                    }
                }
            }
            if (invalidBooks > 0)
            {
                if (invalidBooks == 1)
                {
                    JOptionPane.showMessageDialog(Library.window, String.format("There was %d invalid book in the Books.txt file so we ignore it,\n Defective book will be removed once the program closes.", invalidBooks), "Warning", JOptionPane.WARNING_MESSAGE);
                }
                else
                {
                    JOptionPane.showMessageDialog(Library.window, String.format("There were %d invalid books in the Books.txt file so we ignore them,\n Defective books will be removed once the program closes.", invalidBooks), "Warning", JOptionPane.WARNING_MESSAGE);
                }
            }
        }
        catch (HeadlessException | IOException | NumberFormatException exception)
        {
            JOptionPane.showMessageDialog(Library.window, "An error occurred while trying to read from file...", "ERROR", JOptionPane.ERROR_MESSAGE);
            exception.printStackTrace();
        }
    }
}
