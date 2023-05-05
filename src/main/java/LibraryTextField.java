package main.java;

import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.time.Year;

/**
 * This custom JTextField and PlainDocument
 * is used so that we can limit what the user
 * can type and paste in the field, also it is
 * useful so that we can give the user feedback
 * if what numbers he/she types are correct or
 * not
 */
public class LibraryTextField extends JTextField
{
    private final int limit;
    private boolean textLimited = true;
    private boolean onlyNumber = false;
    private boolean upperCase = false;
    private boolean allowSymbols = false;
    private boolean isBookISBN = false;
    private boolean isBookYear = false;

    public LibraryTextField(int limit)
    {
        this.limit = limit;

        this.addKeyListener(new KeyAdapter()
        {
            @Override
            public void keyReleased(KeyEvent e)
            {
                if (isBookISBN)
                {
                    if (getText().startsWith("978") || getText().startsWith("979"))
                    {
                        if (getText().length() == 13)
                        {
                            setForeground(new Color(0, 144, 0));
                        }
                        else
                        {
                            setForeground(new Color(0, 0, 255));
                        }
                    }
                    else
                    {
                        setForeground(new Color(192, 0, 0));
                    }
                }
                if (isBookYear)
                {
                    try
                    {
                        if (Short.parseShort(getText()) >= 1967 && Short.parseShort(getText()) <= Year.now().getValue())
                        {
                            setForeground(new Color(0, 144, 0));
                        }
                        else
                        {
                            setForeground(new Color(192, 0, 0));
                        }
                    }
                    catch (NumberFormatException exception)
                    {
                        // Nothing ¯\_(ツ)_/¯
                    }
                }
            }
        });
    }
    public void setOnlyNumbers(boolean b)
    {
        onlyNumber = b;
    }
    public void setUpperCase(boolean b)
    {
        upperCase = b;
    }
    public void setAllowSymbols(boolean b)
    {
        allowSymbols = b;
    }
    public void setTextLimited(boolean b)
    {
        textLimited = b;
    }
    public void setIsBookISBN(boolean b)
    {
        isBookISBN = b;
    }
    public void setIsBookYear(boolean b)
    {
        isBookYear = b;
    }
    @Override
    protected Document createDefaultModel()
    {
        return new LimitedDocument();
    }



    private class LimitedDocument extends PlainDocument
    {
        @Override
        public void insertString(int offs, String str, AttributeSet a) throws BadLocationException
        {
            if ((getLength() + str.length()) <= limit)
            {
                if (onlyNumber)
                {
                    super.insertString(offs, str.replaceAll("\\D++", ""), a);
                }
                else
                {
                    if (textLimited)
                    {
                        if (upperCase)
                        {
                            if (allowSymbols)
                            {
                                super.insertString(offs, str.toUpperCase().replaceAll("[^A-ZΑ-ΩΆ-Ώ0-9-_., ]+", ""), a);
                            }
                            else
                            {
                                super.insertString(offs, str.toUpperCase().replaceAll("[^A-ZΑ-ΩΆ-Ώ ]+", ""), a);
                            }
                        }
                        else
                        {
                            if (allowSymbols)
                            {
                                super.insertString(offs, str.replaceAll("[^a-zA-Zα-ωΑ-Ωά-ώΆ-Ώ0-9-_., ]+", ""), a);
                            }
                            else
                            {
                                super.insertString(offs, str.replaceAll("[^a-zA-Zα-ωΑ-Ωά-ώΆ-Ώ ]+", ""), a);
                            }
                        }
                    }
                    else
                    {
                        super.insertString(offs, str, a);
                    }
                }
            }
        }
    }
}
