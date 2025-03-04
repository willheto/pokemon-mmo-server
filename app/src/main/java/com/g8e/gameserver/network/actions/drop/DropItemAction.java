package com.g8e.gameserver.network.actions.drop;

import com.g8e.gameserver.network.actions.Action;

public class DropItemAction extends Action {
    private DropItemActionData data;

    public DropItemAction(String playerID, DropItemActionData data) {
        this.action = "dropItem";
        this.playerID = playerID;
        this.data = data;
    }

    public int getInventoryIndex() {
        return data.getInventoryIndex();
    }

}
