<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="taglibs.jsp" %>

<h2>${context.control.heading}</h2>

<dl>
  <c:forEach var="factory" items="${context.control.factories}">
    <dt>${factory.name}</dt>
    <dd>
      <bfs:control var="control" control="${factory.create(context.control.item)}">
        <jsp:include page="${control.page}" />
      </bfs:control>
    </dd>
  </c:forEach>
</dl>
<a href="${requestURI.substring(0, requestURI.lastIndexOf(context.control.entityView.uri))}${context.control.entityView.uri}">back</a>
<a href="${requestURI}/edit">Edit</a>
