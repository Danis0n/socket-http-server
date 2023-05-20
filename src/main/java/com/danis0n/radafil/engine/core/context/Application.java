package com.danis0n.radafil.engine.core.context;

import com.danis0n.radafil.engine.core.config.JavaConfig;
import com.danis0n.radafil.engine.core.factory.ObjectFactory;
import lombok.SneakyThrows;

import java.util.Map;

public class Application {

    @SneakyThrows
    public static ApplicationContext run(String packageToScan, Map<Class, Class> ifc2ImplClass) {
        JavaConfig config = new JavaConfig(packageToScan, ifc2ImplClass);
        ApplicationContext context = new ApplicationContext(config);
        ObjectFactory objectFactory = new ObjectFactory(context);
        context.setFactory(objectFactory);

        context.scanForControllers();
        return context;
    }



}
