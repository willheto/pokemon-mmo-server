package com.g8e.gameserver.network.actions.edibles;

import com.g8e.gameserver.network.actions.Action;

public class EatItemAction extends Action {

    private EatItemActionData data;

    public EatItemAction(String playerID, EatItemActionData data) {
        this.action = "eat";
        this.playerID = playerID;
        this.data = data;
    }

    public int getInventoryIndex() {
        return data.getInventoryIndex();
    }

}
