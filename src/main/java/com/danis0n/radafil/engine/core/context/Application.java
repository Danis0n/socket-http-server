package com.danis0n.radafil.engine.core.context;

import com.danis0n.radafil.engine.annotation.component.Component;
import com.danis0n.radafil.engine.annotation.component.InternalComponent;
import com.danis0n.radafil.engine.core.config.JavaConfig;
import com.danis0n.radafil.engine.core.factory.ObjectFactory;
import com.danis0n.radafil.engine.core.server.Server;
import lombok.SneakyThrows;

import java.util.Map;

public class Application {

    @SneakyThrows
    public static void run(String packageToScan) {
        JavaConfig config = new JavaConfig(packageToScan);
        ApplicationContext context = new ApplicationContext(config);
        ObjectFactory objectFactory = new ObjectFactory(context);

        context.setFactory(objectFactory);
        context.scanForComponents(InternalComponent.class);
        context.scanForComponents(Component.class);
        context.scanForControllersPrefixUnique();

        Server server = context.getObject(Server.class);

        server.start(context);
    }

}
