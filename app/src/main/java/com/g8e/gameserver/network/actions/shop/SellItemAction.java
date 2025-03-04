package com.g8e.gameserver.network.actions.shop;

import com.g8e.gameserver.network.actions.Action;

public class SellItemAction extends Action {
    private String shopID;
    private int inventoryIndex;
    private int amount;

    public SellItemAction(String playerID, String shopID, int inventoryIndex, int amount) {
        this.action = "sellItem";
        this.playerID = playerID;
        this.shopID = shopID;
        this.inventoryIndex = inventoryIndex;
        this.amount = amount;
    }

    public int getInventoryIndex() {
        return inventoryIndex;
    }

    public int getAmount() {
        return amount;
    }

    public String getShopID() {
        return shopID;
    }

}
