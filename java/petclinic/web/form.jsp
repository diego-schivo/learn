<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="taglibs.jsp" %>

<form method="post" action="${requestURI}">
  <dl>
    <c:forEach var="factory" items="${control.factories}">
      <dt>
        <label>${factory.name}</label>
      </dt>
      <dd>
        <bfs:control var="control" control="${factory.control(control.item)}">
          <jsp:include page="${control.page}" />
        </bfs:control>
      </dd>
    </c:forEach>
  </dl>
  <button>Save</button>
</form>
<a href="${requestURI.substring(0, requestURI.lastIndexOf('/'))}">back</a>
