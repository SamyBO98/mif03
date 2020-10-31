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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

@WebServlet(name = "Admin", urlPatterns = "/admin")
public class Admin extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(true);
        ServletContext context = getServletContext();

        String contenu = req.getParameter("contenu");
        String pageInclude;

        if (req.getParameter("capacite") != null && Integer.parseInt(req.getParameter("capacite")) >= 0){
            HashMap<String,Salle> salles = (HashMap<String,Salle>)(context.getAttribute("salles"));
            Salle s = (salles.get(req.getParameter("nomSalle")));
            s.setCapacite(Integer.parseInt(req.getParameter("capacite")));
            salles.remove(req.getParameter("nomSalle"));
            salles.put(req.getParameter("nomSalle"),s);
        }

        pageInclude = "contenus/salles.jsp";
        //Include page
        session.setAttribute("page", pageInclude);
        req.getRequestDispatcher("interface_admin.jsp").include(req, resp);
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
                if (req.getParameter("nomSalle") != null) {
                    if (req.getParameter("login") != null){
                        req.setAttribute("passagesAffiches",
                                ((GestionPassages)context.getAttribute("passages")).getPassagesByUserAndSalle(
                                        new User(req.getParameter("login")),
                                        new Salle(req.getParameter("nomSalle"))));
                    } else if (req.getParameter("dateEntree") != null && req.getParameter("dateSortie") != null) {
                        try {
                            SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy", new Locale("us"));
                            Date dateEntree = sdf.parse(req.getParameter("dateEntree"));
                            Date dateSortie = sdf.parse(req.getParameter("dateSortie"));
                            req.setAttribute("passagesAffiches",
                                    ((GestionPassages)context.getAttribute("passages")).getPassagesBySalleAndDates(
                                            new Salle(req.getParameter("nomSalle")), dateEntree, dateSortie));

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    } else {
                        req.setAttribute("passagesAffiches",
                                ((GestionPassages)context.getAttribute("passages")).getPassagesBySalle(
                                        new Salle(req.getParameter("nomSalle"))));
                    }

                } else if (req.getParameter("login") != null) {
                    req.setAttribute("passagesAffiches",
                            ((GestionPassages)context.getAttribute("passages")).getPassagesByUser(
                                    new User(req.getParameter("login"))));
                } else {
                    req.setAttribute("passagesAffiches",
                            ((GestionPassages)context.getAttribute("passages")).getAllPassages());
                }


                pageInclude = "contenus/passages.jsp";
            } else if (contenu.equals("user")){
                pageInclude = "contenus/user.jsp?login=" + req.getParameter("login");
            } else {
                pageInclude = "contenus/" + contenu + ".jsp";
            }
        }


        //Include page
        session.setAttribute("page", pageInclude);
        req.getRequestDispatcher("interface_admin.jsp").include(req, resp);
    }
}
