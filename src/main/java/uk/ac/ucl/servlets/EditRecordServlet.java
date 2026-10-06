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

@WebServlet("/editRecord")
public class EditRecordServlet extends HttpServlet
{
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        try {
            Model model = ModelFactory.getModel();

            String ID = request.getParameter("ID");
            String columnName = request.getParameter("columnName");
            String value = request.getParameter("value");

            //if the column name is not one of the ones that allowed to be empty and the value is empty, then it displays an error message
            List<String> columnsEmpty = List.of("DEATHDATE", "DRIVERS", "PASSPORT", "PREFIX", "SUFFIX", "MAIDEN", "MARITAL");
            if ((ID == null || ID.trim().isEmpty()) || (columnName == null || columnName.trim().isEmpty()) ||(!(columnsEmpty.contains(columnName)) && value == null || value.trim().isEmpty())) {
                request.setAttribute("errorMessage", "Please enter a term for each parameter");

            } else
            {
                //otherwise it checks if the value is valid
                boolean valid = model.checkValidInput(value, columnName);
                if (columnName.equals("ID") && !model.checkAvailableID(value)) {
                    valid = false;
                }
                if (!valid) {
                    //if it isn't valid errorMessage attribute is set
                    request.setAttribute("errorMessage", "Value entered is not in correct format");
                } else {
                    //otherwise calls editRecord and if ID is found it can update the value and save the dataFrame to the CSV and successMessage is set otherwise errorMessage is set
                    boolean found = model.editRecord(ID, columnName, value);
                    if (found) {
                        model.saveCSV();
                        request.setAttribute("successMessage", "Record Edited");
                    } else {
                        request.setAttribute("errorMessage", "ID not found");
                    }
                }
            }

            ServletContext context = getServletContext();
            RequestDispatcher dispatch = context.getRequestDispatcher("/editRecord.jsp");
            dispatch.forward(request, response);

        } catch (IOException e) {
            request.setAttribute("errorMessage", "Error loading data: " + e.getMessage());
            ServletContext context = getServletContext();
            RequestDispatcher dispatch = context.getRequestDispatcher("/error.jsp");
            dispatch.forward(request, response);
        }
    }
}
