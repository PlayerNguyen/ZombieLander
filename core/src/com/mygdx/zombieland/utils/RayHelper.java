package com.mygdx.zombieland.utils;

import com.badlogic.gdx.Gdx;
import com.mygdx.zombieland.MapRenderer;
import com.mygdx.zombieland.World;
import com.mygdx.zombieland.entity.Entity;
import com.mygdx.zombieland.entity.projectile.Projectile;
import com.mygdx.zombieland.location.Location;
import com.mygdx.zombieland.location.Vector2D;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.CopyOnWriteArraySet;

public class RayHelper {

    @Deprecated
    public static Set<Entity> projectCollectionRay(Projectile projectile, float determineOffset, final World w) {
        Set<Entity> entities = new LinkedHashSet<>();
        Location location = new Location(projectile.getLocation());
        while (location.distance(projectile.getLocation()) <= projectile.getProjectileRange()) {
            // Linear iteration the location to determine entities
            location.add(projectile.getDirection());
            // Whether determine an entity
            for (Entity entity : projectile.getWorld().getEntities()) {
                if (entity.getLocation().distance(location) <= determineOffset) {
                    entities.add(entity);
                }
            }
        }
        return entities;
    }

    public static Collection<Entity> rayMap(final MapRenderer<Entity> entitiesMap,
                                                        final Location start,
                                                        final Location end) {
        System.out.printf("start = " + start);
        System.out.printf("; end = " + end);
        System.out.printf("; distance = " + end.distance(start));
        System.out.println();

        Collection<Entity> resultEntities = new HashSet<>();
        Location tempPosition = new Location(start);
        Vector2D opposite = new Vector2D(end.x - start.x, end.y - start.y);
        opposite.normalize();

        int positionTick = 0;
        while (positionTick <= 500) {
            // Update the temp position follows the vector
            tempPosition.add(opposite);
            positionTick++;
            if (tempPosition.x < 0
                    || tempPosition.y < 0
                    || tempPosition.x >= World.WINDOW_WIDTH
                    || tempPosition.y >= World.WINDOW_HEIGHT) {
                break;
            }
            // Any strike will catch all entities from current location
            System.out.println("x, y = "+(int)tempPosition.x + " " + (int)tempPosition.y);
            Set<Entity> entities = entitiesMap.get((int) tempPosition.x, (int) tempPosition.y);
            resultEntities.addAll(entities);
        }
        return resultEntities;
    }

}
