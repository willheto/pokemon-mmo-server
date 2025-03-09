package com.g8e.db.migrations;

import java.sql.Connection;
import java.sql.DriverManager;

import com.g8e.util.Logger;

import io.github.cdimascio.dotenv.Dotenv;

public class V1_init {
    Dotenv dotenv = Dotenv.load();

    String createAccountsTable = "CREATE TABLE IF NOT EXISTS accounts ("
            + "account_id INT AUTO_INCREMENT PRIMARY KEY,"
            + "username VARCHAR(50) NOT NULL UNIQUE,"
            + "password VARCHAR(255) NOT NULL,"
            + "login_token VARCHAR(255),"
            + "registration_ip VARCHAR(50),"
            + "registration_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP"
            + ");";

    String createPlayersTable = "CREATE TABLE IF NOT EXISTS players ("
            + "player_id INT AUTO_INCREMENT PRIMARY KEY,"
            + "account_id INT,"
            + "FOREIGN KEY (account_id) REFERENCES accounts(account_id),"
            + "world_x INT,"
            + "world_y INT,"
            + "inventory JSON,"
            + "inventoryAmounts JSON,"
            + "storyProgress INT"
            + ");";

    String createPokemonsTable = "CREATE TABLE IF NOT EXISTS pokemons ("
            + "pokemon_id INT AUTO_INCREMENT PRIMARY KEY,"
            + "player_id INT,"
            + "FOREIGN KEY (player_id) REFERENCES players(player_id),"
            + "id INT,"
            + "xp INT,"
            + "hp INT,"
            + "level INT,"
            + "moves JSON,"
            + "hpIv INT,"
            + "atkIv INT,"
            + "defIv INT,"
            + "spAtkIv INT,"
            + "spDefIv INT,"
            + "spdIv INT,"
            + "hpEv INT,"
            + "atkEv INT,"
            + "defEv INT,"
            + "spAtkEv INT,"
            + "spDefEv INT,"
            + "spdEv INT,"
            + "heldItem INT,"
            + "isOutsider BOOLEAN,"
            + "evasionModifier INT"
            + ");";

    public void up() {
        try {
            Connection connection = DriverManager.getConnection(dotenv.get("DB_URL"), dotenv.get("DB_USERNAME"),
                    dotenv.get("DB_PASSWORD"));
            connection.createStatement().executeUpdate(createAccountsTable);
            Logger.printInfo("Created accounts table");

            connection.createStatement().executeUpdate(createPlayersTable);
            Logger.printInfo("Created players table");

            connection.createStatement().executeUpdate(createPokemonsTable);
            Logger.printInfo("Created pokemons table");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void down() {
        try {
            Connection connection = DriverManager.getConnection(dotenv.get("DB_URL"), dotenv.get("DB_USERNAME"),
                    dotenv.get("DB_PASSWORD"));

            // Drop tables in reverse order of creation to avoid foreign key constraint
            // errors
            connection.createStatement().executeUpdate("DROP TABLE IF EXISTS pokemons;");
            Logger.printInfo("Dropped pokemons table");

            connection.createStatement().executeUpdate("DROP TABLE IF EXISTS players;");
            Logger.printInfo("Dropped players table");

            connection.createStatement().executeUpdate("DROP TABLE IF EXISTS accounts;");
            Logger.printInfo("Dropped accounts table");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
