package com.g8e.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.g8e.util.Logger;

import io.github.cdimascio.dotenv.Dotenv;

public class DatabaseConnection {
    public static Connection createDatabaseConnection() throws SQLException {
        Dotenv dotenv = Dotenv.load();

        try {
            return DriverManager.getConnection(
                    dotenv.get("DB_URL"),
                    dotenv.get("DB_USERNAME"),
                    dotenv.get("DB_PASSWORD"));
        } catch (SQLException e) {
            Logger.printError(
                    "Error connecting to the database. Make sure the database is running and the credentials are correct.");
            throw new SQLException(e.getMessage());
        }
    }
}