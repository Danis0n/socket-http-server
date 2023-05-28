package com.danis0n.radafil.engine.core.context;

import com.danis0n.radafil.engine.annotation.component.Component;
import com.danis0n.radafil.engine.annotation.component.InternalComponent;
import com.danis0n.radafil.engine.core.config.JavaConfig;
import com.danis0n.radafil.engine.core.factory.ObjectFactory;
import com.danis0n.radafil.engine.core.server.Server;
import com.danis0n.radafil.engine.core.store.Store;
import com.danis0n.radafil.engine.exception.exceptions.internal.IllegalPrefixException;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.ConcurrentHashMap;

public class Application {

    public static void run(String packageToScan) {

        ApplicationContext context = init(packageToScan);

        Server server;

        try {
            server = context.getObject(Server.class);
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        server.start(context);
    }

    private static ApplicationContext init(String packageToScan) {
        JavaConfig config = new JavaConfig(packageToScan);
        Store store = new Store(new ConcurrentHashMap<>());
        ApplicationContext context = new ApplicationContext(config, store);
        ObjectFactory objectFactory = new ObjectFactory(context);
        context.setFactory(objectFactory);
        scanForComponents(context);
        return context;
    }

    private static void scanForComponents(ApplicationContext context) {

        try {

            context.scanForControllersPrefixUnique();
            context.scanForComponents(InternalComponent.class);
            context.scanForComponents(Component.class);

        } catch (InvocationTargetException |
                 InstantiationException |
                 IllegalAccessException |
                 IllegalPrefixException e) {
            throw new RuntimeException(e);
        }

    }

}
