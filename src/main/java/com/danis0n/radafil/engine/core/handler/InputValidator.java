package com.danis0n.radafil.engine.core.handler;

import com.danis0n.radafil.engine.annotation.singleton.Singleton;

@Singleton
public class InputValidator {
    public boolean validateWithUrlSignature(String signature, String currentUrn) {
        return true;
    }

    public boolean validateWithBodySignature(String body) {
        return true;
    }

}
