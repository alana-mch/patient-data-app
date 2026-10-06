<%@ page import="java.util.Map" %>
<%@ page import="java.util.Map.Entry" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
  <jsp:include page="/meta.jsp"/>
  <title>Patient Data App</title>
</head>
<body>
<jsp:include page="/header.jsp"/>
<div class="main">
  <h1>Patients: </h1>
  <%-- if the errorMessage is not empty then display it  --%>
  <%
    String errorMessage = (String) request.getAttribute("errorMessage");
    if (errorMessage != null)
    {
  %>
      <p style="color: red;"><%= errorMessage %></p>
  <%
    }
  %>
  <ul>
  <%-- for each, of teh values gotten, display iy as a reference to getting the patient data with the index being the row of the value --%>
    <%
      Map<Integer, String> values = (Map<Integer, String>) request.getAttribute("values");
      String fieldName = (String) request.getAttribute("field");

      if (values != null)
      {
        for (Map.Entry<Integer, String> entry : values.entrySet())
        {
          String href = "displayPatientData?index=" + entry.getKey();
    %>
    <li><a href="<%=href%>"><%=entry.getValue()%></a>
    </li>
    <%  }
      }
    %>
  </ul>
</div>
<jsp:include page="/footer.jsp"/>
</body>
</html>
