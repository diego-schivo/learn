<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="/taglibs.jsp" %>

<table>
  <tr>
    <ui:controlFactories var="factory">
      <th>${factory.name eq 'date' ? 'Visit date' : factory.name}</th>
    </ui:controlFactories>
  </tr>
  <c:forEach var="item" items="${control.items}">
    <tr>
      <ui:controlFactories var="factory">
        <td>
          <ui:control var="control" control="${factory.newControl(item, control)}">
            <jsp:include page="${control.page}" />
          </ui:control>
        </td>
     </ui:controlFactories>
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
