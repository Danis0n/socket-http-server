package com.danis0n.radafil.engine.exception.exceptions.runtime;

import com.danis0n.radafil.engine.annotation.exception.OnRuntimeException;
import com.danis0n.radafil.engine.annotation.status.StatusCode;

@OnRuntimeException
@StatusCode(statusCode = 403)
public class TestException extends java.lang.RuntimeException {
    public TestException(String message) {
        super(message);
    }
}
