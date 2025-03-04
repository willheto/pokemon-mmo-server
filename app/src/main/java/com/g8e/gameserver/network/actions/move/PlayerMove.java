// PlayerMove.java
package com.g8e.gameserver.network.actions.move;

import com.g8e.gameserver.network.actions.Action;

public class PlayerMove extends Action {
    private PlayerMoveData data;

    public PlayerMove(String playerID, PlayerMoveData data) {
        this.action = "playerMove";
        this.playerID = playerID;
        this.data = data;
    }

    public int getX() {
        return data.x;
    }

    public int getY() {
        return data.y;
    }
}
