package com.danis0n.radafil.engine.core.handler;

import com.danis0n.radafil.engine.annotation.component.InternalComponent;
import com.danis0n.radafil.engine.core.context.ApplicationContext;
import com.danis0n.radafil.engine.core.http.HttpHandler;
import com.danis0n.radafil.engine.core.http.request.HttpRequest;
import com.danis0n.utils.RequestUtil;

import java.io.*;
import java.net.Socket;

import static com.danis0n.config.ContentType.CONTENT_TYPES;
import static java.util.Objects.isNull;

@InternalComponent
public class Listener {

    private final HttpHandler httpHandler;
    private final RequestUtil requestUtil;

    private static final String NOT_FOUND_MESSAGE = "NOT FOUND";

    public Listener(HttpHandler httpHandler, RequestUtil requestUtil) {
        this.httpHandler = httpHandler;
        this.requestUtil = requestUtil;
    }

    // TODO: 23.05.2023 в контексте сделать загрузку всех классов с аннатацией @компонент
    // TODO: 23.05.2023 разделить системные классы и другие аннотациями
    // TODO: 23.05.2023 сделать респонс

    public void listen(Socket socket, ApplicationContext context) {

        try (InputStream in = socket.getInputStream();
             OutputStream out = socket.getOutputStream();
             BufferedReader reader = new BufferedReader(new InputStreamReader(in))
        ) {

            HttpRequest httpRequest = requestUtil.getHttpRequest(reader);
            if (isNull(httpRequest)) return;

            httpHandler.handle(httpRequest, in, out, context);

            sendHeader(out, 404, "NOT FOUND", CONTENT_TYPES.get(""), NOT_FOUND_MESSAGE.length());
            out.write(NOT_FOUND_MESSAGE.getBytes());

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

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
