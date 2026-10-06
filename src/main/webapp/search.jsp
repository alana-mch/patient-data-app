
<html>
<head>
  <title>Patient Data App</title>
</head>
<body>
<div class="main">
  <h1>Search</h1>
  <%-- displays a box to enter value to search and button to submit --%>
  <form method="GET" action="/runsearch">
    <input type="text" name="searchstring" placeholder="Enter search keyword here"/>
    <input type="submit" value="Search"/>
  </form>
</div>
<jsp:include page="/footer.jsp"/>
</body>
</html>