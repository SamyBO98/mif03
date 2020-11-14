package fr.univlyon1.m1if.m1if03.servlets;

import fr.univlyon1.m1if.m1if03.classes.User;

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

@WebServlet(name = "UsersController", urlPatterns = {"/users", "/users/*", "/users/*/nom", "/users/*/passages", "/users/login", "/users/logout"})
public class UsersController extends HttpServlet {

    private Map<String, User> users;

    @Override
    @SuppressWarnings("unchecked")
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        this.users = (Map<String, User>) config.getServletContext().getAttribute("users");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<String> uri = parseUri(req.getRequestURI(), "users");

        if (uri.size() == 1){
            /**
             * Récupère la liste des utilisateurs
             */
            req.setAttribute("users", users);

        } else if (uri.size() == 2){
            /**
             * Récupère un utilisateur
             */
            String login = uri.get(1);
            req.setAttribute("user", users.get(login));

        } else if (uri.size() == 3){
            if (uri.get(2).equals("passages")){
                /**
                 * Récupère la liste des passages d'un utilisateur
                 */
            }
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<String> uri = parseUri(req.getRequestURI(), "users");

        if (uri.size() == 3){
            if (uri.get(2).equals("nom")){
                /**
                 * Met à jour le nom d'un utilisateur
                 */
                String login = uri.get(1);
                String name = (String) req.getAttribute("name"); // A modifier
                users.get(login).setNom(name);

            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<String> uri = parseUri(req.getRequestURI(), "users");

        if (uri.size() == 2){
            if (uri.get(1).equals("login")){
                /**
                 * Connecte l'utilisateur
                 */
            } else if (uri.get(1).equals("logout")){
                /**
                 * Déconnecte l'utilisateur
                 */
            }
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
