<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="/taglibs.jsp" %>

  <h2>${context.control.heading}</h2>

  <form action="${context.control.action}" method="${context.control.method}">
    <dl>
      <ui:controlFactories var="factory">
        <dt>
          <label>${factory.heading}</label>
        </dt>
        <dd>
          <ui:control var="control" control="${factory.newControl(context.control.target, context.control)}">
            <jsp:include page="${control.page}" />
          </ui:control>
        </dd>
      </ui:controlFactories>
    </dl>
    <ui:control var="control">
      <button>${context.text(control)}</button>
    </ui:control>
  </form>
  <a href="${requestURI.substring(0, requestURI.lastIndexOf('/'))}">back</a>

