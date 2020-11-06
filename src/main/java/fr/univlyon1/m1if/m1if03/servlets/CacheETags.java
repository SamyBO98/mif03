package fr.univlyon1.m1if.m1if03.servlets;

import fr.univlyon1.m1if.m1if03.classes.GestionPassages;
import fr.univlyon1.m1if.m1if03.classes.Passage;
import fr.univlyon1.m1if.m1if03.classes.Salle;
import fr.univlyon1.m1if.m1if03.classes.User;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebFilter(filterName = "CacheETags", urlPatterns = "/presence")
public class CacheETags extends HttpFilter implements Filter {
    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {


        if (!req.getMethod().equals("POST")){
            String contenu = req.getParameter("contenu");
            //Page d'accueil
            if (contenu == null){

                User user = (User) req.getSession(true).getAttribute("user");
                GestionPassages passages = (GestionPassages) req.getServletContext().getAttribute("passages");
                Map<String, Salle> salles = (Map<String, Salle>) req.getServletContext().getAttribute("salles");


                String tagFromBrowser = req.getHeader("If-None-Match");
                String tagFromServer = getTag(user, passages, salles);

                res.getHeader("ETag");

                if (tagFromBrowser != null && tagFromBrowser.equals(tagFromServer)){
                    res.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
                    return;
                }

                res.setHeader("ETag", tagFromServer);

            }

        }

        super.doFilter(req, res, chain);
    }

    private String getTag(User user, GestionPassages passages, Map<String, Salle> salles){
        List<Passage> temp = passages.getPassagesByUserEncours(user);

        if (temp == null){
            return null;
        }

        String res = "";

        for (Passage passage: temp){
            res += "(" + passage.getSalle().getNom() + "; ";

            if (salles.get(passage.getSalle().getNom()).getSaturee()){
                res += "true";
            } else {
                res += "false";
            }

            res += ")";
        }

        return res;
    }
}
