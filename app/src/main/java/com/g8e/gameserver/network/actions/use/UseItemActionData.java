package com.g8e.gameserver.network.actions.use;

public class UseItemActionData {

    private int itemID;
    private int targetID;

    public UseItemActionData(int itemID, int targetID) {
        this.itemID = itemID;
        this.targetID = targetID;
    }

    public int getItemID() {
        return itemID;
    }

    public int getTargetID() {
        return targetID;
    }
}
