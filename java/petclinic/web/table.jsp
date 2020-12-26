<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="/taglibs.jsp" %>

<h2>${context.control.heading}</h2>

<table>
  <tr>
    <c:forEach var="factory" items="${context.control.factories}">
      <th>${factory.heading}</th>
    </c:forEach>
  </tr>
  <c:forEach var="item" items="${context.control.items}">
    <tr>
      <c:forEach var="factory" items="${context.control.factories}">
        <td>
          <bfs:control var="control" control="${factory.create(item)}">
            <jsp:include page="${control.page}" />
          </bfs:control>
        </td>
      </c:forEach>
    </tr>
  </c:forEach>
</table>

<a href="${requestURI}/new">New</a>
