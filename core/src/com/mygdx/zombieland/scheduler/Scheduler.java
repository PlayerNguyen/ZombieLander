package com.mygdx.zombieland.scheduler;

import com.badlogic.gdx.Gdx;

public class Scheduler {

    public void runTaskAfter(final Runnable runnable, final long afterMillis) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                long last = System.currentTimeMillis();
                while (System.currentTimeMillis() < last + afterMillis) {}
                Gdx.app.postRunnable(runnable);
            }
        }).start();


    }

}
