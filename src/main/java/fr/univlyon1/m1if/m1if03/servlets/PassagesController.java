package fr.univlyon1.m1if.m1if03.servlets;

import fr.univlyon1.m1if.m1if03.classes.GestionPassages;
import fr.univlyon1.m1if.m1if03.classes.Passage;
import fr.univlyon1.m1if.m1if03.classes.Salle;
import fr.univlyon1.m1if.m1if03.classes.User;
import fr.univlyon1.m1if.m1if03.dtos.PassageDTO;
import fr.univlyon1.m1if.m1if03.dtos.PassagesDTO;

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

import static fr.univlyon1.m1if.m1if03.utils.ParseURI.*;
import static fr.univlyon1.m1if.m1if03.utils.RequestBodyReading.*;
import static fr.univlyon1.m1if.m1if03.utils.JsonUtils.*;

public class PassagesController extends HttpServlet {

    GestionPassages passages;
    Map<String, Salle> salles;
    Map<String, User> users;
    String splitter = "passages";

    @Override
    @SuppressWarnings("unchecked")
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        passages = (GestionPassages) config.getServletContext().getAttribute("passages");
        salles = (Map<String, Salle>) config.getServletContext().getAttribute("salles");
        users = (Map<String, User>) config.getServletContext().getAttribute("users");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<String> uri = parseUri(req.getRequestURI(), "passages");
        HttpSession session = req.getSession(true);
        User user = (User) session.getAttribute("user");

