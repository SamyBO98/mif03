package fr.univlyon1.m1if.m1if03.servlets;

import fr.univlyon1.m1if.m1if03.classes.User;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter(filterName = "AdminFilter", urlPatterns = "/admin")
public class AdminFilter extends HttpFilter implements Filter {

    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        response.setContentType("text/html;charset=UTF-8");
        HttpSession session = request.getSession(true);

        User user = (User) session.getAttribute("user");

        System.out.println("Admin Filter");

        if (user != null && user.getAdmin()){
            chain.doFilter(request, response);
        } else {
            response.sendRedirect("presence");
        }

    }
}
