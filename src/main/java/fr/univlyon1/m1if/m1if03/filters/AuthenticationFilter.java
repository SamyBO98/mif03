package fr.univlyon1.m1if.m1if03.filters;

import fr.univlyon1.m1if.m1if03.classes.User;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static fr.univlyon1.m1if.m1if03.utils.ParseURI.parseUri;
import static fr.univlyon1.m1if.m1if03.utils.ParseURI.sourceURI;

@WebFilter(filterName = "AuthenticationFilter")
public class AuthenticationFilter extends HttpFilter {

    Map<String, User> users;
    private String whitelist;

    @SuppressWarnings("unchecked")
    public void init(FilterConfig config) throws ServletException {
        super.init(config);
        this.users = (Map<String, User>) config.getServletContext().getAttribute("users");
        this.whitelist = config.getInitParameter("whitelist");
    }

    public void doFilter(HttpServletRequest req, HttpServletResponse resp, FilterChain chain) throws ServletException, IOException {

        // Test d'une requête d'authentification
        List<String> urls = parseUri(req.getRequestURI(), "users");
        if (urls.size() == 1) {
            String url = urls.get(0);
            if (!whitelist.equals(url)) {

                if (req.getSession(true).getAttribute("user") == null) {
                    resp.sendError(401, "Error | User not logged.");
                    return;
                }

            }
        } else {

            if (req.getSession(true).getAttribute("user") == null) {
                resp.sendError(401, "Error | User not logged.");
                return;
            }

        }
            /*
            if(req.getMethod().equals("POST")) {
                if(req.getParameter("action") != null && req.getParameter("action").equals("Connexion")
                        && req.getParameter("login") != null && !req.getParameter("login").equals("")) {
                    User user = new User(req.getParameter("login"));
                    user.setNom(req.getParameter("nom"));
                    user.setAdmin(req.getParameter("admin") != null && req.getParameter("admin").equals("on"));

                    // On ajoute l'user à la session
                    HttpSession session = req.getSession(true);
                    session.setAttribute("user", user);
                    // On rajoute l'user dans le DAO

                    users.put(req.getParameter("login"), user);
                } else {
                    resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
                    return;
                }
            } else {
                resp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
        }

        */
        chain.doFilter(req, resp);
    }
}
