package com.mygdx.zombieland.entity.enemy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.mygdx.zombieland.World;
import com.mygdx.zombieland.entity.Damageable;
import com.mygdx.zombieland.entity.Entity;
import com.mygdx.zombieland.entity.Player;
import com.mygdx.zombieland.entity.undestructable.Fence;
import com.mygdx.zombieland.location.Location;
import com.mygdx.zombieland.location.Vector2D;
import com.mygdx.zombieland.state.GameState;
import com.mygdx.zombieland.utils.*;

import java.awt.*;
import java.util.*;
import java.util.List;

public class Zombie extends EnemyAbstract {

    public static final int ZOMBIE_SIZE = 64;
    //  public static final float ZOMBIE_MOVEMENT_SPEED = 30f; // Each type has different speed

    public static final long ZOMBIE_HIT_DURATION = 2000;
    public static final short ZOMBIE_DEBUG_MAP_DENSITY = 8; // Only change if needed [1 - ZOMBIE_SIZE]
    public static final short ZOMBIE_DEBUG_VISITED_MAP_DENSITY = 12;
    private static final long ZOMBIE_PATH_UPDATE_DURATION = 500;

    private final World world;
    private final Entity target;
    private final ZombieType type;

    private Location destination;
    private float speed; // Zombie movement speed

    private long lastHit = 0;

