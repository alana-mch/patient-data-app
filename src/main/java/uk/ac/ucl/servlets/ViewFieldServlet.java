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
import java.util.Map;


@WebServlet("/viewField")
public class  ViewFieldServlet extends HttpServlet
{

  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
  {
    try {
      Model model = ModelFactory.getModel();

      //gets the field parameter and uses this with getAllField to get all values for that field
      String fieldName = request.getParameter("field");
      Map<Integer, String> values = model.getAllField(fieldName);

      //sets attributes to pass to display with viewField.jsp
      request.setAttribute("field", fieldName);
      request.setAttribute("values", values);

      ServletContext context = getServletContext();
      RequestDispatcher dispatch = context.getRequestDispatcher("/viewField.jsp");
      dispatch.forward(request, response);

    } catch (IOException e) {

      request.setAttribute("errorMessage", "Error loading data: " + e.getMessage());
      ServletContext context = getServletContext();
      RequestDispatcher dispatch = context.getRequestDispatcher("/error.jsp");
      dispatch.forward(request, response);
    }
  }
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    doGet(request, response);
  }
}
