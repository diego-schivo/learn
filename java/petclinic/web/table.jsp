<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="taglibs.jsp" %>

<table>
  <tr>
    <c:forEach var="factory" items="${control.factories}">
      <th>${factory.name}</th>
    </c:forEach>
  </tr>
  <c:forEach var="item" items="${control.items}">
    <tr>
      <c:forEach var="factory" items="${control.factories}">
        <td>
          <bfs:control var="control" control="${factory.control(item)}">
            <jsp:include page="${control.page}" />
          </bfs:control>
        </td>
      </c:forEach>
    </tr>
  </c:forEach>
</table>
<a href="${requestURI}/new">New</a>
