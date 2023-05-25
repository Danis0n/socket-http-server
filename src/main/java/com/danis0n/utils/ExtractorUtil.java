package com.danis0n.utils;

import com.danis0n.radafil.engine.annotation.component.InternalComponent;
import com.danis0n.radafil.engine.core.http.HttpMethod;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static com.danis0n.radafil.engine.core.http.HttpHandler.HTTP_METHOD;


@InternalComponent
public class ExtractorUtil {

    public ExtractorUtil() {
    }

    public String extractSignatureFromMethod(Method method, HttpMethod httpMethod)
            throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {

        Annotation annotation = method.getAnnotation(HTTP_METHOD.get(httpMethod));
        Class<? extends Annotation> annotationType = annotation.annotationType();
        String annotationMethodName = "path";

        String signature = annotationType
                .getMethod(annotationMethodName)
                .invoke(annotation)
                .toString();

        return signature;
    }

}
