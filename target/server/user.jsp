<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="fr.univlyon1.m1if.m1if03.classes.Passage" %>
<%@ page import="java.util.List" %>
<%@ page import="fr.univlyon1.m1if.m1if03.classes.User" %><%--
  Created by IntelliJ IDEA.
  User: admin
  Date: 17/10/2020
  Time: 14:26
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<jsp:useBean id="passages" class="fr.univlyon1.m1if.m1if03.classes.GestionPassages" scope="application"/>

<%
    if (session.getAttribute("user") == null){
        response.sendRedirect("index.html");
    } else {
        System.out.println(((User)session.getAttribute("user")).getLogin());
    }
%>

<%
    List<Passage> tabPassages = passages.getAllPassages();

    boolean userExists = false;

    for (int i = 0; i < tabPassages.size(); i++){
        System.out.println(i + ": " + tabPassages.get(i).getUser().getLogin());
        if (((User)session.getAttribute("user")).getLogin().equals(tabPassages.get(i).getUser().getLogin())){
            userExists = true;
            break;
        }
    }

%>

<html>
<head>
    <title>Utilisateur</title>
</head>
<body>

<c:if test="${userExists}">
    <h1>Informations de l'utilisateur</h1>
    <h2>Utilisateur: <%= ((User) (session.getAttribute("user"))).getLogin() %></h2>
</c:if>

<c:if test="${!userExists}">
    <h1>Vous n'existez pas</h1>
    <h2>Vous devriez créer des salles pour qu'on puisse vous identifier</h2>
</c:if>

</body>
</html>
