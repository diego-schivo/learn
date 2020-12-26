<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="/taglibs.jsp" %>

  <h2>${context.control.heading}</h2>

  <form action="${context.control.action}" method="${context.control.method}">
    <dl>
      <c:forEach var="factory" items="${context.control.factories}">
        <dt>
          <label>${factory.heading}</label>
        </dt>
        <dd>
          <bfs:control var="control" control="${factory.create(context.control.target)}">
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

