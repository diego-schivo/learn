<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="taglibs.jsp" %>

  <h2>${context.servlet.class1.simpleName}</h2>

  <form method="post" action="${requestURI}">
    <dl>
      <c:forEach var="factory" items="${control.factories}">
        <dt>
          <label>${context.text(factory)}</label>
        </dt>
        <dd>
          <bfs:control var="control" control="${factory.create(control.item)}">
            <jsp:include page="${control.page}" />
          </bfs:control>
        </dd>
      </c:forEach>
    </dl>
    <bfs:control var="control">
      <button>${context.text(control)}</button>
    </bfs:control>
  </form>
  <a href="${requestURI.substring(0, requestURI.lastIndexOf('/'))}">back</a>

