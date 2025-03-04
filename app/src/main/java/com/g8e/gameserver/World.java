package com.g8e.gameserver;

import java.util.ArrayList;
import java.util.List;

import org.java_websocket.WebSocket;
import org.java_websocket.exceptions.WebsocketNotConnectedException;

import com.g8e.gameserver.constants.NpcConstants;
import com.g8e.gameserver.managers.EntitiesManager;
import com.g8e.gameserver.managers.ItemsManager;
import com.g8e.gameserver.managers.PokemonMovesManager;
import com.g8e.gameserver.managers.ShopsManager;
import com.g8e.gameserver.models.ChatMessage;
import com.g8e.gameserver.models.entities.Entity;
import com.g8e.gameserver.models.entities.EntityData;
import com.g8e.gameserver.models.entities.Npc;
import com.g8e.gameserver.models.entities.Player;
import com.g8e.gameserver.models.events.BattleEvent;
import com.g8e.gameserver.models.events.BattleTurnEvent;
import com.g8e.gameserver.models.events.SoundEvent;
import com.g8e.gameserver.models.events.TalkEvent;
import com.g8e.gameserver.models.events.TradeEvent;
import com.g8e.gameserver.models.events.WildBattleTurnEvent;
import com.g8e.gameserver.models.events.WildPokemonEvent;
import com.g8e.gameserver.models.objects.Item;
import com.g8e.gameserver.network.GameState;
import com.g8e.gameserver.network.GameStateComparator;
import com.g8e.gameserver.network.WebSocketEventsHandler;
import com.g8e.gameserver.network.actions.Action;
import com.g8e.gameserver.network.compressing.Compress;
import com.g8e.gameserver.network.dataTransferModels.DTONpc;
import com.g8e.gameserver.network.dataTransferModels.DTOPlayer;
import com.g8e.gameserver.tile.TileManager;
import com.g8e.util.Logger;
import com.google.gson.Gson;

public class World {
    private static final int TICK_RATE = 200;
    public final int maxWorldCol = 200;
    public final int maxWorldRow = 200;
    public final int maxPlayers = 10;

    public WebSocketEventsHandler webSocketEventsHandler;
    public TileManager tileManager = new TileManager(this);
    public ItemsManager itemsManager = new ItemsManager(this);
    public ShopsManager shopsManager = new ShopsManager();
    public EntitiesManager entitiesManager = new EntitiesManager();
    public PokemonMovesManager pokemonMovesManager = new PokemonMovesManager();

    public List<Player> players = new ArrayList<>();
    public List<Npc> npcs = new ArrayList<>();

    public List<Item> items = new ArrayList<>();
    public List<ChatMessage> chatMessages = new ArrayList<>();
    public List<Action> actionQueue = new ArrayList<>();
    public List<TalkEvent> tickTalkEvents = new ArrayList<>();
    public List<TradeEvent> tickTradeEvents = new ArrayList<>();
    public List<SoundEvent> tickSoundEvents = new ArrayList<>();
    public List<BattleEvent> tickBattleEvents = new ArrayList<>();
    public List<BattleTurnEvent> tickBattleTurnEvents = new ArrayList<>();
    public List<WildPokemonEvent> tickWildPokemonEvents = new ArrayList<>();
    public List<WildBattleTurnEvent> tickWildBattleTurnEvents = new ArrayList<>();

    public GameState previousGameState;
    public WebSocket[] connections = new WebSocket[maxPlayers];
    public List<String> onlinePlayers = new ArrayList<>();

    public World() {
        this.setInitialNpcs();
    }

    public WebSocket[] getConnections() {
        return connections;
    }

    public List<String> getOnlinePlayers() {
        return onlinePlayers;
    }

    public void addConnection(WebSocket conn) {
        for (int i = 0; i < maxPlayers; i++) {
            if (connections[i] == null) {
                connections[i] = conn;
                onlinePlayers.add(conn.toString());
                break;
            }
        }
    }

    public void removeConnection(WebSocket conn) {
        for (int i = 0; i < maxPlayers; i++) {
            if (connections[i] == conn) {
                connections[i] = null;
                onlinePlayers.remove(conn.toString());
                break;
            }
        }
    }

