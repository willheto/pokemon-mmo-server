package com.g8e.gameserver.managers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import com.g8e.gameserver.models.teleport.TeleportData;
import com.g8e.gameserver.tile.TilePosition;
import com.google.gson.Gson;

public class TeleportsManager {
    private TeleportData[] teleportData = new TeleportData[351];

    public TeleportsManager() {
        loadTeleportData();
    }

    private void loadTeleportData() {
        URL teleportDataUrl = getClass().getResource("/data/scripts/teleports.json");

        if (teleportDataUrl == null) {
            throw new IllegalArgumentException("Resource not found: /data/teleports.json");
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(teleportDataUrl.openStream()))) {
            Gson gson = new Gson();
            TeleportData[] loadedTeleportDatas = gson.fromJson(reader, TeleportData[].class);
            System.arraycopy(loadedTeleportDatas, 0, teleportData, 0, loadedTeleportDatas.length);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public TeleportData checkTeleportTriggers(TilePosition playerTile) {
        for (TeleportData teleport : teleportData) {
            if (teleport != null) {
                for (TilePosition triggerTile : teleport.getTriggerTiles()) {
                    if (triggerTile.getX() == playerTile.getX() && triggerTile.getY() == playerTile.getY()) {
                        return teleport;
                    }
                }
            }
        }
        return null;
    }

}
