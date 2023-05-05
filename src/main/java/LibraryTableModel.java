package main.java;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

/**
 * This custom table model is used
 * to make the last column of the
 * table an Icon instead of an object
 * and at the same time make all the
 * cells in the table not editable.
 */
public class LibraryTableModel extends DefaultTableModel
{
    private final String[] columns;

    public LibraryTableModel(Object[][] data, String[] columns)
    {
        super(data, columns);
        this.columns = columns;
    }
    @Override
    public Class<?> getColumnClass(int columnIndex)
    {
        return (columnIndex == columns.length - 1) ? Icon.class : Object.class;
    }
    @Override
    public boolean isCellEditable(int row, int column)
    {
        return false;
    }
}
