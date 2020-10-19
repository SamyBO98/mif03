<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="fr.univlyon1.m1if.m1if03.classes.Passage" %>
<%@ page import="fr.univlyon1.m1if.m1if03.classes.Salle" %>
<%@ page import="java.util.Date" %>
<%@ page import="fr.univlyon1.m1if.m1if03.classes.User" %>
<%@ page import="java.util.List" %>

<jsp:useBean id="passages" class="fr.univlyon1.m1if.m1if03.classes.GestionPassages" scope="application"/>

<%
    /**
     * Exercice 1.1: Rediriger l'utilisateur vers la page d'accueil (si aucun utilisateur connecté)
     */
    if (session.getAttribute("user") == null){
        response.sendRedirect("index.html");
        return;
    }

%>

<%
    List<Passage> passageList;

        if ((request.getParameter("user") != null) || (request.getParameter("room") != null)){
            System.out.println("ON EST BONS!");
            //From clicking a link (user / room)
            if (request.getParameter("user") != null){
                passageList = passages.getPassagesByUser(new User(request.getParameter("user")));
            } else {
                passageList = passages.getPassagesBySalle(new Salle(request.getParameter("room")));
            }

        } else if ((request.getParameter("userRoom") != null) && (request.getParameter("textUserRoom") != null)){
            //From passages.html
            
            //Recherche par salle (sinon par utilisateur)
            if (request.getParameter("userRoom").equals("room")){
                passageList = passages.getPassagesBySalle(new Salle(request.getParameter("textUserRoom")));
            } else {
                passageList = passages.getPassagesByUser(new User(request.getParameter("textUserRoom")));
            }

        } else {
            //From saisie.html
            
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

            /**
             * Exercice 1.5: Attribut de requête: GestionPassages
             * Si l'utilisateur est un administrateur, il aura accès à tout
             * Sinon, il n'aura accès qu'à ses passages en salles
             */

            if ((Boolean) request.getSession().getAttribute("admin")){
                passageList = passages.getAllPassages();
            } else {
                passageList = passages.getPassagesByUser((User) session.getAttribute("user"));
            }

    }
    
    session.setAttribute("passageList", passageList);

%>

<table>
    <tr>
        <th>Login</th>
        <th>Salle</th>
        <th>Entrée</th>
        <th>Sortie</th>
    </tr>

    <c:forEach items="${sessionScope.passageList}" var="passage">
        <tr>
            <td>
                <a href="${pageContext.request.contextPath}/interface.jsp?user=${passage.user.login}">
                    ${passage.user.login}
                </a>
            </td>
            <td>
                <a href="${pageContext.request.contextPath}/interface.jsp?room=${passage.salle.nom}">
                        ${passage.salle.nom}
                </a>
            </td>
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

