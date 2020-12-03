package fr.univlyon1.m1if.m1if03.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class JsonUtils {

    public static void writeJson(HttpServletResponse resp, Object o) throws IOException {
        resp.setContentType("application/json");
        ObjectMapper om = new ObjectMapper();
        om.writeValue(resp.getWriter(), o);
    }

    public static <T> T readJson(HttpServletRequest req, Class<T> dto) throws IOException {
        ObjectMapper om = new ObjectMapper();
        return om.readValue(req.getReader(), dto);
    }

    public static void printJsonValues(HttpServletResponse resp, Object o) throws IOException {
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();
        out.write(String.valueOf(o));
        out.close();
    }

}
