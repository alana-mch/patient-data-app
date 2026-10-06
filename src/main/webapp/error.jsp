
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Error</title>
</head>
<body>
    <%
        String errorMessage = (String) request.getAttribute("errorMessage");
        if (errorMessage != null)
        {
      %>
          <p style="color: red;"><%= errorMessage %></p>
      <%
        } %>
</body>
</html>