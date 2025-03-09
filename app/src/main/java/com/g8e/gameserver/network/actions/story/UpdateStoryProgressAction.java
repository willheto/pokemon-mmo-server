package com.g8e.gameserver.network.actions.story;

import com.g8e.gameserver.network.actions.Action;

public class UpdateStoryProgressAction extends Action {
    private int progress;

    public UpdateStoryProgressAction(String playerID, int progress) {
        this.action = "updateStoryProgress";
        this.playerID = playerID;
        this.progress = progress;
    }

    public int getProgress() {
        return progress;
    }

}
