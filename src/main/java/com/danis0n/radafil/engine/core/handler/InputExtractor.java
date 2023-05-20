package com.danis0n.radafil.engine.core.handler;

import com.danis0n.radafil.engine.annotation.http.input.PathVariable;
import com.danis0n.radafil.engine.annotation.singleton.Singleton;

import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Singleton
public class InputExtractor {

    public Map<String, String> extractParams(String signature, String urn) {
        if (urn.equals(signature) && urn.equals("")) {
            return new HashMap<>();
        }

        List<String> paramNames = new ArrayList<>();
        Matcher m = Pattern.compile("\\{(.*?)}").matcher(signature);

        while(m.find()) {
            String paramName = m.group(1);
            paramNames.add(paramName);
            signature = signature.replace("{" + paramName + "}", "(\\w+)");
        }

        Pattern r = Pattern.compile(signature);
        m = r.matcher(urn);

        if (m.matches()) {
            Map<String, String> params = new HashMap<>();

            for (int i = 1; i <= m.groupCount(); i++) {
                params.put(paramNames.get(i - 1), m.group(i));
            }
            return params;

        }
        return null;
    }

    public Object[] putPathArguments(Parameter[] parameters, Map<String, String> params) {

        Object[] argumentValues = new Object[parameters.length];

        for (int i = 0; i < parameters.length; i++) {
            PathVariable pathVariable = parameters[i].getAnnotation(PathVariable.class);
            if (pathVariable != null) {
                argumentValues[i] = params.get(pathVariable.value());
            }
        }

        return argumentValues;
    }

}
