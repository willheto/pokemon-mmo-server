package com.g8e.gameserver.models;

public class ChatMessage {
    private String senderName;
    private String message;
    private long timeSent;
    private boolean isGlobal;
    private boolean isChallenge;
    private String challengerID;

    public ChatMessage(String senderName, String message, long timeSent, boolean isGlobal) {
        this(senderName, message, timeSent, isGlobal, false, null);
    }

    public ChatMessage(String senderName, String message, long timeSent,
            boolean isGlobal,
            boolean isChallenge,
            String challengerID) {
        this.senderName = senderName;
        this.message = message;
        this.timeSent = timeSent;
        this.isGlobal = isGlobal;
        this.isChallenge = isChallenge;
        this.challengerID = challengerID;
    }

    public String getMessage() {
        return message;
    }

    public long getTimeSent() {
        return timeSent;
    }

    public boolean isGlobal() {
        return isGlobal;
    }

    public String getSenderName() {
        return senderName;
    }

    public boolean isChallenge() {
        return isChallenge;
    }
}
