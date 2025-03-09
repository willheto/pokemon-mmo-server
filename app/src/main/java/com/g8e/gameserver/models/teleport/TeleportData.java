package com.g8e.gameserver.models.teleport;

import com.g8e.gameserver.enums.Direction;
import com.g8e.gameserver.tile.TilePosition;

public class TeleportData {

    private String description;
    private TilePosition[] triggerTiles;
    private TilePosition destinationTile;
    private String sfx;
    private Direction facingDirectionAfterTeleport;

    public TeleportData(String description, TilePosition[] triggerTiles, TilePosition destinationTile, String sfx,
            Direction facingDirectionAfterTeleport) {
        this.description = description;
        this.triggerTiles = triggerTiles;
        this.destinationTile = destinationTile;
        this.sfx = sfx;
        this.facingDirectionAfterTeleport = facingDirectionAfterTeleport;
    }

    public String getDescription() {
        return description;
    }

    public TilePosition[] getTriggerTiles() {
        return triggerTiles;
    }

    public TilePosition getDestinationTile() {
        return destinationTile;
    }

    public String getSfx() {
        return sfx;
    }

    public Direction getFacingDirectionAfterTeleport() {
        return facingDirectionAfterTeleport;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setTriggerTiles(TilePosition[] triggerTiles) {
        this.triggerTiles = triggerTiles;
    }

    public void setDestinationTile(TilePosition destinationTile) {
        this.destinationTile = destinationTile;
    }

    public void setSfx(String sfx) {
        this.sfx = sfx;
    }

    public void setFacingDirectionAfterTeleport(Direction facingDirectionAfterTeleport) {
        this.facingDirectionAfterTeleport = facingDirectionAfterTeleport;
    }

    public int getDestinationX() {
        return destinationTile.getX();
    }

    public int getDestinationY() {
        return destinationTile.getY();
    }

}
