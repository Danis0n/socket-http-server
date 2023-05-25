package com.danis0n.radafil.engine.core.server;

import com.danis0n.radafil.engine.annotation.component.InternalComponent;
import com.danis0n.radafil.engine.core.context.ApplicationContext;
import com.danis0n.radafil.engine.core.handler.Listener;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;

@InternalComponent
public class Server {

    private final Listener listener;

    private final int port = 8080;

    public Server(Listener listener) {
        this.listener = listener;
    }

    public void start(ApplicationContext context) {
        try(ServerSocket server = new ServerSocket(port)) {

            System.out.println("Server started on port: " + port);

            while (true) {
                Socket socket = server.accept();
                listener.listen(socket, context);
            }

        } catch (IOException e) {
            System.out.println(Arrays.toString(e.getStackTrace()));
        }
    }

}
