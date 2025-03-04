package com.g8e.gameserver.models;

public class Stock {
    private int itemID;
    private int originalQuantity;
    private int quantity;
    private int restockTime;
    private int restockTimer;
    private boolean isDefaultStock = true;

    public Stock(int itemID, int originalQuantity, int restockTime) {
        this.itemID = itemID;
        this.originalQuantity = originalQuantity;
        this.quantity = originalQuantity;
        this.restockTime = restockTime;
        this.restockTimer = 0;
        this.isDefaultStock = true;
    }

    public boolean isDefaultStock() {
        return isDefaultStock;
    }

    public void setIsDefaultStock(boolean isDefaultStock) {
        this.isDefaultStock = isDefaultStock;
    }

    public int getOriginalQuantity() {
        return originalQuantity;
    }

    public int getItemID() {
        return itemID;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getRestockTime() {
        return restockTime;
    }

    public int getRestockTimer() {
        return restockTimer;
    }

    public void setItemID(int itemID) {
        this.itemID = itemID;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setOriginalQuantity(int originalQuantity) {
        this.originalQuantity = originalQuantity;
    }

    public void setRestockTime(int restockTime) {
        this.restockTime = restockTime;
    }

    public void setRestockTimer(int restockTimer) {
        this.restockTimer = restockTimer;
    }

    public void restock() {
        if (isDefaultStock == false) {
            return;
        }
        if (restockTimer > 0) {
            restockTimer--;
        } else if (restockTimer == 0) {
            if (quantity < originalQuantity) {
                quantity++;
            }
            restockTimer = restockTime;
        }
    }
}
