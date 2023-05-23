package com.danis0n.radafil.engine.core.handler;

import com.danis0n.radafil.engine.annotation.inject.Inject;
import com.danis0n.radafil.engine.annotation.singleton.Singleton;
import com.danis0n.radafil.engine.core.context.ApplicationContext;
import com.danis0n.radafil.engine.core.http.request.HttpRequest;
import com.danis0n.radafil.engine.core.http.request.HttpRequestHandler;
import com.danis0n.utils.RequestUtil;

import java.io.*;
import java.net.Socket;

import static com.danis0n.config.ContentType.CONTENT_TYPES;
import static java.util.Objects.isNull;

@Singleton
public class Listener {

    @Inject
    private Handler handler;
    @Inject
    private RequestUtil requestUtil;

    private static final String NOT_FOUND_MESSAGE = "NOT FOUND";

    public void listen(Socket socket, ApplicationContext context) {

        try (InputStream in = socket.getInputStream();
             OutputStream out = socket.getOutputStream();
             BufferedReader reader = new BufferedReader(new InputStreamReader(in))
        ) {

            HttpRequest httpRequest = requestUtil.getHttpRequest(reader);
            if (isNull(httpRequest)) return;

            handler.handle(httpRequest, in, out, context);

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
