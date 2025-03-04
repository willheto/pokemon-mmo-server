package com.g8e.gameserver.managers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import com.g8e.gameserver.models.Shop;
import com.google.gson.Gson;

public class ShopsManager {
    private static final int TICK_RATE = 600; // 600ms
    private Shop[] shops = new Shop[3];

    public ShopsManager() {
        loadShops();
        startRestockLoop();
    }

    private void startRestockLoop() {
        Thread restockThread = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(TICK_RATE);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                for (Shop shop : shops) {
                    if (shop == null) {
                        continue;
                    }
                    shop.restock();
                }
            }
        });

        restockThread.start();
    }

    private void loadShops() {
        URL shopsUrl = getClass().getResource("/data/scripts/shops.json");

        if (shopsUrl == null) {
            throw new IllegalArgumentException("Resource not found: /data/scripts/shops.json");
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(shopsUrl.openStream()))) {
            Gson gson = new Gson();
            Shop[] loadedShops = gson.fromJson(reader, Shop[].class);

            for (int i = 0; i < loadedShops.length; i++) {
                for (int j = 0; j < loadedShops[i].getStocks().length; j++) {
                    loadedShops[i].getStocks()[j].setOriginalQuantity(loadedShops[i].getStocks()[j].getQuantity());
                }
            }
            System.arraycopy(loadedShops, 0, shops, 0, loadedShops.length);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // experimenting using string instead of int as id for better readability
    public Shop getShopByID(String shopID) {

        for (Shop shop : shops) {
            if (shop == null) {
                continue;
            }
            if (shop.getShopID().equals(shopID)) {
                return shop;
            }
        }

        return null;
    }

    public Shop[] getShops() {
        return shops;
    }
}
