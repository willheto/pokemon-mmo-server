package com.g8e.gameserver.models.entities;

public class EntityData {
    int entityIndex;
    String name = "";
    int type = 0;

    public EntityData(int entityIndex, String name, String examine, int respawnTime, int[] skills, int type,
            String spriteName) {
        this.entityIndex = entityIndex;
        this.name = name;
        this.type = type;
    }

    public int getEntityIndex() {
        return entityIndex;
    }

    public String getName() {
        return name;
    }

    public int getType() {
        return type;
    }

}
