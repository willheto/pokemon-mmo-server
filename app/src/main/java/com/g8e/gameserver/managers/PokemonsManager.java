package com.g8e.gameserver.managers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import com.g8e.gameserver.models.pokemon.PokemonData;
import com.google.gson.Gson;

public class PokemonsManager {
    private PokemonData[] pokemonData = new PokemonData[351];

    public PokemonsManager() {
        loadPokemonData();
    }

    private void loadPokemonData() {
        URL pokemonDataUrl = getClass().getResource("/data/scripts/pokemons.json");

        if (pokemonDataUrl == null) {
            throw new IllegalArgumentException("Resource not found: /data/pokemons.json");
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(pokemonDataUrl.openStream()))) {
            Gson gson = new Gson();
            PokemonData[] loadedPokemonDatas = gson.fromJson(reader, PokemonData[].class);
            System.arraycopy(loadedPokemonDatas, 0, pokemonData, 0, loadedPokemonDatas.length);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public PokemonData getPokemonDataByIndex(int pokemonIndex) {
        for (PokemonData pokemon : pokemonData) {
            if (pokemon.getPokemonIndex() == pokemonIndex) {
                return pokemon;
            }
        }

        return null;
    }

    public int getIdByName(String name) {
        for (PokemonData pokemon : pokemonData) {
            if (pokemon.getName().equals(name)) {
                return pokemon.getPokemonIndex();
            }
        }

        return -1;
    }

}
