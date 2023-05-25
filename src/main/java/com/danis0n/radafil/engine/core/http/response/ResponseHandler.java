package com.danis0n.radafil.engine.core.http.response;

import com.danis0n.radafil.engine.core.http.HttpMethod;

public interface ResponseHandler {

    void processResponse(Object object, HttpMethod httpMethod);
}
