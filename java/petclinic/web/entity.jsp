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
    <h2>${context.servlet.class1.simpleName} ${context.handler.getClass().simpleName}</h2>
  </bfs:context>

  <bfs:control var="control">
    <jsp:include page="${control.page}" />
  </bfs:control>
</section>

</body>
</html>
