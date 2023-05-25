package com.mygdx.zombieland.entity.undestructable;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.mygdx.zombieland.World;
import com.mygdx.zombieland.entity.Entity;
import com.mygdx.zombieland.location.Location;
import com.mygdx.zombieland.location.Vector2D;
import com.mygdx.zombieland.map.Map;
import com.mygdx.zombieland.utils.VisualizeHelper;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Set;

public class Fence implements Entity {
    public static final short FENCE_DEBUG_MAP_DENSITY = 8;
    private static final int SPRITE_SIZE = 64;

    private final Texture texture = new Texture("box.png");
    private Sprite sprite;
    private final World world;
    private final Location location;
    private final Collection<Set<Entity>> lastUpdatedChunks;

    public Fence(World world, Location location) {
        this.world = world;
        this.location = location;
        this.lastUpdatedChunks = new LinkedList<>();
    }

    @Override
    public void create() {
        this.sprite = new Sprite(texture);
        this.sprite.setSize(SPRITE_SIZE, SPRITE_SIZE);
        this.sprite.setOrigin((float) this.getSize() / 2, (float) this.getSize() / 2);
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
        }


        if (this.getLocation().x > 0
                && this.getLocation().y > 0
                && this.getLocation().x < World.WINDOW_WIDTH
                && this.getLocation().y < World.WINDOW_HEIGHT
        ) {
            int left = (int) (this.getLocation().x - SPRITE_SIZE / 2);
            int bottom = (int) (this.getLocation().y - SPRITE_SIZE / 2);
            int top = (int) (this.getLocation().y + SPRITE_SIZE / 2);
            int right = (int) (this.getLocation().x + SPRITE_SIZE / 2);

            for (int x = left; x < right; x++) {
                for (int y = bottom; y < top; y++) {
                    Set<Entity> currentChunk = this.getWorld()
                            .getEntitiesMap()
                            .get(x, y);
                    this.lastUpdatedChunks.add(currentChunk);
                    currentChunk.add(this);

                }
            }

            if (this.getWorld().isDebug()) {
                for (int x = left; x < right; x += FENCE_DEBUG_MAP_DENSITY) {
                    for (int y = bottom; y < top; y += FENCE_DEBUG_MAP_DENSITY) {
                        VisualizeHelper.simulateCircle(this.getWorld(), new Location(x, y), 1);
                    }
                }
                VisualizeHelper
                        .simulateText(this.getWorld(),
                                new Location(left - 6, bottom - 6),
                                String.format("%d, %d", left, bottom),
                                new Color(1, 1, 1, 1)
                        );
            }
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
