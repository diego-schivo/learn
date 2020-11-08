<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="taglibs.jsp" %>

<table>
  <c:forEach var="item" items="${control.items}" varStatus="loop">
    <tr>
      <td>
        <table>
          <c:forEach var="entry" items="${control.factories}">
            <c:set var="uri0" value="${uri}" />
            <c:set var="control0" value="${control}" />
            <c:set var="uri" value="${control.uri}" scope="request" />
            <c:set var="control" value="${entry.value.control(item)}" scope="request" />
            <tr>
              <th align="right">${entry.key}</th>
              <td>
                <jsp:include page="${control.page}" />
              </td>
            </tr>
            <c:set var="uri" value="${uri0}" scope="request" />
            <c:set var="control" value="${control0}" scope="request" />
          </c:forEach>
        </table>
      </td>
      <td>
        <a href="${requestURI}${control.uri}/${item.id}/edit">Edit pet</a>
      </td>
    </tr>
  </c:forEach>
</table>

