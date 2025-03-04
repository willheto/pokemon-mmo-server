package com.g8e.gameserver.network.actions.inventory;

import com.g8e.gameserver.network.actions.Action;

public class RemoveItemFromInventoryAction extends Action {

    private int itemID;
    private int amount;

    public RemoveItemFromInventoryAction(String playerID, int itemID, int amount) {
        this.action = "removeItemFromInventory";
        this.playerID = playerID;
        this.itemID = itemID;
        this.amount = amount;
    }

    public int getItemID() {
        return itemID;
    }

    public int getAmount() {
        return amount;
    }

}
