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

<h2>Menagerie</h2>

<table>
  <tr>
    <c:forEach var="field" items="${controlFactories.keySet()}">
      <th>${field}</th>
    </c:forEach>
    <th></th>
  </tr>
  <c:forEach var="item" items="${items}" varStatus="loop">
    <c:set var="form" value="update${loop.index}" />
    <tr>
      <c:forEach var="factory" items="${controlFactories.values()}">
        <td>
          <c:set var="control" value="${factory.control(item)}" scope="request" />
          <jsp:include page="${control.page}">
            <jsp:param name="form" value="${form}" />
          </jsp:include>
        </td>
      </c:forEach>
      <td>
        <form id="${form}" method="post" action="${requestURI}">
          <c:set var="field" value="${controlFactories.keySet().iterator().next()}" />
          <input type="hidden" name="_${field}" value="${item[field]}">
          <input type="submit" name="update" value="update">
          <input type="submit" name="delete" value="delete">
        </form>
      </td>
    </tr>
  </c:forEach>
</table>

</body>
</html>
