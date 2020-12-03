package fr.univlyon1.m1if.m1if03.servlets;

import fr.univlyon1.m1if.m1if03.classes.User;
import fr.univlyon1.m1if.m1if03.dtos.NomUserDTO;
import fr.univlyon1.m1if.m1if03.dtos.UserDTO;
import fr.univlyon1.m1if.m1if03.dtos.UsersDTO;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import static fr.univlyon1.m1if.m1if03.utils.ParseURI.*;
import static fr.univlyon1.m1if.m1if03.utils.RequestBodyReading.*;
import static fr.univlyon1.m1if.m1if03.utils.JsonUtils.*;
import static fr.univlyon1.m1if.m1if03.utils.PresenceUcblJwtHelper.*;

@WebServlet(name = "UsersController", urlPatterns = {"/users", "/users/*"})
public class UsersController extends HttpServlet {

    Map<String, User> users;
    String splitter = "users";

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

            //Liste tout les utilisateurs existants
            getAllUsers(req, resp);

        } else if (uri.size() == 1){

            //Récupère un utilisateur en particulier
            getUser(req, resp, uri.get(0));

        } else if (uri.size() == 2){
            if (uri.get(1).equals("passages")){

                //Redirige l'utilisateur vers la liste de passages d'un utilisateur
                redirectPassagesUser(req, resp, uri.get(0));

            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<String> uri = parseUri(req.getRequestURI(), "users");

        if (uri.size() == 1){
            if (uri.get(0).equals("login")){

                //Connecte l'utilisateur
                loginUser(req, resp);

            } else if (uri.get(0).equals("logout")){

                //Déconnecte l'utilisateur
                logoutUser(req, resp);

            }
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<String> uri = parseUri(req.getRequestURI(), "users");

        if (uri.size() == 2){
            if (uri.get(1).equals("nom")){

                //Met à jour le nom de l'utilisateur
                updateUserName(req, resp, uri.get(0));

            }
        }
    }


    /***********************************************************/
    /********************  PRIVATE FUNCTIONS  ******************/
    /***********************************************************/

    /**
     * Liste tout les utilisateurs existants.
     * @param req requête.
     * @param resp réponse.
     * @throws IOException exception.
     */
    private void getAllUsers(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        UsersDTO usersDTO = new UsersDTO(users.values(),
                sourceURI(req.getRequestURL().toString(), splitter));

        printJsonValues(resp, usersDTO);

        resp.setStatus(200);
    }

    /**
     * Retourne les informations sur un utilisateur.
     * @param req requête.
     * @param resp réponse.
     * @param userLogin login de l'utilisateur.
     * @throws IOException exception.
     */
    private void getUser(HttpServletRequest req, HttpServletResponse resp, String userLogin) throws IOException {
        if (!users.containsKey(userLogin)){
            resp.sendError(404, "Utilisateur non trouvé");
            return;
        }

        UserDTO userDTO = new UserDTO(users.get(userLogin));

        writeJson(resp, userDTO);

        resp.setStatus(200);
    }

    /**
     * Met à jour le nom de l'utilisateur.
     * @param req requête.
     * @param resp réponse.
     * @param userLogin login de l'utilisateur.
     * @throws IOException exception.
     */
    private void updateUserName(HttpServletRequest req, HttpServletResponse resp, String userLogin) throws IOException {
        if (!users.containsKey(userLogin)){
            resp.sendError(404, "Utilisateur non trouvé");
            return;
        }

        NomUserDTO nomUserDTO = readingBodyRequest(req, NomUserDTO.class);

        if (nomUserDTO.getNom().isEmpty()){
            resp.sendError(400, "Paramètres de requête non acceptables");
            return;
        }

        users.get(userLogin).setNom(nomUserDTO.getNom());

        resp.setStatus(204);

    }

    /**
     * Redirection vers la liste des passages de l'utilisateur indiqué.
     * @param req requête.
     * @param resp réponse.
     * @param userLogin login de l'utilisateur.
     */
    private void redirectPassagesUser(HttpServletRequest req, HttpServletResponse resp, String userLogin){
        resp.setStatus(303);
        String location = sourceURI(req.getRequestURL().toString(),
                splitter + "/passages/byUser/" + userLogin);
        resp.setHeader("Location", location);
    }

    /**
     * Authentifie l'utilisateur en générant un token.
     * @param req requête.
     * @param resp réponse.
     * @throws IOException exception.
     */
    private void loginUser(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        UserDTO userDTO = readingBodyRequest(req, UserDTO.class);

        if (userDTO.getLogin().isEmpty()){
            resp.sendError(400, "Paramètres de requête non acceptables");
            return;
        }

        User user;

        if (users.containsKey(userDTO.getLogin())){
            //L'utilisateur existe déja
            user = users.get(userDTO.getLogin());
        } else {
            //L'utilisateur n'existe pas, on l'ajoute
            user = new User(userDTO.getLogin());
            if (!userDTO.getNom().isEmpty())
                user.setNom(userDTO.getNom());
            user.setAdmin(userDTO.getAdmin());

            users.put(userDTO.getLogin(), user);
        }

        //Génération d'un token
        String location = sourceURI(req.getRequestURL().toString(),
                splitter);

        String authorization = generateToken(location, user.getAdmin(), req);
        resp.setHeader("Authorization", "Bearer " + authorization);
        resp.setHeader("Location", location);
        resp.setStatus(204);

    }

    /**
     * Déconnecte l'utilisateur.
     * @param req requête.
     * @param resp réponse.
     */
    private void logoutUser(HttpServletRequest req, HttpServletResponse resp){
        resp.setStatus(204);
    }

}
