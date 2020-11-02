package fr.univlyon1.m1if.m1if03.servlets;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

@WebFilter(filterName = "CachePassages", urlPatterns = { "/presence", "/admin" })
public class CachePassages extends HttpFilter implements Filter{

    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {

        if (req.getMethod().equals("POST")){
            if (req.getParameter("contenu") != null && req.getParameter("contenu").equals("passages")){
                req.getServletContext().setAttribute("lastModified", new Date());
            }
        } else {
            String contenu = req.getParameter("contenu");

            if (contenu != null && contenu.equals("passages")){
                if (req.getServletContext().getAttribute("lastModified") != null){

                    long lastModifiedFromBrowser = req.getDateHeader("If-Modified-Since");
                    long lastModifiedFromServer = ((Date)req.getServletContext().getAttribute("lastModified")).getTime();

                    res.setDateHeader("Last-Modified", new Date().getTime());

                    System.out.println(lastModifiedFromBrowser + " VS " + lastModifiedFromServer);

                    if (lastModifiedFromBrowser != -1 && lastModifiedFromServer <= lastModifiedFromBrowser) {
                        res.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
                        System.out.println(res.getStatus());
                        return;
                    }
                }
            }
        }

        super.doFilter(req, res, chain);
    }
}
