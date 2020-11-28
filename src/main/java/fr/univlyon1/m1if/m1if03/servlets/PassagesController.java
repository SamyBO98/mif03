package fr.univlyon1.m1if.m1if03.servlets;

import fr.univlyon1.m1if.m1if03.classes.GestionPassages;
import fr.univlyon1.m1if.m1if03.classes.Passage;
import fr.univlyon1.m1if.m1if03.classes.Salle;
import fr.univlyon1.m1if.m1if03.classes.User;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static fr.univlyon1.m1if.m1if03.utils.ParseURI.parseUri;

public class PassagesController extends HttpServlet {

    GestionPassages passages;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        passages = (GestionPassages) config.getServletContext().getAttribute("passages");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<String> uri = parseUri(req.getRequestURI(), "passages");
        HttpSession session = req.getSession(true);
        User user = (User) session.getAttribute("user");

        if (uri.size() == 0){
            /**
             * Renvoie les URI de tous les passages
             * Code 200: OK
             * Code 401: Utilisateur non authentifié
             * Code 403: Utilisateur non administrateur
             */
            if (req.getHeader("accept").contains("application/json")){
                //JSON
                PrintWriter out = resp.getWriter();
                out.write("[\n");
                for (Passage passage: passages.getAllPassages()){
                    out.write("\"http://localhost:8080" + req.getRequestURI() + "/" + passage.getId() + "\"\n");
                }
                out.write("]");
                out.close();

            } else if (req.getHeader("accept").contains("text/html")){
                //HTML
                req.setAttribute("passages", passages.getAllPassages());
            }
            resp.setStatus(200);

        } else if (uri.size() == 1){
            /**
             * Renvoie la représentation d'un passage
             * Code 200: OK
             * Code 401: Utilisateur non authentifié
             * Code 403: Utilisateur non administrateur
             * Code 404: Passage non trouvé
             */
            Passage passage = passages.getPassageById(Integer.parseInt(uri.get(0)));
            if (passage == null){
                resp.sendError(404, "Passage non trouvé.");
                return;
            }

            if (req.getHeader("accept").contains("application/json")) {
                //JSON
                PrintWriter out = resp.getWriter();
                out.write("{\n");
                out.write("\"user\": \"" + passage.getUser().getLogin() + "\",\n");
                out.write("\"salle\": \"" + passage.getSalle().getNom() + "\",\n");
                out.write("\"dateEntree\": " + passage.getEntree().toString() + "\",\n");
                out.write("\"dateSortie\": " + passage.getSortie().toString() + "\n");
                out.write("}");
                out.close();
            } else if (req.getHeader("accept").contains("text/html")){
                req.setAttribute("passages", passage);
                req.setAttribute("page", "passage");
            }
            resp.setStatus(200);

        } else if (uri.size() == 2){
            if (uri.get(0).equals("byUser")){
                /**
                 * Renvoie les URI des passages de l'utilisateur indiqué
                 * Code 200: OK
                 * Code 401: Utilisateur non authentifié
                 * Code 403: Utilisateur non administrateur
                 * Code 404: Passage non trouvé
                 */
                List<Passage> passagesList = passages.getPassagesByUser(new User(uri.get(1)));
                if (passagesList == null){
                    resp.sendError(404, "Aucun passage provenant de l'utilisateur");
                    return;
                }

                if (req.getHeader("accept").contains("application/json")){
                    //JSON
                    PrintWriter out = resp.getWriter();
                    out.write("[\n");
                    for (Passage passage: passagesList){
                        out.write("\"http://localhost:8080" + req.getRequestURI() + "/" + passage.getId() + "\"\n");
                    }
                    out.write("]");
                    out.close();

                } else if (req.getHeader("accept").contains("text/html")){
                    //HTML
                    req.setAttribute("passages", passagesList);
                    req.setAttribute("page", "passages");
                }
                resp.setStatus(200);

            } else if (uri.get(0).equals("bySalle")){
                /**
                 * Renvoie les URI des passages d'une salle indiquée
                 * Code 200: OK
                 * Code 401: Utilisateur non authentifié
                 * Code 403: Utilisateur non administrateur
                 * Code 404: Passage non trouvé
                 */
                List<Passage> passagesList = passages.getPassagesBySalle(new Salle(uri.get(1)));
                if (passagesList == null){
                    resp.sendError(404, "Aucun passage provenant de la salle");
                    return;
                }

                if (req.getHeader("accept").contains("application/json")){
                    //JSON
                    PrintWriter out = resp.getWriter();
                    out.write("[\n");
                    for (Passage passage: passagesList){
                        out.write("\"http://localhost:8080" + req.getRequestURI() + "/" + passage.getId() + "\"\n");
                    }
                    out.write("]");
                    out.close();

                } else if (req.getHeader("accept").contains("text/html")){
                    //HTML
                    req.setAttribute("passages", passagesList);
                    req.setAttribute("page", "passages");
                }
                resp.setStatus(200);

            }
        } else if (uri.size() == 3){
            if (uri.get(0).equals("byUser") && uri.get(2).equals("enCours")){
                /**
                 * Renvoie les URI des passages de l'utilisateur indiqué sans date de sortie
                 * Code 200: OK
                 * Code 401: Utilisateur non authentifié
                 * Code 403: Utilisateur non administrateur
                 * Code 404: Passage non trouvé
                 */
                List<Passage> passagesList = passages.getPassagesByUserEncours(new User(uri.get(1)));
                if (passagesList == null){
                    resp.sendError(404, "Aucun passage provenant de l'utilisateur");
                    return;
                }

                if (req.getHeader("accept").contains("application/json")){
                    //JSON
                    PrintWriter out = resp.getWriter();
                    out.write("[\n");
                    for (Passage passage: passagesList){
                        out.write("\"http://localhost:8080" + req.getRequestURI() + "/" + passage.getId() + "\"\n");
                    }
                    out.write("]");
                    out.close();

                } else if (req.getHeader("accept").contains("text/html")){
                    //HTML
                    req.setAttribute("passages", passagesList);
                    req.setAttribute("page", "passages");
                }
                resp.setStatus(200);

            } else if (uri.get(0).equals("byUserAndSalle")){
                /**
                 * Renvoie les URI des passages de l'utilisateur et d'une salle indiqués
                 * Code 200: OK
                 * Code 401: Utilisateur non authentifié
                 * Code 403: Utilisateur non administrateur
                 * Code 404: Passage non trouvé
                 */
                List<Passage> passagesList = passages.getPassagesByUserAndSalle(new User(uri.get(1)), new Salle(uri.get(2)));
                if (passagesList == null){
                    resp.sendError(404, "Aucun passage provenant de l'utilisateur et de la salle");
                    return;
                }

                if (req.getHeader("accept").contains("application/json")){
                    //JSON
                    PrintWriter out = resp.getWriter();
                    out.write("[\n");
                    for (Passage passage: passagesList){
                        out.write("\"http://localhost:8080" + req.getRequestURI() + "/" + passage.getId() + "\"\n");
                    }
                    out.write("]");
                    out.close();

                } else if (req.getHeader("accept").contains("text/html")){
                    //HTML
                    req.setAttribute("passages", passagesList);
                    req.setAttribute("page", "passages");
                }
                resp.setStatus(200);

            }
        } else if (uri.size() == 4){
            if (uri.get(0).equals("byUserAndDates")){
                /**
                 * Renvoie les URI des passages de l'utilisateur et dates indiqués
                 * Code 200: OK
                 * Code 401: Utilisateur non authentifié
                 * Code 403: Utilisateur non administrateur
                 * Code 404: Passage non trouvé
                 */
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy", new Locale("us"));
                    Date dateEntree = sdf.parse(uri.get(2));
                    Date dateSortie = sdf.parse(uri.get(3));

                    List<Passage> passagesList = passages.getPassagesByUserAndDates(new User(uri.get(1)), dateEntree, dateSortie);

                    req.setAttribute("passages", passagesList);

                    if (req.getHeader("accept").contains("application/json")){
                        //JSON
                        PrintWriter out = resp.getWriter();
                        out.write("[\n");
                        for (Passage passage: passagesList){
                            out.write("\"http://localhost:8080" + req.getRequestURI() + "/" + passage.getId() + "\"\n");
                        }
                        out.write("]");
                        out.close();

                    } else if (req.getHeader("accept").contains("text/html")){
                        //HTML
                        req.setAttribute("passages", passagesList);
                        req.setAttribute("page", "passages");
                    }
                    resp.setStatus(200);
                } catch (ParseException e) {
                    e.printStackTrace();
                    resp.sendError(404, "Passage non trouvé");
                }

            } else if (uri.get(0).equals("bySalleAndDates")){
                /**
                 * Renvoie les URI des passages d'une salle et dates indiqués
                 * Code 200: OK
                 * Code 401: Utilisateur non authentifié
                 * Code 403: Utilisateur non administrateur
                 * Code 404: Passage non trouvé
                 */
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy", new Locale("us"));
                    Date dateEntree = sdf.parse(uri.get(2));
                    Date dateSortie = sdf.parse(uri.get(3));

                    List<Passage> passagesList = passages.getPassagesBySalleAndDates(new Salle(uri.get(1)), dateEntree, dateSortie);
                    req.setAttribute("passages", passagesList);

                    if (req.getHeader("accept").contains("application/json")){
                        //JSON
                        PrintWriter out = resp.getWriter();
                        out.write("[\n");
                        for (Passage passage: passagesList){
                            out.write("\"http://localhost:8080" + req.getRequestURI() + "/" + passage.getId() + "\"\n");
                        }
                        out.write("]");
                        out.close();

                    } else if (req.getHeader("accept").contains("text/html")){
                        //HTML
                        req.setAttribute("passages", passagesList);
                        req.setAttribute("page", "passages");
                    }
                    resp.setStatus(200);
                } catch (ParseException e) {
                    e.printStackTrace();
                    resp.sendError(404, "Passage non trouvé");
                }
            }
        }
        requestDispatcherAdmin(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<String> uri = parseUri(req.getRequestURI(), "passages");
        HttpSession session = req.getSession(true);
        User user = (User) session.getAttribute("user");

        if (uri.size() == 0){
            /**
             * Ajoute un nouveau passage
             * Code 201: Passagé crée (location: URL du passage crée)
             * Code 400: Paramètres de requête non acceptables
             * Code 401: Utilisateur non authentifié
             */
            if (user == null){
                resp.sendError(401, "Utilisateur non authentifié");
                return;
            }

            String nomSalle = req.getParameter("nomSalle");
            if (nomSalle == null) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Salle non spécifiée.");
                return;
            }

            Salle salle = ((Map<String, Salle>) req.getServletContext().getAttribute("salles")).get(nomSalle);

            if (salle == null) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "La salle " + nomSalle + " n'existe pas.");
                return;
            }

            if (req.getParameter("action").equals("Entrer")) {
                Passage p = new Passage(user, salle, new Date());
                passages.add(p);
                salle.incPresent();
            } else if (req.getParameter("action").equals("Sortir")) {
                List<Passage> passTemp = passages.getPassagesByUserAndSalle(user, salle);
                for (Passage p : passTemp) { // On mémorise une sortie de tous les passages existants et sans sortie
                    if (p.getSortie() == null) {
                        p.setSortie(new Date());
                        salle.decPresent();
                    }
                }
            } else {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Action invalide.");
                return;
            }
        }
    }

    private void requestDispatcher(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/jsp/interface.jsp")
                .include(req, resp);
    }

    private void requestDispatcherAdmin(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/jsp/interface_admin.jsp")
                .include(req, resp);
    }

}
