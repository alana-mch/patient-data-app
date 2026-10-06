
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title></title>
</head>
<body>
    <h1>Number In Location</h1>
    <%-- if the errorMessage is not empty then display it --%>
    <%
        String errorMessage = (String) request.getAttribute("errorMessage");
        if (errorMessage != null)
        {
      %>
          <p style="color: red;"><%= errorMessage %></p>
      <%
        }
        else {
            Integer number = (Integer) request.getAttribute("number");
            %>
            <p> <%= number %> </p>
        <%
        } %>
<jsp:include page="/footer.jsp"/>
</body>
</html>