package com.g8e.gameserver.network.actions.chat;

public class ChatMessageActionData {
    String message;
    long timeSent;
    boolean isGlobal;

    public ChatMessageActionData(String message, long timeSent, boolean isGlobal) {
        this.message = message;
        this.timeSent = timeSent;
        this.isGlobal = isGlobal;
    }

}
