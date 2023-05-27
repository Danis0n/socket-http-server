package com.danis0n.radafil.engine.exception.exceptions;

import com.danis0n.radafil.engine.annotation.exception.InternalException;

@InternalException
public class IllegalConstructorAmountException extends RuntimeException {
    public IllegalConstructorAmountException(String message) {
        super(message);
    }
}
