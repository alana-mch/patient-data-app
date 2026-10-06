package uk.ac.ucl.servlets;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import uk.ac.ucl.model.Column;
import uk.ac.ucl.model.Model;
import uk.ac.ucl.model.ModelFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/addRecord")
public class AddRecordServlet extends HttpServlet
{
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        try {
            Model model = ModelFactory.getModel();
            boolean errorOccured = false;

            List<String> record = new ArrayList<>();
            for (Column column : model.getDataFrame().getColumns())
            {
                //for each column gets the parameter for that columnName
                String columnName = column.getName();
                String value = request.getParameter(columnName);

                //checks if the string is not one of the columns that's allowed to be empty and if it is if the string entered was empty it sets the errorMessage attribute
                List<String> columnsEmpty = List.of("DEATHDATE", "DRIVERS", "PASSPORT", "PREFIX", "SUFFIX", "MAIDEN", "MARITAL");
                if ((value == null || value.trim().isEmpty()) && (!(columnsEmpty.contains(columnName))))
                {
                    request.setAttribute("errorMessage", "Please enter a term.");
                    errorOccured = true;
                }
                else {
                    //otherwise check entered string was valid format for its column
                    boolean valid = model.checkValidInput(value, columnName);
                    //if the column is ID also check if it already exists if it does then valid is false
                    if (columnName.equals("ID") && !model.checkAvailableID(value)) {
                        valid = false;
                    }
                    //if the string is not valid it sets errorMessage attribute saying which column was not valid
                    if (!valid) {
                        request.setAttribute("errorMessage", "Value entered for " + columnName + " was not in correct format");
                        errorOccured = true;

                    //otherwise add the value to the record
                    } else {
                        record.add(value);
                    }
                }
            }

            if (!errorOccured)
            {
                //if no error occured with any of the values then the record is complete and can be added and the dataFrame can be saved as a CSV and sets the successMessage attribute
                model.addRecord(record);
                model.saveCSV();
                request.setAttribute("successMessage", "Record Added");
            }

            ServletContext context = getServletContext();
            RequestDispatcher dispatch = context.getRequestDispatcher("/addRecord.jsp");
            dispatch.forward(request, response);

        } catch (IOException e) {
            request.setAttribute("errorMessage", "Error loading data: " + e.getMessage());
            ServletContext context = getServletContext();
            RequestDispatcher dispatch = context.getRequestDispatcher("/error.jsp");
            dispatch.forward(request, response);
        }
    }
}
