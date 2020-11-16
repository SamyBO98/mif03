package fr.univlyon1.m1if.m1if03.servlets;

import fr.univlyon1.m1if.m1if03.classes.User;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@WebServlet(name = "UsersController", urlPatterns = {"/users", "/users/*"})
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

        System.out.println(resp.getContentType());

        if (uri.size() == 1){
            /**
             * Récupère la liste des utilisateurs
             * 200: OK
             * 401: utilisateur non authentifié
             * 403: utilisateur non administrateur
             */
            if (req.getSession(true).getAttribute("user") == null){
                resp.sendError(401);
                return;
            } else if (!((User)req.getSession(true).getAttribute("user")).getAdmin()){
                resp.sendError(403);
                return;
            }
            req.setAttribute("users", users);
            req.setAttribute("page", "contenus/users.jsp");

            processRequest(req, resp);

        } else if (uri.size() == 2){
            /**
             * Récupère un utilisateur
             * 200: OK
             * 401: utilisateur non authentifié
             * 403: utilisateur non administrateur (si l'utilisateur n'est pas celui qui est logué)
             * 404: utilisateur non trouvé
             */
            if (!((User)req.getSession(true).getAttribute("user")).getAdmin()){
                if (!uri.get(1).equals(((User)req.getSession(true).getAttribute("user")).getLogin())){
                    resp.sendError(403);
                    return;
                }
            } else if (users.get(uri.get(1)) == null){
                resp.sendError(404);
                return;
            }
            String login = uri.get(1);
            req.setAttribute("user", users.get(login));
            req.setAttribute("page", "contenus/user.jsp");

            processRequest(req, resp);

        } else if (uri.size() == 3){
            if (uri.get(2).equals("passages")){
                /**
                 * Récupère la liste des passages d'un utilisateur
                 * 303: redirection vers l'URL de la liste des passages de cet utilisateur
                 * 401: utilisateur non authentifié
                 * 403: utilisateur non administrateur
                 */
                resp.setStatus(303);
                resp.sendRedirect("/passages/byUser/" + uri.get(1));
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
                 * 204: OK
                 * 400: paramètres de requêtes non acceptable
                 * 401: utilisateur non authentifié
                 * 403: utilisateur non administrateur
                 * 404: utilisateur non trouvé
                 */
                if (users.get(uri.get(1)) == null){
                    resp.sendError(404);
                    return;
                }
                String login = uri.get(1);
                String name = (String) req.getAttribute("name"); // A modifier
                users.get(login).setNom(name);
                //TODO: METTRE A JOUR LE FORMULAIRE
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
                 * 204: OK
                 * 400: paramètres de requêtes non acceptable
                 */


                StringBuilder buffer = new StringBuilder();
                BufferedReader reader = req.getReader();
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                    buffer.append(line);
                    buffer.append(System.lineSeparator());
                }
                String data = buffer.toString();
                System.out.println("data: " + data);






                String login = req.getParameter("login");
                if(login != null && !login.equals("")) {
                    System.out.println("USERS");
                    User user = new User(login);
                    user.setNom(req.getParameter("nom"));
                    user.setAdmin(req.getParameter("admin") != null && req.getParameter("admin").equals("on"));
                    HttpSession session = req.getSession(true);
                    session.setAttribute("user", user);

                    users.put(req.getParameter("login"), user);

                    resp.setStatus(204);
                    resp.setHeader("Location", "/users/" + user.getLogin());
                } else {
                    resp.sendError(400, "Paramètres de requêtes non acceptables...");
                    return;
                }

            } else if (uri.get(1).equals("logout")){
                /**
                 * Déconnecte l'utilisateur
                 * 204: OK
                 * 401: utilisateur non authentifié
                 */
                resp.setStatus(204);
                req.getSession().invalidate();

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
