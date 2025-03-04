package com.g8e.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.g8e.db.models.DBAccount;
import com.g8e.db.models.DBPlayer;
import com.g8e.db.models.DBPokemon;
import com.google.gson.Gson;

public class CommonQueries {

    public static DBAccount getAccountByLoginToken(String socketId) throws SQLException {
        String query = "SELECT * FROM accounts WHERE login_token = ?";
        try (Connection conn = Database.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, socketId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new DBAccount(rs.getInt("account_id"), rs.getString("username"), rs.getString("password"),
                        rs.getString("login_token"), rs.getString("registration_ip"),
                        rs.getString("registration_date"));
            }
            return null;
        }
    }

    public static DBPlayer getPlayerByAccountId(int accountId) throws SQLException {
        String query = "SELECT * FROM players WHERE account_id = ?";
        try (Connection conn = Database.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, accountId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {

                return new DBPlayer(
                        rs.getInt("player_id"),
                        rs.getInt("account_id"),
                        rs.getInt("world_x"),
                        rs.getInt("world_y"),
                        parseIntArray(rs.getString("inventory")),
                        parseIntArray(rs.getString("inventoryAmounts")));
            }
            return null;
        }
    }

    public static DBPokemon[] getPlayersPartyByPlayerId(int playerId) throws SQLException {
        String query = "SELECT * FROM pokemons WHERE player_id = ?";
        try (Connection conn = Database.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, playerId);
            ResultSet rs = stmt.executeQuery();
            DBPokemon[] party = new DBPokemon[6];
            int i = 0;
            while (rs.next()) {
                party[i] = new DBPokemon(
                        rs.getInt("pokemon_id"),
                        rs.getInt("player_id"),
                        rs.getInt("id"),
                        rs.getInt("xp"),
                        rs.getInt("hp"),
                        rs.getInt("level"),
                        rs.getString("moves"),
                        rs.getInt("hpIv"),
                        rs.getInt("atkIv"),
                        rs.getInt("defIv"),
                        rs.getInt("spAtkIv"),
                        rs.getInt("spDefIv"),
                        rs.getInt("spdIv"),
                        rs.getInt("hpEv"),
                        rs.getInt("atkEv"),
                        rs.getInt("defEv"),
                        rs.getInt("spAtkEv"),
                        rs.getInt("spDefEv"),
                        rs.getInt("spdEv"),
                        rs.getInt("heldItem"),
                        rs.getBoolean("isOutsider"),
                        rs.getInt("evasionModifier"));
                i++;
            }
            return party;
        }
    }

    private static int[] parseIntArray(String str) {
        return (new Gson().fromJson(str, int[].class));

    }

    public static void savePlayerPositionByAccountId(int accountId, int x, int y) throws SQLException {
        String query = "UPDATE players SET world_x = ?, world_y = ? WHERE account_id = ?";
        try (Connection conn = Database.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, x);
            stmt.setInt(2, y);
            stmt.setInt(3, accountId);
            stmt.executeUpdate();
        }
    }

    public static void savePlayerInventoryByAccountId(int accountId, String inventory, String inventoryAmounts)
            throws SQLException {
        String query = "UPDATE players SET inventory = ?, inventoryAmounts = ? WHERE account_id = ?";
        try (Connection conn = Database.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, inventory);
            stmt.setString(2, inventoryAmounts);
            stmt.setInt(3, accountId);
            stmt.executeUpdate();
        }
    }

}
