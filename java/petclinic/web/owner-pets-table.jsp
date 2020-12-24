<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="/taglibs.jsp" %>

<table>
  <c:forEach var="item" items="${control.items}">
    <tr>
      <td>
        <table>
          <c:forEach var="factory" items="${control.factories}">
            <c:if test="${factory.name ne 'visits'}">
              <bfs:control var="control" control="${factory.create(item)}">
                <tr>
                  <th align="right">${control.name}</th>
                  <td>
                    <jsp:include page="${control.page}" />
                  </td>
                </tr>
              </bfs:control>
            </c:if>
          </c:forEach>
        </table>
      </td>
      <td>
        <c:forEach var="factory" items="${control.factories}">
          <c:if test="${factory.name eq 'visits'}">
            <bfs:control var="control" control="${factory.create(item)}">
              <jsp:include page="${control.page}" />
            </bfs:control>
          </c:if>
        </c:forEach>
      </td>
    </tr>
  </c:forEach>
</table>
