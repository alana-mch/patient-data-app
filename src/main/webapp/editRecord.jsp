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
    <title>Edit Record</title>
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

    <%-- display a box to enter ID and value and, a drop down menu to pick columnName --%>
    <form method ="GET" action="/editRecord">
        <input type="text" name="ID" placeholder="Enter ID"/>

        <p>Enter Column Name</p>
        <select name="columnName">
            <% for(Column column : dataFrame.getColumns())
            {
            %> <option value="<%= column.getName() %>"><%= column.getName() %></option>

        <% } %>
        </select>

        <input type="text" name="value" placeholder="Enter Value"/>

        <button type="submit"> Update Record </button>
    </form>
<jsp:include page="/footer.jsp"/>
</body>
</html>