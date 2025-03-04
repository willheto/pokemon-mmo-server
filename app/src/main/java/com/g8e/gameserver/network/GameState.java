package com.g8e.gameserver.network;

import java.util.List;

import com.g8e.gameserver.models.ChatMessage;
import com.g8e.gameserver.models.Shop;
import com.g8e.gameserver.models.events.BattleEvent;
import com.g8e.gameserver.models.events.BattleTurnEvent;
import com.g8e.gameserver.models.events.SoundEvent;
import com.g8e.gameserver.models.events.TalkEvent;
import com.g8e.gameserver.models.events.TradeEvent;
import com.g8e.gameserver.models.events.WildBattleTurnEvent;
import com.g8e.gameserver.models.events.WildPokemonEvent;
import com.g8e.gameserver.models.objects.Item;
import com.g8e.gameserver.network.dataTransferModels.DTONpc;
import com.g8e.gameserver.network.dataTransferModels.DTOPlayer;

public class GameState {
    private List<TalkEvent> tickTalkEvents;
    public List<TradeEvent> tickTradeEvents;
    public List<SoundEvent> tickSoundEvents;
    public List<BattleEvent> tickBattleEvents;
    public List<BattleTurnEvent> tickBattleTurnEvents;
    public List<WildPokemonEvent> tickWildPokemonEvents;
    public List<WildBattleTurnEvent> tickWildBattleTurnEvents;
    private List<DTOPlayer> players;
    private List<DTONpc> npcs;
    private List<ChatMessage> chatMessages;
    private List<Item> items;
    private String playerID;
    private List<String> onlinePlayers;
    private Shop[] shops;

    public GameState(List<TalkEvent> tickTalkEvents,
            List<TradeEvent> tickTradeEvents,
            List<SoundEvent> tickSoundEvents,
            List<BattleEvent> tickBattleEvents,
            List<BattleTurnEvent> tickBattleTurnEvents,
            List<WildPokemonEvent> tickWildPokemonEvents,
            List<WildBattleTurnEvent> tickWildBattleTurnEvents,
            List<DTOPlayer> players,
            List<DTONpc> npcs,
            List<ChatMessage> chatMessages, List<Item> items, String playerID, List<String> onlinePlayers,
            Shop[] shops) {
        this.tickTalkEvents = tickTalkEvents;
        this.tickTradeEvents = tickTradeEvents;
        this.tickSoundEvents = tickSoundEvents;
        this.tickBattleEvents = tickBattleEvents;
        this.tickBattleTurnEvents = tickBattleTurnEvents;
        this.tickWildPokemonEvents = tickWildPokemonEvents;
        this.tickWildBattleTurnEvents = tickWildBattleTurnEvents;
        this.players = players;
        this.npcs = npcs;
        this.playerID = playerID;
        this.chatMessages = chatMessages;
        this.items = items;
        this.onlinePlayers = onlinePlayers;
        this.shops = shops;
    }

    public List<TradeEvent> getTickTradeEvents() {
        return tickTradeEvents;
    }

    public Shop[] getShops() {
        return shops;
    }

    public List<String> getOnlinePlayers() {
        return onlinePlayers;
    }

    public List<DTOPlayer> getPlayers() {
        return players;
    }

    public void setPlayers(List<DTOPlayer> players) {
        this.players = players;
    }

    public String getPlayerID() {
        return playerID;
    }

    public void setPlayerID(String playerID) {
        this.playerID = playerID;
    }

    public List<ChatMessage> getChatMessages() {
        return chatMessages;
    }

    public void setChatMessages(List<ChatMessage> chatMessages) {
        this.chatMessages = chatMessages;
    }

    public List<Item> getItems() {
        return items;
    }

    public List<TalkEvent> getTickTalkEvents() {
        return tickTalkEvents;
    }

    public void setTickTalkEvents(List<TalkEvent> tickTalkEvents) {
        this.tickTalkEvents = tickTalkEvents;
    }

    public List<SoundEvent> getTickSoundEvents() {
        return tickSoundEvents;
    }

    public void setTickSoundEvents(List<SoundEvent> tickSoundEvents) {
        this.tickSoundEvents = tickSoundEvents;
    }

    public List<DTONpc> getNpcs() {
        return npcs;
    }

    public void setNpcs(List<DTONpc> npcs) {
        this.npcs = npcs;
    }

    public List<BattleEvent> getTickBattleEvents() {
        return tickBattleEvents;
    }

    public List<BattleTurnEvent> getTickBattleTurnEvents() {
        return tickBattleTurnEvents;
    }

    public List<WildPokemonEvent> getTickWildPokemonEvents() {
        return tickWildPokemonEvents;
    }

    public List<WildBattleTurnEvent> getTickWildBattleTurnEvents() {
        return tickWildBattleTurnEvents;
    }
}