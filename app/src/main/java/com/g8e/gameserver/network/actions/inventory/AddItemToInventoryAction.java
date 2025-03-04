package com.g8e.gameserver.network.actions.inventory;

import com.g8e.gameserver.network.actions.Action;

public class AddItemToInventoryAction extends Action {
    private int itemID;
    private int quantity;

    public AddItemToInventoryAction(String playerID, int itemID, int quantity) {
        this.action = "addItemToInventory";
        this.playerID = playerID;
        this.itemID = itemID;
        this.quantity = quantity;
    }

    public AddItemToInventoryAction(int itemID, int quantity) {
        this.itemID = itemID;
        this.quantity = quantity;
    }

    public int getItemID() {
        return itemID;
    }

    public int getQuantity() {
        return quantity;
    }

}
