<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<c:set var="form"><c:if test="${!empty(param.form)}">form="${param.form}"</c:if></c:set>
<input ${form} type="${control.type}" name="${control.name}" value="${control.value}">
