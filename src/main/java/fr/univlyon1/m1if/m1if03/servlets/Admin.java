package fr.univlyon1.m1if.m1if03.servlets;

import fr.univlyon1.m1if.m1if03.classes.GestionPassages;
import fr.univlyon1.m1if.m1if03.classes.Passage;
import fr.univlyon1.m1if.m1if03.classes.Salle;
import fr.univlyon1.m1if.m1if03.classes.User;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@WebServlet(name = "Admin", urlPatterns = "/admin")
public class Admin extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(true);
        ServletContext context = getServletContext();

        String contenu = req.getParameter("contenu");
        String pageInclude;

        if (req.getParameter("nomSalle") != null && req.getParameter("capacite") != null && Integer.parseInt(req.getParameter("capacite")) >= 0) {
            ((Map<String, Salle>)context.getAttribute("salles")).get(req.getParameter("nomSalle"))
                    .setCapacite(Integer.parseInt(req.getParameter("capacite")));

        }
        req.setAttribute("salles", (Map<String, Salle>)context.getAttribute("salles"));
        pageInclude = "contenus/salles.jsp";
        //Include page
        req.setAttribute("page", pageInclude);
        req.getRequestDispatcher("WEB-INF/interface_admin.jsp").include(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(true);
        ServletContext context = getServletContext();

        String contenu = req.getParameter("contenu");
        String pageInclude;

        if (contenu == null){
            pageInclude = "contenus/default_admin.jsp";
        } else {
            if (contenu.equals("passages")){

                List<Passage> passages = null;

                if (req.getParameter("nomSalle") != null){
                    if (req.getParameter("login") != null){
                        passages = ((GestionPassages)context.getAttribute("passages")).getPassagesByUserAndSalle(
                                ((Map<String, User>)context.getAttribute("users")).get(req.getParameter("login")),
                                ((Map<String, Salle>)context.getAttribute("salles")).get(req.getParameter("nomSalle"))
                        );
                    } else if (req.getParameter("dateEntree") != null && req.getParameter("dateSortie") != null) {
                        try {
                            SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy", new Locale("us"));
                            Date dateEntree = sdf.parse(req.getParameter("dateEntree"));
                            Date dateSortie = sdf.parse(req.getParameter("dateSortie"));
                            passages = ((GestionPassages) context.getAttribute("passages")).getPassagesBySalleAndDates(
                                    ((Map<String, Salle>) context.getAttribute("salles")).get(req.getParameter("nomSalle")),
                                    dateEntree, dateSortie
                            );
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    } else {
                        passages = ((GestionPassages) context.getAttribute("passages")).getPassagesBySalle(
                                ((Map<String, Salle>) context.getAttribute("salles")).get(req.getParameter("nomSalle"))
                        );
                    }
                } else if (req.getParameter("login") != null){
                    passages = ((GestionPassages)context.getAttribute("passages")).getPassagesByUser(
                            ((Map<String, User>)context.getAttribute("users")).get(req.getParameter("login"))
                    );
                } else {
                    passages = ((GestionPassages)context.getAttribute("passages")).getAllPassages();
                }

                req.setAttribute("passagesAffiches", passages);
                pageInclude = "contenus/passages.jsp";
            } else if (contenu.equals("user")){
                User user = ((Map<String, User>)context.getAttribute("users")).get(req.getParameter("login"));
                req.setAttribute("user", user);
                pageInclude = "contenus/user.jsp";
            } else if (contenu.equals("salles")){
                req.setAttribute("salles", (Map<String, Salle>)context.getAttribute("salles"));
                pageInclude = "contenus/salles.jsp";
            } else {
                pageInclude = "contenus/" + contenu + ".jsp";
            }

        }


        //Include page
        req.setAttribute("page", pageInclude);
        req.getRequestDispatcher("WEB-INF/interface_admin.jsp").include(req, resp);
    }
}
