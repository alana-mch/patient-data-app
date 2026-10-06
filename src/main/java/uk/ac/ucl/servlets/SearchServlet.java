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

@WebServlet("/runsearch")
public class SearchServlet extends HttpServlet {

  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    doPost(request, response);
  }

  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    //gets the searchString parameter
    String searchString = request.getParameter("searchstring");

    try {
      Model model = ModelFactory.getModel();

      // checks if the value searched is empty, if it sets the errorMessage attribute
      if (searchString == null || searchString.trim().isEmpty()) {
        request.setAttribute("errorMessage", "Please enter a search term.");
      } else {
        //performs the search and sets result attribute to what was found
        List<List<String>> searchResult = model.searchFor(searchString);
        request.setAttribute("result", searchResult);
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
