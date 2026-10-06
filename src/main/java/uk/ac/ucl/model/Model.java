package uk.ac.ucl.model;

import java.io.*;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.time.LocalDate;
import java.time.Period;


public class Model
{
  private DataFrame dataFrame = new DataFrame();
  private Map<String, List<Integer>> searchMap = new HashMap<>();

  public void setDataFrame(String fileName) throws IOException
  {
    //to set up the dataFrame readCSV is called and the dataFrame instance variable is set to the dataLoaders dataFrame
    DataLoader dataLoader = new DataLoader();
    dataLoader.readCSV(fileName);
    dataFrame = dataLoader.getDataFrame();
    buildSearchIndex();

    //throws a FileNotFoundException to where it was called (in ModelFactory)
  }

  public void buildSearchIndex()
  {
    //uses indexing to make searching quicker using a map, each key is a value in the csv and the value is a list of row numbers containing this value
    for (int i = 0; i < dataFrame.getRowCount(); i++)
    {
      for (String columnName : dataFrame.getColumnNames())
      {
        //for each row and each columns value
        List<Column> columns = dataFrame.getColumns();

        String value = dataFrame.getValue(columnName, i).toLowerCase();
        if (!(searchMap.containsKey(value)))
        {
          //if the value isn't in the search map add it as a key and its value as a list containing the row it was found
          List<Integer> rows = new ArrayList<>();
          rows.add(i);
          searchMap.put(value, rows);
        }
        else
        {
          //if the value is in the search map add the row it was found to the list associated with the key
          searchMap.get(value).add(i);
        }
      }
    }
  }

  public List<String> getRecord(int index)
  {
    //given a row gets the list of values for the record at that row
    List<String> record = new ArrayList<>();
    for (Column column: dataFrame.getColumns())
    {
      record.add(column.getRowValue(index));
    }
    return record;
  }
  public Map<Integer, String> getAllField(String columnName)
  {
    //result uses the row of the record for the key so that the record can be easily found for displaying patient data
    Map<Integer, String> result = new LinkedHashMap<>();

    if (columnName.equals("NAME")) //if the field is name it needs to combine FIRST and LAST
    {
      for (int i = 0; i < dataFrame.getRowCount(); i++)
      {
        //goes throw every name and creates fullName by combining FIRST and LAST then puts it in the result map with its key being its row
        String fullName = dataFrame.getValue("FIRST", i) + " " + dataFrame.getValue("LAST", i);
        result.put(i,fullName);
      }

    }
    else
    {
      //does the same, but does not need to combine columns
      for (Column column: dataFrame.getColumns())
      {
        if (column.getName().equals(columnName))
        {
          for (int i = 0; i < dataFrame.getRowCount(); i++)
          {
            result.put(i,column.getRowValue(i));
          }
        }
      }
    }
    return result;
  }

  public DataFrame getDataFrame()
  {
    //some other classes need information from the dataFrame so it can be returned with this
    return dataFrame;
  }


  private List<Integer> findRecords(String fieldName, String value, Boolean exactValue)
  {
    //the rows of records that match the search
    List<Integer> rows = new ArrayList<>();

    if (exactValue)
    {
      List<Integer> valueRows = searchMap.get(value.toLowerCase());
      if (valueRows == null)
      {
        return rows;
      }

      for (Integer row : valueRows)
      {
        //goes through each row in the list associated with the value its searching foe in searchMap
        if (dataFrame.getValue(fieldName, row).equalsIgnoreCase(value))
          //if the actual value in the dataFrame for that column and row is the value we are searching for then add that row to rows
          //this is to check the value matches in the correct column
          rows.add(row);
      }

    }
    else
    {
      //for non-exact values (you can search a substring)
      for (Map.Entry<String, List<Integer>> entry : searchMap.entrySet())
      {
        //for each entry in the searchMap
        if (entry.getKey().contains(value.toLowerCase()))
        //if the key contains any of the value its searching for
        {
          for (Integer row : entry.getValue())
          {
            // then goes through the rows associated with that value
            if (dataFrame.getValue(fieldName, row).toLowerCase().contains(value.toLowerCase()))
            {
              //then checks if the actual values in the dataFrame at that column and row contains the value we are searching for
              rows.add(row);
            }
          }
        }
      }
    }
    return rows;
  }

