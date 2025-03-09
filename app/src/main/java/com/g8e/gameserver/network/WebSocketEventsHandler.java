package com.g8e.gameserver.network;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.java_websocket.WebSocket;

import com.g8e.db.CommonQueries;
import com.g8e.db.models.DBAccount;
import com.g8e.db.models.DBPlayer;
import com.g8e.db.models.DBPokemon;
import com.g8e.gameserver.World;
import com.g8e.gameserver.models.ChatMessage;
import com.g8e.gameserver.models.entities.Player;
import com.g8e.gameserver.models.events.BattleEvent;
import com.g8e.gameserver.models.events.BattleTurnEvent;
import com.g8e.gameserver.models.events.SoundEvent;
import com.g8e.gameserver.models.events.TalkEvent;
import com.g8e.gameserver.models.events.TradeEvent;
import com.g8e.gameserver.models.events.WildBattleTurnEvent;
import com.g8e.gameserver.models.events.WildPokemonEvent;
import com.g8e.gameserver.models.pokemon.Pokemon;
import com.g8e.gameserver.network.actions.Action;
import com.g8e.gameserver.network.actions.chat.ChatMessageAction;
import com.g8e.gameserver.network.actions.move.BattleAction;
import com.g8e.gameserver.network.actions.move.ChallengePlayerAction;
import com.g8e.gameserver.network.actions.move.ForceNpcBattlePlayerAction;
import com.g8e.gameserver.network.actions.move.PlayerMove;
import com.g8e.gameserver.network.actions.move.PlayerMoveData;
import com.g8e.gameserver.network.actions.move.PlayerTakeMoveAction;
import com.g8e.gameserver.network.actions.move.PlayerTalkMoveAction;
import com.g8e.gameserver.network.actions.move.StartBattleAction;
import com.g8e.gameserver.network.actions.shop.BuyItemAction;
import com.g8e.gameserver.network.actions.shop.SellItemAction;
import com.g8e.gameserver.network.actions.shop.TradeMoveAction;
import com.g8e.gameserver.network.actions.story.UpdateStoryProgressAction;
import com.g8e.gameserver.network.compressing.Compress;
import com.g8e.gameserver.network.dataTransferModels.DTONpc;
import com.g8e.gameserver.network.dataTransferModels.DTOPlayer;
import com.g8e.util.Logger;
import com.google.gson.Gson;

public class WebSocketEventsHandler {
    private final World world;

    public WebSocketEventsHandler(World world) {
        this.world = world;

    }

    public void handleConnection(WebSocket conn, Map<String, String> queryParams) {
        String loginToken = queryParams.get("loginToken");
        if (loginToken == null) {
            Logger.printError("Player connected without login token");
            conn.close();
            return;
        }

        DBAccount account;
        try {
            account = CommonQueries.getAccountByLoginToken(loginToken);
            if (account == null) {
                Logger.printError("Player connected with invalid login token");
                conn.close();
                return;
            }

            DBPlayer player;

            player = CommonQueries.getPlayerByAccountId(account.getAccountId());

            if (player == null) {
                Logger.printError("Player not found");
                conn.close();
                return;
            }

            world.addConnection(conn);

            String uniquePlayerID = conn.toString();

            Player playerToBeAdded = new Player(this.world, player, uniquePlayerID, account.getUsername(),
                    account.getAccountId());

            world.addPlayer(playerToBeAdded);

            int playerID = player.getPlayerID();

            DBPokemon[] party = (DBPokemon[]) CommonQueries.getPlayersPartyByPlayerId(playerID);

            for (DBPokemon dbPokemon : party) {
                if (dbPokemon != null) {
                    Pokemon pokemon = new Pokemon(dbPokemon);
                    playerToBeAdded.addPokemonToParty(pokemon);
                }
            }

            // Add players party

            addDefaultChatMessages(playerToBeAdded.name);

            List<DTOPlayer> players = this.world.players.stream().map(DTOPlayer::new).toList();
            List<DTONpc> npcs = this.world.npcs.stream().map(DTONpc::new).toList();
            List<TalkEvent> talkEvents = new ArrayList<>();
            List<TradeEvent> tradeEvents = new ArrayList<>();
            List<SoundEvent> soundEvents = new ArrayList<>();
            List<BattleEvent> battleEvents = new ArrayList<>();
            List<BattleTurnEvent> battleTurnEvents = new ArrayList<>();
            List<WildPokemonEvent> wildPokemonEvents = new ArrayList<>();
            List<WildBattleTurnEvent> wildBattleTurnEvents = new ArrayList<>();

            GameState gameState = new GameState(talkEvents, tradeEvents,
                    soundEvents,
                    battleEvents,
                    battleTurnEvents,
                    wildPokemonEvents,
                    wildBattleTurnEvents,
                    players,
                    npcs,
                    world.getChatMessages(),
                    world.getItems(),
                    conn.toString(),
                    world.getOnlinePlayers(),
                    world.shopsManager.getShops());

            String gameStateJson = new Gson().toJson(gameState);
            byte[] compressedData = Compress.compress(gameStateJson);

            conn.send(compressedData);

        } catch (SQLException e) {
            Logger.printError(loginToken + " failed to connect to the game server");
            e.printStackTrace();
        }
    }

