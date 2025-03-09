package com.g8e.gameserver.network.dataTransferModels;

import java.util.List;
import java.util.Objects;

import com.g8e.gameserver.enums.Direction;
import com.g8e.gameserver.models.Chunkable;
import com.g8e.gameserver.models.entities.Player;
import com.g8e.gameserver.models.pokemon.Pokemon;
import com.g8e.gameserver.pathfinding.PathNode;

public class DTOPlayer implements Chunkable {

    public int storyProgress;

    // Entity fields
    public String entityID;
    public int entityIndex;
    public int worldX;
    public int worldY;
    public Direction nextTileDirection = null;
    public Direction facingDirection = Direction.DOWN;
    public String name;
    public int currentChunk;
    public List<PathNode> currentPath;
    public Pokemon[] party;
    public int[] inventory;
    public int[] inventoryAmounts;

    public DTOPlayer(Player player) {
        this.entityID = player.entityID;
        this.entityIndex = player.entityIndex;
        this.worldX = player.worldX;
        this.worldY = player.worldY;
        this.nextTileDirection = player.nextTileDirection;
        this.facingDirection = player.facingDirection;
        this.name = player.name;
        this.currentChunk = player.currentChunk;
        this.currentPath = player.currentPath;
        this.party = player.party;
        this.inventory = player.inventory;
        this.inventoryAmounts = player.inventoryAmounts;
        this.storyProgress = player.storyProgress;
    }

    @Override
    public int getCurrentChunk() {
        return this.currentChunk;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;

        DTOPlayer other = (DTOPlayer) obj;

        return Objects.equals(this.entityID, other.entityID) &&
                this.entityIndex == other.entityIndex &&
                this.worldX == other.worldX &&
                this.worldY == other.worldY &&
                Objects.equals(this.nextTileDirection, other.nextTileDirection) &&
                Objects.equals(this.facingDirection, other.facingDirection) &&
                Objects.equals(this.name, other.name) &&
                this.currentChunk == other.currentChunk &&
                Objects.equals(this.currentPath, other.currentPath) &&
                Objects.equals(this.party, other.party) &&
                Objects.equals(this.inventory, other.inventory) &&
                Objects.equals(this.inventoryAmounts, other.inventoryAmounts) &&
                this.storyProgress == other.storyProgress;
    }

    public String getEntityID() {
        return entityID;
    }

}
