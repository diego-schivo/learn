<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="taglibs.jsp" %>

<dl>
  <c:forEach var="factory" items="${control.factories}">
    <dt>${factory.name}</dt>
    <dd>
      <bfs:control var="control" control="${factory.control(control.item)}">
        <jsp:include page="${control.page}" />
      </bfs:control>
    </dd>
  </c:forEach>
</dl>
<a href="${requestURI.substring(0, requestURI.lastIndexOf(control.entityView.uri))}${control.entityView.uri}">back</a>
<a href="${requestURI}/edit">Edit</a>
