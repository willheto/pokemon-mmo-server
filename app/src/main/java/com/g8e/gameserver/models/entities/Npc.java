package com.g8e.gameserver.models.entities;

import com.g8e.gameserver.World;
import com.g8e.gameserver.enums.Direction;

public class Npc extends Entity {
    public Npc(World world, int entityIndex, int worldX, int worldY, String name) {
        super("npc" + (int) (Math.random() * 1000000), entityIndex, world, worldX, worldY, name, 1);

    }

    @Override
    public void update() {
        this.updateCounters();

        if (interactionTargetID != null) {
            Entity entity = this.world.getEntityByID(interactionTargetID);
            if (entity == null) {
                interactionTargetID = null;
                return;
            }

            int entityX = entity.worldX;
            int entityY = entity.worldY;

            int interactionRange = this.interactionRange * 2; // Adjust for 16px movement

            // Ensure interaction range aligns with 16x16 movement
            if (entityX < this.worldX - interactionRange || entityX > this.worldX + interactionRange
                    || entityY < this.worldY - interactionRange || entityY > this.worldY + interactionRange) {
                interactionTargetID = null;
                return;
            }

            // Face the target based on 16x16 movement grid
            if (entityX < this.worldX) {
                this.facingDirection = Direction.LEFT;
            } else if (entityX > this.worldX) {
                this.facingDirection = Direction.RIGHT;
            } else if (entityY < this.worldY) {
                this.facingDirection = Direction.UP;
            } else if (entityY > this.worldY) {
                this.facingDirection = Direction.DOWN;
            }

            this.targetTile = null;
            this.newTargetTile = null;
            this.nextTileDirection = null;
        }

        // 20% chance to set new target
        if (Math.random() < 0.05 && this.interactionTargetID == null) {
            this.setNewTargetTileWithinWanderArea();
        }

        if (this.isTargetTileNotWithinWanderArea()) {
            this.setNewTargetTileWithinWanderArea();
        }

        this.moveTowardsTarget();

    }

    private void updateCounters() {

    }

    @Override
    public int getCurrentChunk() {
        return this.currentChunk;
    }

}
