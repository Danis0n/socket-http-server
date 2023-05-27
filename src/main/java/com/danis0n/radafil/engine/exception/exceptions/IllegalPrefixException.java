package com.danis0n.radafil.engine.exception.exceptions;

import com.danis0n.radafil.engine.annotation.exception.InternalException;

@InternalException
public class IllegalPrefixException extends RuntimeException {
    public IllegalPrefixException(String message) {
        super(message);
    }
}
