package uk.ac.ucl.servlets;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletContext;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import uk.ac.ucl.model.ModelFactory;
import uk.ac.ucl.model.Model;

import java.util.List;
import java.io.IOException;

@WebServlet("/displayPatientData")
public class DisplayPatientDataServlet extends HttpServlet
{
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
    {
        try {
            Model model = ModelFactory.getModel();

            //gets the row of the record then uses getRecord to get the record
            int index = Integer.parseInt(request.getParameter("index"));

            List<String> record = model.getRecord(index);

            request.setAttribute("record", record);

            ServletContext context = getServletContext();
            RequestDispatcher dispatch = context.getRequestDispatcher("/displayPatientData.jsp");
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
