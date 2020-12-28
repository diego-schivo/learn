<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="/taglibs.jsp" %>

<h2>${context.control.heading}</h2>

<table>
  <tr>
    <ui:controlFactories var="factory">
      <th>${factory.heading}</th>
    </ui:controlFactories>
  </tr>
  <c:forEach var="item" items="${context.control.items}">
    <tr>
      <ui:controlFactories var="factory">
        <td>
          <ui:control var="control" control="${factory.newControl(item, context.control)}">
            <jsp:include page="${control.page}" />
          </ui:control>
        </td>
      </ui:controlFactories>
    </tr>
  </c:forEach>
</table>

<a href="${requestURI}/new">New</a>