    private final List<Set<Entity>> lastUpdateChunks = new ArrayList<>();
    private long lastPathUpdate;
    //    boolean[][] visited = new boolean[801][601];
//    private final FastMatrix<Boolean> visited = new FastMatrix<>();
    private final FastMatrix<Vector2D> paths = new FastMatrix<>();

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
        this.updateMove();
    }

    @Override
    public void render() {
        // Export (render) image
        this.getSprite().setRotation(this.getRotation());
        this.getSprite().setPosition(this.getLocation().x - 32, this.getLocation().y - 32);
        this.getSprite().draw(world.getBatch());

        // update move ================================================================================================================================
        if (this.world.getGameState().equals(GameState.PLAYING)) {
            this.updateMove();
        }
        // =============================================================================================================================================

        // Clear old position values
        for (Set<Entity> lastUpdateChunkSet : this.lastUpdateChunks) {
           if (lastUpdateChunkSet != null) {
               lastUpdateChunkSet.remove(this);
           }
        }
        this.lastUpdateChunks.clear();
        // Put the zombie onto the map
        this.lastUpdateChunks.addAll(this.getWorld().updateEntityMaskPosition(this));

        // Debug
        if (this.getWorld().isDebug()) {
            VisualizeHelper.simulateBox(this.getWorld(), this);
            VisualizeHelper.simulateDirection(this.getWorld(), this);
            VisualizeHelper.visualizeEntityRealtimeMap(this, ZOMBIE_DEBUG_MAP_DENSITY);

            // Draw visited predict to target nodes
            VisualizeHelper.drawFastMatrix(this.getWorld(), this.paths, ZOMBIE_DEBUG_MAP_DENSITY, 1F, Color.YELLOW);
        }
    }



    static enum Direction {
        TOP_LEFT, BOTTOM_LEFT, TOP_RIGHT, BOTTOM_RIGHT
    }

    private Direction predictDirection(Location start, Location end) {
        double deltaX = end.x - start.x;
        double deltaY = end.y - start.y;
        double degrees = Math.atan2(deltaY, deltaX) * 180 / Math.PI;
        if (degrees < 0.0) {
            degrees += 360.0;
        }

        if (degrees >= 0 && degrees < 90) {
            return Direction.TOP_RIGHT;
        }
        if (degrees >= 90 && degrees < 180) {
            return Direction.TOP_LEFT;
        }
        if (degrees >= 180 && degrees < 270) {
            return Direction.BOTTOM_LEFT;
        }
        return Direction.BOTTOM_RIGHT;
    }

    public void findTarget(Location start, Location end) {
        // bfs

        this.paths.clear();
        Queue<CoordinateHelper.Coordinate> cq = new ArrayDeque<>();
        cq.add(new CoordinateHelper.Coordinate(start));
        this.paths.set(Math.round(start.x), Math.round(start.y), new Vector2D(0, 0));
        boolean ok= false;

        while (!cq.isEmpty()) {
            CoordinateHelper.Coordinate curCoordinate = cq.poll();
            curCoordinate = new CoordinateHelper.Coordinate(Math.round(curCoordinate.x), Math.round(curCoordinate.y));

            if (Math.abs(curCoordinate.x - end.x) < 1 && Math.abs(curCoordinate.y - end.y) < 1){
                ok = true;
                break;
            }
            if (new Location(curCoordinate.x, curCoordinate.y).distance(end) < 1) break;

            int[] xMap = new int[]{0, 0, 1, 1, 1, -1, -1, -1};
            int[] yMap = new int[]{1, -1, 0, 1, -1, 0, 1, -1};

            for (int i = 0; i < xMap.length; i++) {
                int x = Math.round(curCoordinate.x + xMap[i] * speed * Gdx.graphics.getDeltaTime());
                int y = Math.round(curCoordinate.y + yMap[i] * speed * Gdx.graphics.getDeltaTime());

                if (x < 0 || y < 0) continue;
                if (x >= World.WINDOW_WIDTH || y >= World.WINDOW_HEIGHT) continue;

                // add blocking way here
                if (!allowMove(new CoordinateHelper.Coordinate(x, y))) {
                    continue;
                }

                // collided fence
                if (isCollidedFence(new Location(x, y))) {
                    continue;
                }

                // chua visit
                if (!paths.contains(x, y)) {
                    paths.set(x, y, new Vector2D(xMap[i], yMap[i]));
                    cq.add(new CoordinateHelper.Coordinate(x, y));
                }
            }
        }

        // backtracking find path =========================================================================================================
        Location startLocation= new Location(start);
        Location endLocation = new Location(end);

        Vector2D minVector = new Vector2D(0, 0);
        while (startLocation.distance(endLocation) >= 1) {
            Vector2D vec = paths.get(Math.round(endLocation.x), Math.round(endLocation.y));
            if (vec == null) break;
            endLocation.set((float) (endLocation.x - vec.x), (float) (endLocation.y - vec.y));
            minVector.set(vec.x, vec.y);
        }
        if(minVector.x ==0 && minVector.y ==0)  {
            if(ok){
                System.out.println("bad logic");
            }
//            System.out.println("random");
            minVector = new Vector2D(Math.random() * 2 -1,Math.random()*2 -1);
            rotateToTarget();
        }
        else this.getDirection().set(minVector.x, minVector.y);
    }


    private boolean isCollidedFence(Location pos) {
        Rectangle rec = new Rectangle(
                (int) (pos.x - this.getSize() / 2),
                (int) (pos.y - this.getSize() / 2),
                this.getSize(),
                this.getSize()
        );

        for (Entity entity : this.world.getEntities()) {
            if (entity instanceof Fence) {
                Rectangle rectangle = new Rectangle(
                        (int) entity.getLocation().x - entity.getSize() / 2,
                        (int) entity.getLocation().y - entity.getSize() / 2,
                        entity.getSize(),
                        entity.getSize()
                );

                if (rec.intersects(rectangle)) {
                    return true;
                }
            }
        }

        return false;
    }

    private boolean allowMove(CoordinateHelper.Coordinate coordinate) {
        Boolean isMovable = this.getWorld().getMovableMask().get(Math.round(coordinate.x), Math.round(coordinate.y));
        return isMovable == null || !isMovable;
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
//        this.getTexture().dispose();
    }


    @Override
    public Location lerp(Location moveTo, float speed) {
        this.destination = new Location(moveTo.x, moveTo.y);
        // This velocity for lerp,
        return this.destination;
    }

    private void updateMove() {
        updateDirection();
        // Update speed
        this.setSpeed(this.getType().getSpeed());
        updateLocation();
        updateHitBox();
    }

    private void updateDirection(){
        // Update rotation to target
        this.rotateToTarget();
//        Set direction to the target
//        this.getDirection().x = Math.cos(Math.toRadians(this.getRotation()));
//        this.getDirection().y = Math.sin(Math.toRadians(this.getRotation()));

        if(this.isCollidedFence(this.getLocation()))    {
            updateDirectionWhenCollision();
        }
        else updateDirectionUsingBFS();
    }

    private void updateDirectionWhenCollision(){
    //  try to find a way out of the fence
        Direction predictDirection = predictDirection(this.getLocation(), this.target.getLocation());

        int[] xMap = new int[]{};
        int[] yMap = new int[]{};
        switch (predictDirection) {
            case TOP_RIGHT: {
                xMap = new int[]{1, 0, 1};
                yMap = new int[]{0, 1, 1};
                break;
            }
            case TOP_LEFT: {
                xMap = new int[]{-1, 0, -1};
                yMap = new int[]{0, 1, 1};
                break;
            }
            case BOTTOM_LEFT: {
                xMap = new int[]{-1, 0, -1};
                yMap = new int[]{0, -1, -1};
                break;
            }
            case BOTTOM_RIGHT: {
                xMap = new int[]{0, 1, 1};
                yMap = new int[]{-1, 0, -1};
                break;
            }
            default: {
                break;
            }
        }
        for (int i = 0; i < xMap.length; i++) {
            if (!this.isCollidedFence(new Location(
                    this.getLocation().x + xMap[i] * speed  ,
                    this.getLocation().y + yMap[i] * speed
            ))){
                this.getDirection().set(xMap[i], yMap[i]);
                return;
            }
        }
        System.out.println("collision and no way out");
    }

    private void updateDirectionUsingBFS(){
        if (System.currentTimeMillis() - lastPathUpdate > ZOMBIE_PATH_UPDATE_DURATION) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    // bfs to find path
                    findTarget(
                            new Location(Math.round(getLocation().x), Math.round(getLocation().y)),
                            new Location(Math.round(target.getLocation().x), Math.round(target.getLocation().y))
                    );
                }
            }).start();
            this.lastPathUpdate = System.currentTimeMillis();
        }
    }

    private void updateLocation(){
        this.translate((float) (this.getDirection().x * this.getSpeed() * Gdx.graphics.getDeltaTime()),
                (float) (this.getDirection().y * this.getSpeed() * Gdx.graphics.getDeltaTime()));
    }

    private void updateHitBox(){
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

        for (Set<Entity> lastUpdateChunkSet : this.lastUpdateChunks) {
            lastUpdateChunkSet.remove(this);
        }

        this.lastUpdateChunks.clear();
    }
}
