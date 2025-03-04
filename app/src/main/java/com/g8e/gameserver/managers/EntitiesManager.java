package com.g8e.gameserver.managers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import com.g8e.gameserver.models.entities.EntityData;
import com.google.gson.Gson;

public class EntitiesManager {
    private EntityData[] entityData = new EntityData[100];

    public EntitiesManager() {
        loadEntityData();
    }

    private void loadEntityData() {
        URL entityDataUrl = getClass().getResource("/data/scripts/npcs.json");

        if (entityDataUrl == null) {
            throw new IllegalArgumentException("Resource not found: /data/npcs.json");
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(entityDataUrl.openStream()))) {
            Gson gson = new Gson();
            EntityData[] loadedEntityDatas = gson.fromJson(reader, EntityData[].class);
            System.arraycopy(loadedEntityDatas, 0, entityData, 0, loadedEntityDatas.length);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public EntityData getEntityDataByIndex(int entityIndex) {
        for (EntityData entity : entityData) {
            if (entity.getEntityIndex() == entityIndex) {
                return entity;
            }
        }

        return null;
    }

}
