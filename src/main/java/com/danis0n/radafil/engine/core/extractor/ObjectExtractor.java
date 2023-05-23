package com.danis0n.radafil.engine.core.extractor;

import com.danis0n.radafil.engine.annotation.http.RequestMapping;
import com.danis0n.radafil.engine.annotation.inject.Inject;
import com.danis0n.radafil.engine.annotation.singleton.Singleton;
import com.danis0n.radafil.engine.core.handler.Handler;
import com.danis0n.radafil.engine.core.handler.ObjectValidator;
import com.danis0n.radafil.engine.core.http.HttpMethod;
import com.danis0n.radafil.engine.exception.EndpointNotFoundException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.danis0n.radafil.engine.core.handler.Handler.HTTP_METHOD;
import static java.util.Objects.isNull;

@Singleton
public class ObjectExtractor {

    @Inject
    private Extractor extractor;
    @Inject
    private ObjectValidator validator;

    public Handler.MethodWithSignature extractMethodFromController(Class<?> controller, HttpMethod httpMethod, String urn)
            throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {

        List<Method> sortedMethods = Arrays
                .stream(controller.getMethods())
                .filter(method -> method.isAnnotationPresent(HTTP_METHOD.get(httpMethod)))
                .collect(Collectors.toList());

        Handler.MethodWithSignature methodWithSignature =
                findMethodBySignature(urn, httpMethod, sortedMethods);

        if (isNull(methodWithSignature))
            throw new EndpointNotFoundException("Endpoint for current url was not found");

        return methodWithSignature;
    }

    public Class<?> extractController(Set<Class<?>> controllers, String url) {

        Optional<Class<?>> controller = controllers
                .stream()
                .filter(clazz -> clazz.isAnnotationPresent(RequestMapping.class) &&
                        validator.isControllerValidatedByPrefix(clazz, url))
                .findFirst();

        if (controller.isEmpty())
            throw new EndpointNotFoundException("Endpoint for current url was not found");

        return controller.get();
    }

    private Handler.MethodWithSignature findMethodBySignature(String urn, HttpMethod httpMethod, List<Method> methods)
            throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {

        for (Method method: methods) {

            String signature = extractor.extractSignatureFromMethod(method, httpMethod);

            if (validator.isUrnValidatedWithSignature(signature, urn))
                return new Handler.MethodWithSignature(method, signature);
        }

        return null;
    }

    public Map<String, String> extractParams(String signature, String urn) {
        if (urn.equals(signature) && urn.equals("")) {
            return new HashMap<>();
        }

        List<String> paramNames = new ArrayList<>();
        Matcher m = Pattern.compile("\\{(.*?)}").matcher(signature);

        while(m.find()) {
            String paramName = m.group(1);
            paramNames.add(paramName);
            signature = signature.replace("{" + paramName + "}", "(\\w+)");
        }

        Pattern r = Pattern.compile(signature);
        m = r.matcher(urn);

        if (m.matches()) {
            Map<String, String> params = new HashMap<>();

            for (int i = 1; i <= m.groupCount(); i++) {
                params.put(paramNames.get(i - 1), m.group(i));
            }
            return params;

        }
        return null;
    }

}
