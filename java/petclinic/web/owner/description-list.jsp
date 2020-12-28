<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="/taglibs.jsp" %>

<h2>${context.control.heading}</h2>

<dl>
  <ui:controlFactories var="factory">
    <dt>${factory.name}</dt>
    <dd>
      <ui:control var="control" control="${factory.newControl(context.control.target, context.control)}">
        <jsp:include page="${control.page}" />
      </ui:control>
    </dd>
  </ui:controlFactories>
</dl>
