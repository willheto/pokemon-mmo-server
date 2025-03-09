package com.g8e.updateserver.util;

import io.github.cdimascio.dotenv.Dotenv;

public class UpdateConstants {
    private static final Dotenv dotenv;
    public static final int UPDATE_SERVER_PORT;

    static {
        dotenv = Dotenv.load();
        UPDATE_SERVER_PORT = Integer.parseInt(dotenv.get("UPDATE_SERVER_PORT"));
    }

    public static final int CACHE_VERSION = 55;
    public static final int UPDATE_REQUEST_CHECK_FOR_UPDATES = 1;
    public static final int UPDATE_RESPONSE_CACHE_UP_TO_DATE = 1;
    public static final int UPDATE_RESPONSE_UPDATE_AVAILABLE = 2;

    public static int getUPDATE_SERVER_PORT() {
        return UPDATE_SERVER_PORT;
    }

    // Prevent instantiation
    private UpdateConstants() {
    }

}
