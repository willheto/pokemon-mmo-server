package com.g8e.gameserver.network.dataTransferModels;

import org.apache.commons.lang3.builder.EqualsBuilder;

import com.g8e.gameserver.enums.Direction;
import com.g8e.gameserver.models.Chunkable;
import com.g8e.gameserver.models.entities.Npc;
import com.g8e.gameserver.models.pokemon.Pokemon;

public class DTONpc implements Chunkable {

    // Entity fields
    public String entityID;
    public int entityIndex;
    public int worldX;
    public int worldY;
    public Direction nextTileDirection = null;
    public Direction facingDirection = Direction.DOWN;
    public String name;
    public int type;
    public int currentChunk;
    public Pokemon[] party;

    public DTONpc(Npc npc) {
        this.entityID = npc.entityID;
        this.entityIndex = npc.entityIndex;
        this.worldX = npc.worldX;
        this.worldY = npc.worldY;
        this.nextTileDirection = npc.nextTileDirection;
        this.facingDirection = npc.facingDirection;
        this.name = npc.name;
        this.type = npc.type;
        this.currentChunk = npc.currentChunk;
        this.party = npc.party;
    }

    public String getEntityID() {
        return this.entityID;
    }

    @Override
    public int getCurrentChunk() {
        return this.currentChunk;
    }

    @Override
    public boolean equals(Object obj) {

        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        DTONpc other = (DTONpc) obj;
        return new EqualsBuilder()
                .append(this.entityID, other.entityID)
                .append(this.entityIndex, other.entityIndex)
                .append(this.worldX, other.worldX)
                .append(this.worldY, other.worldY)
                .append(this.nextTileDirection, other.nextTileDirection)
                .append(this.facingDirection, other.facingDirection)
                .append(this.name, other.name)
                .append(this.type, other.type)
                .append(this.currentChunk, other.currentChunk)
                .append(this.party, other.party)
                .isEquals();

    }

}
