package com.g8e.gameserver.network.actions.quest;

public class QuestProgressUpdateActionData {
    private int questID;
    private int progress;

    public QuestProgressUpdateActionData(int questID, int progress) {
        this.questID = questID;
        this.progress = progress;
    }

    public int getQuestID() {
        return questID;
    }

    public int getProgress() {
        return progress;
    }

}
