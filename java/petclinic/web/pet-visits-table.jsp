<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="taglibs.jsp" %>

<table>
  <tr>
    <c:forEach var="field" items="${control.factories.keySet()}">
      <th>${field eq 'date' ? 'Visit date' : field}</th>
    </c:forEach>
  </tr>
  <c:forEach var="item" items="${control.items}" varStatus="loop">
    <tr>
      <c:forEach var="factory" items="${control.factories.values()}">
        <td>
          <c:set var="control0" value="${control}" />
          <c:set var="control" value="${factory.control(item)}" scope="request" />
          <jsp:include page="${control.page}" />
          <c:set var="control" value="${control0}" scope="request" />
        </td>
      </c:forEach>
    </tr>
  </c:forEach>
  <tr>
    <%--
    <td><a href="${requestURI}${uri}/${control.item.id}/edit">Edit pet</a></td>
    <td><a href="${requestURI}${uri}/${control.item.id}${control.uri}/new">Add visit</a></td>
    --%>
    <td><a href="${requestURI}/edit">Edit pet ${control.entityView.uri}</a></td>
    <td><a href="${requestURI}/new">Add visit</a></td>
  </tr>
</table>
