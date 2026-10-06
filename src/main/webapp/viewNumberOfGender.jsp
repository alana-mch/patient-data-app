
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Number Of Patients By Gender</title>
</head>
<body>
    <h1>Number of Each Gender: </h1>
    <p> Male: <%= request.getAttribute("numberMale") %> </p>
    <p> Female: <%= request.getAttribute("numberFemale") %> </p>
</body>
<jsp:include page="/footer.jsp"/>
</html>