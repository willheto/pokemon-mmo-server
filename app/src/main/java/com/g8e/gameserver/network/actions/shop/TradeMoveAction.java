package com.g8e.gameserver.network.actions.shop;

import com.g8e.gameserver.network.actions.Action;

public class TradeMoveAction extends Action {

    private String entityID;

    public TradeMoveAction(String playerID, String entityID) {
        this.action = "tradeMove";
        this.playerID = playerID;
        this.entityID = entityID;
    }

    public String getEntityID() {
        return entityID;
    }

}
