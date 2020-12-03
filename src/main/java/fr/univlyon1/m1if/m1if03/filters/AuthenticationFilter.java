package fr.univlyon1.m1if.m1if03.filters;

import com.auth0.jwt.exceptions.JWTVerificationException;
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
import static fr.univlyon1.m1if.m1if03.utils.PresenceUcblJwtHelper.verifyToken;

@WebFilter(filterName = "AuthenticationFilter")
public class AuthenticationFilter extends HttpFilter {

    private String whitelist;

    @SuppressWarnings("unchecked")
    public void init(FilterConfig config) throws ServletException {
        super.init(config);
        this.whitelist = config.getInitParameter("whitelist");
    }

    public void doFilter(HttpServletRequest req, HttpServletResponse resp, FilterChain chain) throws ServletException, IOException {

        List<String> urls = parseUri(req.getRequestURI(), "users");
        System.out.println(resp.getHeaderNames());

        if (urls != null){
            if (urls.size() == 1) {
                String url = urls.get(0);
                if (!whitelist.equals(url)) {

                    //Vérif: l'utilisateur est authentifié
                    try {
                        String token = req.getHeader("Authorization").replace("Bearer ", "");
                        req.setAttribute("token", verifyToken(token, req));
                    } catch (NullPointerException | JWTVerificationException e) {
                        resp.sendError(401, "Utilisateur non authentifié");
                        return;
                    }

                }
            } else {

                //Vérif: l'utilisateur est authentifié
                try {
                    String token = req.getHeader("Authorization").replace("Bearer ", "");
                    req.setAttribute("token", verifyToken(token, req));
                } catch (NullPointerException | JWTVerificationException e) {
                    resp.sendError(401, "Utilisateur non authentifié");
                    return;
                }

            }
        } else {
            //Vérif: l'utilisateur est authentifié
            try {
                String token = req.getHeader("Authorization").replace("Bearer ", "");
                req.setAttribute("token", verifyToken(token, req));
            } catch (NullPointerException | JWTVerificationException e) {
                resp.sendError(401, "Utilisateur non authentifié");
                return;
            }
        }

        chain.doFilter(req, resp);
    }

}
