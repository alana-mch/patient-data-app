<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<link rel="stylesheet" href="styles.css">

<%@ page import="uk.ac.ucl.model.Model" %>
<%@ page import="uk.ac.ucl.model.ModelFactory" %>
<%@ page import="uk.ac.ucl.model.DataFrame" %>
<%@ page import="uk.ac.ucl.model.Column" %>

<%
    Model model = ModelFactory.getModel();
    DataFrame dataFrame = model.getDataFrame();
%>

<html>
<head>
  <jsp:include page="/meta.jsp"/>
  <title>Patient Data App</title>
</head>
<body>
<jsp:include page="/header.jsp"/>
<div class="main">
  <h1>Search Result</h1>
  <%-- if the errorMessage is not empty then display it --%>
  <%
    String errorMessage = (String) request.getAttribute("errorMessage");
    if (errorMessage != null)
    {
  %>
      <p style="color: red;"><%= errorMessage %></p>
  <%
    }
    List<List<String>> records = (List<List<String>>) request.getAttribute("result");
    if (records != null && records.size() != 0)
    {
    %>
    <%-- display table of each record --%>
    <table>
      <tr>
      <%
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
    }
  %>
  </table>
</div>
<jsp:include page="/footer.jsp"/>
</body>
</html>