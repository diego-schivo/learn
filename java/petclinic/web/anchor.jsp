<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="/taglibs.jsp" %>

<%-- ${control.parent.entityView.uri} --%>
<%--
<a href="${requestURI}${control.parent.entityView.uri}/${control.value}">${control.value}</a>
--%>
<a href="${control.href}">${control.text}</a>
