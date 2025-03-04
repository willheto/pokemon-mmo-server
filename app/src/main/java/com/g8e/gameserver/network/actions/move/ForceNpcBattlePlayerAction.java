package com.g8e.gameserver.network.actions.move;

import com.g8e.gameserver.network.actions.Action;

public class ForceNpcBattlePlayerAction extends Action {
    private String npcID;

    public ForceNpcBattlePlayerAction(String playerID, String npcID) {
        this.action = "forceNpcBattlePlayer";
        this.playerID = playerID;
        this.npcID = npcID;
    }

    public String getPlayerID() {
        return playerID;
    }

    public String getNpcID() {
        return npcID;
    }
}
