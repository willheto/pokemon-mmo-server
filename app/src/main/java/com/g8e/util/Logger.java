package com.g8e.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {

    // ANSI escape codes for colors
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_MAGENTA = "\u001B[35m";
    private static final String ANSI_CYAN = "\u001B[36m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_YELLOW = "\u001B[33m";

    private static String getCurrentTimestamp() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return formatter.format(new Date());
    }

    public static void printDebug(String message) {
        System.out.println(ANSI_MAGENTA + getCurrentTimestamp() + "\t" +
                ANSI_CYAN + "DEBUG\t" + ANSI_RESET + message);
    }

    public static void printInfo(String message) {
        System.out.println(ANSI_MAGENTA + getCurrentTimestamp() + "\t" +
                ANSI_GREEN + "INFO\t" + ANSI_RESET + message);
    }

    public static void printError(String message) {
        System.err.println(ANSI_MAGENTA + getCurrentTimestamp() + "\t" +
                ANSI_RED + "ERROR\t" + ANSI_RESET + message);
    }

    public static void printFatalError(String message) {
        System.err.println(ANSI_MAGENTA + getCurrentTimestamp() + "\t" +
                ANSI_RED + "FATAL ERROR\t" + ANSI_RESET + message);
        System.exit(1);
    }

    public static void printWarning(String message) {
        System.out.println(ANSI_MAGENTA + getCurrentTimestamp() + "\t" +
                ANSI_YELLOW + "WARN\t" + ANSI_RESET + message);
    }
}
