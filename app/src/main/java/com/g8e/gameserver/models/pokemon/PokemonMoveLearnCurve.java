package com.g8e.gameserver.models.pokemon;

public class PokemonMoveLearnCurve {
    private int level;
    private String name;

    // Constructor
    public PokemonMoveLearnCurve(int level, String name) {
        this.level = level;
        this.name = name;
    }

    // Getters and setters
    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
