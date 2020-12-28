<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="/taglibs.jsp" %>

<h2>Find Owners</h2>

<form method="get" action="${requestURI.substring(0, requestURI.lastIndexOf('/'))}">
  <dl>
    <ui:controlFactories var="factory">
      <c:if test="${factory.name eq 'lastName'}">
        <dt>
          <label>Last name</label>
        </dt>
        <dd>
          <ui:control var="control" control="${factory.newControl(control.target, context.control)}">
            <jsp:include page="${control.page}" />
          </ui:control>
        </dd>
      </c:if>
    </ui:controlFactories>
  </dl>
  <button>Find Owner</button>
</form>
<ui:context var="context">
  <a href="${context.resource.uri}/new">Add Owner</a>
</ui:context>
