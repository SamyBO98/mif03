package fr.univlyon1.m1if.m1if03.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JsonUtils {

    public static void writeJson(HttpServletResponse resp, Object o) throws IOException {
        ObjectMapper om = new ObjectMapper();
        om.writeValue(resp.getWriter(), o);
        resp.setContentType("application/json");
    }

    public static <T> T readJson(HttpServletRequest req, Class<T> dto) throws IOException {
        ObjectMapper om = new ObjectMapper();
        return om.readValue(req.getReader(), dto);
    }

}
