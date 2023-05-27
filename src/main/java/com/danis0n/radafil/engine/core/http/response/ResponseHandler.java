package com.danis0n.radafil.engine.core.http.response;

import com.danis0n.radafil.engine.core.http.HttpMethod;

import java.io.OutputStream;

public interface ResponseHandler {

    void processResponse(Object object, HttpMethod httpMethod, OutputStream out);
    void processException(String message, OutputStream out);
}
