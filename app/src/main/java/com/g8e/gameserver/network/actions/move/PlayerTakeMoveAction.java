package com.g8e.gameserver.network.actions.move;

import com.g8e.gameserver.network.actions.Action;

public class PlayerTakeMoveAction extends Action {

    private PlayerTakeMoveActionData data;

    public PlayerTakeMoveAction(String playerID, PlayerTakeMoveActionData data) {
        this.action = "playerTakeMove";
        this.playerID = playerID;
        this.data = data;
    }

    public String getUniqueItemID() {
        return data.getUniqueItemID();
    }
}
