<%@ page import="uk.ac.ucl.model.Column" %>
<%@ page import="uk.ac.ucl.model.Model" %>
<%@ page import="uk.ac.ucl.model.ModelFactory" %>
<%@ page import="uk.ac.ucl.model.DataFrame" %>

<%
    Model model = ModelFactory.getModel();
    DataFrame dataFrame = model.getDataFrame();
%>

<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Add Record</title>
</head>
<body>
<%-- if the errorMessage is not empty then display it otherwise display the successMessage --%>
    <%
    String errorMessage = (String) request.getAttribute("errorMessage");
    String successMessage = (String) request.getAttribute("successMessage");
    if (errorMessage != null)
    {
  %>
      <p style="color: red;"><%= errorMessage %></p>
  <%
    }
    else if (successMessage != null)
    {
  %>
      <p style = "color: green"><%= successMessage %></p>
  <% } %>

    <div class="main">
        <h1>Enter Patient Details</h1>
        <form method="GET" action="/addRecord">
        <%-- for each column displays a box to enter a value in, and then a submit button when finished--%>
        <%
            for (Column column : dataFrame.getColumns())
            { %>
                <input type="text" name="<%= column.getName() %>" placeholder="Enter <%= column.getName() %>" />
            <% } %>
        <button type="submit">Add Record</button>
        </form>
    </div>
<jsp:include page="/footer.jsp"/>
</body>
</html>