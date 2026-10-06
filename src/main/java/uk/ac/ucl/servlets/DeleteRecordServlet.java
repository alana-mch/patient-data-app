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

@WebServlet("/deleteRecord")
public class DeleteRecordServlet extends HttpServlet
{
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        try {
            Model model = ModelFactory.getModel();

            //gets ID and checks it isn't empty
            String ID = request.getParameter("ID");

            if ((ID == null || ID.trim().isEmpty())) {
                request.setAttribute("errorMessage", "Please enter a term for ID");
            }
            else
            {
                //if ID entered deleteRecord is called and if it returns found then success message attribute is set, otherwise error message attribute is set
                boolean found = model.deleteRecord(ID);
                if (found)
                {
                    model.saveCSV();
                    request.setAttribute("successMessage", "Record Deleted");
                }
                else
                {
                    request.setAttribute("errorMessage", "ID not found");
                }

            }

            ServletContext context = getServletContext();
            RequestDispatcher dispatch = context.getRequestDispatcher("/deleteRecord.jsp");
            dispatch.forward(request, response);

        } catch (IOException e) {
            request.setAttribute("errorMessage", "Error loading data: " + e.getMessage());
            ServletContext context = getServletContext();
            RequestDispatcher dispatch = context.getRequestDispatcher("/error.jsp");
            dispatch.forward(request, response);
        }
    }
}
