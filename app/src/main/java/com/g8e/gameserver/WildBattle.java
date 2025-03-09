package com.g8e.gameserver;

import java.util.ArrayList;
import java.util.List;

import com.g8e.gameserver.enums.BattleOption;
import com.g8e.gameserver.managers.PokemonMovesManager;
import com.g8e.gameserver.managers.PokemonsManager;
import com.g8e.gameserver.models.entities.Entity;
import com.g8e.gameserver.models.entities.Player;
import com.g8e.gameserver.models.events.WildBattleTurnEvent;
import com.g8e.gameserver.models.pokemon.Pokemon;
import com.g8e.gameserver.models.pokemon.PokemonData;
import com.g8e.gameserver.models.pokemon.PokemonMove;
import com.g8e.util.Logger;

public class WildBattle {
    private final Player entity;
    private final Pokemon wildPokemon;
    private Pokemon entityActivePokemon;

    private PokemonMove entityPendingMove;
    private BattleOption entityPendingAction;
    private Integer pendingItemId;
    private Integer pendingPokemonIndex;

    private final PokemonsManager pokemonsManager = new PokemonsManager();
    private final PokemonMovesManager pokemonsMovesManager = new PokemonMovesManager();

    final private List<Pokemon> participants = new ArrayList<>();

    public WildBattle(Player entity, Pokemon wildPokemon) {
        this.entity = entity;
        this.wildPokemon = wildPokemon;
        // get first healthy pokemon
        Pokemon firstHealthyPokemonInParty = null;
        for (Pokemon pokemon : entity.party) {
            if (pokemon == null) {
                continue;
            }
            if (pokemon.getHp() > 0) {
                firstHealthyPokemonInParty = pokemon;
                break;
            }
        }
        this.entityActivePokemon = firstHealthyPokemonInParty;
        this.participants.add(entityActivePokemon);
    }

    public void setEntityPendingAction(BattleOption action, Integer value) {
        this.entityPendingAction = action;

        switch (action) {
            case FIGHT -> {
                if (value == null) {
                    return;
                }
                this.entityPendingMove = pokemonsMovesManager.getPokemonMoveById(value);
            }
            case ITEM -> this.pendingItemId = this.entity.inventory[value];
            case POKEMON -> this.pendingPokemonIndex = value;
            case RUN -> Logger.printDebug("Run action set");
        }
        executeTurn();
    }

    private void executeTurn() {
        if (entityActivePokemon == null || entityPendingAction == null) {
            return;
        }
        if (entityPendingAction == BattleOption.ITEM) {
            useItem();
        } else if (entityPendingAction == BattleOption.RUN) {
            attemptRun();
        } else if (entityPendingAction == BattleOption.POKEMON) {
            switchPokemon();
        } else {
            switch (entityPendingAction) {
                case FIGHT -> executeFightTurn();
                case ITEM -> useItem();
            }
        }
        resetPendingActions();
    }

    private void executeFightTurn() {
        PokemonData entityData = pokemonsManager.getPokemonDataByIndex(entityActivePokemon.getId());
        PokemonData wildPokemonData = pokemonsManager.getPokemonDataByIndex(wildPokemon.getId());

        boolean entityGoesFirst = entityData.getSpeed() > wildPokemonData.getSpeed();

        Pokemon firstPokemon = entityGoesFirst ? entityActivePokemon : wildPokemon;
        Pokemon secondPokemon = entityGoesFirst ? wildPokemon : entityActivePokemon;
        int randomMove = wildPokemon.getRandomMove();
        PokemonMove firstMove = entityGoesFirst ? entityPendingMove
                : pokemonsMovesManager.getPokemonMoveById(randomMove);
        PokemonMove secondMove = entityGoesFirst ? pokemonsMovesManager.getPokemonMoveById(randomMove)
                : entityPendingMove;

        processMove(entityGoesFirst, firstMove, firstPokemon, secondPokemon);

        if (secondPokemon.getHp() > 0) {
            processMove(!entityGoesFirst, secondMove, secondPokemon, firstPokemon);
        }
    }