    public void start() {
        while (true) {
            try {
                Thread.sleep(TICK_RATE);
                gameTick();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void addChatMessage(ChatMessage chatMessage) {
        this.chatMessages.add(chatMessage);
    }

    public List<ChatMessage> getChatMessages() {
        return chatMessages;
    }

    private void gameTick() {
        try {
            this.players.forEach(player -> {
                List<Action> playerActions = this.actionQueue.stream()
                        .filter(action -> action.getPlayerID().equals(player.entityID))
                        .toList();

                player.setTickActions(playerActions);
                player.update();
            });

            // Remove actions after processing all players to avoid concurrent modification
            // exception
            List<Action> actionsToRemove = this.players.stream()
                    .flatMap(player -> this.actionQueue.stream()
                            .filter(action -> action.getPlayerID().equals(player.entityID)))
                    .toList();
            this.actionQueue.removeAll(actionsToRemove);
            this.npcs.forEach(npc -> npc.update());
            itemsManager.updateDespawnTimers();
            sentGameStateToConnections();
        } catch (Exception e) {
            Logger.printError("Error in gameTick: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void sentGameStateToConnections() {
        GameState newGameState = getChangedGameState();

        for (WebSocket conn : connections) {
            if (conn != null) {
                Player player = this.players.stream().filter(p -> p.entityID.equals(conn.toString())).findFirst()
                        .orElse(null);

                if (player != null) {
                    List<DTONpc> npcs = this.npcs.stream().map(npc -> new DTONpc(npc)).toList();
                    List<DTOPlayer> players = this.players.stream().map(p -> new DTOPlayer(p)).toList();

                    newGameState.setNpcs(npcs);
                    newGameState.setPlayers(players);

                    // add current player if not in the list
                    if (players.stream().noneMatch(p -> p.getEntityID().equals(player.entityID))) {
                        players.add(new DTOPlayer(player));
                    }

                    String gameStateJson = new Gson().toJson(newGameState);
                    byte[] compressedData = Compress.compress(gameStateJson);

                    try {
                        conn.send(compressedData);
                    } catch (WebsocketNotConnectedException e) {
                        Logger.printInfo("Connection " + conn
                                + " is not connected, probably in combat and waiting to be logged out");
                    }

                }
            }
        }

        this.tickTalkEvents.clear();
        this.tickTradeEvents.clear();
        this.tickSoundEvents.clear();
        this.tickBattleEvents.clear();
        this.tickBattleTurnEvents.clear();
        this.tickWildPokemonEvents.clear();
        this.tickWildBattleTurnEvents.clear();
    }

    private GameState getChangedGameState() {
        List<DTOPlayer> dtoPlayers = this.players.stream().map(player -> new DTOPlayer(player)).toList();
        List<DTONpc> dtoNpcs = this.npcs.stream().map(npc -> new DTONpc(npc)).toList();

        GameState newGameState = new GameState(this.tickTalkEvents, this.tickTradeEvents,
                this.tickSoundEvents,
                this.tickBattleEvents,
                this.tickBattleTurnEvents,
                this.tickWildPokemonEvents,
                this.tickWildBattleTurnEvents,
                dtoPlayers, dtoNpcs,
                this.chatMessages,
                this.items, null, this.onlinePlayers, this.shopsManager.getShops());
        if (previousGameState == null) {
            previousGameState = newGameState;
            return newGameState;
        } else {
            GameState changedGameState = GameStateComparator.getChangedGameState(previousGameState, newGameState);
            previousGameState = newGameState;
            return changedGameState;
        }

    }

    public List<Item> getItems() {
        return items;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public List<Npc> getNpcs() {
        return npcs;
    }

    public void enqueueAction(Action action) {
        this.actionQueue.add(action);
    }

    public void addPlayer(Player player) {
        this.players.add(player);
    }

    public Entity getEntityByID(String entityID) {
        for (Player player : players) {
            if (entityID.equals(player.entityID)) {
                return player;
            }
        }

        for (Npc npc : npcs) {
            if (entityID.equals(npc.entityID)) {
                return npc;
            }
        }

        return null;
    }

    public Item getItemByID(String itemUniqueID) {
        Item item = null;
        for (Item i : items) {
            if (i != null && i.getUniqueID().equals(itemUniqueID)) {
                item = i;
                break;
            }
        }
        return item;
    }

    public void removePlayer(WebSocket conn) {
        String playerID = conn.toString();
        Player player = this.players.stream().filter(p -> p.entityID.equals(playerID)).findFirst().orElse(null);
        if (player != null) {
            this.players.remove(player);
            removeConnection(conn);
        }
    }

    private void setInitialNpcs() {
        addNpc(NpcConstants.RIVAL, 1000, 1000, 1);
    }

    private void addNpc(int index, int x, int y, int wanderRange) {
        EntityData entityData = entitiesManager.getEntityDataByIndex(index);
        Npc npc = new Npc(this, index, x, y, entityData.getName());
        this.npcs.add(npc);
        npc.setWanderRange(wanderRange);
    }
}
