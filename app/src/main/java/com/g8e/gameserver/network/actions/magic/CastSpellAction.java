package com.g8e.gameserver.network.actions.magic;

import com.g8e.gameserver.network.actions.Action;

public class CastSpellAction extends Action {
    private int spellID;
    private String targetID = null;

    public CastSpellAction(String playerID, int spellID, String targetID) {
        this.action = "castSpell";
        this.playerID = playerID;
        this.spellID = spellID;
        this.targetID = targetID;
    }

    public int getSpellID() {
        return spellID;
    }

    public String getTargetID() {
        return targetID;
    }

}
