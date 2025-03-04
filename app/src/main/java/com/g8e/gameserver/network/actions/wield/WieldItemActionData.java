package com.g8e.gameserver.network.actions.wield;

public class WieldItemActionData {

    private int inventoryIndex;

    public WieldItemActionData(int inventoryIndex) {
        this.inventoryIndex = inventoryIndex;
    }

    public int getInventoryIndex() {
        return inventoryIndex;
    }

}
