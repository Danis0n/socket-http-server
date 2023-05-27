package com.danis0n.radafil.engine.core.config;

import com.danis0n.radafil.engine.core.http.HttpMethod;

import java.util.HashMap;
import java.util.Map;

public class ResponseConfig {

    public static final Map<HttpMethod, Integer> RESPONSE_CODE = new HashMap<>() {{
        put(HttpMethod.GET, 200);
        put(HttpMethod.POST, 201);
        put(HttpMethod.PUT, 201);
        put(HttpMethod.DELETE, 200);
    }};

    public static final Map<HttpMethod, String> RESPONSE_STATUS = new HashMap<>() {{
        put(HttpMethod.GET, "OK");
        put(HttpMethod.POST, "CREATED");
        put(HttpMethod.PUT, "PUT");
        put(HttpMethod.DELETE, "DELETED");
    }};

}
