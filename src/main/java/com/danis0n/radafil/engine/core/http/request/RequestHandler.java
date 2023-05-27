package com.danis0n.radafil.engine.core.http.request;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

public interface RequestHandler {

    Object processRequest(Object controller, Method method, Map<String, String> params)
            throws InvocationTargetException, IllegalAccessException;
}
