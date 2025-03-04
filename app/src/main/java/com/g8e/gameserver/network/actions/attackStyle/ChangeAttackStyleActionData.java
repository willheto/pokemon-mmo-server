package com.g8e.gameserver.network.actions.attackStyle;

public class ChangeAttackStyleActionData {
    String attackStyle;

    public ChangeAttackStyleActionData(String attackStyle) {
        this.attackStyle = attackStyle;
    }

    public String getAttackStyle() {
        return attackStyle;
    }
}
