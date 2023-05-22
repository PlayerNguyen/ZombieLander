package com.mygdx.zombieland;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

public class ZombieLandMain extends ApplicationAdapter {
    public static final String gameVersion = "1.0.0";

    private SpriteBatch batch;

    public ZombieLandMain() {
    }

    @Override
    public void create() {
        this.batch = new SpriteBatch();
    }

    @Override
    public void render() {
        ScreenUtils.clear(0.96078431372f, 0.96078431372f, 0.96078431372f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


    }

    public SpriteBatch getBatch() {
        return batch;
    }

    @Override
    public void dispose() {
        batch.dispose();
    }
}
