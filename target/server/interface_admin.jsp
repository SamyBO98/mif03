<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: admin
  Date: 17/10/2020
  Time: 15:53
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    if (session.getAttribute("admin") != null){
        if (!(Boolean) request.getSession().getAttribute("admin")){
            if (request.getParameter("user") != null){
                response.sendRedirect("interface.jsp?user=" + request.getParameter("user"));
            } else if (request.getParameter("room") != null){
                response.sendRedirect("interface.jsp?room=" + request.getParameter("room"));
            } else {
                response.sendRedirect("interface.jsp");
            }
        }
    }
%>

<html>
<head>
    <title>Menu</title>
</head>
<body>
    <h1>Bienvenue dans la page d'accueil</h1>

    <h2>Menu</h2>

    <a href="${pageContext.request.contextPath}/interface_admin.jsp?page=user.jsp">Informations sur l'utilisateur</a>
    <a href="${pageContext.request.contextPath}/interface_admin.jsp?page=passage.jsp">Liste de tout les passages</a>

    <!-- Routeurs: includes -->
    <c:choose>
        <c:when test="${ param.page != null }">
            <jsp:include page="/${ param.page }"/>
        </c:when>

        <c:when test="${ pageContext.request.getParameter(\"saisie_done\") != null }">
            <jsp:include page="/${ pageContext.request.getParameter(\"saisie_done\") }"/>
        </c:when>

        <c:when test="${ param.user != null || param.room != null }">
            <jsp:include page="/passage.jsp"/>
        </c:when>

        <c:otherwise>
            <h3>Rien à afficher pour le moment...</h3>
        </c:otherwise>
    </c:choose>

    <p><a href="cherche_utilisateur.html">Rechercher un utilisateur</a></p>
    <p><a href="passages.html">Passages selon un utilisateur ou une salle</a></p>
    <p><a href="saisie.html">Saisir un nouveau passage</a></p>
    <p><a href="Deco">Se déconnecter</a></p>
</body>
</html>
