package com.g8e.gameserver.models.events;

public class BattleTurnEvent {
    public String playerID;
    public String targetID;
    public int moveId;
    public boolean isBattleOver = false;
    public String winnerEntityID = null;

    public BattleTurnEvent(String playerID, String targetID, int moveId) {
        this.playerID = playerID;
        this.targetID = targetID;
        this.moveId = moveId;
    }

    public void setBattleOver(String winnerEntityID) {
        this.isBattleOver = true;
        this.winnerEntityID = winnerEntityID;
    }

    public boolean isBattleOver() {
        return isBattleOver;
    }

    public String getWinnerEntityID() {
        return winnerEntityID;
    }

    public String getPlayerID() {
        return playerID;
    }
}
