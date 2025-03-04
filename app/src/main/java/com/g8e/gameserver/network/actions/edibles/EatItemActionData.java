package com.g8e.gameserver.network.actions.edibles;

public class EatItemActionData {
    private int inventoryIndex;

    public EatItemActionData(int inventoryIndex) {
        this.inventoryIndex = inventoryIndex;
    }

    public int getInventoryIndex() {
        return inventoryIndex;
    }

}
