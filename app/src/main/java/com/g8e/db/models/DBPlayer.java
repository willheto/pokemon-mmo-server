package com.g8e.db.models;

public class DBPlayer {
    final private int player_id;
    final private int account_id;
    final private int world_x;
    final private int world_y;
    final private int[] inventory;
    final private int[] inventoryAmounts;

    public DBPlayer(int player_id, int account_id, int world_x, int world_y, int[] inventory, int[] inventoryAmounts) {
        this.player_id = player_id;
        this.account_id = account_id;
        this.world_x = world_x;
        this.world_y = world_y;
        this.inventory = inventory;
        this.inventoryAmounts = inventoryAmounts;
    }

    public int getPlayerID() {
        return player_id;
    }

    public int getAccountID() {
        return account_id;
    }

    public int getWorldX() {
        return world_x;
    }

    public int getWorldY() {
        return world_y;
    }

    public int[] getInventory() {
        return inventory;
    }

    public int[] getInventoryAmounts() {
        return inventoryAmounts;
    }
}