  public List<List<String>> getRecords(String fieldName, String value, Boolean exactValue)
  {
    List<Integer> rows = new ArrayList<>();
    List<List<String>> records = new ArrayList<>();

    if (fieldName.equals("NAME"))
    //if searching for a name
    {
      String[] names = value.split(" ");
      //splits the name into first and last and searches for each seperately
      List<Integer> firstNameRows = findRecords("FIRST", names[0], exactValue);
      List<Integer> lastNameRows = findRecords("LAST", names[1], exactValue);
      for (Integer row : firstNameRows)
      {
        //for every firstName found if lastName also matches than add that row
        if (lastNameRows.contains(row))
        {
          rows.add(row);
        }
      }
    }
    //for every other column, it just finds the records
    else {rows = findRecords(fieldName, value, exactValue);}

    //for each row found, get the whole record and add it to records
    for (Integer row: rows) {
      List<String> record = new ArrayList<>();
      for (Column column : dataFrame.getColumns()) {
        record.add(column.getRowValue(row));
      }
      records.add(record);
    }

    return records;
  }

  public List<List<String>> searchFor(String keyword)
  {
    List<List<String>> records = new ArrayList<>();

    for (Column column: dataFrame.getColumns())
    {
      //searches every column by calling getRecords on each column and adding all results to records
      records.addAll(getRecords(column.getName(), keyword, false));
    }
    return records;
  }

  private List<Map.Entry<Integer, LocalDate>> getAges()
  {
    //gets all birthdates and deathdates
    Map<Integer, String> birthDates = getAllField("BIRTHDATE");
    Map<Integer, String> deathDates = getAllField("DEATHDATE");
    Map<Integer, LocalDate> aliveDates = new LinkedHashMap<>();


    for (Map.Entry<Integer, String> date : birthDates.entrySet())
    {
      //for each birthdate gets the row and date
      int row = date.getKey();
      String birth = date.getValue();
      String death = deathDates.get(row);

      //checks if the deathDate for that row is empty, then the birthdate is added to alive dates
      if (death.isEmpty()) {
        aliveDates.put(row, LocalDate.parse(birth));
      }
    }
    List<Map.Entry<Integer, LocalDate>> aliveDatesEntries = new ArrayList<>(aliveDates.entrySet());
    aliveDatesEntries.sort(Map.Entry.comparingByValue());
    //returns a sorted list of birthdates of alive patients
    return aliveDatesEntries;
  }

  public List<List<String>> getExtremeAge(String type)
  {
    List<List<String>> records = new ArrayList<>();
    List<Map.Entry<Integer,LocalDate>> birthDates = getAges();
    List<Integer> oldestRows = new ArrayList<>();
    List<Integer> youngestRows = new ArrayList<>();
    int i = 0;
    //gets the oldest and youngest dates by taking from the start and end while people have the same birthdate
    while (birthDates.get(i).getValue().equals(birthDates.getFirst().getValue()))
    {
      oldestRows.add(birthDates.get(i).getKey());
      i++;
    }

    int j = birthDates.size()-1;
    while (birthDates.get(j).getValue().equals(birthDates.getLast().getValue()))
    {
      youngestRows.add(birthDates.get(j).getKey());
      j--;
    }


    if (type.equals("Oldest"))
    {
      for (int count = 0; count < oldestRows.size(); count++)
      {
        //adds the oldest birthdates records
        records.add(getRecord(oldestRows.get(count)));
      }
    }
    else
    {
      for (int count = 0; count < youngestRows.size(); count++)
      {
        //adds the youngest birthdates records
        records.add(getRecord(youngestRows.get(count)));
      }
    }
    return records;
  }

