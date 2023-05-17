package com.danis0n.config;

import java.util.HashMap;
import java.util.Map;

public class ContentType {

    public static final Map<String, String> CONTENT_TYPES = new HashMap<>() {{
        put("jpg", "image/jpeg");
        put("html", "text/html");
        put("json", "application/json");
        put("txt", "text/plain");
        put("", "text/plain");
    }};

}
