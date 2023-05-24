package com.mygdx.zombieland.setting;

public final class GameSetting {
    public static GameSetting instance;
    private float musicSoundLevel = 1f;
    private float vfxSoundLevel = 0.4f;
    private float hudVisibleLevel = 0.6f;


    private GameSetting() {}

    public static GameSetting getInstance() {
        if(instance == null){
            instance = new GameSetting();
        }
        return instance;
    }

    public float getMusicSoundLevel() {
        return musicSoundLevel;
    }

    public void setMusicSoundLevel(float musicSoundLevel) {
        this.musicSoundLevel = musicSoundLevel;
    }

    public float getVfxSoundLevel() {
        return vfxSoundLevel;
    }

    public void setVfxSoundLevel(float vfxSoundLevel) {
        this.vfxSoundLevel = vfxSoundLevel;
    }

    public float getHudVisibleLevel() {
        return hudVisibleLevel;
    }

    public void setHudVisibleLevel(float hudVisibleLevel) {
        this.hudVisibleLevel = hudVisibleLevel;
    }
}
