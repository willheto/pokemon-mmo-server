package com.g8e.gameserver.network.actions.move;

import com.g8e.gameserver.network.actions.Action;

public class StartBattleAction extends Action {
    private String targetID;

    public StartBattleAction(String playerID, String targetID) {
        this.action = "startBattle";
        this.playerID = playerID;
        this.targetID = targetID;
    }

    public String getPlayerID() {
        return playerID;
    }

    public String getTargetID() {
        return targetID;
    }
}
