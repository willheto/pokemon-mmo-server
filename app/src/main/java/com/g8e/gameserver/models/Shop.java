package com.g8e.gameserver.models;

public class Shop {
    private String shopID;
    private String shopName;
    private int sellsAtPercentage;
    private int buysAtPercentage;
    private boolean buysAnything;

    private Stock[] stocks;

    public Shop(String shopID, String shopName,
            int sellsAtPercentage, int buysAtPercentage,
            boolean buysAnything,
            Stock[] stocks) {
        this.shopID = shopID;
        this.shopName = shopName;
        this.sellsAtPercentage = sellsAtPercentage;
        this.buysAtPercentage = buysAtPercentage;
        this.buysAnything = buysAnything;
        this.stocks = stocks;
    }

    public void removeStock(int itemID) {
        Stock[] newStocks = new Stock[stocks.length - 1];
        int j = 0;
        for (int i = 0; i < stocks.length; i++) {
            if (stocks[i].getItemID() != itemID) {
                newStocks[j] = stocks[i];
                j++;
            }
        }
        stocks = newStocks;
    }

    public void addStock(Stock stock) {
        Stock[] newStocks = new Stock[stocks.length + 1];
        for (int i = 0; i < stocks.length; i++) {
            newStocks[i] = stocks[i];
        }
        newStocks[stocks.length] = stock;
        stocks = newStocks;
    }

    public boolean getBuysAnything() {
        return buysAnything;
    }

    public String getShopID() {
        return shopID;
    }

    public String getShopName() {
        return shopName;
    }

    public Stock[] getStocks() {
        return stocks;
    }

    public float getSellsAtPercentage() {
        return sellsAtPercentage / 100.0f;
    }

    public float getBuysAtPercentage() {
        return buysAtPercentage / 100.0f;
    }

    public void setStocks(Stock[] stocks) {
        this.stocks = stocks;
    }

    public Stock getStock(int itemID) {
        for (Stock stock : stocks) {
            if (stock.getItemID() == itemID) {
                return stock;
            }
        }
        return null;
    }

    public void restock() {
        for (Stock stock : stocks) {
            stock.restock();
        }
    }

    public void restock(int itemID) {
        for (Stock stock : stocks) {
            if (stock.getItemID() == itemID) {
                stock.restock();
            }
        }
    }

}
