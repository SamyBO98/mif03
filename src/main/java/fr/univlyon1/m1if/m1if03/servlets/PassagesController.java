package fr.univlyon1.m1if.m1if03.servlets;

import fr.univlyon1.m1if.m1if03.classes.GestionPassages;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

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

        if (uri.size() == 0){
            /**
             * Renvoie les URI de tous les passages
             * Code 200: OK
             * Code 401: Utilisateur non authentifié
             * Code 403: Utilisateur non administrateur
             */
        } else if (uri.size() == 1){
            /**
             * Renvoie la représentation d'un passage
             * Code 200: OK
             * Code 401: Utilisateur non authentifié
             * Code 403: Utilisateur non administrateur
             * Code 404: Passage non trouvé
             */
        } else if (uri.size() == 2){
            if (uri.get(0).equals("byUser")){
                /**
                 * Renvoie les URI des passages de l'utilisateur indiqué
                 * Code 200: OK
                 * Code 401: Utilisateur non authentifié
                 * Code 403: Utilisateur non administrateur
                 * Code 404: Passage non trouvé
                 */
            } else if (uri.get(0).equals("bySalle")){
                /**
                 * Renvoie les URI des passages d'une salle indiquée
                 * Code 200: OK
                 * Code 401: Utilisateur non authentifié
                 * Code 403: Utilisateur non administrateur
                 * Code 404: Passage non trouvé
                 */
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
            } else if (uri.get(0).equals("byUserAndSalle")){
                /**
                 * Renvoie les URI des passages de l'utilisateur et d'une salle indiqués
                 * Code 200: OK
                 * Code 401: Utilisateur non authentifié
                 * Code 403: Utilisateur non administrateur
                 * Code 404: Passage non trouvé
                 */
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
            } else if (uri.get(0).equals("bySalleAndDates")){
                /**
                 * Renvoie les URI des passages d'une salle et dates indiqués
                 * Code 200: OK
                 * Code 401: Utilisateur non authentifié
                 * Code 403: Utilisateur non administrateur
                 * Code 404: Passage non trouvé
                 */
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<String> uri = parseUri(req.getRequestURI(), "passages");

        if (uri.size() == 0){
            /**
             * Ajoute un nouveau passage
             * Code 201: Passagé crée (location: URL du passage crée)
             * Code 400: Paramètres de requête non acceptables
             * Code 401: Utilisateur non authentifié
             */
        }
    }

}
