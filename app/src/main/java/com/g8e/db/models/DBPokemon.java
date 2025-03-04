package com.g8e.db.models;

public class DBPokemon {
    final private int pokemon_id;
    final private int player_id;
    final private int id;
    final private int xp;
    final private int hp;
    final private int level;
    final private String moves;
    final private int hpIv;
    final private int atkIv;
    final private int defIv;
    final private int spAtkIv;
    final private int spDefIv;
    final private int spdIv;
    final private int hpEv;
    final private int atkEv;
    final private int defEv;
    final private int spAtkEv;
    final private int spDefEv;
    final private int spdEv;
    final private int heldItem;
    final private boolean isOutsider;
    final private int evasionModifier;

    public DBPokemon(int pokemon_id, int player_id, int id, int xp, int hp, int level, String moves, int hpIv,
            int atkIv, int defIv, int spAtkIv, int spDefIv, int spdIv, int hpEv, int atkEv, int defEv, int spAtkEv,
            int spDefEv, int spdEv, int heldItem, boolean isOutsider, int evasionModifier) {
        this.pokemon_id = pokemon_id;
        this.player_id = player_id;
        this.id = id;
        this.xp = xp;
        this.hp = hp;
        this.level = level;
        this.moves = moves;
        this.hpIv = hpIv;
        this.atkIv = atkIv;
        this.defIv = defIv;
        this.spAtkIv = spAtkIv;
        this.spDefIv = spDefIv;
        this.spdIv = spdIv;
        this.hpEv = hpEv;
        this.atkEv = atkEv;
        this.defEv = defEv;
        this.spAtkEv = spAtkEv;
        this.spDefEv = spDefEv;
        this.spdEv = spdEv;
        this.heldItem = heldItem;
        this.isOutsider = isOutsider;
        this.evasionModifier = evasionModifier;
    }

    public int getPokemonID() {
        return pokemon_id;
    }

    public int getPlayerID() {
        return player_id;
    }

    public int getID() {
        return id;
    }

    public int getXP() {
        return xp;
    }

    public int getHP() {
        return hp;
    }

    public int getLevel() {
        return level;
    }

    public String getMoves() {
        return moves;
    }

    public int getHPIV() {
        return hpIv;
    }

    public int getAtkIV() {
        return atkIv;
    }

    public int getDefIV() {
        return defIv;
    }

    public int getSpAtkIV() {
        return spAtkIv;
    }

    public int getSpDefIV() {
        return spDefIv;
    }

    public int getSpdIV() {
        return spdIv;
    }

    public int getHPEV() {
        return hpEv;
    }

    public int getAtkEV() {
        return atkEv;
    }

    public int getDefEV() {
        return defEv;
    }

    public int getSpAtkEV() {
        return spAtkEv;
    }

    public int getSpDefEV() {
        return spDefEv;
    }

    public int getSpdEV() {
        return spdEv;
    }

    public int getHeldItem() {
        return heldItem;
    }

    public boolean getIsOutsider() {
        return isOutsider;
    }

    public int getEvasionModifier() {
        return evasionModifier;
    }

}
