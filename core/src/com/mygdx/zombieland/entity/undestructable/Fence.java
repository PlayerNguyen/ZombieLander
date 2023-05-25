package com.mygdx.zombieland.entity.undestructable;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.mygdx.zombieland.World;
import com.mygdx.zombieland.entity.Entity;
import com.mygdx.zombieland.location.Location;
import com.mygdx.zombieland.location.Vector2D;
import com.mygdx.zombieland.map.Map;
import com.mygdx.zombieland.utils.EntityMask;
import com.mygdx.zombieland.utils.VisualizeHelper;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

public class Fence implements Entity {
    public static final short FENCE_DEBUG_MAP_DENSITY = 8;
    private static final int SPRITE_SIZE = 64;

    private final Texture texture = new Texture("box.png");
    private Sprite sprite;
    private final World world;
    private final Location location;
//    private final Collection<Set<Entity>> lastUpdatedChunks;

    public Fence(World world, Location location) {
        this.world = world;
        this.location = location;
    }

    @Override
    public void create() {
        this.sprite = new Sprite(texture);
        this.sprite.setSize(SPRITE_SIZE, SPRITE_SIZE);
        this.sprite.setOrigin((float) this.getSize() / 2, (float) this.getSize() / 2);

        // Only update the fence once
        this.getWorld().updateEntityMaskPosition(this);
        EntityMask entityMask = this.getWorld().getEntityMask(this);
        for (int x = entityMask.getLeft()-32; x < entityMask.getRight() + 32; x++) {
            for (int y = entityMask.getBottom() -32; y < entityMask.getTop() + 32; y++) {
                this.getWorld().setBlockMoveAtPosition(x, y, true);
            }
        }

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
            VisualizeHelper.simulateBox(this.getWorld(), this.getCenterLocation(), SPRITE_SIZE);
            VisualizeHelper.visualizeEntityRealtimeMap(this, FENCE_DEBUG_MAP_DENSITY);
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
        return new Vector2D(0, 0);
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
