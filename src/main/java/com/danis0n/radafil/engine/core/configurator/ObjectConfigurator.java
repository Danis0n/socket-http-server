package com.danis0n.radafil.engine.core.configurator;

import com.danis0n.radafil.engine.core.context.ApplicationContext;

public interface ObjectConfigurator {
    void configure(Object t, ApplicationContext context);
}
