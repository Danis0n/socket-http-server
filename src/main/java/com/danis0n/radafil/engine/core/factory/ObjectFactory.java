package com.danis0n.radafil.engine.core.factory;

import com.danis0n.radafil.engine.core.configurator.ObjectConfigurator;
import com.danis0n.radafil.engine.core.configurator.ProxyConfigurator;
import com.danis0n.radafil.engine.core.context.ApplicationContext;
import com.danis0n.radafil.engine.exception.exceptions.internal.IllegalConstructorAmountException;
import lombok.SneakyThrows;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.util.*;

public class ObjectFactory {

    private final ApplicationContext context;
    private final List<ObjectConfigurator> configurators = new ArrayList<>();
    private final List<ProxyConfigurator> proxyConfigurators = new ArrayList<>();

    @SneakyThrows
    public ObjectFactory(ApplicationContext context) {
        this.context = context;
        for (Class<? extends ObjectConfigurator> aClass :
                context.getConfig().getScanner().getSubTypesOf(ObjectConfigurator.class)) {
            configurators.add(aClass.getDeclaredConstructor().newInstance());
        }
        for (Class<? extends ProxyConfigurator> aClass :
                context.getConfig().getScanner().getSubTypesOf(ProxyConfigurator.class)) {
            proxyConfigurators.add(aClass.getDeclaredConstructor().newInstance());
        }
    }

    public <T> T createObject(Class<T> implClass)
            throws InvocationTargetException, InstantiationException, IllegalAccessException {
        T t = create(implClass);
        configure(t);

        t = wrapWithProxyIfNeeded(implClass, t);
        return t;

    }

    private <T> T wrapWithProxyIfNeeded(Class<T> implClass, T t) {
        for (ProxyConfigurator proxyConfigurator : proxyConfigurators) {
            t = (T) proxyConfigurator.replaceWithProxyIfNeeded(t, implClass);
        }
        return t;
    }

    private <T> void configure(T t) {
        configurators.forEach(objectConfigurator -> objectConfigurator.configure(t,context));
    }

    private <T> T create(Class<T> implClass)
            throws InstantiationException, IllegalAccessException, InvocationTargetException {

        Constructor<?>[] constructors = implClass.getConstructors();

        if (constructors.length != 1) throw new IllegalConstructorAmountException
                ("Illegal amount of constructors for " + implClass.getName() + " class");

        Constructor<?> constructor = constructors[0];
        Parameter[] parameters = constructor.getParameters();

        List<Object> classList = new ArrayList<>();
        for (Parameter parameter: parameters) {
            classList.add(context.getObject(parameter.getType()));
        }

        return classList.size() == 0 ?
                (T) constructor.newInstance() :
                (T) constructor.newInstance(classList.toArray());
    }

}
