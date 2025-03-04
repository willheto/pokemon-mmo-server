package com.g8e.loginserver;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import com.g8e.db.DatabaseConnection;
import com.g8e.loginserver.models.Account;
import com.g8e.loginserver.models.LoginRequest;
import com.g8e.loginserver.models.LoginResponse;
import com.g8e.loginserver.util.LoginConstants;
import com.g8e.util.Logger;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import at.favre.lib.crypto.bcrypt.BCrypt;

public class LoginServer extends WebSocketServer {
    private static final int MAX_PLAYERS = 5;
    private static final String SQL_SELECT_USER = "SELECT * FROM accounts WHERE username = ?";
    private static final String SQL_UPDATE_LOGIN_TOKEN = "UPDATE accounts SET login_token = ? WHERE account_id = ?";

    private final Gson gson = new Gson();
    final private int[] players = new int[MAX_PLAYERS];

    public LoginServer(int port) throws IOException {
        super(new InetSocketAddress(port));
        setConnectionLostTimeout(100);
    }

    public void startServer() throws IOException {
        start();
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        try {
            LoginRequest loginMessage = gson.fromJson(message, LoginRequest.class);
            int type = loginMessage.getType();
            switch (type) {
                case LoginConstants.LOGIN -> handleLoginRequest(conn, loginMessage);
                case LoginConstants.LOGOUT -> handleLogoutRequest(conn);
                case LoginConstants.RESET_WORLD -> handleWorldResetRequest(conn);
                case LoginConstants.COUNT_PLAYERS -> handleCountPlayersRequest(conn);
                default -> Logger.printWarning("Invalid login request type: " + type);
            }
        } catch (JsonSyntaxException e) {
            Logger.printWarning("Invalid JSON received: " + message);
        }
    }

    private void handleLoginRequest(WebSocket conn, LoginRequest loginMessage) {
        String username = loginMessage.getUsername();
        String password = loginMessage.getPassword();

        try (Connection connection = DatabaseConnection.createDatabaseConnection()) {
            Account account = findAccountByUsername(connection, username);

            if (account != null && verifyPassword(password, account.getPassword())) {
                handleSuccessfulLogin(conn, account);
            } else {
                sendInvalidCredentialsResponse(conn);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Account findAccountByUsername(Connection connection, String username) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_USER)) {
            preparedStatement.setString(1, username);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return buildAccountFromResultSet(resultSet);
                }
            }
        }
        return null;
    }

    private Account buildAccountFromResultSet(ResultSet resultSet) throws SQLException {
        Account account = new Account();
        account.setUsername(resultSet.getString("username"));
        account.setPassword(resultSet.getString("password"));
        account.setAccountID(resultSet.getInt("account_id"));
        return account;
    }

    private boolean verifyPassword(String password, String hashedPassword) {
        BCrypt.Result result = BCrypt.verifyer().verify(password.toCharArray(), hashedPassword);
        return result.verified;
    }

    private void handleSuccessfulLogin(WebSocket conn, Account account) throws SQLException {

        for (int i = 0; i < players.length; i++) {
            if (players[i] == 0) {
                break;
            }
            if (i == players.length - 1) {
                sendWorldFullResponse(conn);
                return;
            }
        }

        for (int i = 0; i < players.length; i++) {
            if (players[i] == account.getAccountId()) {
                sendAlreadyLoggedInResponse(conn);
                return;
            }
        }

        String loginToken = conn.toString();
        updateLoginToken(account.getAccountId(), loginToken);
        sendLoginSuccessResponse(conn, loginToken);

        // add player to player list
        for (int i = 0; i < players.length; i++) {
            if (players[i] == 0) {
                players[i] = account.getAccountId();
                break;
            }
        }

    }

    private void sendWorldFullResponse(WebSocket conn) {
        LoginResponse response = new LoginResponse(LoginConstants.LOGIN, LoginConstants.WORLD_FULL);
        conn.send(gson.toJson(response));
    }

    private void updateLoginToken(int accountId, String loginToken) throws SQLException {
        try (Connection connection = DatabaseConnection.createDatabaseConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(SQL_UPDATE_LOGIN_TOKEN)) {
            preparedStatement.setString(1, loginToken);
            preparedStatement.setInt(2, accountId);
            preparedStatement.executeUpdate();
        }
    }

    private void sendInvalidCredentialsResponse(WebSocket conn) {
        LoginResponse response = new LoginResponse(LoginConstants.LOGIN, LoginConstants.INVALID_CREDENTIALS);
        conn.send(gson.toJson(response));
    }

    private void sendAlreadyLoggedInResponse(WebSocket conn) {
        LoginResponse response = new LoginResponse(LoginConstants.LOGIN, LoginConstants.ALREADY_LOGGED_IN);
        conn.send(gson.toJson(response));
    }

    private void sendLoginSuccessResponse(WebSocket conn, String loginToken) {
        LoginResponse response = new LoginResponse(LoginConstants.LOGIN, LoginConstants.LOGIN_SUCCESS, loginToken);
        conn.send(gson.toJson(response));
    }

    private void removePlayer(String socket) throws SQLException {
        String SQL_SELECT_ACCOUNT_BY_LOGIN_TOKEN = "SELECT account_id FROM accounts WHERE login_token = ?";
        Connection connection = DatabaseConnection.createDatabaseConnection();

        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_ACCOUNT_BY_LOGIN_TOKEN)) {
            preparedStatement.setString(1, socket);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int accountID = resultSet.getInt("account_id");
                    for (int i = 0; i < players.length; i++) {
                        if (players[i] == accountID) {
                            players[i] = 0;
                            break;
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private void handleLogoutRequest(WebSocket conn) {
        Logger.printInfo("Logout request received from " + conn);
        try {
            removePlayer(conn.toString());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void handleWorldResetRequest(WebSocket conn) {
        broadcast("World reset request received");
    }

    private void handleCountPlayersRequest(WebSocket conn) {
        broadcast("Count players request received");
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        Logger.printInfo(conn + " has connected to login server");
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        Logger.printInfo(conn + " has disconnected");
        try {
            removePlayer(conn.toString());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        Logger.printError("An error occurred: " + ex.getMessage());
    }

    @Override
    public void onStart() {
        Logger.printInfo("Login server started on port: " + getPort());
    }

}
