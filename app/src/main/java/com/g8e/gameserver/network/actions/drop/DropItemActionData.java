package com.g8e.gameserver.network.actions.drop;

public class DropItemActionData {
    private int inventoryIndex;

    public DropItemActionData(int inventoryIndex) {
        this.inventoryIndex = inventoryIndex;
    }

    public int getInventoryIndex() {
        return inventoryIndex;
    }

}
