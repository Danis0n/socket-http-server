package com.danis0n.radafil.engine.core.handler;

import com.danis0n.radafil.engine.annotation.component.InternalComponent;
import com.danis0n.radafil.engine.core.context.ApplicationContext;
import com.danis0n.radafil.engine.core.http.HttpHandler;
import com.danis0n.radafil.engine.core.http.request.HttpRequest;
import com.danis0n.utils.RequestUtil;

import java.io.*;
import java.net.Socket;

import static java.util.Objects.isNull;

@InternalComponent
public class Listener {

    private final HttpHandler httpHandler;
    private final RequestUtil requestUtil;

    public Listener(HttpHandler httpHandler, RequestUtil requestUtil) {
        this.httpHandler = httpHandler;
        this.requestUtil = requestUtil;
    }

    public void listen(Socket socket, ApplicationContext context) {

        try (InputStream in = socket.getInputStream();
             OutputStream out = socket.getOutputStream();
             BufferedReader reader = new BufferedReader(new InputStreamReader(in))
        ) {

            HttpRequest httpRequest = requestUtil.getHttpRequest(reader);
            if (isNull(httpRequest)) return;

            httpHandler.handle(httpRequest, out, context);


        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

    }


}
