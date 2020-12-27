<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="/taglibs.jsp" %>

<table>
  <tr>
    <c:forEach var="factory" items="${control.childFactories()}">
      <th>${factory.name eq 'date' ? 'Visit date' : factory.name}</th>
    </c:forEach>
  </tr>
  <c:forEach var="item" items="${control.items}">
    <tr>
      <c:forEach var="factory" items="${control.childFactories()}">
        <td>
          <bfs:control var="control" control="${factory.newControl(item, control)}">
            <jsp:include page="${control.page}" />
          </bfs:control>
        </td>
      </c:forEach>
    </tr>
  </c:forEach>
  <tr>
    <%--
    <td><a href="${requestURI}${uri}/${control.target.id}/edit">Edit pet</a></td>
    <td><a href="${requestURI}${uri}/${control.target.id}${control.uri}/new">Add visit</a></td>
    --%>
    <%--
    <td><a href="${requestURI}${control.parent.entityView.uri}/${control.target.id}/edit">Edit pet</a></td>
    <td><a href="${requestURI}/new">Add visit</a></td>
    --%>
  </tr>
</table>
