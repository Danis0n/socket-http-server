package com.danis0n.radafil.engine.jdbc;

import com.danis0n.radafil.engine.annotation.component.InternalComponent;
import com.danis0n.radafil.engine.jdbc.connector.JdbcConnector;

@InternalComponent
public class JdbcContext {

    private final JdbcConnector connector;

    public JdbcContext(JdbcConnector connector) {
        this.connector = connector;
    }


}
