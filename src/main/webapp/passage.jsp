<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="fr.univlyon1.m1if.m1if03.classes.Passage" %>
<%@ page import="fr.univlyon1.m1if.m1if03.classes.Salle" %>
<%@ page import="java.util.Date" %>
<%@ page import="fr.univlyon1.m1if.m1if03.classes.User" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>

<jsp:useBean id="passages" class="fr.univlyon1.m1if.m1if03.classes.GestionPassages" scope="application"/>

<%
    /**
     * Exercice 1.1: Rediriger l'utilisateur vers la page d'accueil (si aucun utilisateur connecté)
     */
    if (session.getAttribute("user") == null){
        response.sendRedirect("index.html");
        return;
    }

    /**
     * Exercice 1.5: Attribut de requête: GestionPassages
     * Si l'utilisateur est un administrateur, il aura accès à tout
     * Sinon, il n'aura accès qu'à ses passages en salles
     */
    List<Passage> passageList;
    if ((Boolean) request.getSession().getAttribute("admin")){
        passageList = passages.getAllPassages();
    } else {
        passageList = passages.getPassagesByUser((User) session.getAttribute("user"));
    }
    session.setAttribute("passages", passageList);

%>

<% if (request.getMethod().equals("POST")) { // Traitement du formulaire envoyé par saisie.html

    if(request.getParameter("entree") != null) {
        passages.add(new Passage(
                (User) session.getAttribute("user"),
                new Salle(request.getParameter("nom")),
                new Date())
        );
    } else if(request.getParameter("sortie") != null) {
        List<Passage> passTemp = passages.getPassagesByUserAndSalle(
                (User) session.getAttribute("user"),
                new Salle(request.getParameter("nom"))
        );
        if(!passTemp.isEmpty()) {
            Passage p = passTemp.get(0);
            p.setSortie(new Date());
        }
    }

} %>

<table>
    <tr>
        <th>Login</th>
        <th>Salle</th>
        <th>Entrée</th>
        <th>Sortie</th>
    </tr>

    <c:forEach items="${sessionScope.passages}" var="passage">
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

