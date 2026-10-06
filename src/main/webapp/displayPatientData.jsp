<%@ page import="java.util.List" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<link rel="stylesheet" href="styles.css">

<%@ page import="uk.ac.ucl.model.Model" %>
<%@ page import="uk.ac.ucl.model.ModelFactory" %>
<%@ page import="uk.ac.ucl.model.DataFrame" %>
<%@ page import="uk.ac.ucl.model.Column" %>

<%
    Model model = ModelFactory.getModel();
    DataFrame dataFrame = model.getDataFrame();
%>

<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>A Patient</title>
</head>

<body>
    <%-- if the errorMessage is not empty then display it --%>
    <%
    String errorMessage = (String) request.getAttribute("errorMessage");
    if (errorMessage != null)
    {
      %>
          <p style="color: red;"><%= errorMessage %></p>
      <%
    } else { %>
        <%-- display each record in a table --%>
          <table>
              <tr> <%
                for (Column column: dataFrame.getColumns())
                {
              %>
                  <th><%= column.getName() %></th>
              <%
                } %>
              </tr>
              <tr>
              <%
                List<String> record = (List<String>) request.getAttribute("record");
                for (String value : record)
                {
              %>
                  <td><%= value %></td>
              <%
                }
          %>
            </tr>
          </table>
    <% } %>
<jsp:include page="/footer.jsp"/>
</body>
</html>