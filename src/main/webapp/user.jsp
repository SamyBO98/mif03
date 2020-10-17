<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%@ page import="fr.univlyon1.m1if.m1if03.classes.User" %>
<%@ page import="java.util.List" %>
<%@ page import="fr.univlyon1.m1if.m1if03.classes.GestionPassages" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:useBean id="passages" class="fr.univlyon1.m1if.m1if03.classes.GestionPassages" scope="application"/>
<% HttpSession session1 = request.getSession(true);
    User u = (User)session1.getAttribute("user");%>

<html>
<head>
    <title>Liste de passages</title>
</head>
<body>

<table>
    <tr>
        <th>Login</th>
    </tr>

    <c:if test="${ passages.getPassagesByUser(u) != null }">
        <tr>
            <td>${passage.user.login}</td>
        </tr>
    </c:if>
</table>

</body>
</html>