    private void addDefaultChatMessages(String name) {
        ChatMessage welcomeMessage = new ChatMessage(name, "Welcome to Pokemon online!",
                System.currentTimeMillis(),
                false);

        world.addChatMessage(welcomeMessage);
    }

    public void handleMessage(WebSocket conn, String message) {
        Gson gson = new Gson();
        Action parsedMessage = gson.fromJson(message, Action.class);
        String action = parsedMessage.getAction();
        String playerID = parsedMessage.getPlayerID();

        switch (action) {
            case "logOut" -> {
                this.world.removePlayer(conn);
                conn.close();
            }
            case "ping" -> conn.send("pong");

            case "playerMove" -> {
                PlayerMove playerMoveAction = gson.fromJson(message, PlayerMove.class);
                int x = playerMoveAction.getX();
                int y = playerMoveAction.getY();

                this.world.enqueueAction(new PlayerMove(playerID, new PlayerMoveData(x, y)));
            }

            case "playerTalkMove" -> {
                PlayerTalkMoveAction playerTalkMoveAction = gson.fromJson(message,
                        PlayerTalkMoveAction.class);
                this.world.enqueueAction(playerTalkMoveAction);
            }

            case "chatMessage" -> {
                ChatMessageAction chatMessage = gson.fromJson(message, ChatMessageAction.class);
                Player player = this.world.players.stream().filter(p -> p.entityID.equals(playerID)).findFirst().get();
                String senderName = player != null ? player.name : "";
                ChatMessage chatMessageModel = new ChatMessage(senderName, chatMessage.getMessage(),
                        chatMessage.getTimeSent(), chatMessage.isGlobal());
                this.world.addChatMessage(chatMessageModel);
            }
            case "buyItem" -> {
                BuyItemAction buyItemAction = gson.fromJson(message, BuyItemAction.class);
                this.world.enqueueAction(buyItemAction);
            }

            case "sellItem" -> {
                SellItemAction sellItemAction = gson.fromJson(message, SellItemAction.class);
                this.world.enqueueAction(sellItemAction);
            }
            case "playerTakeMove" -> {
                PlayerTakeMoveAction playerTakeMoveAction = gson.fromJson(message,
                        PlayerTakeMoveAction.class);
                this.world.enqueueAction(playerTakeMoveAction);
            }
            case "tradeMove" -> {
                TradeMoveAction tradeMoveAction = gson.fromJson(message, TradeMoveAction.class);
                this.world.enqueueAction(tradeMoveAction);
            }
            case "forceNpcBattlePlayer" -> {
                ForceNpcBattlePlayerAction forceNpcAttackPlayer = gson.fromJson(message,
                        ForceNpcBattlePlayerAction.class);
                this.world.enqueueAction(forceNpcAttackPlayer);
            }
            case "challengePlayer" -> {
                ChallengePlayerAction challengePlayer = gson.fromJson(message,
                        ChallengePlayerAction.class);
                this.world.enqueueAction(challengePlayer);
            }

            case "startBattle" -> {
                StartBattleAction startBattle = gson.fromJson(message,
                        StartBattleAction.class);
                this.world.enqueueAction(startBattle);
            }

            case "battleAction" -> {
                BattleAction battleAction = gson.fromJson(message, BattleAction.class);
                this.world.enqueueAction(battleAction);
            }

            case "updateStoryProgress" -> {
                UpdateStoryProgressAction updateStoryProgressAction = gson.fromJson(message,
                        UpdateStoryProgressAction.class);
                this.world.enqueueAction(updateStoryProgressAction);
            }

            default -> {
            }
        }

    }

}
