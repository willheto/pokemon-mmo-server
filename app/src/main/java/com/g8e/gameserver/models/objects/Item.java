package com.g8e.gameserver.models.objects;

public class Item {
    private String uniqueID;
    private int itemID;
    private String name;
    private String spriteName;
    private int value;
    private boolean isStackable;
    private int amount;
    private Integer worldX = null;
    private Integer worldY = null;

    public Item(int itemID, String name, boolean isStackable, String spriteName,
            int value) {
        this.itemID = itemID;
        this.name = name;
        this.isStackable = isStackable;
        this.spriteName = spriteName;
        this.value = value;
        this.amount = 1;
    }

    public int getValue() {
        return value;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public boolean isStackable() {
        return isStackable;
    }

    public void setStackable(boolean stackable) {
        isStackable = stackable;
    }

    public int getItemID() {
        return itemID;
    }

    public void setItemID(int itemID) {
        this.itemID = itemID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpriteName() {
        return spriteName;
    }

    public void setSpriteName(String spriteName) {
        this.spriteName = spriteName;
    }

    public String getUniqueID() {
        return uniqueID;
    }

    public void setUniqueID(String uniqueID) {
        this.uniqueID = uniqueID;
    }

    public Integer getWorldX() {
        return worldX;
    }

    public void setWorldX(Integer worldX) {
        this.worldX = worldX;
    }

    public Integer getWorldY() {
        return worldY;
    }

    public void setWorldY(Integer worldY) {
        this.worldY = worldY;
    }

}
