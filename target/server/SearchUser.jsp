<%@ page import="fr.univlyon1.m1if.m1if03.classes.User" %>
<%@ page import="fr.univlyon1.m1if.m1if03.classes.Passage" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: samyb
  Date: 20/10/2020
  Time: 16:31
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:useBean id="passages" class="fr.univlyon1.m1if.m1if03.classes.GestionPassages" scope="application"/>

<% if(request.getParameter("nom_user") != null){
    List<Passage> passages1;
    List<String> utilisateursRisky = new ArrayList<>();
    passages1 = passages.getPassagesByUser(new User(request.getParameter("nom_user")));
    //System.out.println(passages1);
    for(int i=0; i < passages1.size(); i++){
        List<Passage> passages2;
        //System.out.println("Passage 1 " + passages1.get(i).getUser().getLogin());
        passages2 = passages.getPassagesBySalleAndDates(passages1.get(i).getSalle(), passages1.get(i).getEntree(), passages1.get(i).getSortie());
        //System.out.println(passages2);
        for(int j=0; j<passages2.size(); j++){
            //System.out.println("Passage 2 " + passages2.get(j).getUser().getLogin());
            if(!request.getParameter("nom_user").equals(passages2.get(j).getUser().getLogin())){
                if(!utilisateursRisky.contains(passages2.get(j).getUser().getLogin())){
                    utilisateursRisky.add(passages2.get(j).getUser().getLogin());
                }
            }
        }
        //System.out.println(utilisateursRisky);
    }
    session.setAttribute("risky_user", utilisateursRisky);
}else{
    response.sendRedirect("interface.jsp");
}
%>
<table>
    <tr>
        <th>Utilisateurs</th>
    </tr>

    <c:forEach items="${ sessionScope.risky_user }" var="risky">
        <tr>
            <td>${ risky }</td>
        </tr>
    </c:forEach>
</table>
