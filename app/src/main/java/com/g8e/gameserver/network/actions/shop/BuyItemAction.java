package com.g8e.gameserver.network.actions.shop;

import com.g8e.gameserver.network.actions.Action;

public class BuyItemAction extends Action {
    private String shopID;
    private int itemID;
    private int amount;

    public BuyItemAction(String playerID, String shopID, int itemID, int amount) {
        this.action = "buyItem";
        this.playerID = playerID;
        this.shopID = shopID;
        this.itemID = itemID;
        this.amount = amount;
    }

    public int getItemID() {
        return itemID;
    }

    public int getAmount() {
        return amount;
    }

    public String getShopID() {
        return shopID;
    }
}
