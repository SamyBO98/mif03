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
import java.util.Date;
import java.util.List;
import java.util.Map;

@WebServlet(name = "Presence", urlPatterns = "/presence")
public class Presence extends HttpServlet {

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("Basic User: Post Method");

        HttpSession session = req.getSession(true);
        ServletContext context = getServletContext();

        String contenu = req.getParameter("contenu");
        String pageInclude;

        /**
         * Ici nous aurons 2 méthodes POST: une venant du formulair de connexion (on fait rien dedans)
         * Une autre venant de l'ajout d'un passage, il faudra ...
         * -> Ajouter l'utilisateur dans le tableau d'users (s'il n'existe pas)
         * -> Ajouter la salle dans le tableau de salles (s'il n'existe pas)
         * -> Insérer un nouveau passage dans cette salle par l'utilisateur
         *      (Sans oublier l'histoire des dates de début et de fin...)
         */
        if (contenu != null && contenu.equals("passages")){
            saisiePassage(req, context, session);
        }

        req.setAttribute("salleSaturee", ((GestionPassages)context.getAttribute("passages"))
                .getPassagesByUserEncours((User)session.getAttribute("user")));
        pageInclude = "contenus/default.jsp";

        //Include page
        req.setAttribute("page", pageInclude);
        req.getRequestDispatcher("WEB-INF/interface.jsp").include(req, resp);
    }

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("Basic User: Get Method");

        HttpSession session = req.getSession(true);
        ServletContext context = this.getServletContext();

        String contenu = req.getParameter("contenu");
        String pageInclude;

        /**
         * On récupère les liens
         * Si aucun paramètre "contenu", on affiche la page d'accueil
         * Sinon
         * Si contenu vaut "passages" -> On met en paramètre la liste des salles à afficher
         */
        if (contenu == null){
            req.setAttribute("salleSaturee", ((GestionPassages)context.getAttribute("passages"))
                    .getPassagesByUserEncours((User)session.getAttribute("user")));
            pageInclude = "contenus/default.jsp";
        } else {
            if (contenu.equals("passages")){

                List<Passage> passages;

                if (req.getParameter("nomSalle") != null){
                    passages = ((GestionPassages)context.getAttribute("passages")).getPassagesBySalle(
                            ((Map<String, Salle>)context.getAttribute("salles")).get(req.getParameter("nomSalle"))
                    );
                } else if (req.getParameter("login") != null){
                    passages = ((GestionPassages)context.getAttribute("passages")).getPassagesByUser(
                            ((Map<String, User>)context.getAttribute("users")).get(req.getParameter("login"))
                    );
                } else {
                    passages = ((GestionPassages)context.getAttribute("passages")).getPassagesByUser(
                            (User)session.getAttribute("user")
                    );
                }

                req.setAttribute("passagesAffiches", passages);
                pageInclude = "contenus/passages.jsp";
            } else if (contenu.equals("user")){
                User user = ((Map<String, User>)context.getAttribute("users")).get(req.getParameter("login"));
                req.setAttribute("user", user);
                pageInclude = "contenus/user.jsp";
            } else {
                pageInclude = "contenus/" + contenu + ".jsp";
            }
        }

        //Include page
        req.setAttribute("page", pageInclude);
        req.getRequestDispatcher("WEB-INF/interface.jsp").include(req, resp);
    }


    /**
     * Saisie d'un nouveau passage
     */
    private void saisiePassage(HttpServletRequest req, ServletContext context, HttpSession session){
        String nomSalle = req.getParameter("nom");
        Salle salle;

        /**
         * Ajout de la salle dans "salles"
         */
        if (((Map<String, Salle>)context.getAttribute("salles")).get(nomSalle) == null) {
            salle = new Salle(nomSalle);
            ((Map<String, Salle>)context.getAttribute("salles")).put(nomSalle, salle);
        } else
            salle = ((Map<String, Salle>)context.getAttribute("salles")).get(nomSalle);
        User user = (User) session.getAttribute("user");

        /**
         * Ajout de l'utilisateur dans "users"
         */
        if (((Map<String, User>)context.getAttribute("users")).get(user.getLogin()) == null){
            ((Map<String, User>)context.getAttribute("users")).put(user.getLogin(), user);
        }

        /**
         * Ajout d'une salle dans GestionPassage
         */
        if (req.getParameter("entree") != null) {
            Passage p = new Passage(user, salle, new Date());
            ((GestionPassages)context.getAttribute("passages")).add(p);
            ((Map<String, Salle>)context.getAttribute("salles")).get(nomSalle).incPresent();
        } else if (req.getParameter("sortie") != null) {
            List<Passage> passTemp = ((GestionPassages)context.getAttribute("passages")).getPassagesByUserAndSalle(user, salle);
            for (Passage p : passTemp) { // On mémorise une sortie de tous les passages existants et sans sortie
                if (p.getSortie() == null) {
                    p.setSortie(new Date());
                    ((Map<String, Salle>)context.getAttribute("salles")).get(nomSalle).decPresent();
                }
            }
        }
    }

}
