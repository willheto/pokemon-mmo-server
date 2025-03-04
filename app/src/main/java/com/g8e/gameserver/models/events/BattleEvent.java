package com.g8e.gameserver.models.events;

public class BattleEvent {
    public String entity1ID;
    public String entity2ID;

    public BattleEvent(String entity1ID, String entity2ID) {
        this.entity1ID = entity1ID;
        this.entity2ID = entity2ID;
    }
}
