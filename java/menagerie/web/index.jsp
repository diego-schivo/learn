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
    <c:forEach var="field" items="${fields}">
      <th>${field}</th>
    </c:forEach>
    <th></th>
  </tr>
  <c:forEach var="item" items="${items}" varStatus="loop">
    <c:set var="form" value="update${loop.index}" />
    <tr>
      <c:forEach var="field" items="${fields}">
        <td>
          <c:set var="control" value="${controls[field]}" scope="request" />
          <jsp:include page="${control.page}">
            <jsp:param name="form" value="${form}" />
            <jsp:param name="name" value="${field}" />
            <jsp:param name="value" value="${converters[field].convertToString(item[field])}" />
          </jsp:include>
        </td>
      </c:forEach>
      <td>
        <form id="${form}" method="post" action="${pageContext.request.contextPath}">
          <input type="hidden" name="_${fields[0]}" value="${item[fields[0]]}">
          <input type="submit" name="update" value="update">
          <input type="submit" name="delete" value="delete">
        </form>
      </td>
    </tr>
  </c:forEach>
</table>

</body>
</html>
