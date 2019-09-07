<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags/form" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<li> <span><a href="#${copyitem.id}" oncontextmenu="copyDropdown(${copyitem.id});return false">${copyitem.name}</a></span>
    <div id="dropdownChangeAction" class="dropdown-content">
        <a id="1" href="#ParentNode" onclick="selectNodeToChange()">Вставить</a>
    </div>
    <c:if test="${fn:length(copyitem.children) gt 0}">
        <ul>
            <c:forEach var="copyitem" items="${copyitem.children}">
                <c:set var="copyitem" value="${copyitem}" scope="request" />
                <jsp:include page="nodeParentToSelect.jsp" />
            </c:forEach>
        </ul>
    </c:if>
</li>

