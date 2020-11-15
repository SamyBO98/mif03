package fr.univlyon1.m1if.m1if03.servlets;

import fr.univlyon1.m1if.m1if03.classes.GestionPassages;
import fr.univlyon1.m1if.m1if03.classes.Passage;
import fr.univlyon1.m1if.m1if03.classes.Salle;
import fr.univlyon1.m1if.m1if03.classes.User;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@WebServlet(name = "PassagesController",
        urlPatterns = {
                "/passages", "/passages/*",
                "/passages/byUser/*", "/passages/byUser/*/enCours",
                "/passages/byUserAndDates/*/*/*",
                "passages/bySalle/*",
                "/passages/bySalleAndDates/*/*/*",
                "/passages/byUserAndSalle/*/*"
        })
public class PassagesController extends HttpServlet {

    GestionPassages passages;

    @Override
    @SuppressWarnings("unchecked")
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        this.passages = (GestionPassages) config.getServletContext().getAttribute("passages");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<String> uri = parseUri(req.getRequestURI(), "passages");
        List<Passage> passagesAffiches = null;

        if (uri.size() == 1){
            /**
             * Récupére la liste complète de tout les passages
             * 200:OK
             * 401: utilisateur non authentifié
             * 403: utilisateur non administrateur
             */
            passagesAffiches = passages.getAllPassages();

        } else if (uri.size() == 2){
            /**
             * Récupère un passage en particulier
             * 200: OK
             * 401: utilisateur non authentifié
             * 403: utilisateur non administrateur
             * 404: passage non trouvé
             */
            int id = Integer.parseInt(uri.get(1));
            passagesAffiches = (List<Passage>) passages.getPassageById(id);

        } else if (uri.size() == 3){
            if (uri.get(1).equals("byUser")){
                /**
                 * Récupérer la liste des passages d'un utilisateur
                 * 200: OK
                 * 401: utilisateur non authentifié
                 * 403: utilisateur non administrateur
                 * 404: utilisateur non trouvé
                 */
                String login = uri.get(2);
                passagesAffiches = passages.getPassagesByUser(new User(login));

            } else if (uri.get(1).equals("bySalle")){
                /**
                 * Récupère la liste des passages dans une salle
                 * 200: OK
                 * 401: utilisateur non authentifié
                 * 403: utilisateur non administrateur
                 * 404: salle non trouvé
                 */
                String room = uri.get(2);
                passagesAffiches = passages.getPassagesBySalle(new Salle(room));
            }
        } else if (uri.size() == 4){
            if (uri.get(1).equals("byUser") && uri.get(3).equals("enCours")){
                /**
                 * Récupérer la liste des passages en cours d'un utilisateur
                 * 200: OK
                 * 401: utilisateur non authentifié
                 * 403: utilisateur non administrateur
                 * 404: utilisateur non trouvé
                 */
                String login = uri.get(2);
                passagesAffiches = passages.getPassagesByUserEncours(new User(login));

            } else if (uri.get(1).equals("byUserAndSalle")){
                /**
                 * Récupère la liste des pasages d'un utilisateur
                 * dans une salle
                 * 200: OK
                 * 401: utilisateur non authentifié
                 * 403: utilisateur non administrateur
                 * 404: utilisateur ou salle non trouvé
                 */
                String login = uri.get(2);
                String room = uri.get(3);
                passagesAffiches = passages.getPassagesByUserAndSalle(new User(login), new Salle(room));

            }
        } else if (uri.size() == 5){
            if (uri.get(1).equals("byUserAndDates")){
                /**
                 * Récupère la liste des passages d'un utilisateur
                 * entre 2 dates
                 * 200: OK
                 * 401: utilisateur non authentifié
                 * 403: utilisateur non administrateur
                 * 404: utilisateur non trouvé
                 */
                try {
                    String login = uri.get(2);
                    SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy", new Locale("us"));
                    Date dateEntree = sdf.parse(uri.get(3));
                    Date dateSortie = sdf.parse(uri.get(4));
                    passagesAffiches = passages.getPassagesByUserAndDates(new User(login), dateEntree, dateSortie);
                } catch (ParseException e) {
                    e.printStackTrace();
                }


            } else if (uri.get(1).equals("bySalleAndDates")){
                /**
                 * Récupère la liste des passages dans une salle
                 * entre 2 dates
                 * 200: OK
                 * 401: utilisateur non authentifié
                 * 403: utilisateur non administrateur
                 * 404: salle non trouvé
                 */
                try {
                    String room = uri.get(2);
                    SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy", new Locale("us"));
                    Date dateEntree = sdf.parse(uri.get(3));
                    Date dateSortie = sdf.parse(uri.get(4));
                    passagesAffiches = passages.getPassagesBySalleAndDates(new Salle(room), dateEntree, dateSortie);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<String> uri = parseUri(req.getRequestURI(), "passages");

        if (uri.size() == 1){
            /**
             * Création d'un passage
             * 201: OK
             * 400: paramètres de requête non acceptable
             * 401: utilisateur non authentifié
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
