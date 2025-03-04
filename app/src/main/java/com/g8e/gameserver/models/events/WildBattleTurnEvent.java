package com.g8e.gameserver.models.events;

import com.g8e.gameserver.enums.BattleOption;

public class WildBattleTurnEvent {
    public String playerID;
    public int moveId;
    public int newHp;
    public boolean isPlayersMove = false;
    public boolean isBattleOver = false;
    public boolean isAllPokemonsFainted = false;
    public boolean isPlayerWinner = false;
    public boolean isRunSuccessful = false; // Flag for successful run attempts
    public int itemUsedId = -1; // For item usage tracking
    public int switchedPokemonIndex = -1; // For Pokémon switch tracking
    public int effect = 0; // For move effects
    public BattleOption actionType;
    public boolean isCaught = false; // Flag for successful Pokémon catches

    // Constructor for fight moves
    public WildBattleTurnEvent(String playerID, int moveId, boolean isPlayersMove) {
        this.playerID = playerID;
        this.moveId = moveId;
        this.isPlayersMove = isPlayersMove;
        this.actionType = BattleOption.FIGHT;
    }

    // Generic constructor for ITEM and POKEMON actions
    public WildBattleTurnEvent(String playerID, int actionId, boolean isPlayersMove, BattleOption actionType) {
        this.playerID = playerID;
        this.isPlayersMove = isPlayersMove;

        // Handle item usage or Pokémon switch based on the action type
        if (actionType == BattleOption.ITEM) {
            this.itemUsedId = actionId; // actionId is itemId for ITEM action
        } else if (actionType == BattleOption.POKEMON) {
            this.switchedPokemonIndex = actionId; // actionId is pokemonIndex for POKEMON action
        }
        this.actionType = actionType;
    }

    // Constructor for run attempts
    public WildBattleTurnEvent(String playerID, boolean isPlayersMove, boolean isRunSuccessful) {
        this.playerID = playerID;
        this.isPlayersMove = isPlayersMove;
        this.isRunSuccessful = isRunSuccessful;
        this.actionType = BattleOption.RUN;

    }

    public void setBattleOver(boolean isPlayerWinner) {
        this.isBattleOver = true;
        this.isPlayerWinner = isPlayerWinner;
    }

    public boolean isBattleOver() {
        return isBattleOver;
    }

    public String getPlayerID() {
        return playerID;
    }

    public void setHp(int newHp) {
        this.newHp = newHp;
    }

    public void setEffect(int effect) {
        this.effect = effect;
    }
}
