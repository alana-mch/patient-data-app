package uk.ac.ucl.model;
import java.io.FileNotFoundException;
import java.io.IOError;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.io.File;


public class DataLoader
{
    private DataFrame dataFrame;

    public DataLoader()
    {
        dataFrame = new DataFrame();
    }

    public void readCSV(String fileName) throws IOException
    {
        try (Scanner scanner = new Scanner(new File(fileName), "UTF-8"))
        {
            //try with resources, a scanner object
            if (scanner.hasNextLine()) //the first line is column names
            {
                String firstLine = scanner.nextLine();
                setColumnNames(processLine(firstLine)); //process line splits on comma and generates a list
            }
            while (scanner.hasNextLine()) // for all other lines
            {
                String line = scanner.nextLine();
                String[] values = processLine(line);

                if (values.length != dataFrame.getColumns().size()) {
                    throw new IOException();
                }

                addRow(values);

            }
        }
        //throws a FileNotFoundException to where it is called (setDataFrame in model)
    }

    private String[] processLine(String line) {return line.split(",",-1);}

    private void setColumnNames(String[] names)
    {
        for (String name : names)
        {
            Column column = new Column(name);
            dataFrame.addColumn(column);
        }
    }

    private void addRow(String[] values)
    {
        for (int count = 0; count < values.length; count++) //for each value given from processLine
        {
            dataFrame.getColumns().get(count).addRowValue(values[count]); //adds that value to that column number
        }
    }

    public DataFrame getDataFrame() {return dataFrame;}
}
