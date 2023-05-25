package com.danis0n.radafil.engine.core.handler;

import com.danis0n.radafil.engine.annotation.http.RequestMapping;
import com.danis0n.radafil.engine.annotation.component.InternalComponent;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@InternalComponent
public class ObjectValidator {

    public ObjectValidator() {}

    public boolean isControllerValidatedByPrefix(Class<?> clazz, String url) {
        RequestMapping annotation = clazz.getAnnotation(RequestMapping.class);
        String prefix = annotation.path();
        return url.startsWith(prefix);
    }

    public boolean isUrnValidatedWithSignature(String signature, String currentUrn) {
        if (currentUrn.equals(signature) && currentUrn.equals("")) {
            return true;
        }

        Matcher m = Pattern.compile("\\{(.*?)}").matcher(signature);
        while(m.find()) {
            String paramName = m.group(1);
            signature = signature.replace("{" + paramName + "}", "\\w+");
        }
        Pattern r = Pattern.compile(signature);
        m = r.matcher(currentUrn);
        return m.matches();
    }

}