        if (uri.size() == 0){

            //Liste de tout les passages
            getAllPassages(req, resp);

        } else if (uri.size() == 1){

            //Renvoie un passage
            getPassage(req, resp, Integer.parseInt(uri.get(0)));

        } else if (uri.size() == 2){
            if (uri.get(0).equals("byUser")){

                //Liste tout les passages d'un utilisateur
                getPassagesByUser(req, resp, uri.get(1));

            } else if (uri.get(0).equals("bySalle")){

                //Liste tout les passages d'une salle
                getPassagesBySalle(req, resp, uri.get(1));

            }
        } else if (uri.size() == 3){
            if (uri.get(0).equals("byUser") && uri.get(2).equals("enCours")){

                //Liste tout les passages en cours d'un utilisateur
                getPassagesByUserEnCours(req, resp, uri.get(1));

            } else if (uri.get(0).equals("byUserAndSalle")){

                //Liste tout les passages d'un utilisateur et d'une salle
                getPassagesByUserAndSalle(req, resp, uri.get(1), uri.get(2));

            }
        } else if (uri.size() == 4){
            if (uri.get(0).equals("byUserAndDates")){

                //Liste tout les passages d'un utilisateur et d'une date
                getPassagesByUserAndDates(req, resp, uri.get(1), uri.get(2), uri.get(3));

            } else if (uri.get(0).equals("bySalleAndDates")){

                //Liste tout les passages d'une salle et d'une date
                getPassagesBySalleAndDates(req, resp, uri.get(1), uri.get(2), uri.get(3));

            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<String> uri = parseUri(req.getRequestURI(), "passages");

        if (uri.size() == 0){

            //Crée un nouveau passage
            createPassage(req, resp);

        }
    }


    /***********************************************************/
    /********************  PRIVATE FUNCTIONS  ******************/
    /***********************************************************/

    /**
     * Liste tout les passages existants.
     * @param req requête.
     * @param resp réponse.
     * @throws IOException exception.
     */
    private void getAllPassages(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        PassagesDTO passagesDTO = new PassagesDTO(passages.getAllPassages(),
                sourceURI(req.getRequestURL().toString(), splitter));

        printJsonValues(resp, passagesDTO);

        resp.setStatus(200);

    }

    /**
     * Crée un nouveau passage.
     * @param req requête.
     * @param resp réponse.
     */
    private void createPassage(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        PassageDTO passageDTO = readJson(req, PassageDTO.class);

        //On vérifie si toutes les infos (excepté pour la date) ne sont pas vides
        if (passageDTO.getUser().isEmpty() || passageDTO.getSalle().isEmpty()
                || (passageDTO.getDateEntree().isEmpty() && passageDTO.getDateSortie().isEmpty())){
            resp.sendError(400, "Paramètres de requête non acceptables");
            return;
        }

        //On vérifie si l'utilisateur spécifié dans la requête correspond à celui stocké dans le token
        String userLogin = passageDTO.getUser();
        String userToken = parseUri((String) req.getAttribute("token"), "users").get(0);
        System.out.println("User login: " + userLogin);
        System.out.println("User token: " + userToken);

        User user = users.get(userLogin);
        if (user == null || !user.getLogin().equals(userToken)) {
            resp.sendError(400, "Paramètres de requête non acceptables");
            return;
        }

        String roomName = passageDTO.getSalle();
        Salle salle = salles.get(roomName);
        if (salle == null) {
            resp.sendError(400, "Paramètres de requête non acceptables");
            return;
        }

        Date dateEntree;
        Date dateSortie;
        try {
            dateEntree = passageDTO.getDateEntree() != null ?
                    new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy", new Locale("us")).parse(passageDTO.getDateEntree())
                    : null;
            dateSortie = passageDTO.getDateSortie() != null ?
                    new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy", new Locale("us")).parse(passageDTO.getDateSortie())
                    : null;
        } catch (ParseException e) {
            resp.sendError(400, "Paramètres de la requête non acceptables");
            return;
        }

        Passage passage = null;
        if (dateEntree != null && dateSortie == null) {
            passage = new Passage(user, salle, dateEntree);
            salle.incPresent();
            passages.add(passage);
        } else if (dateSortie != null) {
            List<Passage> passTemp = passages.getPassagesByUserAndSalle(user, salle);
            for (Passage p : passTemp) { // On mémorise une sortie de tous les passages existants et sans sortie
                if (p.getSortie() == null) {
                    passage = p;
                    passage.setSortie(dateSortie);
                    salle.decPresent();
                }
            }
        }

        if (passage != null) {
            resp.setStatus(201);
            String location = sourceURI(req.getRequestURL().toString(), splitter) + "/passages/" + passage.getId();
            resp.setHeader("Location", location);
        } else {
            resp.sendError(400, "Paramètres de la requête non acceptables");
            return;
        }


    }

    /**
     * Récupère un passage.
     * @param req requête.
     * @param resp réponse.
     * @param id id du passage.
     * @throws IOException exception.
     */
    private void getPassage(HttpServletRequest req, HttpServletResponse resp, int id) throws IOException {
        if (passages.getPassageById(id) == null){
            resp.sendError(404, "Passage non trouvé");
            return;
        }

        PassageDTO passageDTO = new PassageDTO(passages.getPassageById(id));

        writeJson(resp, passageDTO);

        resp.setStatus(200);
    }

    /**
     * Liste tout les passages d'un utilisateur.
     * @param req requête.
     * @param resp réponse.
     * @param userLogin login de l'utilisateur.
     * @throws IOException exception.
     */
    private void getPassagesByUser(HttpServletRequest req, HttpServletResponse resp, String userLogin) throws IOException {
        if (!users.containsKey(userLogin)){
            resp.sendError(404, "Utilisateur non trouvé");
            return;
        }

        PassagesDTO passagesDTO = new PassagesDTO(
                passages.getPassagesByUser(users.get(userLogin)),
                sourceURI(req.getRequestURL().toString(), splitter));

        printJsonValues(resp, passagesDTO);

        resp.setStatus(200);
    }

    /**
     * Liste tout les passages d'une salle
     * @param req requête.
     * @param resp réponse.
     * @param roomName nom de la salle.
     * @throws IOException exception.
     */
    private void getPassagesBySalle(HttpServletRequest req, HttpServletResponse resp, String roomName) throws IOException {
        if (!salles.containsKey(roomName)){
            resp.sendError(404, "Salle non trouvée");
            return;
        }

        PassagesDTO passagesDTO = new PassagesDTO(
                passages.getPassagesBySalle(salles.get(roomName)),
                sourceURI(req.getRequestURL().toString(), splitter));

        printJsonValues(resp, passagesDTO);

        resp.setStatus(200);
    }

    /**
     * Liste tout les passages en cours d'un utilisateur.
     * @param req requête.
     * @param resp réponse.
     * @param userLogin login de l'utilisateur.
     * @throws IOException exception.
     */
    private void getPassagesByUserEnCours(HttpServletRequest req, HttpServletResponse resp, String userLogin) throws IOException {
        if (!users.containsKey(userLogin)){
            resp.sendError(404, "Utilisateur non trouvé");
            return;
        }

        PassagesDTO passagesDTO = new PassagesDTO(
                passages.getPassagesByUserEncours(users.get(userLogin)),
                sourceURI(req.getRequestURL().toString(), splitter));

        printJsonValues(resp, passagesDTO);

        resp.setStatus(200);
    }

    /**
     * Liste tout les passages d'un utilisateur d'une date.
     * @param req requête.
     * @param resp réponse.
     * @param userLogin login de l'utilisateur.
     * @param dateEntree date d'entrée.
     * @param dateSortie date de sortie.
     * @throws IOException exception.
     */
    private void getPassagesByUserAndDates(HttpServletRequest req, HttpServletResponse resp, String userLogin, String dateEntree, String dateSortie) throws IOException {
        if (!users.containsKey(userLogin)){
            resp.sendError(404, "Utilisateur non trouvé");
            return;
        }

        Date entree;
        Date sortie;
        try {
            entree = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy", new Locale("us"))
                    .parse(dateEntree);
            sortie = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy", new Locale("us"))
                    .parse(dateSortie);
        } catch (ParseException e) {
            resp.sendError(400, "Paramètres de la requête non acceptables");
            return;
        }

        PassagesDTO passagesDTO = new PassagesDTO(
                passages.getPassagesByUserAndDates(users.get(userLogin), entree, sortie),
                sourceURI(req.getRequestURL().toString(), splitter));

        printJsonValues(resp, passagesDTO);

        resp.setStatus(200);
    }

    /**
     * Liste tout les passages d'une salle d'une date.
     * @param req requête.
     * @param resp réponse.
     * @param roomName nom de la salle.
     * @param dateEntree date d'entrée.
     * @param dateSortie date de sortie.
     * @throws IOException exception.
     */
    private void getPassagesBySalleAndDates(HttpServletRequest req, HttpServletResponse resp, String roomName, String dateEntree, String dateSortie) throws IOException {
        if (!salles.containsKey(roomName)){
            resp.sendError(404, "Salle non trouvée");
            return;
        }

        Date entree;
        Date sortie;
        try {
            entree = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy", new Locale("us"))
                    .parse(dateEntree);
            sortie = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy", new Locale("us"))
                    .parse(dateSortie);
        } catch (ParseException e) {
            resp.sendError(400, "Paramètres de la requête non acceptables");
            return;
        }

        PassagesDTO passagesDTO = new PassagesDTO(
                passages.getPassagesBySalleAndDates(salles.get(roomName), entree, sortie),
                sourceURI(req.getRequestURL().toString(), splitter));

        printJsonValues(resp, passagesDTO);

        resp.setStatus(200);
    }

    /**
     * Liste tout les passages d'un utilisateur et d'une salle.
     * @param req requête.
     * @param resp réponse.
     * @param userLogin login de l'utilisateur.
     * @param roomName nom de la salle.
     * @throws IOException exception.
     */
    private void getPassagesByUserAndSalle(HttpServletRequest req, HttpServletResponse resp, String userLogin, String roomName) throws IOException {
        if (!users.containsKey(userLogin)){
            resp.sendError(404, "Utilisateur non trouvé");
            return;
        }

        if (!salles.containsKey(roomName)){
            resp.sendError(404, "Salle non trouvé");
            return;
        }

        PassagesDTO passagesDTO = new PassagesDTO(
                passages.getPassagesByUserAndSalle(users.get(userLogin), salles.get(roomName)),
                sourceURI(req.getRequestURL().toString(), splitter));

        printJsonValues(resp, passagesDTO);

        resp.setStatus(200);
    }
}
