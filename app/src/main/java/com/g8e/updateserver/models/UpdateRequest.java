package com.g8e.updateserver.models;

public class UpdateRequest {
    private int cacheNumber;
    private int type;

    public int getCachenumber() {
        return cacheNumber;
    }

    public void setCachenumber(int cacheNumber) {
        this.cacheNumber = cacheNumber;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
