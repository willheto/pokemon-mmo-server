package com.g8e.gameserver.network.actions.wield;

import com.g8e.gameserver.network.actions.Action;

public class WieldItemAction extends Action {
    private WieldItemActionData data;

    public WieldItemAction(String playerID, WieldItemActionData data) {
        this.action = "wieldItem";
        this.playerID = playerID;
        this.data = data;
    }

    public int getInventoryIndex() {
        return data.getInventoryIndex();
    }

}
