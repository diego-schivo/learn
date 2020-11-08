<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="taglibs.jsp" %>

<c:set var="form"><c:if test="${!empty(param.form)}">form="${param.form}"</c:if></c:set>
<select ${form} name="${control.name}" ${control.multiple ? 'multiple' : ''}>
  <c:if test="${!control.multiple}">
    <option value=""></option>
  </c:if>
  <c:forEach var="option" items="${control.options}">
    <c:set var="selected" value="${control.values.contains(option)}" />
    <option value="${option}" ${selected ? 'selected' : ''}>${option}</option>
  </c:forEach>
</select>
