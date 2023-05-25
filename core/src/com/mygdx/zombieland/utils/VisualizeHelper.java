package com.mygdx.zombieland.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.mygdx.zombieland.World;
import com.mygdx.zombieland.entity.Entity;
import com.mygdx.zombieland.location.Location;
import com.mygdx.zombieland.location.Vector2D;
import sun.jvm.hotspot.utilities.IntegerEnum;

import java.util.Map;

public class VisualizeHelper {

    public static void simulateLine(World world, Location location, Location location2, Color color) {
        world.getBatch().end();

        world.getShapeRenderer().begin(ShapeRenderer.ShapeType.Line);
        world.getShapeRenderer().setColor(color);

        world.getShapeRenderer().line(location.x, location.y, location2.x, location2.y);
        world.getShapeRenderer().end();

        world.getBatch().begin();
    }

    public static void simulateLine(World world, Location location, Location location2) {
        simulateLine(world, location, location2, Color.BLACK);
    }

    public static void simulateDirection(World world, Entity entity, Color color) {
        Location offsetLocation = new Location(
                entity.getLocation()
        ).add(new Vector2D(entity.getDirection()).scalar(100));
        simulateLine(world, entity.getLocation(), offsetLocation, color);
    }

    public static void simulateVector(World world, Location start, Vector2D vector2D, Color color) {
        Location offsetLocation = new Location(start).add(vector2D);
        simulateLine(world, start, offsetLocation, color);
    }

    public static void simulateDirection(World world, Entity entity) {
        simulateDirection(world, entity, Color.RED);
    }

    public static void simulateBox(World world, Location originLocation, float size, Color color) {
        world.getBatch().end();

        world.getShapeRenderer().begin(ShapeRenderer.ShapeType.Line);
        world.getShapeRenderer().setColor(color);

        world.getShapeRenderer().rect(originLocation.x, originLocation.y, size, size);
        world.getShapeRenderer().end();

        world.getBatch().begin();
    }

    public static void simulateBox(World world, Location originLocation, float size) {
        world.getBatch().end();

        world.getShapeRenderer().begin(ShapeRenderer.ShapeType.Line);
        world.getShapeRenderer().setColor(Color.BLACK);

        world.getShapeRenderer().rect(originLocation.x, originLocation.y, size, size);
        world.getShapeRenderer().end();

        world.getBatch().begin();
    }

    public static void simulateBox(World world, Entity entity) {
        world.getBatch().end();
        // Draw center location
        world.getShapeRenderer().begin(ShapeRenderer.ShapeType.Line);
        world.getShapeRenderer().setColor(Color.BLACK);

        world.getShapeRenderer().rect(entity.getCenterLocation().x,
                entity.getCenterLocation().y,
                entity.getSize(),
                entity.getSize());

        // Draw a red (location) location
        world.getShapeRenderer().setColor(Color.RED);

        world.getShapeRenderer().rect(entity.getLocation().x,
                entity.getLocation().y,
                entity.getSize(),
                entity.getSize());
        world.getShapeRenderer().end();

        world.getShapeRenderer().begin(ShapeRenderer.ShapeType.Filled);
        world.getShapeRenderer().circle(entity.getLocation().x,
                entity.getLocation().y,
                8);
        world.getShapeRenderer().end();

        world.getBatch().begin();
    }

    public static void simulateCircle(World world, Location location, float radius) {
        simulateCircle(world, location, radius, Color.BLACK);
    }

    public static void simulateCircle(World world, Location location, float radius, Color color) {
        world.getBatch().end();
        world.getShapeRenderer().begin(ShapeRenderer.ShapeType.Filled);
        world.getShapeRenderer().setColor(color);
        world.getShapeRenderer().circle(location.x,
                location.y,
                radius);
        world.getShapeRenderer().end();

        world.getBatch().begin();
    }

    public static void simulateText(World world, Location location, String text, Color c) {
        world.getFont().setColor(c);
        world.getFont().draw(world.getBatch(),
                text,
                location.x,
                location.y
        );
    }

    public static void visualizeEntityRealtimeMap(Entity entity, int density) {
        if (!(entity.getLocation().x > 0)
                || !(entity.getLocation().y > 0)
                || !(entity.getLocation().x < World.WINDOW_WIDTH)
                || !(entity.getLocation().y < World.WINDOW_HEIGHT)
        ) {
            return;
        }

        EntityMask entityMask = entity.getWorld().getEntityMask(entity);
        for (int x = entityMask.getLeft(); x < entityMask.getRight(); x += density) {
            for (int y = entityMask.getBottom(); y < entityMask.getTop(); y += density) {
                VisualizeHelper.simulateCircle(entity.getWorld(), new Location(x, y), 1);
            }
        }
        VisualizeHelper
                .simulateText(entity.getWorld(),
                        new Location(entityMask.getLeft() - 6, entityMask.getBottom() - 6),
                        String.format("%d, %d", entityMask.getLeft(), entityMask.getBottom()),
                        new Color(1, 1, 1, 1)
                );
    }

    public static void drawFastMatrix(World w, FastMatrix<?> fastMatrix, int density, float radius, Color color) {
        for (Map.Entry<FastMatrix.Key, ?> keyEntry : fastMatrix.entrySet()) {
            FastMatrix.Key key = keyEntry.getKey();
            int x = key.x;
            int y = key.y;
            if (x % density == 0 && y % density == 0) {
                VisualizeHelper.simulateCircle(w, new Location(x, y), radius, color);
            }
        }
    }
}
