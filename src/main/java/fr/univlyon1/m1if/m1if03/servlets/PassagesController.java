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
            /**
             * Renvoie les URI de tous les passages
             * Code 200: OK
             * Code 401: Utilisateur non authentifié
             * Code 403: Utilisateur non administrateur
             */
            if (req.getHeader("accept").contains("application/json")){
                //JSON
                PrintWriter out = resp.getWriter();
                out.write("[\n");
                for (Passage passage: passages.getAllPassages()){
                    out.write("\"http://localhost:8080" + req.getRequestURI() + "/" + passage.getId() + "\"\n");
                }
                out.write("]");
                out.close();

            } else if (req.getHeader("accept").contains("text/html")){
                //HTML
                req.setAttribute("passages", passages.getAllPassages());
            }
            resp.setStatus(200);

        } else if (uri.size() == 1){
            /**
             * Renvoie la représentation d'un passage
             * Code 200: OK
             * Code 401: Utilisateur non authentifié
             * Code 403: Utilisateur non administrateur
             * Code 404: Passage non trouvé
             */
            Passage passage = passages.getPassageById(Integer.parseInt(uri.get(0)));
            if (passage == null){
                resp.sendError(404, "Passage non trouvé.");
                return;
            }

            if (req.getHeader("accept").contains("application/json")) {
                //JSON
                PrintWriter out = resp.getWriter();
                //out.write(writeJsonData(passage));
                out.close();
            } else if (req.getHeader("accept").contains("text/html")){
                req.setAttribute("passages", passage);
                req.setAttribute("page", "passage");
            }
            resp.setStatus(200);

        } else if (uri.size() == 2){
            if (uri.get(0).equals("byUser")){
                /**
                 * Renvoie les URI des passages de l'utilisateur indiqué
                 * Code 200: OK
                 * Code 401: Utilisateur non authentifié
                 * Code 403: Utilisateur non administrateur
                 * Code 404: Passage non trouvé
                 */
                List<Passage> passagesList = passages.getPassagesByUser(new User(uri.get(1)));
                if (passagesList == null){
                    resp.sendError(404, "Aucun passage provenant de l'utilisateur");
                    return;
                }

                if (req.getHeader("accept").contains("application/json")){
                    //JSON
                    PrintWriter out = resp.getWriter();
                    out.write("[\n");
                    for (Passage passage: passagesList){
                        out.write("\"http://localhost:8080" + req.getRequestURI() + "/" + passage.getId() + "\"\n");
                    }
                    out.write("]");
                    out.close();

                } else if (req.getHeader("accept").contains("text/html")){
                    //HTML
                    req.setAttribute("passages", passagesList);
                    req.setAttribute("page", "passages");
                }
                resp.setStatus(200);

            } else if (uri.get(0).equals("bySalle")){
                /**
                 * Renvoie les URI des passages d'une salle indiquée
                 * Code 200: OK
                 * Code 401: Utilisateur non authentifié
                 * Code 403: Utilisateur non administrateur
                 * Code 404: Passage non trouvé
                 */
                List<Passage> passagesList = passages.getPassagesBySalle(new Salle(uri.get(1)));
                if (passagesList == null){
                    resp.sendError(404, "Aucun passage provenant de la salle");
                    return;
                }

                if (req.getHeader("accept").contains("application/json")){
                    //JSON
                    PrintWriter out = resp.getWriter();
                    out.write("[\n");
                    for (Passage passage: passagesList){
                        out.write("\"http://localhost:8080" + req.getRequestURI() + "/" + passage.getId() + "\"\n");
                    }
                    out.write("]");
                    out.close();

                } else if (req.getHeader("accept").contains("text/html")){
                    //HTML
                    req.setAttribute("passages", passagesList);
                    req.setAttribute("page", "passages");
                }
                resp.setStatus(200);

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
                List<Passage> passagesList = passages.getPassagesByUserEncours(new User(uri.get(1)));
                if (passagesList == null){
                    resp.sendError(404, "Aucun passage provenant de l'utilisateur");
                    return;
                }

                if (req.getHeader("accept").contains("application/json")){
                    //JSON
                    PrintWriter out = resp.getWriter();
                    out.write("[\n");
                    for (Passage passage: passagesList){
                        out.write("\"http://localhost:8080" + req.getRequestURI() + "/" + passage.getId() + "\"\n");
                    }
                    out.write("]");
                    out.close();

                } else if (req.getHeader("accept").contains("text/html")){
                    //HTML
                    req.setAttribute("passages", passagesList);
                    req.setAttribute("page", "passages");
                }
                resp.setStatus(200);

            } else if (uri.get(0).equals("byUserAndSalle")){
                /**
                 * Renvoie les URI des passages de l'utilisateur et d'une salle indiqués
                 * Code 200: OK
                 * Code 401: Utilisateur non authentifié
                 * Code 403: Utilisateur non administrateur
                 * Code 404: Passage non trouvé
                 */
                List<Passage> passagesList = passages.getPassagesByUserAndSalle(new User(uri.get(1)), new Salle(uri.get(2)));
                if (passagesList == null){
                    resp.sendError(404, "Aucun passage provenant de l'utilisateur et de la salle");
                    return;
                }

                if (req.getHeader("accept").contains("application/json")){
                    //JSON
                    PrintWriter out = resp.getWriter();
                    out.write("[\n");
                    for (Passage passage: passagesList){
                        out.write("\"http://localhost:8080" + req.getRequestURI() + "/" + passage.getId() + "\"\n");
                    }
                    out.write("]");
                    out.close();

                } else if (req.getHeader("accept").contains("text/html")){
                    //HTML
                    req.setAttribute("passages", passagesList);
                    req.setAttribute("page", "passages");
                }
                resp.setStatus(200);

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
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy", new Locale("us"));
                    Date dateEntree = sdf.parse(uri.get(2));
                    Date dateSortie = sdf.parse(uri.get(3));

                    List<Passage> passagesList = passages.getPassagesByUserAndDates(new User(uri.get(1)), dateEntree, dateSortie);

                    req.setAttribute("passages", passagesList);

                    if (req.getHeader("accept").contains("application/json")){
                        //JSON
                        PrintWriter out = resp.getWriter();
                        out.write("[\n");
                        for (Passage passage: passagesList){
                            out.write("\"http://localhost:8080" + req.getRequestURI() + "/" + passage.getId() + "\"\n");
                        }
                        out.write("]");
                        out.close();

                    } else if (req.getHeader("accept").contains("text/html")){
                        //HTML
                        req.setAttribute("passages", passagesList);
                        req.setAttribute("page", "passages");
                    }
                    resp.setStatus(200);
                } catch (ParseException e) {
                    e.printStackTrace();
                    resp.sendError(404, "Passage non trouvé");
                }

            } else if (uri.get(0).equals("bySalleAndDates")){
                /**
                 * Renvoie les URI des passages d'une salle et dates indiqués
                 * Code 200: OK
                 * Code 401: Utilisateur non authentifié
                 * Code 403: Utilisateur non administrateur
                 * Code 404: Passage non trouvé
                 */
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy", new Locale("us"));
                    Date dateEntree = sdf.parse(uri.get(2));
                    Date dateSortie = sdf.parse(uri.get(3));

                    List<Passage> passagesList = passages.getPassagesBySalleAndDates(new Salle(uri.get(1)), dateEntree, dateSortie);
                    req.setAttribute("passages", passagesList);

                    if (req.getHeader("accept").contains("application/json")){
                        //JSON
                        PrintWriter out = resp.getWriter();
                        out.write("[\n");
                        for (Passage passage: passagesList){
                            out.write("\"http://localhost:8080" + req.getRequestURI() + "/" + passage.getId() + "\"\n");
                        }
                        out.write("]");
                        out.close();

                    } else if (req.getHeader("accept").contains("text/html")){
                        //HTML
                        req.setAttribute("passages", passagesList);
                        req.setAttribute("page", "passages");
                    }
                    resp.setStatus(200);
                } catch (ParseException e) {
                    e.printStackTrace();
                    resp.sendError(404, "Passage non trouvé");
                }
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

        //On vérifie si l'utilisateur spécifié existe


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
