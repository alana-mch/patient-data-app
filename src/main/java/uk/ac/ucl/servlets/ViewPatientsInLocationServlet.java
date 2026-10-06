package uk.ac.ucl.servlets;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import uk.ac.ucl.model.Model;
import uk.ac.ucl.model.ModelFactory;

import java.io.IOException;
import java.util.List;

@WebServlet("/patientsInLocation")
public class ViewPatientsInLocationServlet extends HttpServlet
{
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String searchString = request.getParameter("searchstring");

        try {
            Model model = ModelFactory.getModel();

            //if the entered location to search is empty errorMessage attribute is set
            if (searchString == null || searchString.trim().isEmpty()) {
                request.setAttribute("errorMessage", "Please enter a location.");
            } else {
                //otherwise goes through each of the location fields and searches for string
                List<List<String>> searchResult = model.getRecordsWithFilter("CITY", searchString);
                searchResult.addAll(model.getRecordsWithFilter("ADDRESS", searchString));
                searchResult.addAll(model.getRecordsWithFilter("STATE", searchString));
                request.setAttribute("result", searchResult); //sets the result attribute as the result from searching
            }
            ServletContext context = getServletContext();
            RequestDispatcher dispatch = context.getRequestDispatcher("/searchResult.jsp");
            dispatch.forward(request, response);

        } catch (IOException e) {
            request.setAttribute("errorMessage", "Error loading data: " + e.getMessage());
            ServletContext context = getServletContext();
            RequestDispatcher dispatch = context.getRequestDispatcher("/error.jsp");
            dispatch.forward(request, response);
        }
    }
}
