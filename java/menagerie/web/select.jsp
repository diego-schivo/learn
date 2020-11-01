<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<select form="${param.form}" name="${param.name}" ${control.multiple ? 'multiple' : ''}>
  <c:if test="${!control.multiple}">
    <option value=""></option>
  </c:if>
  <c:forEach var="option" items="${control.options}">
    <option value="${option}" ${option eq param.value ? 'selected' : ''}>${option}</option>
  </c:forEach>
</select>
