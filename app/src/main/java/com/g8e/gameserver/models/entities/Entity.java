package com.g8e.gameserver.models.entities;

import java.util.List;

import com.g8e.gameserver.Battle;
import com.g8e.gameserver.WildBattle;
import com.g8e.gameserver.World;
import com.g8e.gameserver.enums.Direction;
import com.g8e.gameserver.models.ChatMessage;
import com.g8e.gameserver.models.Chunkable;
import com.g8e.gameserver.models.events.WildPokemonEvent;
import com.g8e.gameserver.models.pokemon.Pokemon;
import com.g8e.gameserver.models.teleport.TeleportData;
import com.g8e.gameserver.pathfinding.AStar;
import com.g8e.gameserver.pathfinding.PathNode;
import com.g8e.gameserver.tile.Tile;
import com.g8e.gameserver.tile.TilePosition;

public abstract class Entity implements Chunkable {
    public String entityID;
    public int entityIndex;
    public transient World world;
    public transient AStar pathFinder;
    public String targetedEntityID = null;

    public transient int originalWorldX;
    public transient int originalWorldY;
    public int worldX;
    public int worldY;

    public TilePosition targetTile = null;
    public TilePosition newTargetTile = null;
    public Direction nextTileDirection = null;

    public String name;
    public int type; // 0 = player, 1 = npc, 2 = monster

    protected int tickCounter = 0;
    public String targetItemID = null;
    public Direction facingDirection = Direction.DOWN;

    public List<PathNode> currentPath;
    protected TilePosition targetEntityLastPosition;
    protected Integer goalAction = null; // 1 talk, 2 attack, 3 trade

    public int currentChunk;

    public int wanderRange = 5;
    public int interactionRange = 1;
    public String interactionTargetID = null;

    public Pokemon[] party = new Pokemon[6];
    public String battleTargetID = null;

    public Battle battle = null;
    public WildBattle wildBattle = null;
    public int[] inventory = new int[20];
    public int[] inventoryAmounts = new int[20];

    public Entity(String entityID, int entityIndex, World world, int worldX, int worldY, String name, int type) {
        this.entityID = entityID;
        this.world = world;
        this.originalWorldX = worldX;
        this.originalWorldY = worldY;
        this.worldX = worldX;
        this.worldY = worldY;
        this.name = name;
        this.type = type;
        this.entityIndex = entityIndex;
        this.pathFinder = new AStar(world);
        this.party = new Pokemon[6];
    }

    public abstract void update();

    @Override
    public int getCurrentChunk() {
        return this.currentChunk;
    }

    protected void setNewTargetTileWithinWanderArea() {
        int targetX = this.originalWorldX + (int) (Math.random() * (this.wanderRange * 2 + 1)
                - this.wanderRange);

        int targetY = this.originalWorldY + (int) (Math.random() * (this.wanderRange * 2 + 1)
                - this.wanderRange);

        // Adjust to always target the bottom-left tile of the 16x16 group
        int adjustedX = (targetX / 2) * 2; // Align to leftmost tile in 16x16 grid
        int adjustedY = (targetY / 2) * 2 + 1; // Align to bottom tile in 16x16 grid
        // Check if the new target tile is walkable
        boolean collision = this.world.tileManager.getCollisionByXandY(adjustedX, adjustedY);
        if (!collision) {
            this.newTargetTile = new TilePosition(adjustedX, adjustedY);
        }
    }

    protected boolean isTargetTileNotWithinWanderArea() {
        TilePosition targetTile = this.getTarget();
        if (targetTile == null) {
            return false;
        }

        return Math.abs(targetTile.x - this.originalWorldX) > this.wanderRange
                || Math.abs(targetTile.y - this.originalWorldY) > this.wanderRange;

    }

    private TilePosition getPositionOneTileAwayFromTarget(TilePosition target) {
        // Create an array of tile offsets around the target
        TilePosition[] tilesAroundTarget = new TilePosition[] {
                new TilePosition(target.x, target.y - 1), // Up
                new TilePosition(target.x + 1, target.y), // Right
                new TilePosition(target.x, target.y + 1), // Down
                new TilePosition(target.x - 1, target.y), // Left
        };

        TilePosition closestTile = null;
        double minDistance = Double.MAX_VALUE;

        // Check each tile and calculate its distance from the current position
        for (TilePosition tile : tilesAroundTarget) {
            Tile currentTile = this.world.tileManager.getTileByXandY(tile.x, tile.y);

            if (currentTile != null && !currentTile.collision) {
                // Calculate Euclidean distance to the current position
                double distance = Math.sqrt(Math.pow(tile.x - this.worldX, 2) + Math.pow(tile.y - this.worldY, 2));

                if (distance < minDistance) {
                    minDistance = distance;
                    closestTile = tile;
                }
                if (distance == minDistance) {
                    // might as well pick a random one if they are the same distance
                    if (Math.random() > 0.5) {

                        minDistance = distance;
                        closestTile = tile;
                    }
                }
            }
        }

        return closestTile;
    }

    protected Direction getDirectionTowardsTile(int entityX, int entityY) {
        if (entityX < this.worldX) {
            return Direction.LEFT;
        } else if (entityX > this.worldX) {
            return Direction.RIGHT;
        } else if (entityY < this.worldY) {
            return Direction.UP;
        } else {
            return Direction.DOWN;
        }

    }

    protected void moveToNextTile() {
        if (this.nextTileDirection == null) {
            return;
        }
        switch (this.nextTileDirection) {
            case UP -> move(this.worldX, this.worldY - 1);
            case DOWN -> move(this.worldX, this.worldY + 1);
            case LEFT -> move(this.worldX - 1, this.worldY);
            case RIGHT -> move(this.worldX + 1, this.worldY);
            default -> {
            }
        }

    }

