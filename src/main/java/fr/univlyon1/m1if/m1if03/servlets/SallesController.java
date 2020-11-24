package fr.univlyon1.m1if.m1if03.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.univlyon1.m1if.m1if03.classes.Salle;
import fr.univlyon1.m1if.m1if03.classes.User;
import fr.univlyon1.m1if.m1if03.dtos.SalleDTO;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import static fr.univlyon1.m1if.m1if03.utils.ParseURI.parseUri;

@WebServlet(name = "SallesControlller", urlPatterns = {"/salles", "/salles/*"})
public class SallesController extends HttpServlet {

    Map<String, Salle> salles;

    @Override
    @SuppressWarnings("unchecked")
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        salles = (Map<String, Salle>) config.getServletContext().getAttribute("salles");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<String> uri = parseUri(req.getRequestURI(), "salles");
        HttpSession session = req.getSession(true);
        User user = (User) session.getAttribute("user");

        if (uri.size() == 0){
            /**
             * Renvoie les URI des salles
             * Code 200: OK
             * Code 401: Utilisateur non authentifié
             * Code 403: Utilisateur non administrateur
             */
            if (user == null){
                resp.sendError(401, "Utilisateur non authentifié");
                return;
            } else if (!user.getAdmin()){
                resp.sendError(403, "Utilisateur non administrateur");
                return;
            } else {
                req.setAttribute("page", "salles");
                requestDispatcherAdmin(req, resp);
            }
        } else if (uri.size() == 1){
            /**
             * Renvoie la représentation d'une salle
             * Code 200: OK
             * Code 401: Utilisateur non authentifié
             * Code 403: Utilisateur non administrateur
             * Code 404: Salle non trouvée
             */
            if (user == null){
                resp.sendError(401, "Utilisateur non authentifié");
                return;
            } else if (!user.getAdmin()){
                resp.sendError(403, "Utilisateur non administrateur");
                return;
            } else {
                Salle salle;
                salle = salles.get(uri.get(0));
                if (salle == null){
                    resp.sendError(404, "Salle non trouvée");
                    return;
                } else {
                    req.setAttribute("page", "salle");
                    req.setAttribute("salle", salle);
                    requestDispatcherAdmin(req, resp);
                }
            }
        } else if (uri.size() == 2){
            if (uri.get(1).equals("passages")){
                /**
                 * Renvoie les URI des passages dans la salle demandée
                 * Code 303: redirection vers la liste des passages dans la salle
                 *      URL:
                 * Code 401: Utilisateur non authentifié
                 * Code 403: Utilisateur non administrateur
                 */
                if (user == null){
                    resp.sendError(401, "Utilisateur non authentifié");
                    return;
                } else if (!user.getAdmin()){
                    resp.sendError(403, "Utilisateur non administrateur");
                    return;
                } else {
                    // Redirection 303
                }
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<String> uri = parseUri(req.getRequestURI(), "salles");
        HttpSession session = req.getSession(true);
        User user = (User) session.getAttribute("user");

        if (uri.size() == 0){
            /**
             * Crée une nouvelle salle
             * Code 201: Salle crée (location: URL de la salle crée)
             * Code 400: Paramètres de requêtes non acceptables
             * Code 401: Utilisateur non authentifié
             * Code 403: Utilisateur non administrateur
             */
            if (user == null){
                resp.sendError(401, "Utilisateur non authentifié");
                return;
            } else if (!user.getAdmin()){
                resp.sendError(403, "Utilisateur non administrateur");
                return;
            } else {
                /**
                 * Modif pour le moment: si on appuie sur "Ajouter", on ajoute une salle
                 * Modif pour le moment: si on appuie sur "Modifier", on modifie la capacité de la salle
                 * Modif pour le moment: si on appuie sur "Supprimer", on supprime la salle
                 */
                String nomSalle = req.getParameter("nomSalle");

                if (nomSalle != null && !nomSalle.equals("")){
                    Salle salle = salles.get(nomSalle);
                    String action = req.getParameter("action");

                    switch (action){
                        case "Ajouter":
                            if (salle != null){
                                resp.sendError(400, "La salle existe déjà");
                                return;
                            } else {
                                salles.put(nomSalle, new Salle(nomSalle));
                            }
                            break;
                        case "Modifier":
                            try {
                                salle.setCapacite(Integer.parseInt(req.getParameter("capacite")));
                            } catch (NumberFormatException e) {
                                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "La capacité d'une salle doit être un nombre entier.");
                                return;
                            }
                            break;
                        case "Supprimer":
                            if (salle == null){
                                resp.sendError(400, "Salle non trouvée");
                                return;
                            } else {
                                salles.remove(nomSalle);
                            }
                            break;
                    }
                    req.setAttribute("page", "salles");
                } else {
                    resp.sendError(400, "Paramètres de requête non acceptables");
                    return;
                }
            }
        }

        doGet(req, resp);

    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<String> uri = parseUri(req.getRequestURI(), "salles");

        if (uri.size() == 1){
            /**
             * Supprime une salle spécifiée
             * Code 204: Salle supprimée
             * Code 401: Utilisateur non authentifié
             * Code 403: Utilisateur non administrateur
             * Code 404: Salle non trouvée
             */
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<String> uri = parseUri(req.getRequestURI(), "salles");

        if (uri.size() == 1){
            /**
             * Met à jour la salle (seulement sa capacité) ou la crée
             * Code 204: Salle crée ou modifiée
             * Code 400: Paramètres de requête non acceptables
             * Code 401: Utilisateur non authentifié
             * Code 403: Utilisateur non administrateur
             */
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
