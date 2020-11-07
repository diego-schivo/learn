<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<select form="${param.form}" name="${control.name}" ${control.multiple ? 'multiple' : ''}>
  <c:if test="${!control.multiple}">
    <option value=""></option>
  </c:if>
  <c:forEach var="option" items="${control.options}">
    <c:set var="selected" value="${control.values.contains(option)}" />
    <option value="${option}" ${selected ? 'selected' : ''}>${option}</option>
  </c:forEach>
</select>
