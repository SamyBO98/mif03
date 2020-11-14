package fr.univlyon1.m1if.m1if03.servlets;

import fr.univlyon1.m1if.m1if03.classes.GestionPassages;

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

        if (uri.size() == 1){
            /**
             * Récupére la liste complète de tout les passages
             */
        } else if (uri.size() == 2){
            /**
             * Récupère un passage en particulier
             */
        } else if (uri.size() == 3){
            if (uri.get(1).equals("byUser")){
                /**
                 * Récupérer la liste des passages d'un utilisateur
                 */
            } else if (uri.get(1).equals("bySalle")){
                /**
                 * Récupère la liste des passages dans une salle
                 */
            }
        } else if (uri.size() == 4){
            if (uri.get(1).equals("byUser") && uri.get(3).equals("enCours")){
                /**
                 * Récupérer la liste des passages en cours d'un utilisateur
                 */
            } else if (uri.get(1).equals("byUserAndSalle")){
                /**
                 * Récupère la liste des pasages d'un utilisateur
                 * dans une salle
                 */
            }
        } else if (uri.size() == 5){
            if (uri.get(1).equals("byUserAndDates")){
                /**
                 * Récupère la liste des passages d'un utilisateur
                 * entre 2 dates
                 */
            } else if (uri.get(1).equals("bySalleAndDates")){
                /**
                 * Récupère la liste des passages dans une salle
                 * entre 2 dates
                 */
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<String> uri = parseUri(req.getRequestURI(), "passages");

        if (uri.size() == 1){
            /**
             * Création d'un passage
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
