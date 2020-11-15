package fr.univlyon1.m1if.m1if03.servlets;

import fr.univlyon1.m1if.m1if03.classes.Salle;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@WebServlet(name = "RoomsController", urlPatterns = {"/salles", "/salles/*", "/salles/*/passages"})
public class RoomsController extends HttpServlet {

    private Map<String, Salle> salles;

    @Override
    @SuppressWarnings("unchecked")
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        this.salles = (Map<String, Salle>) config.getServletContext().getAttribute("salles");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<String> uri = parseUri(req.getRequestURI(), "salles");

        if (uri.size() == 1){
            /**
             * Retourner toutes les salles
             * 200: OK
             * 401: utilisateur non authentifié
             * 403: utilisateur non administrateur
             */
            req.setAttribute("salles", salles);

        } else if (uri.size() == 2){
            /**
             * Récupérer une salle en particulier
             * 200: OK
             * 401: utilisateur non authentifié
             * 403: utilisateur non administrateur
             * 404: salle non trouvée
             */
            String room = uri.get(1);
            req.setAttribute("salle", salles.get(room));

        } else if (uri.size() == 3){
            if (uri.get(2).equals("passages")){
                /**
                 * Récupérer la liste des passages dans une salle
                 * 303: redirection vers l'URL de la liste des passages dans la salle
                 * 401: utilisateur non authentifié
                 * 403: utilisateur non administrateur
                 */
            }
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<String> uri = parseUri(req.getRequestURI(), "salles");

        if (uri.size() == 2){
            /**
             * Mettre à jour ou création d'une salle
             * 204: OK
             * 400: paramètres de requête non acceptables
             * 401: utilisateur non authentifié
             * 403: utilisateur non administrateur
             */
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<String> uri = parseUri(req.getRequestURI(), "salles");

        if (uri.size() == 2){
            /**
             * Supprimer une salle en particulier
             * 204: OK
             * 401: utilisateur non authentifié
             * 403: utilisateur non administrateur
             * 404: salle non trouvée
             */
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<String> uri = parseUri(req.getRequestURI(), "salles");

        if (uri.size() == 1){
            /**
             * Création d'une salle
             * 201: OK
             * 400: paramètres de requête non acceptable
             * 401: utilisateur non authentifié
             * 403: utilisateur non administrateur
             */
        }
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("WEB-INF/jsp/interface_admin.jsp");
        dispatcher.include(request, response);
    }

    /**********************************************/
    /************** PRIVATE FUNCTIONS *************/
    /**********************************************/

    /**
     * Return a list of the url splitted by '/'.
     * @param uri url path.
     * @param name name of the parser.
     * @return list of url.
     */
    private List<String> parseUri(String uri, String name){
        List<String> temp = Arrays.asList(uri.split("/"));

        List<String> res = new ArrayList<>();
        boolean wordVisited = false;

        for (String word: temp){
            if (!wordVisited){
                if (word.equals(name)){
                    wordVisited = true;
                }
            }

            if (wordVisited){
                res.add(word);
            }
        }

        System.out.println(res);

        return res;
    }
}
