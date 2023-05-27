package com.danis0n.radafil.engine.core.converter;

import com.danis0n.radafil.engine.annotation.component.InternalComponent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import static java.util.Objects.isNull;

@InternalComponent
public class ObjectToJSONConverter {

    private static final ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();

    public String convertObjectToString(Object object) throws JsonProcessingException {
        return isNull(object) ? "" : convertObjectToJSON(object);
    }

    public boolean isObjectString(Object object) {
        return object.getClass().equals(String.class);
    }

    private String convertObjectToJSON(Object object) throws JsonProcessingException {
        return ow.writeValueAsString(object);
    }

}
