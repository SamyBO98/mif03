package fr.univlyon1.m1if.m1if03.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.univlyon1.m1if.m1if03.classes.Salle;
import fr.univlyon1.m1if.m1if03.dtos.SalleDTO;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

        if (uri.size() == 0){
            /**
             * Renvoie les URI des salles
             * Code 200: OK
             * Code 401: Utilisateur non authentifié
             * Code 403: Utilisateur non administrateur
             */
        } else if (uri.size() == 1){
            /**
             * Renvoie la représentation d'une salle
             * Code 200: OK
             * Code 401: Utilisateur non authentifié
             * Code 403: Utilisateur non administrateur
             * Code 404: Salle non trouvée
             */
        } else if (uri.size() == 2){
            if (uri.get(1).equals("passages")){
                /**
                 * Renvoie les URI des passages dans la salle demandée
                 * Code 303: redirection vers la liste des passages dans la salle
                 *      URL:
                 * Code 401: Utilisateur non authentifié
                 * Code 403: Utilisateur non administrateur
                 */
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<String> uri = parseUri(req.getRequestURI(), "salles");

        if (uri.size() == 0){
            /**
             * Crée une nouvelle salle
             * Code 201: Salle crée (location: URL de la salle crée)
             * Code 400: Paramètres de requêtes non acceptables
             * Code 401: Utilisateur non authentifié
             * Code 403: Utilisateur non administrateur
             */
            SalleDTO salle = new ObjectMapper().readValue(req.getReader(), SalleDTO.class);

            if (salle == null){
                resp.sendError(400);
            } else {
                resp.setStatus(201);
            }
        }
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

}
