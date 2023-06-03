package com.danis0n.radafil.engine.core.http;

import com.danis0n.radafil.engine.annotation.component.InternalComponent;
import com.danis0n.radafil.engine.annotation.component.RestController;
import com.danis0n.radafil.engine.annotation.http.method.Delete;
import com.danis0n.radafil.engine.annotation.http.method.Get;
import com.danis0n.radafil.engine.annotation.http.method.Post;
import com.danis0n.radafil.engine.annotation.http.method.Put;
import com.danis0n.radafil.engine.core.context.ApplicationContext;
import com.danis0n.radafil.engine.core.http.request.HttpRequest;
import com.danis0n.radafil.engine.core.http.request.HttpRequestHandler;
import com.danis0n.radafil.engine.core.http.request.RequestHandler;
import com.danis0n.radafil.engine.core.http.response.HttpResponseHandler;
import com.danis0n.radafil.engine.core.http.response.ResponseHandler;
import com.danis0n.radafil.engine.exception.ExceptionHandler;
import com.danis0n.radafil.engine.exception.ExceptionMessage;
import com.danis0n.utils.ObjectExtractorUtil;

import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@InternalComponent
public class HttpHandler {

    private final ObjectExtractorUtil extractor;
    private final RequestHandler requestHandler;
    private final ResponseHandler responseHandler;
    private final ExceptionHandler exceptionHandler;

    public static Map<HttpMethod, Class<? extends Annotation>> HTTP_METHOD = new ConcurrentHashMap<>(){{
        put(HttpMethod.GET, Get.class);
        put(HttpMethod.DELETE, Delete.class);
        put(HttpMethod.POST, Post.class);
        put(HttpMethod.PUT, Put.class);
    }};

    public HttpHandler(ObjectExtractorUtil extractor,
                       HttpRequestHandler requestHandler,
                       HttpResponseHandler responseHandler,
                       ExceptionHandler exceptionHandler) {
        this.extractor = extractor;
        this.requestHandler = requestHandler;
        this.responseHandler = responseHandler;
        this.exceptionHandler = exceptionHandler;
    }

    static public class MethodWithSignature {
        private final Method method;
        private final String signature;

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

    public void handle(HttpRequest httpRequest, OutputStream out, ApplicationContext context) {

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

            Object returnedValue = requestHandler
                    .processRequest(objectController, method.getMethod(), params);

            responseHandler.processResponse(returnedValue, httpMethod, out);

        } catch (Exception e) {
            ExceptionMessage message = exceptionHandler.handle(e, context);
            responseHandler.processException(message, out);
        }
    }

}
