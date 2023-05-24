package com.mygdx.zombieland;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Window;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3WindowListener;

class ApplicationClass {
    private static final int frameWidth = 800;
    private static final int frameHeight = 600;

    void startApplication() {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        ZombieLandMain zombieLandMain = new ZombieLandMain();
        config.useVsync(true);
        config.setResizable(false);
        config.setForegroundFPS(60);
        config.setTitle(String.format("ZombieLand [ %s ]", ZombieLandMain.gameVersion));
        config.setWindowedMode(frameWidth, frameHeight);

        config.setWindowListener(new Lwjgl3WindowListener() {
            @Override
            public void created(Lwjgl3Window window) {

            }

            @Override
            public void iconified(boolean isIconified) {

            }

            @Override
            public void maximized(boolean isMaximized) {

            }

            @Override
            public void focusLost() {

            }

            @Override
            public void focusGained() {

            }

            @Override
            public boolean closeRequested() {
                FrameClass frame = new FrameClass();
                frame.createFrame();
                return true;
            }

            @Override
            public void filesDropped(String[] files) {

            }

            @Override
            public void refreshRequested() {

            }
        });

       new Lwjgl3Application(zombieLandMain, config);
    }
}