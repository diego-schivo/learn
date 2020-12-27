<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="/taglibs.jsp" %>

<table>
  <c:forEach var="item" items="${control.items}">
    <tr>
      <td>
        <table>
          <c:forEach var="factory" items="${control.childFactories()}">
            <c:if test="${factory.name ne 'visits'}">
              <bfs:control var="control" control="${factory.newControl(item, control)}">
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
        <c:forEach var="factory" items="${control.childFactories()}">
          <c:if test="${factory.name eq 'visits'}">
            <bfs:control var="control" control="${factory.newControl(item, control)}">
              <jsp:include page="${control.page}" />
            </bfs:control>
          </c:if>
        </c:forEach>
      </td>
    </tr>
  </c:forEach>
</table>
