package fr.univlyon1.m1if.m1if03.servlets;

import fr.univlyon1.m1if.m1if03.classes.Salle;
import fr.univlyon1.m1if.m1if03.dtos.SalleDTO;
import fr.univlyon1.m1if.m1if03.dtos.SalleIdDTO;
import fr.univlyon1.m1if.m1if03.dtos.SallesDTO;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;
import java.util.Map;

import static fr.univlyon1.m1if.m1if03.utils.JsonUtils.writeJson;
import static fr.univlyon1.m1if.m1if03.utils.RequestBodyReading.*;
import static fr.univlyon1.m1if.m1if03.utils.ParseURI.*;

@WebServlet(name = "SallesControlller", urlPatterns = {"/salles", "/salles/*"})
public class SallesController extends HttpServlet {

    Map<String, Salle> salles;
    String splitter = "salles";

    @Override
    @SuppressWarnings("unchecked")
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        salles = (Map<String, Salle>) config.getServletContext().getAttribute("salles");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        List<String> uri = parseUri(req.getRequestURI(), "salles");

        if (uri.size() == 0){

            //Liste toutes les URI des salles
            getAllSalles(req, resp);

        } else if (uri.size() == 1) {

            //Donne les informations sur une salle
            getSalle(req, resp, uri.get(0));

        } else if (uri.size() == 2 && uri.get(1).equals("passages")){

            //Redirige l'utilisateur vers les passages de la salle indiquée
            redirectPassagesSalle(req, resp, uri.get(0));

        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        List<String> uri = parseUri(req.getRequestURI(), "salles");

        if (uri.size() == 0){
            //Procédure de création d'une salle
            createSalle(req, resp);
        }

    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        List<String> uri = parseUri(req.getRequestURI(), "salles");

        if (uri.size() == 1){

            //Supprime une salle existante
            deleteSalle(req, resp, uri.get(0));

        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        List<String> uri = parseUri(req.getRequestURI(), "salles");

        if (uri.size() == 1){

           //Met à jour une salle ou la crée si elle n'existe pas
            updateSalle(req, resp, uri.get(0));

        }
    }


    /**
     * Crée une nouvelle salle.
     * @param req requête.
     * @param resp réponse.
     */
    private void createSalle(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        SalleIdDTO salleIdDTO = readingBodyRequest(req, SalleIdDTO.class);

        assert salleIdDTO != null;

        if (salleIdDTO.getNomSalle() == null || salleIdDTO.getNomSalle().equals("")){
            resp.sendError(400, "Paramètres de requête non acceptables");
            return;
        }

        if (salles.containsKey(salleIdDTO.getNomSalle())){
            resp.sendError(400, "Salle déja existante");
            return;
        }

        salles.put(salleIdDTO.getNomSalle(), new Salle(salleIdDTO.getNomSalle()));

        resp.setStatus(201);
        String location = sourceURI(req.getRequestURL().toString(), splitter)
                + "/" + splitter + "/" + salleIdDTO.getNomSalle();
        resp.setHeader("Location", location);

    }

    /**
     * Liste toutes les salles existantes.
     * @param req requête.
     * @param resp réponse.
     * @throws IOException Exception.
     */
    private void getAllSalles(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        SallesDTO sallesDTO = new SallesDTO(salles.values(),
                sourceURI(req.getRequestURL().toString(), splitter));

        req.setAttribute("sallesDTO", sallesDTO);

        PrintWriter out = resp.getWriter();
        out.write(String.valueOf(sallesDTO));
        out.close();

        resp.setStatus(200);
    }

    /**
     * Renvoie la représentation d'une salle.
     * @param req requête.
     * @param resp réponse.
     * @param roomName nom de la salle.
     */
    private void getSalle(HttpServletRequest req, HttpServletResponse resp, String roomName) throws IOException {
        if (!salles.containsKey(roomName)) {
            resp.sendError(404, "Salle non trouvée");
            return;
        }

        SalleDTO salleDTO = new SalleDTO(salles.get(roomName));

        req.setAttribute("salleDTO", salleDTO);

        writeJson(resp, salleDTO);

        resp.setStatus(200);
    }

    /**
     * Met à jour une salle ou en crée une nouvelle.
     * @param req requête.
     * @param resp réponse.
     * @param roomName nom de la salle.
     * @throws IOException exception.
     */
    private void updateSalle(HttpServletRequest req, HttpServletResponse resp, String roomName) throws IOException {
        SalleDTO salleDTO = readingBodyRequest(req, SalleDTO.class);

        if (salleDTO.getNomSalle().isEmpty()) {
            resp.sendError(400, "Paramètres de requête non acceptables");
            return;
        }

        if (salles.containsKey(roomName)) {
            salles.get(roomName).setCapacite(salleDTO.getCapacite());
        } else {
            Salle salle = new Salle(salleDTO.getNomSalle());
            salle.setCapacite(salleDTO.getCapacite());
            salles.put(roomName, salle);
        }

        resp.setStatus(204);
    }

    /**
     * Supprime une salle existante.
     * @param req requête.
     * @param resp réponse.
     * @param roomName nom de la salle.
     * @throws IOException exception.
     */
    private void deleteSalle(HttpServletRequest req, HttpServletResponse resp, String roomName) throws IOException {
        if (!salles.containsKey(roomName)) {
            resp.sendError(404, "Salle non trouvée");
            return;
        }

        salles.remove(roomName);

        resp.setStatus(204);
    }

    /**
     * Redirige l'utilisateur vers la liste des passages de la salle indiquée.
     * @param req requête.
     * @param resp réponse.
     * @param roomName nom de la salle.
     */
    private void redirectPassagesSalle(HttpServletRequest req, HttpServletResponse resp, String roomName) {
        resp.setStatus(303);
        String location = sourceURI(req.getRequestURL().toString(), splitter)
                + "/passages/bySalle/" + roomName;
        resp.setHeader("Location", location);
    }

}
