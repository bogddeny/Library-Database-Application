package main.java;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/*
All the books that in the books.txt right now are fully randomly generated using:
https://blog.reedsy.com/book-title-generator/
https://www.name-generator.org.uk/pen-name/
https://www.random.org/strings/?num=10&len=10&digits=on&unique=on&format=html&rnd=new
https://www.random.org/strings/?num=1&len=10&upperalpha=on&loweralpha=on&unique=on&format=html&rnd=new
 */
public class Library
{
    // Creates a list of books (we use list since we need a mutable array)
    public static final List<Book> books = new ArrayList<>();
    private static final BookFileIO bookFileIO = new BookFileIO();
    private static final BookStringHandling stingHandling = new BookStringHandling();
    private static final String FILE_NAME = "Books.txt";

    private final ImageIcon iconDelete = new ImageIcon(System.getProperty("user.dir") + "/resources" + "/textures/delete_icon.png");
    private final ImageIcon iconRemove = new ImageIcon(System.getProperty("user.dir") + "/resources" + "/textures/remove_icon.png");
    private final ImageIcon iconSearch = new ImageIcon(System.getProperty("user.dir") + "/resources" + "/textures/search_icon.png");
    private final ImageIcon iconAdd = new ImageIcon(System.getProperty("user.dir") + "/resources" + "/textures/add_icon.png");

    // Initialize the JFrame here since we need to use it in other classes for warning ect.
    public static final JFrame window = new JFrame();
    private final JTable tableBooks;

