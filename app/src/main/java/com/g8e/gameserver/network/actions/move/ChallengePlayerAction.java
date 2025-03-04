package com.g8e.gameserver.network.actions.move;

import com.g8e.gameserver.network.actions.Action;

public class ChallengePlayerAction extends Action {

    private String targetID;

    public ChallengePlayerAction(String playerID, String targetID) {
        this.action = "forceNpcBattlePlayer";
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
