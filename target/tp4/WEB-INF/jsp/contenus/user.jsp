<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=UTF-8" %>

<c:set var="user" value="${ requestScope.user }"/>

<section>
    <h1>Utilisateur ${ user.login }</h1>
    <ul>
        <li> Login : ${user.login}</li>
        <li>Nom : ${user.nom}</li>
        <li> admin : ${user.admin == true ? "oui" : "non"}</li>
    </ul>
</section>
