
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Delete Record</title>
</head>
<body>
  <div class="main">
    <h1>Enter Patient ID</h1>
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
    else if (successMessage != null) {
      %>
          <p style="color: green;"><%= successMessage %></p>
      <%
     } %>

    <%-- displays a box to enter ID and a button to delete --%>
    <form method="GET" action="/deleteRecord">
      <input type="text" name="ID" placeholder="Enter ID"/>
      <input type="submit" value="Delete"/>
    </form>
  </div>
<jsp:include page="/footer.jsp"/>
</body>
</html>