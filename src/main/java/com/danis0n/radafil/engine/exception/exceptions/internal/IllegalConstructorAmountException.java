package com.danis0n.radafil.engine.exception.exceptions.internal;

import com.danis0n.radafil.engine.annotation.status.StatusCode;

@StatusCode(statusCode = 200)
public class IllegalConstructorAmountException extends ControllerException {
    public IllegalConstructorAmountException(String message) {
        super(message);
    }
}
