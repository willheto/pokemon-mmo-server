package com.g8e.gameserver.models.events;

public class TalkEvent {
    public String talkerID;
    public String targetID;
    public int targetIndex;

    public TalkEvent(String talkerID, String targetID, int targetIndex) {
        this.talkerID = talkerID;
        this.targetID = targetID;
        this.targetIndex = targetIndex;
    }

}
