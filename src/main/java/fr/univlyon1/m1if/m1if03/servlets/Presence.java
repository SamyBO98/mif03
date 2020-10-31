package fr.univlyon1.m1if.m1if03.servlets;

import fr.univlyon1.m1if.m1if03.classes.GestionPassages;
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

@WebServlet(name = "Presence", urlPatterns = "/presence")
public class Presence extends HttpServlet {

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("Basic User: Post Method");

        HttpSession session = req.getSession(true);
        ServletContext context = getServletContext();

        String pageInclude;

        if (session.getAttribute("user") != null){

            //Affichage des passages
            if (req.getParameter("nomSalle") == null) {
                req.setAttribute("passagesAffiches",
                        ((GestionPassages) context.getAttribute("passages"))
                                .getPassagesByUser((User) session.getAttribute("user")));

            } else {
                req.setAttribute("passagesAffiches",
                        ((GestionPassages) context.getAttribute("passages"))
                                .getPassagesByUserAndSalle((User) session.getAttribute("user"),
                                        new Salle(req.getParameter("nomSalle"))));
            }

            pageInclude = "contenus/passages.jsp";

        } else if (req.getParameter("login") != null && !req.getParameter("login").equals("")){
            String login = req.getParameter("login");
            User user = new User(login);
            user.setNom(req.getParameter("nom"));
            user.setAdmin(req.getParameter("admin") != null && req.getParameter("admin").equals("on"));
            session.setAttribute("user", user);

            pageInclude = "contenus/default.jsp";
        } else {
            resp.sendRedirect("");
            return;
        }

        //Include page
        session.setAttribute("page", pageInclude);
        req.getRequestDispatcher("interface.jsp").include(req, resp);
    }

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("Basic User: Get Method");

        HttpSession session = req.getSession(true);
        ServletContext context = this.getServletContext();

        String contenu = req.getParameter("contenu");
        String pageInclude;

        if (contenu == null){
            pageInclude = "contenus/default.jsp";
        } else {
            if (contenu.equals("passages")){

                //Affichage des passages
                if (req.getParameter("nomSalle") != null) {
                    req.setAttribute("passagesAffiches",
                            ((GestionPassages) context.getAttribute("passages"))
                                    .getPassagesByUserAndSalle((User) session.getAttribute("user"),
                                            new Salle(req.getParameter("nomSalle"))));
                } else {
                    req.setAttribute("passagesAffiches",
                            ((GestionPassages) context.getAttribute("passages"))
                                    .getPassagesByUser((User) session.getAttribute("user")));
                }

                pageInclude = "contenus/passages.jsp";
            } else if (contenu.equals("user")){
                pageInclude = "contenus/user.jsp?login=" + req.getParameter("login") + ".jsp";
            } else {
                pageInclude = "contenus/" + contenu + ".jsp";
            }
        }

        //Include page
        session.setAttribute("page", pageInclude);
        req.getRequestDispatcher("interface.jsp").include(req, resp);
    }

}
