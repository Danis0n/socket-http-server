package com.danis0n.radafil.engine.exception;

import com.danis0n.radafil.engine.annotation.component.InternalComponent;
import com.danis0n.radafil.engine.annotation.exception.OnRuntimeException;
import com.danis0n.radafil.engine.core.context.ApplicationContext;

@InternalComponent
public class ExceptionHandler {

    public String handle(Exception e, ApplicationContext context) {

        boolean annotationPresent = e.getClass().isAnnotationPresent(OnRuntimeException.class);

        if (annotationPresent) {
            return e.getMessage();
        }

        return e.getCause().getMessage();
    }

}
