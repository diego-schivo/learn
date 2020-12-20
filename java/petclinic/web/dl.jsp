<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="taglibs.jsp" %>

<dl>
  <c:forEach var="factory" items="${control.factories}">
    <bfs:control var="control" control="${factory.control(control.item)}">
      <dt>${control.name}</dt>
      <dd>
        <jsp:include page="${control.page}" />
      </dd>
    </bfs:control>
  </c:forEach>
</dl>
