<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%--
  Created by IntelliJ IDEA.
  User: admin
  Date: 16/10/2020
  Time: 19:35
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<jsp:useBean id="passages" class="fr.univlyon1.m1if.m1if03.classes.GestionPassages" scope="application"/>

<html>
<head>
    <title>Liste de passages</title>
</head>
<body>

    <table>
        <tr>
            <th>Login</th>
            <th>Salle</th>
            <th>Entrée</th>
            <th>Sortie</th>
        </tr>

        <c:forEach items="<%= passages.getAllPassages() %>" var="passage">
            <tr>
                <td>${passage.user.login}</td>
                <td>${passage.salle.nom}</td>
                <td>
                    <fmt:formatDate value="${passage.entree}" var="heureEntree" type="time" />
                        ${heureEntree}
                </td>
                <td>
                    <fmt:formatDate value="${passage.sortie}" var="heureSortie" type="time" />
                        ${heureSortie}
                </td>
            </tr>
        </c:forEach>
    </table>

</body>
</html>
