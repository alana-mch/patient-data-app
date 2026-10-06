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

@WebServlet("/viewNumberOfGender")
public class ViewNumberOfGenderServlet extends HttpServlet
{
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
    {
        try {
            Model model = ModelFactory.getModel();

            //gets the number of gender column where the value is M and then F
            int numberMale = model.getNumberWithFilter("GENDER", "M");
            int numberFemale = model.getNumberWithFilter("GENDER", "F");

            //sets the number to be passed on to be displayed with the viewNumberOfGender.jsp
            request.setAttribute("numberMale", numberMale);
            request.setAttribute("numberFemale", numberFemale);

            ServletContext context = getServletContext();
            RequestDispatcher dispatch = context.getRequestDispatcher("/viewNumberOfGender.jsp");
            dispatch.forward(request, response);
        }
        catch (IOException e) {
            request.setAttribute("errorMessage", "Error loading data: " + e.getMessage());
            ServletContext context = getServletContext();
            RequestDispatcher dispatch = context.getRequestDispatcher("/error.jsp");
            dispatch.forward(request, response);
        }
    }
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        doGet(request, response);
    }
}
