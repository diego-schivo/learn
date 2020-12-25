<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="/taglibs.jsp" %>

<%-- ${control.parent.entityView.uri} --%>
<a href="${requestURI}/${control.value}">${control.target.firstName} ${control.target.lastName}</a>
