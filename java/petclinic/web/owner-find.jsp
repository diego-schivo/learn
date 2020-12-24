<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="taglibs.jsp" %>

<form method="get" action="${requestURI.substring(0, requestURI.lastIndexOf('/'))}">
  <dl>
    <c:forEach var="factory" items="${control.factories}">
      <c:if test="${factory.name eq 'lastName'}">
        <dt>
          <label>${factory.name}</label>
        </dt>
        <dd>
          <bfs:control var="control" control="${factory.create(control.item)}">
            <jsp:include page="${control.page}" />
          </bfs:control>
        </dd>
      </c:if>
    </c:forEach>
  </dl>
  <button>Find Owners</button>
</form>
