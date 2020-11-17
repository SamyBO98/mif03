package fr.univlyon1.m1if.m1if03.servlets;

import fr.univlyon1.m1if.m1if03.classes.User;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import static fr.univlyon1.m1if.m1if03.utils.ParseURI.parseUri;

public class UsersController extends HttpServlet {

    Map<String, User> users;

    @Override
    @SuppressWarnings("unchecked")
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        users = (Map<String, User>) config.getServletContext().getAttribute("users");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<String> uri = parseUri(req.getRequestURI(), "users");

        if (uri.size() == 0){
            /**
             * Renvoie les URI de tous les utilisateurs
             * Code 200: OK
             * Code 401: Utilisateur non authentifié
             * Code 403: Utilisateur non administrateur
             */
        } else if (uri.size() == 1){
            /**
             * Renvoie la représentation d'un utilisateur
             * Code 200: OK
             * Code 401: Utilisateur non authentifié
             * Code 403: Utilisateur non administrateur
             * Code 404: Utilisateur non trouvé
             */
        } else if (uri.size() == 2){
            if (uri.get(1).equals("passages")){
                /**
                 * Renvoie les URI de tous les utilisateurs
                 * Code 303 Redirection vers l'URL de la liste des passages d'un utilisateur
                 * Code 401: Utilisateur non authentifié
                 * Code 403: Utilisateur non administrateur
                 */
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<String> uri = parseUri(req.getRequestURI(), "users");

        if (uri.size() == 1){
            if (uri.get(0).equals("login")){
                /**
                 * Connecte l'utilisateur (à l'aide d'un token JWT)
                 * Code 204: OK
                 * Code 400: Paramètres de requête non acceptables
                 */
            } else if (uri.get(0).equals("logout")){
                /**
                 * Déconnecte l'utilisateur
                 * Code 204: OK
                 * Code 401: Utilisateur non authentifié
                 */
            }
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<String> uri = parseUri(req.getRequestURI(), "users");

        if (uri.size() == 2){
            if (uri.get(1).equals("nom")){
                /**
                 * Met à jour le nom de l'utilisateur
                 * Code 204: OK
                 * Code 400: Paramètres de requête non acceptables
                 * Code 401: Utilisateur non authentifié
                 * Code 403: Utilisateur non administrateur
                 * Code 404: Utilisateur non trouvé
                 */
            }
        }
    }
}
