<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%@ page import="fr.univlyon1.m1if.m1if03.classes.User" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<jsp:useBean id="passages" class="fr.univlyon1.m1if.m1if03.classes.GestionPassages" scope="application"/>

<%
    User u = (User) session.getAttribute("user");
    System.out.println(u.getLogin());
%>

<html>
<head>
    <title>Liste de passages</title>
</head>
<body>

<table>
    <tr>
        <th>Login</th>
    </tr>

    <c:if test="<%= passages.getPassagesByUser(u).size() > 0 %>">
        <tr>
            <td><%= u.getLogin() %></td>
        </tr>
    </c:if>

</table>

</body>
</html>

