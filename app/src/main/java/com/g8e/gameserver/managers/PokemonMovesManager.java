package com.g8e.gameserver.managers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import com.g8e.gameserver.models.pokemon.PokemonMove;
import com.g8e.util.Logger;
import com.google.gson.Gson;

public class PokemonMovesManager {
    final private PokemonMove[] pokemonMoves = new PokemonMove[100];

    public PokemonMovesManager() {
        loadPokemonMoves();
    }

    private void loadPokemonMoves() {
        URL pokemonMovesUrl = getClass().getResource("/data/scripts/pokemonMoves.json");

        if (pokemonMovesUrl == null) {
            throw new IllegalArgumentException("Resource not found: /data/pokemonMoves.json");
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(pokemonMovesUrl.openStream()))) {
            Gson gson = new Gson();
            PokemonMove[] loadedPokemonMoves = gson.fromJson(reader, PokemonMove[].class);
            System.arraycopy(loadedPokemonMoves, 0, pokemonMoves, 0, loadedPokemonMoves.length);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public PokemonMove getPokemonMoveById(int moveId) {
        for (PokemonMove move : pokemonMoves) {
            if (move.getId() == moveId) {
                return move;
            }
        }

        return null;
    }

    public int getMoveIdByName(String moveName) {
        Logger.printDebug(moveName);
        for (PokemonMove move : pokemonMoves) {
            if (move.getName().equals(moveName)) {
                return move.getId();
            }
        }

        return -1;
    }

}
