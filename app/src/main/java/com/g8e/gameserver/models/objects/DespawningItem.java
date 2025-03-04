package com.g8e.gameserver.models.objects;

public class DespawningItem {
    private String uniqueItemID;
    private int despawnTimer;

    public DespawningItem(String uniqueItemID, int despawnTimer) {
        this.uniqueItemID = uniqueItemID;
        this.despawnTimer = despawnTimer;
    }

    public String getUniqueItemID() {
        return uniqueItemID;
    }

    public void setUniqueItemID(String uniqueItemID) {
        this.uniqueItemID = uniqueItemID;
    }

    public int getDespawnTimer() {
        return despawnTimer;
    }

    public void setDespawnTimer(int despawnTimer) {
        this.despawnTimer = despawnTimer;
    }

}
