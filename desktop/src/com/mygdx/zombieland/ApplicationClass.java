package com.mygdx.zombieland;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

public final class ApplicationClass {
    private static ApplicationClass instance;
    private static final int FRAME_WIDTH = 800;
    private static final int FRAME_HEIGHT = 600;
    private final Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
    private final ZombieLandMain zombieLandMain = new ZombieLandMain();

    private ApplicationClass(){
        config.useVsync(true);
        config.setResizable(false);
        config.setForegroundFPS(60);
        config.setTitle(String.format("ZombieLand [ %s ]", ZombieLandMain.gameVersion));
        config.setWindowedMode(FRAME_WIDTH, FRAME_HEIGHT);
        config.setInitialVisible(true);


    }

    public static ApplicationClass getInstance(){
        if(instance == null){
            instance = new ApplicationClass();
        }
        return instance;
    }

    public void startApplication() {
        new Lwjgl3Application(zombieLandMain, config);

    }

    public void setGameVisible(boolean state){
        config.setInitialVisible(state);
    }
}
