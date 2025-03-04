package com.g8e.updateserver;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URISyntaxException;
import java.util.List;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import com.g8e.updateserver.AssetLoader.Asset;
import com.g8e.updateserver.models.UpdateRequest;
import com.g8e.updateserver.models.UpdateResponse;
import com.g8e.updateserver.util.UpdateConstants;
import com.g8e.util.Logger;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

public class UpdateServer extends WebSocketServer {

    private final Gson gson = new Gson();

    public UpdateServer() throws IOException {

        super(new InetSocketAddress(UpdateConstants.UPDATE_SERVER_PORT));
        setConnectionLostTimeout(100); // Customize timeout settings
    }

    public void startServer() throws IOException {
        start();
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        try {
            UpdateRequest updateMessage = gson.fromJson(message, UpdateRequest.class);
            processUpdateRequest(conn, updateMessage);
        } catch (JsonSyntaxException e) {
            Logger.printWarning("Invalid JSON received: " + message);
        }
    }

    private void processUpdateRequest(WebSocket conn, UpdateRequest updateRequest) {
        if (updateRequest.getCachenumber() != UpdateConstants.CACHE_VERSION) {
            handleUpdateAvailable(conn);
        } else {
            UpdateResponse response = new UpdateResponse(UpdateConstants.UPDATE_RESPONSE_CACHE_UP_TO_DATE,
                    UpdateConstants.CACHE_VERSION, null);
            conn.send(gson.toJson(response));
        }
    }

    private void handleUpdateAvailable(WebSocket conn) {
        try {
            List<Asset> assets = new AssetLoader().getAssets("/data");
            UpdateResponse response = new UpdateResponse(UpdateConstants.UPDATE_RESPONSE_UPDATE_AVAILABLE,
                    UpdateConstants.CACHE_VERSION, assets);
            conn.send(gson.toJson(response));
        } catch (IOException | URISyntaxException e) {
            Logger.printError("Failed to pack assets: " + e.getMessage());
        }
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        Logger.printInfo(conn + " has connected to update server");
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        Logger.printInfo(conn + " has disconnected");
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        Logger.printError("An error occurred: " + ex.getMessage());
    }

    @Override
    public void onStart() {
        Logger.printInfo("Update server started on port: " + getPort());
    }

}
