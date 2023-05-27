package com.danis0n.radafil.engine.core.http.request;

import com.danis0n.radafil.engine.annotation.http.input.PathVariable;
import com.danis0n.radafil.engine.annotation.component.InternalComponent;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Map;

import static java.util.Objects.isNull;

@InternalComponent
public class HttpRequestHandler implements RequestHandler {

    @Override
    public Object processRequest(Object controller, Method method, Map<String, String> params)
            throws InvocationTargetException, IllegalAccessException {

        Object returnedValue = processMethod(method, params, controller);

        return isNull(returnedValue) ? null : returnedValue;
    }

    private Object processMethod(Method method, Map<String, String> params, Object controller)
            throws InvocationTargetException, IllegalAccessException {

        Parameter[] parameters = method.getParameters();
        Object[] argumentValues = putPathArguments(parameters, params);

        Object returnedValue = method.invoke(controller, argumentValues);
        return returnedValue;
    }

    public Object[] putPathArguments(Parameter[] parameters, Map<String, String> params) {

        Object[] argumentValues = new Object[parameters.length];

        for (int i = 0; i < parameters.length; i++) {
            PathVariable pathVariable = parameters[i].getAnnotation(PathVariable.class);
            if (pathVariable != null) {
                argumentValues[i] = params.get(pathVariable.value());
            }
        }

        return argumentValues;
    }

}
