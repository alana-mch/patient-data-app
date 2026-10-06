package uk.ac.ucl.model;
import java.util.ArrayList;
import java.util.List;

public class DataFrame
{
    private List<Column> columns;

    public DataFrame()
    {
        columns = new ArrayList<Column>();
    } //set columns as empty list

    public List<Column> getColumns() {return columns;}  //column list can be used by other classes

    public List<String> getColumnNames()
    {
        //for each column use getName and add the column name to the list of columnNames
        List<String> columnNames = new ArrayList<String>();
        for (Column column : columns)
        {
            columnNames.add(column.getName());
        }
        return columnNames;
    }

    public int getRowCount() {return columns.getFirst().getSize();}

    public String getValue(String columnName, int row)
    {
        for (Column column: columns)
        {
            if (column.getName().equals(columnName))
            {
                return column.getRowValue(row);
            }
        }
        return "";
    }

    public void addColumn(Column column) {columns.add(column);}

    public void putValue(Column column, int row, String value) {column.setRowValue(row, value);}

    public void addValue(Column column, String value) {column.addRowValue(value);}

}
