package com.mygdx.zombieland.entity.enemy;

import com.badlogic.gdx.Gdx;
// <<<<<<< master
// import com.badlogic.gdx.graphics.Texture;
// import com.badlogic.gdx.graphics.g2d.Sprite;
// import com.mygdx.zombieland.World;
// import com.mygdx.zombieland.entity.Damageable;
// import com.mygdx.zombieland.entity.Entity;
// import com.mygdx.zombieland.location.Location;
// import com.mygdx.zombieland.location.Vector2D;
// import com.mygdx.zombieland.state.GameState;
// =======
// import com.badlogic.gdx.graphics.Color;
// import com.badlogic.gdx.graphics.Texture;
// import com.badlogic.gdx.graphics.g2d.Sprite;
// import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
// import com.mygdx.zombieland.World;
// import com.mygdx.zombieland.entity.Damageable;
// import com.mygdx.zombieland.entity.Entity;
// import com.mygdx.zombieland.entity.undestructable.Fence;
// import com.mygdx.zombieland.location.Location;
// import com.mygdx.zombieland.location.Vector2D;
// import com.mygdx.zombieland.map.Map;
// import com.mygdx.zombieland.state.GameState;
// import com.mygdx.zombieland.utils.Rectangle;
// >>>>>>> feat/zombie-move

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.mygdx.zombieland.World;
import com.mygdx.zombieland.effects.TextIndicator;
import com.mygdx.zombieland.entity.Damageable;
import com.mygdx.zombieland.entity.Entity;
import com.mygdx.zombieland.entity.Player;
import com.mygdx.zombieland.entity.undestructable.Fence;
import com.mygdx.zombieland.location.Location;
import com.mygdx.zombieland.location.Vector2D;
import com.mygdx.zombieland.map.Map;
import com.mygdx.zombieland.state.GameState;
import com.mygdx.zombieland.utils.Rectangle;
import com.mygdx.zombieland.utils.VisualizeHelper;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Zombie extends EnemyAbstract {

    public static final int ZOMBIE_SIZE = 64;
    //  public static final float ZOMBIE_MOVEMENT_SPEED = 30f; // Each type has different speed

    public static final long ZOMBIE_HIT_DURATION = 2000;

    private final World world;
    private final Entity target;
    private final ZombieType type;

    private Location destination;
    private float fraction = 1;
    private float speed; // Zombie movement speed

    private long lastHit = 0;
    private TextIndicator.TextItem currentTextItem = null;

    private Set<Entity> lastMovedBlock;

    public Zombie(World world, Location startLocation, Entity target, ZombieType type) {
        super(startLocation, new Vector2D(), null, null, type.getHealth());

        this.world = world;
        this.destination = new Location(this.getLocation());
        this.target = target;
        this.type = type;

        // Set texture later
        Texture texture = this.getType().getTexture();
        this.setSprite(new Sprite(texture));
        this.setTexture(texture);

        // Set zombie speed
        this.speed = this.type.getSpeed();

        // Set angle
        this.rotateToTarget();
        // Set direction to the target
        this.getDirection().x = Math.sin(this.getRotation());
        this.getDirection().y = -Math.cos(this.getRotation());

    }

    @Override
    public void create() {
//        this.getSprite().setTexture(ZOMBIE_TEXTURE);
        this.getSprite().setSize(ZOMBIE_SIZE, ZOMBIE_SIZE);
        this.getSprite().setOrigin((float) ZOMBIE_SIZE / 2, (float) ZOMBIE_SIZE / 2);

        this.rotateToTarget();
        this.updateMove();
    }

// <<<<<<< master
//     @Override
//     public void render() {

//         if (this.world.getGameState().equals(GameState.PLAYING)) {
//             this.updateMove();

//             // Update lerp
//             if (fraction < 1) {
//                 fraction += Gdx.graphics.getDeltaTime() * speed ;
//                 this.getLocation().x += (this.destination.x - this.getLocation().x) * fraction;
//                 this.getLocation().y += (this.destination.y - this.getLocation().y) * fraction;
//             }

//             this.getLocation().add(
//                     this.getDirection().x * Gdx.graphics.getDeltaTime() * speed * (this.getWorld().isDebug() ? 5 : 1),
//                     this.getDirection().y * Gdx.graphics.getDeltaTime() * speed * (this.getWorld().isDebug() ? 5 : 1)
//             );
//         }
// =======
//     private int reactionRate=0;
//     @Override
//     public void render() {
//         if (this.world.getGameState().equals(GameState.PLAYING)) {
//             this.updateMove();
//         }


    // >>>>>>> feat/zombie-move
    private int reactionRate = 0;

    @Override
    public void render() {

        // Put the zombie onto the map
        if (this.lastMovedBlock != null) {
            this.lastMovedBlock.remove(this);
            System.out.println(this.lastMovedBlock);
        }

        if (this.getLocation().x > 0 && this.getLocation().y > 0) {
            HashSet<Entity> currentBlock = this.getWorld()
                    .getEntitiesMap()
                    .get((int) this.getLocation().x, (int) this.getLocation().y);
            currentBlock.add(this);
            this.lastMovedBlock = currentBlock;
            System.out.println(this.lastMovedBlock);
        }


        if (this.world.getGameState().equals(GameState.PLAYING)) {
            this.updateMove();
        }

        // Export (render) image
        this.getSprite().setRotation(this.getRotation());
        this.getSprite().setPosition(this.getLocation().x - 32, this.getLocation().y - 32);
        this.getSprite().draw(world.getBatch());

        // Debug
        if (this.getWorld().isDebug()) {
            VisualizeHelper.simulateBox(this.getWorld(), this);
            VisualizeHelper.simulateDirection(this.getWorld(), this);
        }

        boolean ok = true;
        for (Entity entity : this.world.getEntities()) {
            if (!(entity instanceof Fence) && !(entity instanceof Player)) {
                continue;
            }

            Rectangle rectangle = new Rectangle(this.getCenterLocation(), ZOMBIE_SIZE, ZOMBIE_SIZE);
            if (!rectangle.isCollided(new Rectangle(entity.getCenterLocation(), entity.getSize(), entity.getSize()))) {
                // pass
                continue;
            }

            System.out.println("colision");
            ok = false;
            break;
        }
        if (ok && reactionRate == 0) {
            this.translate((float) this.getDirection().x * speed * Gdx.graphics.getDeltaTime(),
                    (float) this.getDirection().y * speed * Gdx.graphics.getDeltaTime());
            rotateToTarget();
        } else {
            this.translate(0, 1);
            reactionRate++;
            reactionRate %= Gdx.graphics.getDeltaTime() + 20;
        }
    }


    public void translate(float x, float y) {
        this.getLocation().add(x, y);
    }

    private void rotateToTarget() {
        Location cur = this.getLocation();
        Location targetLocation = this.target.getLocation();
        Location temp = new Location(targetLocation.x, targetLocation.y);

        float atan2 = (float) Math.atan2(temp.y - cur.y, temp.x - cur.x);
        float atan2AsDegree = atan2 * MathUtils.radiansToDegrees;

        this.setRotation(atan2AsDegree);
    }

    @Override
    public void dispose() {
        this.getTexture().dispose();
    }


    @Override
    public Location lerp(Location moveTo, float speed) {
        this.fraction = 0;
        this.destination = new Location(moveTo.x, moveTo.y);
        // This velocity for lerp,
        return this.destination;

    }

    private void updateMove() {
        // Set direction to the target
        this.getDirection().x = Math.cos(Math.toRadians(this.getRotation()));
        this.getDirection().y = Math.sin(Math.toRadians(this.getRotation()));

        // Update rotation to target
        this.rotateToTarget();

        // Update speed
        this.setSpeed(this.getType().getSpeed());

        // Hit player when get close
        if (this.target.getLocation().distance(this.getLocation()) <= (float) this.target.getSize() / 2) {
            if (System.currentTimeMillis() - ZOMBIE_HIT_DURATION >= lastHit || lastHit == 0) {
                this.attack();
                ((Damageable) this.target).damage(this, this.getType().getDamage());
                this.lastHit = System.currentTimeMillis();
            }
        }
    }

    @Override
    public World getWorld() {
        return world;
    }

    @Override
    public int getSize() {
        return ZOMBIE_SIZE;
    }

    private void attack() {
        // Set attack texture
        this.getSprite().setTexture(this.getType().getAttackTexture());
        this.world.getScheduler().runTaskAfter(new Runnable() {
            @Override
            public void run() {
                getSprite().setTexture(getType().getTexture());
            }
        }, 200);
    }

    public ZombieType getType() {
        return type;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public float getSpeed() {
        return speed;
    }

    @Override
    public void kill() {
        super.kill();

        this.lastMovedBlock.remove(this);
    }
}
