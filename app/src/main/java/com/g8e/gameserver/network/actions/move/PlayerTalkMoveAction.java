package com.g8e.gameserver.network.actions.move;

import com.g8e.gameserver.network.actions.Action;

public class PlayerTalkMoveAction extends Action {
    private PlayerTalkMoveActionData data;

    public PlayerTalkMoveAction(String playerID, PlayerTalkMoveActionData data) {
        this.action = "playerTalkMove";
        this.playerID = playerID;
        this.data = data;
    }

    public String getEntityID() {
        return data.getEntityID();
    }

}
