package com.danis0n.radafil.engine.core.http.response;

import com.danis0n.radafil.engine.annotation.component.InternalComponent;
import com.danis0n.radafil.engine.core.http.HttpMethod;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;

@InternalComponent
public class HttpResponseHandler implements ResponseHandler {

    public HttpResponseHandler() {}

    @Override
    public void processResponse(Object object, HttpMethod httpMethod) {

        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(out)) {

//            oos.writeObject(object);

            System.out.println(object.toString());

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

}
