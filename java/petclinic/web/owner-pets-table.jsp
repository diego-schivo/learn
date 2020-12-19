<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="taglibs.jsp" %>

<table>
  <c:forEach var="item" items="${control.items}" varStatus="loop">
    <tr>
      <td>
        <table>
          <c:forEach var="entry" items="${control.factories}">
            <c:if test="${entry.key ne 'visits'}">
              <c:set var="control0" value="${control}" />
              <c:set var="control" value="${entry.value.control(item)}" scope="request" />
              <tr>
                <th align="right">${entry.key}</th>
                <td>
                  <jsp:include page="${control.page}" />
                </td>
              </tr>
              <c:set var="control" value="${control0}" scope="request" />
            </c:if>
          </c:forEach>
        </table>
      </td>
      <td>
        <c:forEach var="entry" items="${control.factories}">
          <c:if test="${entry.key eq 'visits'}">
            <c:set var="control0" value="${control}" />
            <c:set var="control" value="${entry.value.control(item)}" scope="request" />
            <jsp:include page="${control.page}" />
            <c:set var="control" value="${control0}" scope="request" />
          </c:if>
        </c:forEach>
      </td>
    </tr>
  </c:forEach>
</table>
