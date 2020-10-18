<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%@ page import="fr.univlyon1.m1if.m1if03.classes.User" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<jsp:useBean id="passages" class="fr.univlyon1.m1if.m1if03.classes.GestionPassages" scope="application"/>

<%
    User u;
    if (request.getParameter("login") != null) {
        u = new User(request.getParameter("login"));
    } else {
        u = (User) session.getAttribute("user");
    }
%>

<table>
    <tr>
        <th>Login</th>
    </tr>

    <c:choose>
        <c:when test="<%= passages.getPassagesByUser(u).size() > 0 %>">
            <tr>
                <td><%= u.getLogin() %></td>
            </tr>
        </c:when>

        <c:otherwise>
            <tr>
                <td>L'utilisateur n'existe pas ou n'a pas crée de salles</td>
            </tr>
        </c:otherwise>
    </c:choose>

</table>


