package fr.univlyon1.m1if.m1if03.servlets;

import fr.univlyon1.m1if.m1if03.classes.User;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

import static fr.univlyon1.m1if.m1if03.utils.ParseURI.parseUri;

@WebServlet(name = "UsersController", urlPatterns = {"/users", "/users/*"})
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
        HttpSession session = req.getSession(true);
        User user = (User) session.getAttribute("user");

        if (uri.size() == 0){
            /**
             * Renvoie les URI de tous les utilisateurs
             * Code 200: OK
             * Code 401: Utilisateur non authentifié
             * Code 403: Utilisateur non administrateur
             */

            if (req.getHeader("accept").contains("application/json")){
                //JSON
                PrintWriter out = resp.getWriter();
                out.write("[\n");
                for (Map.Entry<String, User> userName: users.entrySet()){
                    out.write("\"http://localhost:8080" + req.getRequestURI() + "/" + userName.getValue().getLogin() + "\"\n");
                }
                out.write("]");
                out.close();

            } else if (req.getHeader("accept").contains("text/html")){
                //HTML
                req.setAttribute("page", "users");
                requestDispatcherAdmin(req, resp);
            }
            resp.setStatus(200);
        } else if (uri.size() == 1){
            /**
             * Renvoie la représentation d'un utilisateur
             * Code 200: OK
             * Code 401: Utilisateur non authentifié
             * Code 403: Utilisateur non administrateur
             * Code 404: Utilisateur non trouvé
             */
            User u = users.get(uri.get(0));
            if (u == null){
                resp.sendError(404, "Utilisateur non trouvé");
                return;
            }

            if (req.getHeader("accept").contains("application/json")){
                //JSON
                PrintWriter out = resp.getWriter();
                out.write("{\n");
                out.write("\"login\": \"" + u.getLogin() + "\",\n");
                out.write("\"nom\": \"" + u.getNom() + "\",\n");
                out.write("\"admin\": \"" + (u.getAdmin() ? "true": "false") + "\"\n");
                out.write("}");
                out.close();

            } else if (req.getHeader("accept").contains("text/html")){
                //HTML
                req.setAttribute("user", u);
                req.setAttribute("page", "user");
                requestDispatcherAdmin(req, resp);
            }

            resp.setStatus(200);

        } else if (uri.size() == 2){
            if (uri.get(1).equals("passages")){
                /**
                 * Renvoie les URI de tous les utilisateurs
                 * Code 303 Redirection vers l'URL de la liste des passages d'un utilisateur
                 * Code 401: Utilisateur non authentifié
                 * Code 403: Utilisateur non administrateur
                 */
            }
            if (!user.getAdmin()){
                resp.sendError(403, "Utilisateur non administrateur");
                return;
            } else {
                // Redirection 303
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
                if (req.getParameter("action") != null && req.getParameter("action").equals("Connexion")
                        && req.getParameter("login") != null && !req.getParameter("login").equals("")
                        && req.getSession(true).getAttribute("user") == null){
                    User user = new User(req.getParameter("login"));
                    user.setNom(req.getParameter("nom"));
                    user.setAdmin(req.getParameter("admin") != null && req.getParameter("admin").equals("on"));

                    // On ajoute l'user à la session
                    HttpSession session = req.getSession(true);
                    session.setAttribute("user", user);
                    // On rajoute l'user dans le DAO

                    users.put(req.getParameter("login"), user);
                }
            } else if (uri.get(0).equals("logout")){
                /**
                 * Déconnecte l'utilisateur
                 * Code 204: OK
                 * Code 401: Utilisateur non authentifié
                 */
                if (req.getSession(true).getAttribute("user") != null){
                    req.getSession().invalidate();
                    resp.sendRedirect("./");
                }
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



    private void requestDispatcher(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/jsp/interface.jsp")
                .include(req, resp);
    }

    private void requestDispatcherAdmin(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/jsp/interface_admin.jsp")
                .include(req, resp);
    }
}
