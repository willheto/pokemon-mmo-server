package com.g8e.gameserver.models.events;

public class SoundEvent {
    public String soundName;
    public boolean isSfx = true;
    public boolean shouldInterrupt = false;
    public String entityID;
    public boolean isGlobal = false; // 10x10 area

    public SoundEvent(String soundName, boolean isSfx, boolean shouldInterrupt, String entityID, boolean isGlobal) {
        this.soundName = soundName;
        this.isSfx = isSfx;
        this.shouldInterrupt = shouldInterrupt;
        this.entityID = entityID;
        this.isGlobal = isGlobal;
    }

}
