package com.g8e.gameserver.models.events;

import com.g8e.gameserver.managers.PokemonsManager;
import com.g8e.gameserver.models.pokemon.Pokemon;

public class WildPokemonEvent {
    public String entityID;
    public int route;
    public Pokemon wildPokemon;
    final private PokemonsManager pokemonsManager = new PokemonsManager();

    public WildPokemonEvent(String entityID, int route) {
        this.entityID = entityID;
        this.route = route;
    }

    public void rollRandomPokemon() {

        int random = (int) (Math.random() * 100);

        if (random < 55) {
            int pidgeyID = pokemonsManager.getIdByName("Pidgey");
            int randomLevel = (int) (Math.random() * 3) + 2;
            wildPokemon = new Pokemon(pidgeyID, randomLevel);
        } else if (random < 60) {
            int rattataID = pokemonsManager.getIdByName("Rattata");
            int randomLevel = (int) (Math.random() * 3) + 2;
            wildPokemon = new Pokemon(rattataID, randomLevel);
        } else {
            int sentretID = pokemonsManager.getIdByName("Sentret");
            int randomLevel = (int) (Math.random() * 2) + 2;
            wildPokemon = new Pokemon(sentretID, randomLevel);
        }
    }
}
