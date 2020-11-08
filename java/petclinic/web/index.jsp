<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
<link rel="icon" href="data:,">
</head>
<body>

<h2>Petclinic</h2>

<ul>
  <c:forEach var="entry" items="${servlets}">
    <li>
      <a href="${pageContext.request.contextPath}${entry.value.mappings.iterator().next()}">${entry.key}</a>
    </li>
  </c:forEach>
</ul>

</body>
</html>
