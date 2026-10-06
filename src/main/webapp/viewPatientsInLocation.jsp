
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>See Patients In a Location</title>
</head>
<body>
<div class="main">
  <h1>Enter Location</h1>
  <%-- if the errorMessage is not empty displays the error message--%>
  <%
  String errorMessage = (String) request.getAttribute("errorMessage");
  if (errorMessage != null)
  {
    %>
        <p style="color: red;"><%= errorMessage %></p>
    <%
  } else { %>
        <%-- displays a box for user to enter location--%>
      <form method="GET" action="/patientsInLocation">
        <input type="text" name="searchstring" placeholder="Enter Location"/>
        <input type="submit" value="Search"/>
      </form>
  <% } %>
</div>
<jsp:include page="/footer.jsp"/>
</body>
</html>