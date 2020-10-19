<%@ page import="fr.univlyon1.m1if.m1if03.classes.Passage" %>
<%@ page import="java.util.List" %>
<%@ page import="fr.univlyon1.m1if.m1if03.classes.Salle" %>
<%@ page import="fr.univlyon1.m1if.m1if03.classes.User" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%--
  Created by IntelliJ IDEA.
  User: admin
  Date: 19/10/2020
  Time: 18:28
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<jsp:useBean id="passages" class="fr.univlyon1.m1if.m1if03.classes.GestionPassages" scope="application"/>


<c:choose>
    <c:when test="${ pageContext.request.getParameter(\"userRoom\") == \"user\" }">
        <h1>Recherche de passages par l'utilisateur <%= request.getParameter("textUserRoom") %></h1>
    </c:when>

    <c:otherwise>
        <h1>Recherche de passages par la salle <%= request.getParameter("textUserRoom") %></h1>
    </c:otherwise>
</c:choose>

<table>
    <tr>
        <th>Login</th>
        <th>Salle</th>
        <th>Entrée</th>
        <th>Sortie</th>
    </tr>

    <c:forEach items="${ sessionScope.passageList }" var="passage">
        <tr>
            <td>${ passage.user.login }</td>
            <td>${ passage.salle.nom }</td>
            <td>
                <fmt:formatDate value="${ passage.entree }" var="heureEntree" type="time" />
                    ${ heureEntree }
            </td>
            <td>
                <fmt:formatDate value="${ passage.sortie }" var="heureSortie" type="time" />
                    ${ heureSortie }
            </td>
        </tr>
    </c:forEach>
</table>




