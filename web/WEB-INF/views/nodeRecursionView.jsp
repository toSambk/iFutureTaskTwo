<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags/form" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

    <li> <span><a href="#${menuitem.id}" oncontextmenu="basicDropdown(${menuitem.id});return false">${menuitem.name}</a></span>
        <div class="dropdown">
        <div id="dropdownBasic" class="dropdown-content">
                <a id="1" href="#Delete" onclick="deleteItem()">Удалить</a>
                <a id="2" href="#Add" onclick="addItem()">Добавить</a>
                <a id="3" href="#Copy" onclick="copyItem()">Копировать</a>
                <a id="4" href="#Paste" onclick="cutItem()">Вырезать</a>
            </div>
            </div>
        <c:if test="${fn:length(menuitem.children) gt 0}">
        <ul>
            <c:forEach var="menuitem" items="${menuitem.children}">
                <c:set var="menuitem" value="${menuitem}" scope="request" />
                <jsp:include page="nodeRecursionView.jsp" />
            </c:forEach>
        </ul>
        </c:if>
    </li>