  public List<List<String>> getDeceased()
  {
    Map<Integer, String> deathDates = getAllField("DEATHDATE");
    List<List<String>> deceasedPatients = new ArrayList<>();

    //for each deathDate if it is not empty then add that record to deceased patients
    for (Map.Entry<Integer, String> deathDate : deathDates.entrySet())
    {
      if (!(deathDate.getValue().isEmpty()))
      {
        deceasedPatients.add(getRecord(deathDate.getKey()));
      }
    }
    return deceasedPatients;
  }

  public int getNumberWithFilter(String fieldName, String filter) {return(getRecords(fieldName, filter, true).size());}

  public List<List<String>> getRecordsWithFilter(String fieldName, String filter){return(getRecords(fieldName, filter, true));}

  public boolean checkAvailableID (String value)
  {
    //checks if an ID is already contained in the current IDs before allowing it to be used for a new record
    Map<Integer, String> allID = getAllField("ID");
    return !allID.containsValue(value);
  }

  //each of the following check strings are in the correct format, there are no checks for fields that can take any format like FIRST and LAST
  public boolean checkValidID(String value)
  {
    value = value.trim();
    if (value.length()==36)
    {
      return value.charAt(8) == '-' && value.charAt(13) == '-' && value.charAt(18) == '-' && value.charAt(23) == '-';
    }
    return false;
  }

  public boolean checkValidDate(String value)
  {
    try
    {
      LocalDate.parse(value);
      return true;
    }
    catch (DateTimeParseException e)
    {
      return false;
    }
  }

  public boolean checkValidSSN(String value)
  {
    if (value.length() == 11)
    {
      return value.substring(0,3).matches("\\d+") && value.charAt(3) == '-' && value.substring(4,6).matches("\\d+") && value.charAt(6) == '-' &&value.substring(7).matches("\\d+");
    }
    return false;
  }

  public boolean checkValidDrivers(String value)
  {
    if (value.length()==9)
    {
      return Character.isUpperCase(value.charAt(0)) && value.substring(1).matches("\\d+");
    }
    return false;
  }

  public boolean checkValidPassport(String value)
  {
    if (value.length()>=3)
    {
      return Character.isUpperCase(value.charAt(0)) && Character.isUpperCase(value.charAt(value.length()-1)) && value.substring(1,value.length()-1).matches("\\d+");
    }
    return false;
  }

  public boolean checkValidPrefix(String value) {return (value.equals("Mr.") || value.equals("Mrs.") || value.equals("Ms.") || value.isEmpty());}

  public boolean checkValidMarital(String value) {return (value.equals("M") || value.equals("S")|| value.isEmpty());}

  public boolean checkValidGender(String value) {return (value.equals("M") || value.equals("F"));}

  public boolean checkValidAddress(String value)
  {
    String[] addressComponents = value.split(" ");
    return addressComponents[0].matches("\\d+");
  }

  public boolean checkValidZip(String value)
  {
    if (value.length()==5)
    {
      return value.matches("\\d+");
    }
    return false;
  }

  public boolean checkValidInput(String value, String columnName)
  {
    //depending on column called with it calls the different validation methods
    boolean valid = true;
    if (columnName.equals("ID")) {valid = checkValidID(value);}
    else if (columnName.equals("BIRTHDATE")) {valid =checkValidDate(value);}
    else if (columnName.equals("DEATHDATE")) {valid =checkValidDate(value) || value.isEmpty();}
    else if (columnName.equals("SSN")) {valid = checkValidSSN(value); }
    else if (columnName.equals("DRIVERS")) {valid = checkValidDrivers(value);}
    else if (columnName.equals("PASSPORT")) {valid = checkValidPassport(value);}
    else if (columnName.equals("PREFIX")) {valid = checkValidPrefix(value);}
    else if (columnName.equals("MARITAL")) {valid = checkValidMarital(value);}
    else if (columnName.equals("GENDER")) {valid = checkValidGender(value);}
    else if (columnName.equals("ADDRESS")) {valid = checkValidAddress(value);}
    else if (columnName.equals("ZIP")) {valid = checkValidZip(value);}
    return valid;
  }

