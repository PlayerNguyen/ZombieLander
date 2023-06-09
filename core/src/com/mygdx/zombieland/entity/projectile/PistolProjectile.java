package com.mygdx.zombieland.entity.projectile;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.mygdx.zombieland.World;
import com.mygdx.zombieland.entity.Entity;
import com.mygdx.zombieland.location.Location;
import com.mygdx.zombieland.location.Vector2D;
import com.mygdx.zombieland.runnable.DamageEntityOnShootRunnable;
import com.mygdx.zombieland.utils.MathHelper;
import com.mygdx.zombieland.utils.RayHelper;
import com.mygdx.zombieland.utils.VisualizeHelper;
import com.mygdx.zombieland.weapon.Gun;

import java.util.Collection;
import java.util.Set;

public class PistolProjectile extends AbstractProjectile {

    public static final Texture PISTOL_TEXTURE = new Texture(Gdx.files.internal("pistol_bullet.png"));

    public PistolProjectile(World world, ProjectileSource source) {
        super(world, source, PISTOL_TEXTURE, 20);

        // Set recoil
        Gun gunSourceWeapon = (Gun) source.getWeapon();
        this.getDirection().x += MathHelper.nextDouble(-gunSourceWeapon.getRecoil(), gunSourceWeapon.getRecoil());
        this.getDirection().y += MathHelper.nextDouble(-gunSourceWeapon.getRecoil(), gunSourceWeapon.getRecoil());
    }

    @Override
    public void create() {
        this.getSprite().setSize(8, 5);
        this.getSprite().setOrigin(4, 2.5f);
        this.getSprite().scale(3);
        this.setRotation(this.getProjectileSource().getAngle());

        Gdx.app.log("Projectile", "Generated projectile @" + System.identityHashCode(this));

        // Project a straight-forward ray from player location to estimate triggered entity
        final Location destinationLocation = new Location(
                this.getLocation()
        ).add(new Vector2D(this.getDirection()).scalar(500));

        Collection<Entity> entitiesFromMap = RayHelper.rayMap(getWorld().getEntitiesMap(), getLocation(), destinationLocation);
        System.out.println("entities on the ray = " + entitiesFromMap);
//         Damage entities from the list above
        getWorld().getScheduler().runTaskAfter(new DamageEntityOnShootRunnable(this,
                        getDamage(),
                        entitiesFromMap),
                20);
    }

    @Override
    public void render() {
        super.render();

        // Remove the projectile if is out of distance
        if (this.getProjectileSource().getLocation().distance(this.getLocation()) > getProjectileRange() || isHit()) {
            if (this.getWorld().removeProjectile(this)) {
                Gdx.app.log("Projectile", "Removed projectile @" + System.identityHashCode(this));
            }
            return;
        }

        // Render projectile location
        this.getSprite().setPosition(this.getLocation().x, this.getLocation().y);
        this.getSprite().setRotation(this.getRotation());
        // Update bullet location from direction
        this.getLocation().x = (float) (this.getLocation().x + (this.getDirection().x * Gdx.graphics.getDeltaTime() * 2000));
        this.getLocation().y = (float) (this.getLocation().y + (this.getDirection().y * Gdx.graphics.getDeltaTime() * 2000));

        // Draw the texture when it's far away from the player
        if (this.getLocation().distance(this.getProjectileSource().getLocation()) >=
                this.getWorld().getPlayer().getSize() + 32) {
//            this.getSprite().draw(this.getWorld().getBatch());
        }

        // Check collision
        for (final Entity entity : this.getWorld().getEntities()) {
            Location entityLocation = new Location(entity.getLocation());

            // Hit the entity
            if (entityLocation.distance(this.getLocation()) <= (float) entity.getSize() / 2F) {
                Gdx.app.log("Triggered", "Hit to entity ...");
                // Set triggered
                setHit(true);
            }
        }

        Location destinationLocation = new Location(
                this.getLocation()
        ).add(new Vector2D(this.getDirection()).scalar(500));

        VisualizeHelper.simulateLine(this.getWorld(), new Location(this.getLocation()), destinationLocation, Color.YELLOW);
    }

    @Override
    public void dispose() {
//        this.getDirection().set(0, 0);
//        this.getTexture().dispose();
    }

    @Override
    public float getProjectileRange() {
        return 1000;
    }

    @Override
    public void setProjectileRange(float projectileRange) {
//        this.projectileRange = projectileRange;
    }
}
