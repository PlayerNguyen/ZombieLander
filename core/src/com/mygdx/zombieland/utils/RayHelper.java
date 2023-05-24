package com.mygdx.zombieland.utils;

import com.mygdx.zombieland.World;
import com.mygdx.zombieland.entity.Entity;
import com.mygdx.zombieland.entity.projectile.Projectile;
import com.mygdx.zombieland.location.Location;

import java.util.LinkedHashSet;
import java.util.Set;

public class RayHelper {

    public static Set<Entity> projectCollectionRay(Projectile projectile, float determineOffset, World w) {
        Set<Entity> entities = new LinkedHashSet<>();
        Location location = new Location(projectile.getLocation());
        while (location.distance(projectile.getLocation()) <= projectile.getProjectileRange()) {
            // Linear iteration the location to determine entities
            location.add(projectile.getDirection());
            VisualizeHelper.simulateCircle(w, location, 0.2F);
            // Whether determine an entity
            for (Entity entity : projectile.getWorld().getEntities()) {
                if (entity.getLocation().distance(location) <= determineOffset) {
                    entities.add(entity);
                }
            }
        }
        return entities;
    }

}
