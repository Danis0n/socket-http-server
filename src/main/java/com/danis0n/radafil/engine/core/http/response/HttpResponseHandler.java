package com.danis0n.radafil.engine.core.http.response;

import com.danis0n.radafil.engine.annotation.singleton.Singleton;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;

@Singleton
public class HttpResponseHandler {

    public void sendResponse(Object object) {

        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(out)) {

//            oos.writeObject(object);

            System.out.println(object);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

}
