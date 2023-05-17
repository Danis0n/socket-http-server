package com.danis0n;

import com.danis0n.radafil.engine.core.context.Application;
import com.danis0n.radafil.engine.core.context.ApplicationContext;
import com.danis0n.radafil.engine.core.server.Server;

import java.util.HashMap;

public class Main {

    public static void main(String[] args) {
        ApplicationContext context = Application.run("com.danis0n", new HashMap<>());
        Server server = context.getObject(Server.class);
        server.start(context);
    }

}