    private Library()
    {
        // Tries to make the look and feel of the program match the look and feel of your current OS (looks horrible in linux)
        try
        {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch (ClassNotFoundException | UnsupportedLookAndFeelException | InstantiationException | IllegalAccessException exception)
        {
            exception.printStackTrace();
        }
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        /* START PANEL CONTENT */
        tableBooks = new JTable();
        tableBooks.setGridColor(new Color(235, 235, 235));
        tableBooks.setSelectionForeground(new Color(0, 0, 0));
        tableBooks.setSelectionBackground(new Color(192, 192, 192));
        tableBooks.setRowHeight(20);
        tableBooks.setAutoCreateRowSorter(true);
        tableBooks.getTableHeader().setReorderingAllowed(false);
        tableBooks.getTableHeader().setResizingAllowed(false);
        JScrollPane scrollableTable = new JScrollPane(tableBooks);

        JPanel panelContent = new JPanel();
        panelContent.setLayout(new BorderLayout());
        panelContent.add(scrollableTable);
        /* END PANEL CONTENT */

        /* START PANEL BUTTON */
        JButton buttonList = new JButton("List");
        JButton buttonSearch = new JButton("Search");
        JButton buttonAdd = new JButton("Add");
        JButton buttonExit = new JButton("Exit");
        // This is the only way I found to get the current font used by the program
        Font font = new Font(buttonList.getFont().getName(), Font.BOLD, 14);
        buttonList.setFont(font);
        buttonSearch.setFont(font);
        buttonAdd.setFont(font);
        buttonExit.setFont(font);

        JPanel panelButton = new JPanel();
        panelButton.setLayout(new GridLayout());
        panelButton.setPreferredSize(new Dimension(0, screenSize.height / 24)); // Width is handled by the layout manager
        panelButton.add(buttonList);
        panelButton.add(buttonSearch);
        panelButton.add(buttonAdd);
        panelButton.add(buttonExit);
        /* END PANEL BUTTON */

        /* START FRAME */
        ImageIcon iconWindow = new ImageIcon(System.getProperty("user.dir") + "/resources" + "/textures/window_icon.png");
        window.setIconImage(iconWindow.getImage());

        window.setTitle("Library");
        window.setBounds(screenSize.width / 8, screenSize.height / 8, screenSize.width / 4 * 3, screenSize.height / 4 * 3);
        window.setMinimumSize(new Dimension(screenSize.width / 4, screenSize.height / 4));
        window.setVisible(true);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setLayout(new BorderLayout());

        window.add(panelContent, BorderLayout.CENTER);
        window.add(panelButton, BorderLayout.SOUTH);
        /* END FRAME */

        /*
        Creates a new listener that acts whenever the user left-clicks on the last cell of the
        table (which is the delete icon) and deletes the appropriate book by search which book
        the user clicked using the ISBN of the books list and the isbn of the table since it is
        unique to each book. Also, if the column the user clicked on is not the last one and the
        used double-clicked shows a message box to the user with the content of the cell the user
        clicked on
         */
        tableBooks.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                int row = tableBooks.rowAtPoint(e.getPoint());
                int column = tableBooks.columnAtPoint(e.getPoint());

                if (e.getButton() == MouseEvent.BUTTON1)
                {
                    if (column == tableBooks.getColumnCount() - 1)
                    {
                        int userInput = JOptionPane.showConfirmDialog(window, "Are you sure you want to delete this book?", "DELETION", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, iconRemove);

                        if (userInput == JOptionPane.YES_OPTION)
                        {
                            for (int i = 0; i < books.size(); i++)
                            {
                                if (tableBooks.getModel().getValueAt(tableBooks.convertRowIndexToModel(row), 3).toString().equals(getBookISBNArray()[i]))
                                {
                                    books.remove(i);
                                    updateTable();
                                    JOptionPane.showMessageDialog(window, "Book successfully removed.");
                                    break;
                                }
                            }
                        }
                    }
                    else if (e.getClickCount() == 2)
                    {
                        if (tableBooks.getModel().getValueAt(tableBooks.convertRowIndexToModel(row), tableBooks.convertColumnIndexToModel(column)) != null && column != tableBooks.getColumnCount() - 1)
                        {
                            JTextArea textArea = new JTextArea(10, 40);
                            textArea.setText(tableBooks.getModel().getValueAt(tableBooks.convertRowIndexToModel(row), tableBooks.convertColumnIndexToModel(column)).toString());
                            textArea.setEditable(false);
                            textArea.setLineWrap(true);
                            textArea.setWrapStyleWord(true);
                            textArea.setCaretPosition(0);
                            JScrollPane scrollPane = new JScrollPane(textArea);
                            JOptionPane.showMessageDialog(window, scrollPane, "Details", JOptionPane.PLAIN_MESSAGE);
                        }
                    }
                }
            }
        });

        // Whenever any of the four buttons are clicked: calls appropriate method

        buttonList.addActionListener(e -> updateTable());

        buttonSearch.addActionListener(e -> searchTable());

        buttonAdd.addActionListener(e -> addBookToTable());

        buttonExit.addActionListener(e -> exitPrompt());

        updateTable();
    }
    public static void main(String[] args)
    {
        /*
        When the program starts it reads the file and then shows the window, buttons table ect.
        Also adds a shutdown hook so that when the program closes by the user the books in the list
        are written to the file
         */
        bookFileIO.readFile(FILE_NAME);
        new Library();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> bookFileIO.writeFile(FILE_NAME)));
    }

    /**
     * Creates a new table model for the books
     * based on the amount of books and sets the
     * model of the tableBooks to the created
     * table model
     */
    private void updateTable()
    {
        String[] columns = new String[] { "Type", "Title", "Author", "ISBN", "Year", "Category", "Free Text", "" };
        Object[][] data = new Object[books.size()][columns.length];

        for (int i = 0; i < books.size(); i++)
        {
            for (int j = 0; j < books.get(i).toStringArray().length; j++)
            {
                data[i][j] = books.get(i).toStringArray()[j];
            }
            data[i][columns.length - 1] = iconDelete;
        }
        LibraryTableModel model = new LibraryTableModel(data, columns);
        tableBooks.setModel(model);
        tableBooks.getColumnModel().getColumn(columns.length - 1).setMaxWidth(20);
    }

    /**
     * Uses the book ISBN to search for matches
     * (again since it is unique to each book)
     * and stores the matched books in a list of
     * integers then uses that list to create a
     * new table model with the matched books
     */
    private void searchTable()
    {
        boolean matched = false;
        List<Integer> matches = new ArrayList<>();

        LibraryTextField userInputField = new LibraryTextField(50);
        userInputField.setUpperCase(true);
        userInputField.setAllowSymbols(true);
        Object[] userInput = {"Title/Author Name: ", userInputField};
        int result = JOptionPane.showConfirmDialog(window, userInput, "Search Book", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, iconSearch);

        if (result == JOptionPane.OK_OPTION && !userInputField.getText().isEmpty())
        {
            for (int i = 0; i < books.size(); i++)
            {
                for (int j = 1; j < 3; j++)
                {
                    if (stingHandling.removeGreekSpelling(books.get(i).toStringArray()[j]).contains(stingHandling.removeGreekSpelling(stingHandling.removeGreekAccents(userInputField.getText().toUpperCase()))))
                    {
                        if (!matches.contains(i))
                        {
                            matches.add(i);
                        }
                        matched = true;
                    }
                }
            }
            if (matched)
            {
                String[] columns = new String[] { "Type", "Title", "Author", "ISBN", "Year", "Category", "Free Text", "" };
                Object[][] data = new Object[matches.size()][columns.length];

                for (int i = 0; i < matches.size(); i++)
                {
                    for (int j = 0; j < books.get(matches.get(i)).toStringArray().length; j++)
                    {
                        data[i][j] = books.get(matches.get(i)).toStringArray()[j];
                    }
                    data[i][columns.length - 1] = iconDelete;
                }
                LibraryTableModel model = new LibraryTableModel(data, columns);
                tableBooks.setModel(model);
                tableBooks.getColumnModel().getColumn(columns.length - 1).setMaxWidth(20);
            }
            else
            {
                JOptionPane.showMessageDialog(window, "There is no book or author with that name.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * A lot of things... too lazy to type
     * but basically adds user inputted books
     * to the list of books
     */
    private void addBookToTable()
    {
        LibraryTextField titleField = new LibraryTextField(50);
        titleField.setUpperCase(true);
        titleField.setAllowSymbols(true);
        titleField.setToolTipText("Must be English or Greek letters (, . - _ and numbers are allowed)");

        LibraryTextField authorField = new LibraryTextField(50);
        authorField.setUpperCase(true);
        authorField.setToolTipText("Must be English or Greek letters");

        LibraryTextField isbnField = new LibraryTextField(13);
        isbnField.setOnlyNumbers(true);
        isbnField.setIsBookISBN(true);
        isbnField.setToolTipText("Must start with 978 or 979 and be 13 digits long (Red - Incorrect, Blue - Correct Start, Green - Good)");

        LibraryTextField yearField = new LibraryTextField(4);
        yearField.setOnlyNumbers(true);
        yearField.setIsBookYear(true);
        yearField.setToolTipText("Must be more than 1967 and not more than the current year (Red - Incorrect, Green - Good)");

        JTextArea freeTextArea = new JTextArea(5, 30);
        freeTextArea.setLineWrap(true);
        freeTextArea.setWrapStyleWord(true);
        freeTextArea.setToolTipText("Must contain some free text (without restrictions)");
        JScrollPane scrollableFreeTextArea = new JScrollPane(freeTextArea);

        Object[] optionsType = {BookTypesAndCategories.TYPE_LIT, BookTypesAndCategories.TYPE_SCI};

        int type = JOptionPane.showOptionDialog(window, "Please select a book type...", "Book Type", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, optionsType, optionsType[0]);

        if (type == 0)
        {
            String[] optionsLiteratureCategory = {BookTypesAndCategories.CAT_LIT_1.toString(), BookTypesAndCategories.CAT_LIT_2.toString(), BookTypesAndCategories.CAT_LIT_3.toString(), BookTypesAndCategories.CAT_LIT_4.toString()};
            JComboBox<String> comboBoxCategory = new JComboBox<>(optionsLiteratureCategory);

            Object[] literature = {
                    "Title:", titleField,
                    "Author:", authorField,
                    "ISBN:", isbnField,
                    "Year:", yearField,
                    "Category:", comboBoxCategory
            };

            int result = JOptionPane.showConfirmDialog(window, literature, "New Book", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, iconAdd);

            if (result == JOptionPane.OK_OPTION)
            {
                if (stingHandling.checkBookInput(
                        stingHandling.removeGreekAccents(stingHandling.removeUnacceptedCharacters(titleField.getText().toUpperCase(), true)),
                        stingHandling.removeGreekAccents(stingHandling.removeUnacceptedCharacters(authorField.getText().toUpperCase(), false)),
                        isbnField.getText(),
                        yearField.getText(),
                        null,
                        false, true))
                {
                    books.add(new Book(
                            stingHandling.removeGreekAccents(stingHandling.removeUnacceptedCharacters(titleField.getText().toUpperCase(), true)),
                            stingHandling.removeGreekAccents(stingHandling.removeUnacceptedCharacters(authorField.getText().toUpperCase(), false)),
                            Long.parseLong(isbnField.getText()),
                            Short.parseShort(yearField.getText()),
                            Objects.requireNonNull(comboBoxCategory.getSelectedItem()).toString()));
                }
            }
        }
        else if (type == 1)
        {
            String[] optionsScienceCategory = {BookTypesAndCategories.CAT_SCI_1.toString(), BookTypesAndCategories.CAT_SCI_2.toString(), BookTypesAndCategories.CAT_SCI_3.toString()};
            JComboBox<String> comboBoxCategory = new JComboBox<>(optionsScienceCategory);

            Object[] science = {
                    "Title:", titleField,
                    "Author:", authorField,
                    "ISBN:", isbnField,
                    "Year:", yearField,
                    "Category:", comboBoxCategory,
                    "Free Text:", scrollableFreeTextArea
            };

            int result = JOptionPane.showConfirmDialog(window, science, "New Book", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, iconAdd);

            if (result == JOptionPane.OK_OPTION)
            {
                if (stingHandling.checkBookInput(
                        stingHandling.removeGreekAccents(stingHandling.removeUnacceptedCharacters(titleField.getText().toUpperCase(), true)),
                        stingHandling.removeGreekAccents(stingHandling.removeUnacceptedCharacters(authorField.getText().toUpperCase(), false)),
                        isbnField.getText(),
                        yearField.getText(),
                        freeTextArea.getText(),
                        true, true))
                {
                    books.add(new Book(
                            stingHandling.removeGreekAccents(stingHandling.removeUnacceptedCharacters(titleField.getText().toUpperCase(), true)),
                            stingHandling.removeGreekAccents(stingHandling.removeUnacceptedCharacters(authorField.getText().toUpperCase(), false)),
                            Long.parseLong(isbnField.getText()),
                            Short.parseShort(yearField.getText()),
                            Objects.requireNonNull(comboBoxCategory.getSelectedItem()).toString(),
                            freeTextArea.getText().replaceAll("\\R", "")));
                }
            }
        }
    }

    /**
     * Shows an exit prompt to the user
     */
    private void exitPrompt()
    {
        int result = JOptionPane.showConfirmDialog(window, "Are you sure you want to exit?", "Exit", JOptionPane.YES_NO_OPTION);

        if (result == JOptionPane.YES_OPTION)
        {
            System.exit(0);
        }
    }

    /**
     * @return an array of all book ISBNs that is used for search and for knowing if the book already exists when adding a new one (either from the program or the file)
     */
    public static String[] getBookISBNArray()
    {
        String[] bookISBNs = new String[books.size()];

        for (int i = 0; i < books.size(); i++)
        {
            bookISBNs[i] = books.get(i).toStringArray()[3];
        }
        return bookISBNs;
    }
}
