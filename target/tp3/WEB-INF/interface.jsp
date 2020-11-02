<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page errorPage="erreurs/error.jsp" %>

<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>Présence</title>
    <link rel="stylesheet" type="text/css" href="static/presence.css">
</head>
<body>
<jsp:include page="composants/header.jsp"/>

<main class="wrapper">
    <jsp:include page="composants/menu.jsp"/>
    <article class="contenu">
        <jsp:include page="${ requestScope.page }"/>
    </article>
</main>

<jsp:include page="composants/footer.html"/>
</body>
</html>
