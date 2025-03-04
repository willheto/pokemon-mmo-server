package com.g8e.gameserver;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.g8e.gameserver.managers.PokemonMovesManager;
import com.g8e.gameserver.managers.PokemonsManager;
import com.g8e.gameserver.models.entities.Entity;
import com.g8e.gameserver.models.entities.Npc;
import com.g8e.gameserver.models.events.BattleTurnEvent;
import com.g8e.gameserver.models.pokemon.Pokemon;
import com.g8e.gameserver.models.pokemon.PokemonData;
import com.g8e.gameserver.models.pokemon.PokemonMove;

public class Battle {
    private final Entity entity1;
    private final Entity entity2;
    private Pokemon entity1ActivePokemon;
    private Pokemon entity2ActivePokemon;
    private PokemonMove entity1PendingMove;
    private PokemonMove entity2PendingMove;
    private final PokemonsManager pokemonsManager = new PokemonsManager();
    private final PokemonMovesManager pokemonsMovesManager = new PokemonMovesManager();
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    public Battle(Entity entity1, Entity entity2) {
        this.entity1 = entity1;
        this.entity2 = entity2;
    }

    public void setEntity1PendingMove(PokemonMove move) {
        entity1PendingMove = move;
        checkAndExecuteTurn();
    }

    public void setEntity2PendingMove(PokemonMove move) {
        entity2PendingMove = move;
        checkAndExecuteTurn();
    }

    private void checkAndExecuteTurn() {
        if (entity1PendingMove != null && entity2PendingMove != null) {
            executeTurn();
            return;
        }

        // if instance of entity 1 or 2 is npc
        if (entity1 instanceof Npc npc) {
            if (entity1PendingMove == null) {
                // random move
                int randomMove = entity1ActivePokemon.getRandomMove();
                entity1PendingMove = pokemonsMovesManager.getPokemonMoveById(randomMove);
                executeTurn();

            }
        }

        if (entity2 instanceof Npc npc) {
            if (entity2PendingMove == null) {
                // random move
                entity2PendingMove = pokemonsMovesManager.getPokemonMoveById(entity2ActivePokemon.getRandomMove());
                executeTurn();

            }
        }
    }

    private void executeTurn() {
        if (entity1ActivePokemon == null || entity2ActivePokemon == null) {
            return;
        }

        PokemonData entity1Data = pokemonsManager.getPokemonDataByIndex(entity1ActivePokemon.getId());
        PokemonData entity2Data = pokemonsManager.getPokemonDataByIndex(entity2ActivePokemon.getId());

        boolean entity1GoesFirst = entity1Data.getSpeed() > entity2Data.getSpeed();
        Entity firstEntity = entity1GoesFirst ? entity1 : entity2;
        Entity secondEntity = entity1GoesFirst ? entity2 : entity1;
        Pokemon firstPokemon = entity1GoesFirst ? entity1ActivePokemon : entity2ActivePokemon;
        Pokemon secondPokemon = entity1GoesFirst ? entity2ActivePokemon : entity1ActivePokemon;
        PokemonMove firstMove = entity1GoesFirst ? entity1PendingMove : entity2PendingMove;
        PokemonMove secondMove = entity1GoesFirst ? entity2PendingMove : entity1PendingMove;

        processMove(firstEntity, secondEntity, firstPokemon, secondPokemon, firstMove);

        if (secondPokemon.getHp() > 0) {
            scheduler.schedule(
                    (Runnable) () -> processMove(secondEntity, firstEntity, secondPokemon, firstPokemon, secondMove), 5,
                    TimeUnit.SECONDS);
        }

        resetPendingMoves();
    }

    private void processMove(Entity attacker, Entity target, Pokemon attackerPokemon, Pokemon targetPokemon,
            PokemonMove move) {
        attackerPokemon.useMove(move.getId(), targetPokemon);
        BattleTurnEvent event = new BattleTurnEvent(attacker.entityID,
                target.entityID,
                move.getId());

        if (targetPokemon.getHp() == 0) {
            event.setBattleOver(attacker.entityID);
        }

        attacker.world.tickBattleTurnEvents.add(event);
    }

    private void resetPendingMoves() {
        entity1PendingMove = null;
        entity2PendingMove = null;
    }

    public Entity getEntity1() {
        return entity1;
    }

    public Entity getEntity2() {
        return entity2;
    }

    public void setEntity1ActivePokemon(Pokemon pokemon) {
        entity1ActivePokemon = pokemon;
    }

    public void setEntity2ActivePokemon(Pokemon pokemon) {
        entity2ActivePokemon = pokemon;
    }
}
