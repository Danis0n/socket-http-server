package com.danis0n.radafil.engine.exception;

import com.danis0n.radafil.engine.annotation.component.InternalComponent;
import com.danis0n.radafil.engine.annotation.exception.OnRuntimeException;
import com.danis0n.radafil.engine.annotation.http.input.PathVariable;
import com.danis0n.radafil.engine.annotation.status.StatusCode;
import com.danis0n.radafil.engine.core.context.ApplicationContext;

import java.awt.event.ActionListener;

@InternalComponent
public class ExceptionHandler {

    public ExceptionMessage handle(Exception e, ApplicationContext context) {
        boolean isRunTime = e.getClass().isAnnotationPresent(OnRuntimeException.class);
        return isRunTime ?
                handleRunTimeException(e) :
                new ExceptionMessage(e.getMessage(), null);
    }

    private ExceptionMessage handleRunTimeException(Exception e) {
        boolean hasStatusCode = e.getClass().isAnnotationPresent(StatusCode.class);

        if (hasStatusCode) {
            StatusCode status = e.getClass().getAnnotation(StatusCode.class);
            return new ExceptionMessage(e.getMessage(), status.statusCode());
        }

        return new ExceptionMessage(e.getMessage(), null);
    }

}
