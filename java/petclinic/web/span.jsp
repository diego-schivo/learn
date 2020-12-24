<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="/taglibs.jsp" %>

<span>${control.multiple ? fn:join(control.values.toArray(), ' ') : control.value}</span>
