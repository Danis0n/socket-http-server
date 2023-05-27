package com.danis0n.radafil.engine.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ExceptionMessage {
    String message;
    int statusCode;
}
