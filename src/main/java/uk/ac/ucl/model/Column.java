package uk.ac.ucl.model;
import java.util.ArrayList;
import java.util.List;

public class Column
{
    private String name;
    private List<String> rows;

    public Column(String name)
    {
        this.name = name;
        rows = new ArrayList<String>();
    }

    public String getName() {return name;}

    public List<String> getRows() {return rows;}

    public int getSize() {return rows.size();}

    public String getRowValue(int row) {return rows.get(row);}

    public void setRowValue(int row, String value) {rows.set(row, value);}

    public void addRowValue(String value) {rows.add(value);}

    public void deleteRowValue(int row) {rows.remove(row);}
}
