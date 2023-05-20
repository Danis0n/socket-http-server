package com.danis0n.radafil.engine.core.http.request;

import com.danis0n.radafil.engine.annotation.http.RequestMapping;
import com.danis0n.radafil.engine.annotation.http.RestController;
import com.danis0n.radafil.engine.annotation.http.method.*;
import com.danis0n.radafil.engine.annotation.inject.Inject;
import com.danis0n.radafil.engine.annotation.singleton.Singleton;
import com.danis0n.radafil.engine.core.context.ApplicationContext;
import com.danis0n.radafil.engine.core.handler.InputExtractor;
import com.danis0n.radafil.engine.core.handler.InputValidator;
import com.danis0n.radafil.engine.core.http.HttpMethod;

import java.io.InputStream;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;

@Singleton
public class HttpRequestHandler {

    @Inject
    private InputValidator validator;
    @Inject
    private InputExtractor inputExtractor;

    public static Map<HttpMethod, Class<? extends Annotation>> httpMethods = new ConcurrentHashMap<>(){{
        put(HttpMethod.GET, Get.class);
        put(HttpMethod.DELETE, Delete.class);
        put(HttpMethod.POST, Post.class);
        put(HttpMethod.PUT, Put.class);
    }};

    public void doProcess(HttpRequest httpRequest, InputStream in, OutputStream out, ApplicationContext context)
            throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {

        Set<Class<?>> controllers =
                context.getConfig()
                        .getScanner()
                        .getTypesAnnotatedWith(RestController.class);

        Optional<Class<?>> controller = controllers
                .stream()
                .filter(clazz -> clazz.isAnnotationPresent(RequestMapping.class) &&
                                isValidatedByPrefix(clazz, httpRequest.getUrl()))
                .findFirst();

        if (controller.isEmpty()) return;

        HttpMethod httpMethod = httpRequest.getHttpMethod();
        List<Method> methods = Arrays
                .stream(controller.get().getMethods())
                .filter(method -> method.isAnnotationPresent(httpMethods.get(httpMethod)))
                .collect(Collectors.toList());

        for (Method method: methods) {

            Annotation annotation = method.getAnnotation(httpMethods.get(httpMethod));
            Class<? extends Annotation> annotationType = annotation.annotationType();

            String annotationMethodName = "path";

            String signature = annotationType
                    .getMethod(annotationMethodName)
                    .invoke(annotation)
                    .toString();

            Map<String, String> params = inputExtractor
                    .extractParams(signature, httpRequest.getUrn());

            if (isNull(params)) continue;

            Parameter[] parameters = method.getParameters();
            Object[] argumentValues = inputExtractor.putPathArguments(parameters, params);

            Object returnedValue = method.invoke(
                    context.getFactory().createObject(controller.get()),
                    argumentValues
            );

            System.out.println(method.getReturnType());

            if (isNull(returnedValue)) return;

            System.out.println("Objet " + returnedValue);
            return;
        }

    }

    private boolean isValidatedByPrefix(Class<?> clazz, String url) {
        RequestMapping annotation = clazz.getAnnotation(RequestMapping.class);
        String prefix = annotation.path();
        return url.startsWith(prefix);
    }

}