    protected void moveTowardsTarget() {

        if (this.nextTileDirection != null) {
            moveToNextTile();
        }

        TilePosition target = this.getTarget();

        if (target == null) {
            return;
        }

        if (this.newTargetTile != null) {

            currentPath = this.pathFinder.findPath(this.worldX, this.worldY, target.x, target.y);

            if (currentPath.size() == 0) {
                this.world.chatMessages
                        .add(new ChatMessage(name, "I can't reach that!", System.currentTimeMillis(), false));
            }

            if (currentPath.size() < 2) {
                currentPath = null;
                this.newTargetTile = null;
                this.targetTile = null;
                this.nextTileDirection = null;
                return;
            }
            this.targetTile = newTargetTile;
            this.newTargetTile = null;

            int deltaX = currentPath.get(1).x - this.worldX;
            int deltaY = currentPath.get(1).y - this.worldY;
            Direction nextTileDirection = this.getDirection(deltaX, deltaY);
            this.nextTileDirection = nextTileDirection;
            this.facingDirection = nextTileDirection;

            return;
        }

        if (currentPath == null || currentPath.size() == 0) {
            this.world.chatMessages
                    .add(new ChatMessage(name, "I can't reach that!", System.currentTimeMillis(), false));
            return;
        }

        // Already at target
        if (currentPath.size() == 1) {
            this.nextTileDirection = null;
            this.targetTile = null;
            this.newTargetTile = null;
            return;
        }

        // Last step
        if (currentPath.size() == 2) {
            PathNode nextStep = currentPath.get(1);
            moveAlongPath(nextStep);

        } else if (currentPath.size() > 2) {
            PathNode nextStep = currentPath.get(1);
            PathNode nextNextStep = currentPath.get(2);
            moveAlongPath(nextStep, nextNextStep);
        }

    }

    protected void moveAlongPath(PathNode nextStep, PathNode nextNextStep) {
        /* move(nextStep.x, nextStep.y); */

        Direction nextTileDirection = this.getDirection(nextNextStep.x - nextStep.x, nextNextStep.y - nextStep.y);
        this.nextTileDirection = nextTileDirection;
        this.facingDirection = nextTileDirection;
        currentPath.remove(0);
    }

    // Last step
    protected void moveAlongPath(PathNode nextStep) {
        this.nextTileDirection = null;

        /* move(nextStep.x, nextStep.y); */
        this.targetTile = null;
        currentPath = null;
    }

    // Always use move instead of explicitly setting worldX and worldY
    // This will ensure that the chunk is updated correctly
    protected void move(int worldX, int worldY) {
        this.worldX = worldX;
        this.worldY = worldY;
        if (this instanceof Player player) {
            TilePosition newTile = new TilePosition(worldX, worldY);
            TeleportData teleport = this.world.teleportsManager.checkTeleportTriggers(newTile);

            if (teleport != null) {
                player.worldX = teleport.getDestinationX();
                player.worldY = teleport.getDestinationY();
                player.facingDirection = teleport.getFacingDirectionAfterTeleport();
                player.currentPath = null;
                player.targetTile = null;
                player.newTargetTile = null;
                player.nextTileDirection = null;
            }

            player.savePosition();

        }
        updateChunk();

        if (this instanceof Player) {
            boolean canWildPokemonAppearOnCurrentTile = this.world.tileManager.canWildPokemonAppearOnTile(this.worldX,
                    this.worldY);

            // 1/16 roll
            if (canWildPokemonAppearOnCurrentTile && Math.random() < 0.0625) {
                WildPokemonEvent wildPokemonEvent = new WildPokemonEvent(this.entityID, 29);
                wildPokemonEvent.rollRandomPokemon();
                this.world.tickWildPokemonEvents.add(wildPokemonEvent);
                WildBattle battle = new WildBattle((Player) this, wildPokemonEvent.wildPokemon);
                this.wildBattle = battle;
                this.currentPath = null;
                this.targetTile = null;
                this.newTargetTile = null;
                this.nextTileDirection = null;

            }
        }

    }

    private void updateChunk() {
        int chunk = this.world.tileManager.getChunkByWorldXandY(this.worldX, this.worldY);
        if (this.currentChunk != chunk) {
            this.currentChunk = chunk;

            if (this instanceof Player player) {
                player.needsFullChunkUpdate = true;
            }
        }
    }

    protected Direction getDirection(int deltaX, int deltaY) {
        if (deltaX == 0 && deltaY == 0) {
            return null;
        }

        if (deltaX == 0 && deltaY == -1) {
            return Direction.UP;
        }

        if (deltaX == 0 && deltaY == 1) {
            return Direction.DOWN;
        }

        if (deltaX == -1 && deltaY == 0) {
            return Direction.LEFT;
        }

        if (deltaX == 1 && deltaY == 0) {
            return Direction.RIGHT;
        }

        return null;
    }

    protected TilePosition getTarget() {
        return this.newTargetTile != null ? this.newTargetTile : this.targetTile;
    }

    public void setWanderRange(int wanderRange) {
        this.wanderRange = wanderRange;
    }

    public void setInteractionRange(int interactionRange) {
        this.interactionRange = interactionRange;
    }

    public int getFreePartySlot() {
        for (int i = 0; i < this.party.length; i++) {
            if (this.party[i] == null) {
                return i;
            }
        }
        return -1;
    }

}
