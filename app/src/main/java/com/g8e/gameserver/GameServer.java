package com.g8e.gameserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import com.g8e.gameserver.network.WebSocketEventsHandler;
import com.g8e.util.Logger;

import io.github.cdimascio.dotenv.Dotenv;

public class GameServer extends WebSocketServer {
    static Dotenv dotenv = Dotenv.load();

    private final WebSocketEventsHandler eventsHandler;
    private final World world = new World();

    public GameServer() {
        super(new InetSocketAddress(Integer.parseInt(dotenv.get("GAME_SERVER_PORT"))));
        this.eventsHandler = new WebSocketEventsHandler(world);
    }

    public void startServer() {
        try {
            start();
            world.start();
            handleConsoleInput();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleConsoleInput() throws IOException, InterruptedException {
        try (BufferedReader sysin = new BufferedReader(new InputStreamReader(System.in))) {
            String input;
            while ((input = sysin.readLine()) != null && !input.equals("exit")) {
                broadcast(input);
            }
        }

    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        Logger.printDebug("New connection from " + conn.getRemoteSocketAddress());
        Map<String, String> queryParams = getQueryParams(handshake.getResourceDescriptor());
        eventsHandler.handleConnection(conn, queryParams);
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        eventsHandler.handleMessage(conn, message);
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        Logger.printInfo(conn + " has disconnected");
        world.removePlayer(conn);
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        if (conn != null) {
            Logger.printError("Error from: " + conn.getRemoteSocketAddress());
        } else {
            Logger.printError("Error occurred, but connection is null.");
        }
        ex.printStackTrace();
    }

    @Override
    public void onStart() {
        Logger.printInfo("Game server started on port: " + getPort());

    }

    private Map<String, String> getQueryParams(String resourceDescriptor) {
        Map<String, String> queryParams = new HashMap<>();
        if (resourceDescriptor.contains("?")) {
            String[] parts = resourceDescriptor.split("\\?");
            if (parts.length > 1) {
                String[] pairs = parts[1].split("&");
                for (String pair : pairs) {
                    String[] keyValue = pair.split("=");
                    if (keyValue.length == 2) {
                        queryParams.put(keyValue[0], keyValue[1]);
                    }
                }
            }
        }
        return queryParams;
    }

}
