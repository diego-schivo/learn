<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="/taglibs.jsp" %>

<h2>${context.control.heading}</h2>

<dl>
  <c:forEach var="factory" items="${context.control.childFactories()}">
    <dt>${factory.name}</dt>
    <dd>
      <bfs:control var="control" control="${factory.newControl(context.control.target, context.control)}">
        <jsp:include page="${control.page}" />
      </bfs:control>
    </dd>
  </c:forEach>
</dl>
