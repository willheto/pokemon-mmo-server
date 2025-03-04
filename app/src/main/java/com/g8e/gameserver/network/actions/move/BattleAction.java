package com.g8e.gameserver.network.actions.move;

import com.g8e.gameserver.network.actions.Action;

public class BattleAction extends Action {
    public enum BattleOption {
        FIGHT,
        ITEM,
        POKEMON,
        RUN
    }

    final private BattleOption option;
    private int moveId; // Only used if FIGHT is chosen
    private int itemId; // Only used if ITEM is chosen
    private int pokemonIndex; // Only used if POKEMON is chosen

    public BattleAction(String playerID, BattleOption option) {
        this.action = "battleAction";
        this.playerID = playerID;
        this.option = option;
    }

    public static BattleAction fight(String playerID, int moveId) {
        BattleAction action = new BattleAction(playerID, BattleOption.FIGHT);
        action.moveId = moveId;
        return action;
    }

    public static BattleAction useItem(String playerID, int itemId) {
        BattleAction action = new BattleAction(playerID, BattleOption.ITEM);
        action.itemId = itemId;
        return action;
    }

    public static BattleAction changePokemon(String playerID, int pokemonIndex) {
        BattleAction action = new BattleAction(playerID, BattleOption.POKEMON);
        action.pokemonIndex = pokemonIndex;
        return action;
    }

    public static BattleAction run(String playerID) {
        return new BattleAction(playerID, BattleOption.RUN);
    }

    public BattleOption getOption() {
        return option;
    }

    public int getMoveId() {
        return moveId;
    }

    public int getItemId() {
        return itemId;
    }

    public int getPokemonIndex() {
        return pokemonIndex;
    }
}
