<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="taglibs.jsp" %>

<c:set var="form"><c:if test="${!empty(param.form)}">form="${param.form}"</c:if></c:set>
<input ${form} type="${control.type}" name="${control.name}" value="${control.value}">
