<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="/taglibs.jsp" %>

<!DOCTYPE html>
<html>
<head>
<link rel="icon" href="data:,">
<link href="${pageContext.request.contextPath}/main.css" rel="stylesheet">
</head>
<body>

<nav>
  <ul>
    <li>
      <a href="${pageContext.request.contextPath}/">Home</a>
    </li>
    <li>
      <a href="${pageContext.request.contextPath}/owners/find">Find Owners</a>
    </li>
  </ul>
</nav>

<section>
  <h2>Welcome</h2>
</section>

<%--
<ul>
  <c:forEach var="entry" items="${servlets}">
    <li>
      <a href="${pageContext.request.contextPath}${entry.value.mappings.iterator().next()}">${entry.key}</a>
    </li>
  </c:forEach>
</ul>
--%>

</body>
</html>
