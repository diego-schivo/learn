<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="taglibs.jsp" %>

<table>
  <tr>
    <c:forEach var="field" items="${control.factories.keySet()}">
      <th>${field}</th>
    </c:forEach>
  </tr>
  <c:forEach var="item" items="${control.items}" varStatus="loop">
    <tr>
      <c:forEach var="factory" items="${control.factories.values()}">
        <td>
          <c:set var="uri0" value="${uri}" />
          <c:set var="control0" value="${control}" />
          <c:set var="uri" value="${control.uri}" scope="request" />
          <c:set var="control" value="${factory.control(item)}" scope="request" />
          <jsp:include page="${control.page}" />
          <c:set var="uri" value="${uri0}" scope="request" />
          <c:set var="control" value="${control0}" scope="request" />
        </td>
      </c:forEach>
    </tr>
  </c:forEach>
</table>
