package com.danis0n.radafil.engine.core.store;

import lombok.Getter;

import java.util.Map;

public class Store {

    @Getter
    private Map<Class, Object> cachedObjects;

    public Store(Map<Class, Object> cachedObjects) {
        this.cachedObjects = cachedObjects;
    }
}
