package com.danis0n.radafil.engine.core.http.response;

import com.danis0n.radafil.engine.annotation.component.InternalComponent;
import com.danis0n.radafil.engine.core.converter.ObjectToJSONConverter;
import com.danis0n.radafil.engine.core.http.HttpMethod;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import static com.danis0n.radafil.engine.core.config.ContentType.CONTENT_TYPES;
import static com.danis0n.radafil.engine.core.config.ResponseConfig.RESPONSE_CODE;
import static com.danis0n.radafil.engine.core.config.ResponseConfig.RESPONSE_STATUS;

@InternalComponent
public class HttpResponseHandler implements ResponseHandler {

    private final ObjectToJSONConverter converter;

    public HttpResponseHandler(ObjectToJSONConverter converter) {
        this.converter = converter;
    }

    @Override
    public void processResponse(Object object, HttpMethod httpMethod, OutputStream out) {

        try {
            boolean isString = converter.isObjectString(object);

            String contentType = isString ? "txt" : "json";

            String response = converter.convertObjectToString(object);
            System.out.println(response);

            sendHeader(
                    out,
                    RESPONSE_CODE.get(httpMethod),
                    RESPONSE_STATUS.get(httpMethod),
                    CONTENT_TYPES.get(contentType),
                    response.length()
            );

            out.write(response.getBytes());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void processException(String message, OutputStream out) {
        System.out.println(message);
    }

    private void sendHeader(OutputStream out,
                            int statusCode,
                            String statusText,
                            String type,
                            long length) {
        PrintStream ps = new PrintStream(out);
        ps.printf("HTTP/1.1 %s %s%n", statusCode, statusText);
        ps.printf("Content-Type: %s%n", type);
        ps.printf("Content-Length: %s%n%n", length);
    }

}
