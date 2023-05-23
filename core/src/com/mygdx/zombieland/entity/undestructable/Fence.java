package com.mygdx.zombieland.entity.undestructable;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.mygdx.zombieland.World;
import com.mygdx.zombieland.entity.Entity;
import com.mygdx.zombieland.location.Location;
import com.mygdx.zombieland.location.Vector2D;
import com.mygdx.zombieland.map.Map;
import com.mygdx.zombieland.utils.VisualizeHelper;

public class Fence implements Entity {
    private static final int SPRITE_SIZE = 64;

    private final Texture texture = new Texture("box.png");
    private Sprite sprite;
    private final World world;
    private final Location location;
    public Fence(World world, Location location) {
        this.world = world;
        this.location = location;


        // mapping the position of the obj to the array
        int x = (int) (location.x);
        int y= (int) (location.y);
        for(int i=x; i<x+SPRITE_SIZE; i++) {
            for(int j=y; j<y+SPRITE_SIZE; j++) {
                Map.getInstance().setArr(i, j , 1);
            }
        }
    }

    @Override
    public void create() {
        this.sprite = new Sprite(texture);
        this.sprite.setSize(SPRITE_SIZE, SPRITE_SIZE);
        this.sprite.setOrigin(this.getSize()/2,this.getSize()/2);


    }

    @Override
    public void render() {
        this.getLocation().add(
                this.getDirection().x,
                this.getDirection().y
        );
        this.getSprite().setPosition(this.getCenterLocation().x, this.getCenterLocation().y);
        sprite.draw(this.world.getBatch());
        if (this.getWorld().isDebug()) {
            VisualizeHelper.simulateBox(this.getWorld(), this);
        }
    }

    @Override
    public void dispose() {
        this.texture.dispose();
    }

    @Override
    public Location getLocation() {
        return location;
    }

    @Override
    public Vector2D getDirection() {
        return new Vector2D(0,0);
    }

    @Override
    public Texture getTexture() {
        return texture;
    }

    @Override
    public Sprite getSprite() {
        return sprite;
    }

    @Override
    public Location lerp(Location moveTo, float speed) {
        throw new UnsupportedOperationException("Player cannot be moved");
    }

    @Override
    public float getRotation() {
        return 0;
    }

    @Override
    public void setRotation(float rotation) {
    }

    @Override
    public World getWorld() {
        return world;
    }

    @Override
    public int getSize() {
        return SPRITE_SIZE;
    }

    @Override
    public Location getCenterLocation() {
        return new Location(this.getLocation().x - ((float) this.getSize() / 2)
                , this.getLocation().y - ((float) this.getSize() / 2));
    }
}
