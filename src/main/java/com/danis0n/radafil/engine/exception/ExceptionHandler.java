package com.danis0n.radafil.engine.exception;

import com.danis0n.radafil.engine.annotation.component.InternalComponent;
import com.danis0n.radafil.engine.annotation.exception.InternalException;
import com.danis0n.radafil.engine.core.context.ApplicationContext;

@InternalComponent
public class ExceptionHandler {

    public String handle(Exception e, ApplicationContext context) {

        boolean annotationPresent = e.getClass().isAnnotationPresent(InternalException.class);

        if (annotationPresent) {
            return "There are no controller with current prefix";
        }

        return e.getCause().getMessage();
    }

}
