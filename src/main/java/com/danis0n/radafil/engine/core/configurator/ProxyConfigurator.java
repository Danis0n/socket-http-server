package com.danis0n.radafil.engine.core.configurator;

public interface ProxyConfigurator {
    Object replaceWithProxyIfNeeded(Object t, Class implClass);
}
