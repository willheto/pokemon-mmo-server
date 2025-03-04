package com.g8e.gameserver.network.actions.quest;

public class QuestProgressUpdate {
    private int questID;
    private int progress;

    public QuestProgressUpdate(int questId, int progress) {
        this.questID = questId;
        this.progress = progress;
    }

    public int getQuestId() {
        return questID;
    }

    public int getProgress() {
        return progress;
    }

}
