package com.g8e.gameserver.tile;

public class Tile {

    public boolean collision = false;
    public int numberRepresentation;

    public Tile(boolean collision, int numberRepresentation) {
        this.collision = collision;
        this.numberRepresentation = numberRepresentation;
    }

    public boolean isCollision() {
        return collision;
    }

    public void setCollision(boolean collision) {
        this.collision = collision;
    }

    public int getNumberRepresentation() {
        return numberRepresentation;
    }

    public void setNumberRepresentation(int numberRepresentation) {
        this.numberRepresentation = numberRepresentation;
    }

}
