package com.danis0n.radafil.engine.core.context;

import com.danis0n.radafil.engine.annotation.http.RequestMapping;
import com.danis0n.radafil.engine.annotation.component.RestController;
import com.danis0n.radafil.engine.annotation.component.InternalComponent;
import com.danis0n.radafil.engine.core.config.Config;
import com.danis0n.radafil.engine.core.factory.ObjectFactory;
import com.danis0n.radafil.engine.core.store.Store;
import com.danis0n.radafil.engine.exception.exceptions.internal.IllegalPrefixException;
import lombok.Getter;
import lombok.Setter;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ApplicationContext {

    @Setter
    @Getter
    private ObjectFactory factory;
    @Getter
    private final Store store;
    @Getter
    private final Config config;

    public ApplicationContext(Config config, Store store) {
        this.config = config;
        this.store = store;
    }

    public <T> T getObject(Class<T> type)
            throws InvocationTargetException, InstantiationException, IllegalAccessException {

        if (store.getCachedObjects().containsKey(type)) {
            return (T) store.getCachedObjects().get(type);
        }

        Class<? extends T> implClass = type;
        if (type.isInterface()) {
            implClass = config.getImplClass(type);
        }

        T t = factory.createObject(implClass);

        if (implClass.isAnnotationPresent(InternalComponent.class)){
            store.getCachedObjects().put(type, t);
        }

        return t;
    }

    public void scanForControllersPrefixUnique() throws IllegalPrefixException {

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

    public void scanForComponents(Class<? extends Annotation> annotation)
            throws InvocationTargetException, InstantiationException, IllegalAccessException {
        Set<Class<?>> components = config
                .getScanner()
                .getTypesAnnotatedWith(annotation);

        List<Class<?>> collect = components
                .stream()
                .filter(clazz -> !clazz.isAnnotation())
                .collect(Collectors.toList());

        for (Class<?> clazz: collect) {
            getObject(clazz);
        }
    }
}
