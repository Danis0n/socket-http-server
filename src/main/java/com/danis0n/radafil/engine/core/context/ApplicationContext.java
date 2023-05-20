package com.danis0n.radafil.engine.core.context;

import com.danis0n.radafil.engine.annotation.http.RequestMapping;
import com.danis0n.radafil.engine.annotation.http.RestController;
import com.danis0n.radafil.engine.annotation.singleton.Singleton;
import com.danis0n.radafil.engine.core.config.Config;
import com.danis0n.radafil.engine.core.factory.ObjectFactory;
import com.danis0n.radafil.engine.exception.IllegalPrefixException;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class ApplicationContext {

    @Setter
    @Getter
    private ObjectFactory factory;
    @Getter
    private Map<Class, Object> cache = new ConcurrentHashMap<>();
    @Getter
    private Config config;

    public ApplicationContext(Config config) {
        this.config = config;
    }

    public <T> T getObject(Class<T> type) {
        if (cache.containsKey(type)) {
            return (T) cache.get(type);
        }

        Class<? extends T> implClass = type;
        if (type.isInterface()) {
            implClass = config.getImplClass(type);
        }

        T t = factory.createObject(implClass);

        if (implClass.isAnnotationPresent(Singleton.class)){
            cache.put(type, t);
        }

        return t;
    }

    public void scanForControllers() throws IOException {

        Set<Class<?>> controllers = config
                .getScanner()
                .getTypesAnnotatedWith(RestController.class);

        List<String> controllersPrefix = controllers
                .stream()
                .filter(clazz -> clazz.isAnnotationPresent(RequestMapping.class))
                .map(clazz -> {
                    RequestMapping annotation = clazz.getAnnotation(RequestMapping.class);
                    return annotation.path();
                })
                .collect(Collectors.toList());

        Set<String> set = new HashSet<>(controllersPrefix);

        if (set.size() != controllersPrefix.size())
            throw new IllegalPrefixException
                    ("There are two or more controllers with same prefix");
    }
}
