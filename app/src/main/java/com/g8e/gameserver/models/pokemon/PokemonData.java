package com.g8e.gameserver.models.pokemon;

import java.util.ArrayList;
import java.util.List;

public class PokemonData {
    int pokemonIndex;
    String name = "";
    String type1 = "";
    String type2 = "";
    int hp = 0;
    int attack = 0;
    int defense = 0;
    int specialAttack = 0;
    int specialDefense = 0;
    int speed = 0;
    int xpYield = 0;
    List<PokemonMoveLearnCurve> movesLearned = new ArrayList<>();

    public PokemonData(int pokemonIndex, String name, String type1, String type2, int hp, int attack, int defense,
            int specialAttack, int specialDefense, int speed,
            int xpYield, List<PokemonMoveLearnCurve> moveLearnCurve) {

        this.pokemonIndex = pokemonIndex;
        this.name = name;
        this.type1 = type1;
        this.type2 = type2;
        this.hp = hp;
        this.attack = attack;
        this.defense = defense;
        this.specialAttack = specialAttack;
        this.specialDefense = specialDefense;
        this.speed = speed;
        this.xpYield = xpYield;
        this.movesLearned = moveLearnCurve;
    }

    // Getters and setters
    public int getPokemonIndex() {
        return pokemonIndex;
    }

    public void setPokemonIndex(int pokemonIndex) {
        this.pokemonIndex = pokemonIndex;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType1() {
        return type1;
    }

    public void setType1(String type1) {
        this.type1 = type1;
    }

    public String getType2() {
        return type2;
    }

    public void setType2(String type2) {
        this.type2 = type2;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public int getAttack() {
        return attack;
    }

    public void setAttack(int attack) {
        this.attack = attack;
    }

    public int getDefense() {
        return defense;
    }

    public void setDefense(int defense) {
        this.defense = defense;
    }

    public int getSpecialAttack() {
        return specialAttack;
    }

    public void setSpecialAttack(int specialAttack) {
        this.specialAttack = specialAttack;
    }

    public int getSpecialDefense() {
        return specialDefense;
    }

    public void setSpecialDefense(int specialDefense) {
        this.specialDefense = specialDefense;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public List<PokemonMoveLearnCurve> getMovesLearned() {
        return this.movesLearned;
    }

    public void setMovesLearned(List<PokemonMoveLearnCurve> movesLearned) {
        this.movesLearned = movesLearned;
    }

    public int getXpYield() {
        return xpYield;
    }

    public void setXpYield(int xpYield) {
        this.xpYield = xpYield;
    }
}
