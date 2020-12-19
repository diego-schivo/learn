<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="taglibs.jsp" %>

<!DOCTYPE html>
<html>
<head>
<link rel="icon" href="data:,">
</head>
<body>

<h2>Petclinic</h2>

<table>
  <c:set var="controlFactories" value="${entityView.controlFactories(view)}" />
  <c:forEach var="entry" items="${controlFactories}">
    <tr>
      <td>
        <label>${entry.key}</label>
      </td>
      <td>
        <c:set var="control" value="${entry.value.control(item)}" scope="request" />
        <jsp:include page="${control.page}" />
      </td>
    </tr>
  </c:forEach>
</table>
<a href="${requestURI}/edit">edit</a>
<a href="${requestURI.substring(0, requestURI.lastIndexOf('/'))}">back</a>

</body>
</html>
