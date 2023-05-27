package com.danis0n.radafil.engine.exception.exceptions;

import com.danis0n.radafil.engine.annotation.exception.InternalException;
import com.danis0n.radafil.engine.annotation.status.StatusCode;

@InternalException
@StatusCode(statusCode = 403)
public class TestException extends RuntimeException {
    public TestException(String message) {
        super(message);
    }
}
