package com.danis0n.radafil.engine.exception.exceptions.runtime;

import com.danis0n.radafil.engine.annotation.exception.OnRuntimeException;
import com.danis0n.radafil.engine.annotation.status.StatusCode;

@OnRuntimeException
@StatusCode(statusCode = 404)
public class EndpointNotFoundException extends java.lang.RuntimeException {
    public EndpointNotFoundException(String message) {
        super(message);
    }
}
