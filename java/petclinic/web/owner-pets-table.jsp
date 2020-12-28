<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="/taglibs.jsp" %>

<table>
  <c:forEach var="item" items="${control.items}">
    <tr>
      <td>
        <table>
          <ui:controlFactories var="factory">
            <c:if test="${factory.name ne 'visits'}">
              <ui:control var="control" control="${factory.newControl(item, control)}">
                <tr>
                  <th align="right">${control.name}</th>
                  <td>
                    <jsp:include page="${control.page}" />
                  </td>
                </tr>
              </ui:control>
            </c:if>
          </ui:controlFactories>
        </table>
      </td>
      <td>
        <ui:controlFactories var="factory">
          <c:if test="${factory.name eq 'visits'}">
            <ui:control var="control" control="${factory.newControl(item, control)}">
              <jsp:include page="${control.page}" />
            </ui:control>
          </c:if>
        </ui:controlFactories>
      </td>
    </tr>
  </c:forEach>
</table>
