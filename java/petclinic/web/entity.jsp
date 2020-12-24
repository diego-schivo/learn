<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="taglibs.jsp" %>

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
  <bfs:context var="context">
    <jsp:include page="${context.control.page}" />
  </bfs:context>
</section>

</body>
</html>
