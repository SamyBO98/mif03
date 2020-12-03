package fr.univlyon1.m1if.m1if03.filters;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static fr.univlyon1.m1if.m1if03.utils.PresenceUcblJwtHelper.verifyAdmin;
import static fr.univlyon1.m1if.m1if03.utils.ParseURI.parseUri;

@WebFilter(filterName = "AuthorizationFilter")
public class AuthorizationFilter extends HttpFilter {

    public void doFilter(HttpServletRequest req, HttpServletResponse resp, FilterChain chain) throws ServletException, IOException {
        if (!isWhiteListed(req, resp)){
            String token = req.getHeader("Authorization").replace("Bearer ", "");
            if (verifyAdmin(token)) {
                chain.doFilter(req, resp);
            } else {
                resp.sendError(403, "Utilisateur non administrateur");
                return;
            }
        }

        chain.doFilter(req, resp);
    }

    private boolean isWhiteListed(HttpServletRequest req, HttpServletResponse resp){
        List<String> uri = parseUri(req.getRequestURI(), "users");

        //Ici l'url contient bien "users": Controlleur Users
        if (uri != null){
            if (req.getMethod().equals("POST")){
                if (uri.size() == 1 && (uri.get(0).equals("login") || uri.get(0).equals("logout"))){
                    return true;
                }
            } else if (req.getMethod().equals("GET")){
                if (uri.size() == 1){
                    String userToken = parseUri((String) req.getAttribute("token"), "users").get(0);;

                    if (uri.get(0).equals(userToken)){
                        return true;
                    }
                }
            }
        }

        uri = parseUri(req.getRequestURI(), "passages");
        if (uri != null){
            if (req.getMethod().equals("GET")){
                if (uri.size() == 0){
                    return true;
                }
            }
        }
        return false;
    }
}
