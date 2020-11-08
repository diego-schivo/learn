<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
<link rel="icon" href="data:,">
</head>
<body>

<h2>Petclinic</h2>

<table>
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
