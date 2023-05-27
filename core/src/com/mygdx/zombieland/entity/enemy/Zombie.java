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
import com.mygdx.zombieland.path.Path;
import com.mygdx.zombieland.path.PathFinder;
import com.mygdx.zombieland.state.GameState;
import com.mygdx.zombieland.utils.*;

import java.util.*;

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
    private FastMatrix<Double> bfs;

    private Location destination;
    private float speed; // Zombie movement speed

    private long lastHit = 0;

    private final List<Set<Entity>> lastUpdateChunks = new ArrayList<>();
    private long lastPathUpdate;
    //    boolean[][] visited = new boolean[801][601];
//    private final FastMatrix<Boolean> visited = new FastMatrix<>();
    private final FastMatrix<Path> paths = new FastMatrix<>();
    PathFinder pathFinder = new PathFinder(new Location(0, 0), new Location(300, 300));

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

        pathFinder.setSource(this.getLocation());
        pathFinder.setTarget(this.target.getLocation());
        pathFinder.addRestricted(this.getWorld().getMovableMask());
    }

    @Override
    public void create() {
//        this.getSprite().setTexture(ZOMBIE_TEXTURE);
        this.getSprite().setSize(ZOMBIE_SIZE, ZOMBIE_SIZE);
        this.getSprite().setOrigin((float) ZOMBIE_SIZE / 2, (float) ZOMBIE_SIZE / 2);

        this.rotateToTarget();
        this.updateMove();
    }

    @Override
    public void render() {

        // Export (render) image
        this.getSprite().setRotation(this.getRotation());
        this.getSprite().setPosition(this.getLocation().x - 32, this.getLocation().y - 32);
        this.getSprite().draw(world.getBatch());

        if (this.world.getGameState().equals(GameState.PLAYING)) {
            this.updateMove();
        }

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
            VisualizeHelper.drawFastMatrix(this.getWorld(), bfs, 1, 1, Color.CYAN);
        }


        if (System.currentTimeMillis() - lastPathUpdate > ZOMBIE_PATH_UPDATE_DURATION) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    long a = System.currentTimeMillis();
                    findTarget(getLocation(), target.getLocation());

                    System.out.println("Find path in " + (System.currentTimeMillis() - a) + " ms");
                }
            }).start();
            long a = System.currentTimeMillis();
            bfs = pathFinder.getPath((this.getSize() / 2));
            System.out.println("Find A* path in " + (System.currentTimeMillis() - a) + " ms");
            this.lastPathUpdate = System.currentTimeMillis();

            Vector2D minVector = new Vector2D();
            for (Map.Entry<FastMatrix.Key, Double> entry : bfs.entrySet()) {
                int x = entry.getKey().x;
                int y = entry.getKey().y;
                Location predictLocation = new Location(x, y);

                minVector.set((predictLocation.x - this.getLocation().x), (predictLocation.y - this.getLocation().y));
                break;
            }
            minVector.normalize();
            System.out.println("minVector = " + minVector);
            this.getDirection().set(minVector.x * speed * Gdx.graphics.getDeltaTime(), minVector.y * speed * Gdx.graphics.getDeltaTime());
        }

        boolean isMovable = true;
        Location blockedPosition = null;
        EntityMask entityMask = this.getWorld().getEntityMask(this);
        for (int x = entityMask.getLeft() + 6; x < entityMask.getRight() - 6; x++) {
            for (int y = entityMask.getBottom() + 6; y < entityMask.getTop() - 6; y++) {
                Boolean isBlocked = this.getWorld().getMovableMask().get(
                        x, y
                );
                if (isBlocked != null && isBlocked) {
                    isMovable = false;
                    blockedPosition = new Location(x, y);
                    break;
                }
            }
        }
        Vector2D inverseVector = this.getDirection().getInverseVector(this.getLocation());
        VisualizeHelper.simulateVector(this.getWorld(), this.getLocation(), inverseVector, Color.BLACK);
        if (isMovable) {

            this.translate((float) this.getDirection().x * speed * Gdx.graphics.getDeltaTime(),
                    (float) this.getDirection().y * speed * Gdx.graphics.getDeltaTime());
        } else {
            // Push the zombie out a little bit by
            // get the nearest blocked region,
            // and find a vector that opposite with it
            // and push the zombie out
            System.out.println("Collision triggered");


//            this.translate((float) (inverseVector.x * speed * Gdx.graphics.getDeltaTime()), (float) (inverseVector.y * speed * Gdx.graphics.getDeltaTime()));
        }
        rotateToTarget();
    }

    static enum Direction {
        TOP_LEFT, BOTTOM_LEFT, TOP_RIGHT, BOTTOM_RIGHT, TOP, LEFT, RIGHT, BOTTOM
    }

    private Direction predictDirection(Location start, Location end) {

        double deltaX = end.x - start.x;
        double deltaY = end.y - start.y;
        double degrees = Math.atan2(deltaY, deltaX) * 180 / Math.PI;
        if (degrees < 0.0) {
            degrees += 360.0;
        }
        if (degrees == 0) {
            return Direction.RIGHT;
        }
        if (degrees > 0 && degrees < 90) {
            return Direction.TOP_RIGHT;
        }
        if (degrees == 90) {
            return Direction.TOP;
        }
        if (degrees > 90 && degrees < 180) {
            return Direction.TOP_LEFT;
        }
        if (degrees == 180) {
            return Direction.LEFT;
        }
        if (degrees > 180 && degrees < 270) {
            return Direction.BOTTOM_LEFT;
        }
        if (degrees == 270) {
            return Direction.BOTTOM;
        }
        return Direction.BOTTOM_RIGHT;
    }

    public void findTarget(Location start, Location end) {
//        this.visited.clear();
//        this.paths.clear();
//        Direction predictDirection = predictDirection(start, end);
//
//        Queue<CoordinateHelper.Coordinate> cq = new PriorityQueue<>();
//        cq.add(new CoordinateHelper.Coordinate(start));
//        this.paths.set((int) start.x, (int) start.y, new Path(new Vector2D(0, 0), start.distance(end)));
//
//        while (!cq.isEmpty()) {
//            CoordinateHelper.Coordinate curCoordinate = cq.poll();
//
//            if (curCoordinate.x == end.x && curCoordinate.y == end.y) break;
//
//            int[] xMap = new int[]{};
//            int[] yMap = new int[]{};
//            switch (predictDirection) {
//                case TOP_RIGHT: {
//                    xMap = new int[]{1, 0, 1};
//                    yMap = new int[]{0, 1, 1};
//                    break;
//                }
//                case TOP_LEFT: {
//                    xMap = new int[]{-1, 0, -1};
//                    yMap = new int[]{0, 1, 1};
//                    break;
//                }
//                case BOTTOM_LEFT: {
//                    xMap = new int[]{-1, 0, -1};
//                    yMap = new int[]{0, -1, -1};
//                    break;
//                }
//                case BOTTOM_RIGHT: {
//                    xMap = new int[]{0, 1, 1};
//                    yMap = new int[]{-1, 0, -1};
//                    break;
//                }
//                case RIGHT: {
//                    xMap = new int[]{1};
//                    yMap = new int[]{0};
//                    break;
//                }
//                case TOP: {
//                    xMap = new int[]{0};
//                    yMap = new int[]{1};
//                    break;
//                }
//                case LEFT: {
//                    xMap = new int[]{-1};
//                    yMap = new int[]{0};
//                    break;
//                }
//                case BOTTOM: {
//                    xMap = new int[]{0};
//                    yMap = new int[]{-1};
//                    break;
//                }
//                default: {
//                    break;
//                }
//            }
//            for (int i = 0; i < xMap.length; i++) {
//                int x = (int) (curCoordinate.x + xMap[i]);
//                int y = (int) (curCoordinate.y + yMap[i]);
//
//                if (x < 0 || y < 0) continue;
//                if (x >= World.WINDOW_WIDTH || y >= World.WINDOW_HEIGHT) continue;
//
//                // add blocking way here
//                if (!allowMove(new CoordinateHelper.Coordinate(x, y))) {
//                    continue;
//                }
//
//                if (!paths.contains(x, y)) {
////                    visited.set(x, y, true);
//
//                    // Set to the next point
//                    paths.set(x, y, new Vector2D(xMap[i], yMap[i]));
//                    cq.add(new CoordinateHelper.Coordinate(x, y));
//                }
//            }
//        }

//        Vector2D minVector = new Vector2D(Integer.MAX_VALUE, Integer.MAX_VALUE);
//
//        for (Map.Entry<FastMatrix.Key, Vector2D> entry : paths.entrySet()) {
//            Vector2D vec = entry.getValue();
//            Location curLocation = new Location(start).add(vec);
//            Location minLocation = new Location(start).add(minVector);
//
//            if (curLocation.distance(end) < minLocation.distance(end)) {
//                minVector = vec;
//            }
//        }
//        this.getDirection().set(minVector.x, minVector.y);
    }

    private boolean allowMove(CoordinateHelper.Coordinate coordinate) {
        Boolean isMovable = this.getWorld().getMovableMask().get((int) coordinate.x, (int) coordinate.y);
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
