package fr.univlyon1.m1if.m1if03.utils;

import javax.servlet.http.HttpServletRequest;

import java.io.IOException;

import static fr.univlyon1.m1if.m1if03.utils.JsonUtils.readJson;

public class RequestBodyReading {


    public static <T> T readingBodyRequest(HttpServletRequest req, Class<T> dto) throws IOException {
        switch(req.getContentType()){
            case "application/json":
                return readJson(req, dto);
        }
        return null;
    }

}
