package com.danis0n.radafil.engine.core.handler;

import com.danis0n.radafil.engine.annotation.http.RestController;
import com.danis0n.radafil.engine.annotation.http.method.Delete;
import com.danis0n.radafil.engine.annotation.http.method.Get;
import com.danis0n.radafil.engine.annotation.http.method.Post;
import com.danis0n.radafil.engine.annotation.http.method.Put;
import com.danis0n.radafil.engine.annotation.inject.Inject;
import com.danis0n.radafil.engine.annotation.singleton.Singleton;
import com.danis0n.radafil.engine.core.context.ApplicationContext;
import com.danis0n.radafil.engine.core.extractor.ObjectExtractor;
import com.danis0n.radafil.engine.core.http.HttpMethod;
import com.danis0n.radafil.engine.core.http.request.HttpRequest;
import com.danis0n.radafil.engine.core.http.request.HttpRequestHandler;
import com.danis0n.radafil.engine.core.http.response.HttpResponseHandler;

import java.io.InputStream;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Singleton
public class Handler {

    @Inject
    private ObjectExtractor extractor;
    @Inject
    private HttpRequestHandler requestHandler;
    @Inject
    private HttpResponseHandler responseHandler;

    public static Map<HttpMethod, Class<? extends Annotation>> HTTP_METHOD = new ConcurrentHashMap<>(){{
        put(HttpMethod.GET, Get.class);
        put(HttpMethod.DELETE, Delete.class);
        put(HttpMethod.POST, Post.class);
        put(HttpMethod.PUT, Put.class);
    }};

    static public class MethodWithSignature {
        private Method method;
        private String signature;

        public Method getMethod() {
            return method;
        }

        public String getSignature() {
            return signature;
        }

        public MethodWithSignature(Method method, String signature) {
            this.method = method;
            this.signature = signature;
        }
    }

    public void handle(HttpRequest httpRequest, InputStream in, OutputStream out, ApplicationContext context) {

        HttpMethod httpMethod = httpRequest.getHttpMethod();
        String urn = httpRequest.getUrn();
        String url = httpRequest.getUrl();

        try {
            Set<Class<?>> controllers =
                    context.getConfig()
                            .getScanner()
                            .getTypesAnnotatedWith(RestController.class);

            Class<?> controller = extractor
                    .extractController(controllers, url);
            MethodWithSignature method = extractor
                    .extractMethodFromController(controller, httpMethod, urn);

            Object objectController = context
                    .getFactory().createObject(controller);
            Map<String, String> params = extractor
                    .extractParams(method.getSignature(), urn);

// TODO: 22.05.2023 обновить handler, перенести логику поиска метода контроллера сюда
// TODO: 22.05.2023 вкинуть нужные данные из requestHandler (сигнатура и т.д...)

            Object returnedValue = requestHandler
                    .processRequest(objectController, method.getMethod(), params);

            responseHandler.sendResponse(returnedValue);

        } catch (InvocationTargetException | IllegalAccessException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }

    }

}
