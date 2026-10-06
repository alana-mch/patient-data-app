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
    <title>Deceased Patients</title>
</head>
<body>
    <h1>Deceased Patients</h1>
    <%-- if the errorMessage is not empty then display it --%>
<%
    String errorMessage = (String) request.getAttribute("errorMessage");
    if (errorMessage != null)
    {
  %>
      <p style="color: red;"><%= errorMessage %></p>
  <%
    }
    else
    {

        List<List<String>> records = (List<List<String>>) request.getAttribute("records");
        if (records.isEmpty()) {
        %>
            <p>No deceased patients found.</p>
        <%
        } else { %>
            <%-- display a table with each record --%>
            <table>
              <tr> <%
                for (Column column: dataFrame.getColumns())
                {
              %>
                  <th><%= column.getName() %></th>
              <%
                } %>
              </tr>
              <%
                for (List<String> record : records)
                { %>
                  <tr> <%
                    for (String value : record)
                    {
                  %>
                      <td><%= value %></td>
                  <% } %>
                  </tr> <%
                }
          %>
          </table> <%
        }
    }
%>
<jsp:include page="/footer.jsp"/>
</body>
</html>