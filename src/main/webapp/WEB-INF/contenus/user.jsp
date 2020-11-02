<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>

<c:set var="user" value="${ requestScope.user }"/>

<section>

    <c:if test="${ user == null }">
        <h1>Utilisateur non trouvé</h1>
        <h2>Cet utilisateur n'a soit jamais existé, soit pas encore rejoint de salles...</h2>
    </c:if>

    <c:if test="${ user != null }">
        <h1>Utilisateur ${ user.login } trouvé</h1>
        <ul>
            <li>Login : ${ user.login }</li>
            <li>Nom : ${ user.nom }</li>
            <li>${ user.admin == true ? "Administrateur" : "Non administrateur" }</li>
        </ul>
    </c:if>

</section>
