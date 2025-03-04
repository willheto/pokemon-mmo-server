package com.g8e.gameserver.models.spells;

public class Spell {
    private int spellID;
    private String name;
    private int type;
    private int level_req;
    private Integer targetX;
    private Integer targetY;
    private Integer maxHit;

    public Spell(int spellID, String name, int type, int level_req, Integer targetX, Integer targetY, Integer maxHit) {
        this.spellID = spellID;
        this.name = name;
        this.type = type;
        this.level_req = level_req;
        this.targetX = targetX;
        this.targetY = targetY;
        this.maxHit = maxHit;
    }

    public Integer getMaxHit() {
        return maxHit;
    }

    public int getSpellID() {
        return spellID;
    }

    public String getName() {
        return name;
    }

    public int getType() {
        return type;
    }

    public int getLevelRequirement() {
        return level_req;
    }

    public Integer getTargetX() {
        return targetX;
    }

    public Integer getTargetY() {
        return targetY;
    }

}
