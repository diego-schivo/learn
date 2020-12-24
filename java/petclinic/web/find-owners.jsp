<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="/taglibs.jsp" %>

<h2>Find Owners</h2>

<form method="get" action="${requestURI.substring(0, requestURI.lastIndexOf('/'))}">
  <dl>
    <c:forEach var="factory" items="${context.control.factories}">
      <c:if test="${factory.name eq 'lastName'}">
        <dt>
          <label>Last name</label>
        </dt>
        <dd>
          <bfs:control var="control" control="${factory.create(control.item)}">
            <jsp:include page="${control.page}" />
          </bfs:control>
        </dd>
      </c:if>
    </c:forEach>
  </dl>
  <button>Find Owner</button>
</form>
<bfs:context var="context">
  <a href="${context.servlet.view.uri}/new">Add Owner</a>
</bfs:context>
