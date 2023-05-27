package com.danis0n.radafil.engine.exception.exceptions;

import com.danis0n.radafil.engine.annotation.exception.InternalException;
import com.danis0n.radafil.engine.annotation.status.StatusCode;

@InternalException
@StatusCode(statusCode = 404)
public class EndpointNotFoundException extends RuntimeException {
    public EndpointNotFoundException(String message) {
        super(message);
    }
}
