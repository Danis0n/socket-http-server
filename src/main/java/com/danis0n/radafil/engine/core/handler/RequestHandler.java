package com.danis0n.radafil.engine.core.handler;

import com.danis0n.radafil.engine.annotation.http.RestController;
import com.danis0n.radafil.engine.annotation.http.input.Body;
import com.danis0n.radafil.engine.annotation.http.method.*;
import com.danis0n.radafil.engine.annotation.inject.Inject;
import com.danis0n.radafil.engine.annotation.singleton.Singleton;
import com.danis0n.radafil.engine.core.context.ApplicationContext;
import com.danis0n.radafil.engine.core.extractor.Extractor;
import com.danis0n.radafil.engine.dto.HttpRequest;
import com.danis0n.radafil.engine.http.HttpMethod;

import java.io.InputStream;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.Objects.isNull;

@Singleton
public class RequestHandler {

    @Inject
    private InputValidator inputValidator;
    @Inject
    private Extractor extractor;

    public Map<HttpMethod, Class<? extends Annotation>> httpMethods = new ConcurrentHashMap<>(){{
        put(HttpMethod.GET, Get.class);
        put(HttpMethod.DELETE, Delete.class);
        put(HttpMethod.POST, Post.class);
        put(HttpMethod.PUT, Put.class);
    }};

    public void doProcess(HttpRequest httpRequest, InputStream in, OutputStream out, ApplicationContext context)
            throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {

        Set<Class<?>> classesWithController =
                context.getConfig()
                        .getScanner()
                        .getTypesAnnotatedWith(RestController.class);

        for (Class<?> entry: classesWithController) {

            if (entry.isAnnotationPresent(RequestMapping.class)) {
                RequestMapping annotation = entry.getAnnotation(RequestMapping.class);

                String prefix = annotation.path();
                if (httpRequest.getUrl().startsWith(prefix)) {

                    for (Method method: entry.getMethods()) {
                        HttpMethod httpMethod = httpRequest.getHttpMethod();

                        Annotation annotatedMethod = method.getAnnotation(httpMethods.get(httpMethod));
                        if (isNull(annotatedMethod)) continue;

                        Class<? extends Annotation> annotationType = annotatedMethod.annotationType();

                        if (method.isAnnotationPresent(annotationType)) {

                            String signature = annotationType
                                    .getMethod("path")
                                    .invoke(annotatedMethod)
                                    .toString();

                            if (inputValidator.validateWithUrlSignature(signature, httpRequest.getUrn())) {

//                                if (method.getAnnotatedReturnType(Body.class)) {
//                                    System.out.println("BODY is HERE!");
//                                }
// TODO: IMPLEMENT validate method
                                Object obj = method.invoke(context.getFactory().createObject(entry),
                                        (Object) null
                                );

                                if (isNull(obj)) return;

                                System.out.println("Objet " + obj);
                                return;

                            }

                        }

                    }

                }

            }

        }

    }

}
