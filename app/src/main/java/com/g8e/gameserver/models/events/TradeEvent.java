package com.g8e.gameserver.models.events;

public class TradeEvent {
    public String traderID;
    public String targetID;
    public int targetIndex;

    public TradeEvent(String traderID, String targetID, int targetIndex) {
        this.traderID = traderID;
        this.targetID = targetID;
        this.targetIndex = targetIndex;
    }
}
