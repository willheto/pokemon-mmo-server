package com.g8e.gameserver.models.pokemon;

public class PokemonMove {

    private int id;
    private String name;
    private String type;
    private String category;
    private Integer power;
    private Integer accuracy;
    private int pp;

    public PokemonMove(int id, String name, String type, String category,
            Integer power, Integer accuracy, int pp, String effect) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.category = category;
        this.power = power;
        this.accuracy = accuracy;
        this.pp = pp;
    }

    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public Integer getPower() {
        return this.power;
    }

    public Integer getAccuracy() {
        return this.accuracy;
    }

    public int getPP() {
        return this.pp;
    }

    public String getType() {
        return this.type;
    }

    public String getCategory() {
        return this.category;
    }
}
