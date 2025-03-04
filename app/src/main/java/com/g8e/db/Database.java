package com.g8e.db;

import java.sql.Connection;
import java.sql.SQLException;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import io.github.cdimascio.dotenv.Dotenv;

public class Database {
    final private static HikariDataSource dataSource;

    static {
        Dotenv dotenv = Dotenv.load();

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(dotenv.get("DB_URL"));
        config.setUsername(dotenv.get("DB_USERNAME"));
        config.setPassword(dotenv.get("DB_PASSWORD"));
        dataSource = new HikariDataSource(config);
    }

    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}