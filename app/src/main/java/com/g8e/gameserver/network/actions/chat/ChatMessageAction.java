package com.g8e.gameserver.network.actions.chat;

import com.g8e.gameserver.network.actions.Action;

public class ChatMessageAction extends Action {
    private ChatMessageActionData data;

    public ChatMessageAction(String playerID, ChatMessageActionData data) {
        this.action = "chatMessage";
        this.playerID = playerID;
        this.data = data;
    }

    public String getMessage() {
        return data.message;
    }

    public long getTimeSent() {
        return data.timeSent;
    }

    public boolean isGlobal() {
        return data.isGlobal;
    }
}
