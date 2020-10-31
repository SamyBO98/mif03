package fr.univlyon1.m1if.m1if03.servlets;

import fr.univlyon1.m1if.m1if03.classes.User;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter(filterName = "Authentificate", urlPatterns = {"/interface.jsp", "/interface_admin.jsp"})
public class Authentificate extends HttpFilter implements Filter {
    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpSession session = request.getSession(true);
        System.out.println("Authentificate Session");

        if (session.getAttribute("user") != null) {
            chain.doFilter(request, response);
        } else if (request.getParameter("login") != null && !request.getParameter("login").equals("")) {
            User user = new User(request.getParameter("login"));
            user.setNom(request.getParameter("nom"));
            user.setAdmin(request.getParameter("admin") != null && request.getParameter("admin").equals("on"));
            session.setAttribute("user", user);
            response.sendRedirect("presence");
        } else {
            response.sendRedirect("index.jsp");
        }
    }
}