    private void processMove(boolean isPlayersMove, PokemonMove move, Pokemon attackerPokemon, Pokemon targetPokemon) {
        // Updated to use the refactored WildBattleTurnEvent constructor
        WildBattleTurnEvent event = new WildBattleTurnEvent(
                this.entity.entityID,
                move.getId(),
                isPlayersMove);

        int effect = attackerPokemon.useMove(move.getId(), targetPokemon);

        switch (effect) {
            case 0 -> event.setEffect(0);
            case 1 -> event.setEffect(1);
            case -1 -> event.setEffect(-1);
            default -> {
            }
        }

        if (targetPokemon.getHp() == 0) {

            event.setBattleOver(isPlayersMove);
            if (!isPlayersMove) {

                // Check if any of the player's Pokémon are still alive after the opponent's
                // move
                boolean isAnyPokemonAlive = false;
                for (Pokemon pokemon : entity.party) {
                    if (pokemon == null) {
                        continue;
                    }
                    if (pokemon.getHp() > 0) {
                        isAnyPokemonAlive = true;
                        break;
                    }
                }

                if (!isAnyPokemonAlive) {

                    event.setHp(targetPokemon.getHp());

                    event.isAllPokemonsFainted = true;
                    entity.world.tickWildBattleTurnEvents.add(event);
                    entity.faint();
                    return;
                }
            } else {
                // add xp to all participants
                for (Pokemon participant : participants) {
                    participant.addXp(targetPokemon, false, participants.size());
                }
            }
        }

        event.setHp(targetPokemon.getHp());

        entity.world.tickWildBattleTurnEvents.add(event);
    }

    private void useItem() {
        // Now use the generic constructor for item usage
        WildBattleTurnEvent event = new WildBattleTurnEvent(
                this.entity.entityID,
                pendingItemId,
                true,
                BattleOption.ITEM);

        if (this.pendingItemId == 1) {
            int isCaught = this.wildPokemon.tryCatch(pendingItemId);
            if (isCaught == 0 || isCaught == 1) {
                event.isCaught = true;
                event.isBattleOver = true;

                int freePartySlot = entity.getFreePartySlot();
                this.entity.party[freePartySlot] = wildPokemon;
                wildPokemon.saveUsersNewPokemon(entity.playerID);
            }
        }

        // Add event for item usage
        entity.world.tickWildBattleTurnEvents.add(event);

        if (event.isCaught) {
            entity.battle = null;
        } else {
            int randomMove = wildPokemon.getRandomMove();
            processMove(false, pokemonsMovesManager.getPokemonMoveById(randomMove), wildPokemon,
                    entityActivePokemon);
        }
    }

    private void switchPokemon() {
        // Now use the generic constructor for switching Pokémon
        WildBattleTurnEvent event = new WildBattleTurnEvent(
                this.entity.entityID,
                pendingPokemonIndex,
                true,
                BattleOption.POKEMON);

        this.entityActivePokemon = entity.party[pendingPokemonIndex];
        // Add the new Pokémon to the list of participants if it is not already there
        if (!participants.contains(entityActivePokemon)) {
            participants.add(entityActivePokemon);
        }

        // Add event for Pokémon switch
        entity.world.tickWildBattleTurnEvents.add(event);
        int randomMove = wildPokemon.getRandomMove();
        processMove(false, pokemonsMovesManager.getPokemonMoveById(randomMove), wildPokemon,
                this.entityActivePokemon);
    }

    private void attemptRun() {

        boolean runSuccessful = Math.random() < (wildPokemon.getSpeed() * 255.0 / entityActivePokemon.getSpeed());
        WildBattleTurnEvent event = new WildBattleTurnEvent(
                this.entity.entityID,
                true,
                runSuccessful

        );

        entity.world.tickWildBattleTurnEvents.add(event);

        if (runSuccessful) {
            entity.battle = null;
        } else {
            // Schedule enemy move after failed run attempt
            int randomMove = wildPokemon.getRandomMove();

            processMove(false, pokemonsMovesManager.getPokemonMoveById(randomMove), wildPokemon,
                    entityActivePokemon);
        }

    }

    private void resetPendingActions() {
        entityPendingMove = null;
        entityPendingAction = null;
        pendingItemId = null;
        pendingPokemonIndex = null;
    }

    public Entity getEntity() {
        return entity;
    }

    public void setEntityActivePokemon(Pokemon pokemon) {
        entityActivePokemon = pokemon;
    }
}
