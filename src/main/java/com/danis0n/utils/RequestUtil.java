package com.danis0n.utils;

import com.danis0n.radafil.engine.annotation.component.InternalComponent;
import com.danis0n.radafil.engine.core.http.HttpMethod;
import com.danis0n.radafil.engine.core.http.request.HttpRequest;

import java.io.BufferedReader;
import java.io.IOException;

import static com.danis0n.api.Api.API_PREFIX;
import static java.util.Objects.nonNull;

@InternalComponent
public class RequestUtil {

    public HttpRequest getHttpRequest(BufferedReader reader) throws IOException {
        String[] params = extractUrl(reader);
        if (!nonNull(params)) return null;

        HttpMethod httpMethod = HttpMethod.valueOf(params[0]);
        String url = params[1];
        String content = extractContentType(reader);
        String keyPrefix = extractKeyPrefixFromUrl(url);
        String urn = extractUrnFromUrl(url, keyPrefix);

        return new HttpRequest(
                urn,
                url,
                keyPrefix,
                content,
                httpMethod
        );
    }

    private String extractKeyPrefixFromUrl(String url) {
        if (url.startsWith(API_PREFIX)) {
            String key = url.substring(API_PREFIX.length());

            if (key.contains("?"))
                key = key.substring(0, key.indexOf("?"));

            if (key.contains("/"))
                key = key.substring(0, key.indexOf("/"));

            return key;
        }
        return "";
    }

    private String extractUrnFromUrl(String url, String keyPrefix) {
        return url.substring((API_PREFIX + keyPrefix).length());
    }

    private String[] extractUrl(BufferedReader reader) throws IOException {

        String line = reader.readLine();

        if (nonNull(line)) {

            System.out.println("Received request: " + line);
            String[] requestParts = line.split(" ");

            if (requestParts.length > 1) {
                return requestParts;
            }

        }

        return null;
    }

    private String extractContentType(BufferedReader in) throws IOException {

        String line;
        int contentLength = 0;
        while ((line = in.readLine()) != null && !line.isEmpty()) {

            if (line.startsWith("Content-Length: ")) {
                contentLength = Integer.parseInt(line.substring("Content-Length: ".length()));
            }

        }

        if (contentLength > 0) {
            char[] body = new char[contentLength];
            in.read(body, 0, contentLength);
            return new String(body);
        }

        return null;
    }


}
