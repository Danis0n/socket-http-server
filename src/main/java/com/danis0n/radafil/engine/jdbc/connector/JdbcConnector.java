package com.danis0n.radafil.engine.jdbc.connector;

import com.danis0n.radafil.engine.annotation.component.PostConstruct;
import com.danis0n.radafil.engine.annotation.inject.InjectProperty;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static java.util.Objects.isNull;

public class JdbcConnector {

    @InjectProperty("database.url")
    private String url;
    @InjectProperty("database.username")
    private String username;
    @InjectProperty("database.password")
    private String password;

    @PostConstruct()
    public void initConnector() {
        if (!testConnection()) System.exit(500);
    }

    public boolean testConnection() {

        try (Connection conn = DriverManager.getConnection(url, username, password)) {
            return !isNull(conn);

        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    public void connect() {}

}
