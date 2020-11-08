<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
<link rel="icon" href="data:,">
<style>
table {
  font-family: arial, sans-serif;
  border-collapse: collapse;
}

td, th {
  border: 1px solid #dddddd;
  text-align: left;
  padding: 8px;
}

tr:nth-child(even) {
  background-color: #dddddd;
}
</style>
</head>
<body>

<h2>Petclinic</h2>

<table>
  <tr>
    <c:forEach var="field" items="${controlFactories.keySet()}">
      <th>${field}</th>
    </c:forEach>
  </tr>
  <c:forEach var="item" items="${items}" varStatus="loop">
    <tr>
      <c:forEach var="factory" items="${controlFactories.values()}">
        <td>
          <c:set var="control" value="${factory.control(item)}" scope="request" />
          <jsp:include page="${control.page}" />
        </td>
      </c:forEach>
    </tr>
  </c:forEach>
</table>

<a href="${requestURI.substring(0, requestURI.lastIndexOf('/'))}">back</a>

</body>
</html>