  private void updateSearchMap(String value, Integer row)
  {
    //for new or edited records, if the new value is already in search map just adds the row, if it isn't adds the value key along with a list containing the row
    if (searchMap.containsKey(value.toLowerCase()))
    {
      searchMap.get(value.toLowerCase()).add(row);
    }
    else
    {
      List<Integer> rows = new ArrayList<>();
      rows.add(row);
      searchMap.put(value.toLowerCase(), rows);
    }
  }
  public void addRecord(List<String> record)
  {
    int previousRowCount = dataFrame.getRowCount();
    List<Column> columns = dataFrame.getColumns();
    for (int count = 0; count < columns.size(); count++)
    {
      //for each column adds the value at that column index
      columns.get(count).addRowValue(record.get(count));
      //updates the search map using the previous size before adding the record
      updateSearchMap(record.get(count), previousRowCount-1);
    }
  }

  public boolean editRecord(String ID, String columnName, String value)
  {
    List<Column> columns = dataFrame.getColumns();
    Map<Integer, String> IDs = getAllField("ID");
    //found is used to determine if the ID exists
    boolean found = false;
    for (Map.Entry<Integer, String> IDEntry: IDs.entrySet())
    {
      if (IDEntry.getValue().equals(ID))
      //for each ID check if the ID is equal to the ID its searching for
      {
        //if it is sets found to true
        found = true;
        int row = IDEntry.getKey();
        for(Column column : columns)
        {
          if (column.getName().equals(columnName))
          {
            //goes through columns and if it's the column that's being edited it sets the new value and updates searchMap
            //remove row from searchMap for previous value
            searchMap.get(dataFrame.getValue(columnName,row).toLowerCase()).remove(Integer.valueOf(row));

            //set new value
            column.setRowValue(row, value);

            //update searchMap
            updateSearchMap(value, row);
          }
        }
        break;
      }
    }
    return found;
  }

  public boolean deleteRecord(String ID)
  {
    List<Column> columns = dataFrame.getColumns();
    Map<Integer, String> IDs = getAllField("ID");
    //found is used to determine if the ID exists
    boolean found = false;
    for (Map.Entry<Integer, String> IDEntry: IDs.entrySet())
    {
      if (IDEntry.getValue().equals(ID))
      {
        //goes through each ID and if it is equal to the ID its searching for it removes that row from the value of each its columns in the searchMap and deletes the column value and sets found to true
        int row = IDEntry.getKey();
        for(Column column : columns)
        {
          searchMap.get(dataFrame.getValue(column.getName(), row).toLowerCase()).remove(Integer.valueOf(row));
          column.deleteRowValue(row);
          found = true;
        }
        break;
      }
    }
    return found;
  }

  public void saveCSV() throws IOException
  {
    try (BufferedWriter writer = new BufferedWriter(new FileWriter("data/patients1000.csv"))){
      int columnCount = 1;
      List<Column> columns = dataFrame.getColumns();

      //writes each columnName to the file separated by commas
      for (Column column : columns )
      {
        writer.write(column.getName());
        if (columnCount < columns.size())
        {
          writer.write(",");
        }
        columnCount++;
      }

      writer.newLine();

      //does the same for each record
      for (int row = 0; row < dataFrame.getRowCount(); row++)
      {
        columnCount = 1;
        for (Column column : columns)
        {
          writer.write(column.getRowValue(row));
          if (columnCount < columns.size())
          {
            writer.write(",");
          }
          columnCount++;
        }
        writer.newLine();
      }

    }
  }

}


