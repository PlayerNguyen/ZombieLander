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

import java.util.*;

public class Zombie extends EnemyAbstract {

    public static final int ZOMBIE_SIZE = 64;
    //  public static final float ZOMBIE_MOVEMENT_SPEED = 30f; // Each type has different speed

    public static final long ZOMBIE_HIT_DURATION = 2000;
    public static final short ZOMBIE_DEBUG_MAP_DENSITY = 8; // Only change if needed [1 - ZOMBIE_SIZE]
    public static final short ZOMBIE_DEBUG_VISITED_MAP_DENSITY = 12;
    private static final long ZOMBIE_PATH_UPDATE_DURATION = 50;

    private final World world;
    private final Entity target;
    private final ZombieType type;

    private Location destination;
    private float speed; // Zombie movement speed

    private long lastHit = 0;

    private final List<Set<Entity>> lastUpdateChunks = new ArrayList<>();
    private long lastPathUpdate;
    //    boolean[][] visited = new boolean[801][601];
    private final FastMatrix<Boolean> visited = new FastMatrix<>();
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
    private final int reactionRate = 0;

    @Override
    public void render() {

        // Export (render) image
        this.getSprite().setRotation(this.getRotation());
        this.getSprite().setPosition(this.getLocation().x - 32, this.getLocation().y - 32);
        this.getSprite().draw(world.getBatch());

        if (this.world.getGameState().equals(GameState.PLAYING)) {
            this.updateMove();
        }


        // Put the zombie onto the map
        for (Set<Entity> lastUpdateChunkSet : this.lastUpdateChunks) {
            lastUpdateChunkSet.remove(this);
        }

        this.lastUpdateChunks.clear();

        if (this.getLocation().x > 0 && this.getLocation().y > 0 && this.getLocation().x < World.WINDOW_WIDTH && this.getLocation().y < World.WINDOW_HEIGHT) {
            int left = (int) (this.getLocation().x - ZOMBIE_SIZE / 2);
            int bottom = (int) (this.getLocation().y - ZOMBIE_SIZE / 2);
            int top = (int) (this.getLocation().y + ZOMBIE_SIZE / 2);
            int right = (int) (this.getLocation().x + ZOMBIE_SIZE / 2);

            for (int x = left; x < right; x++) {
                for (int y = bottom; y < top; y++) {
                    // in bound area
                    if (x > 0 && y > 0 && x < World.WINDOW_WIDTH && y < World.WINDOW_HEIGHT) {
                        Set<Entity> currentChunk = this.getWorld().getEntitiesMap().get(x, y);
                        this.lastUpdateChunks.add(currentChunk);
                        currentChunk.add(this);
                    }
                }
            }

            // Render the mash when debug is on to check the 2d map
            if (this.getWorld().isDebug()) {
                for (int x = left; x < right; x += ZOMBIE_DEBUG_MAP_DENSITY) {
                    for (int y = bottom; y < top; y += ZOMBIE_DEBUG_MAP_DENSITY) {
                        VisualizeHelper.simulateCircle(this.getWorld(), new Location(x, y), 1);
                    }
                }
                VisualizeHelper.simulateText(this.getWorld(), new Location(left - 6, bottom - 6), String.format("%d, %d", left, bottom), new Color(1, 1, 1, 1));

                // Draw visited predict to target nodes
                for (Map.Entry<FastMatrix.Key, Boolean> entry : this.visited.entrySet()) {
                    FastMatrix.Key coordinate = entry.getKey();
                    int x = coordinate.x;
                    int y = coordinate.y;
                    if (x % ZOMBIE_DEBUG_VISITED_MAP_DENSITY == 0 && y % ZOMBIE_DEBUG_VISITED_MAP_DENSITY == 0) {
                        VisualizeHelper.simulateCircle(this.getWorld(), new Location(x, y), 1F);
                    }
                }
            }
        }

        // Debug
        if (this.getWorld().isDebug()) {
            VisualizeHelper.simulateBox(this.getWorld(), this);
            VisualizeHelper.simulateDirection(this.getWorld(), this);
        }


        if (System.currentTimeMillis() - lastPathUpdate > ZOMBIE_PATH_UPDATE_DURATION) {
            new Thread(new Runnable() {
                @Override
                public void run() {

                    pathFinder(getLocation(), target.getLocation());
                }
            }).start();
            this.lastPathUpdate = System.currentTimeMillis();
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

//            System.out.println("colision");
            ok = false;
            break;
        }


        this.translate((float) this.getDirection().x * speed * Gdx.graphics.getDeltaTime(), (float) this.getDirection().y * speed * Gdx.graphics.getDeltaTime());
        rotateToTarget();
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

    public void pathFinder(Location start, Location end) {
        this.visited.clear();
        Direction predictDirection = predictDirection(start, end);

        Queue<CoordinateHelper.Coordinate> cq = new ArrayDeque<>();
        cq.add(new CoordinateHelper.Coordinate(start));
        this.visited.set((int) start.x, (int) start.y, true);

        while (!cq.isEmpty()) {
            CoordinateHelper.Coordinate curCoordinate = cq.poll();

            if (curCoordinate.x == end.x && curCoordinate.y == end.y) break;

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
                int x = (int) (curCoordinate.x + xMap[i]);
                int y = (int) (curCoordinate.y + yMap[i]);

                if (x < 0 || y < 0) continue;
                if (x >= World.WINDOW_WIDTH || y >= World.WINDOW_HEIGHT) continue;
                // add blocking way here
                if (!allowMove(new CoordinateHelper.Coordinate(x, y))) {
                    continue;
                }

                if (!visited.contains(x, y)) {
                    visited.set(x, y, true);

                    // Set to the next point
                    paths.set(x, y, new Vector2D(xMap[i], yMap[i]));
                    cq.add(new CoordinateHelper.Coordinate(x, y));
                }
            }
        }

        Vector2D minVector = new Vector2D(Integer.MAX_VALUE, Integer.MAX_VALUE);

        for (Map.Entry<FastMatrix.Key, Vector2D> entry : paths.entrySet()) {
            Vector2D vec = entry.getValue();
            Location curLocation = new Location(start).add(vec);
            Location minLocation = new Location(start).add(minVector);

            if (curLocation.distance(end) < minLocation.distance(end)) {
                minVector = vec;
            }
        }
        this.getDirection().set(minVector.x, minVector.y);
    }

    private boolean allowMove(CoordinateHelper.Coordinate coordinate) {
        Set<Entity> entities = this.getWorld().getEntitiesMap().get((int) coordinate.x, (int) coordinate.y);
        for (Entity entity : entities) {
            if (entity instanceof Fence) return false;
        }
        return true;
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
        // Set direction to the target
//        this.getDirection().x = Math.cos(Math.toRadians(this.getRotation()));
//        this.getDirection().y = Math.sin(Math.toRadians(this.getRotation()));

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

        for (Set<Entity> lastUpdateChunkSet : this.lastUpdateChunks) {
            lastUpdateChunkSet.remove(this);
        }

        this.lastUpdateChunks.clear();
    }
}
