package com.mygdx.zombieland;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Align;
import com.mygdx.zombieland.effects.TextIndicator;
import com.mygdx.zombieland.entity.Entity;
import com.mygdx.zombieland.entity.Player;
import com.mygdx.zombieland.entity.projectile.Projectile;
import com.mygdx.zombieland.entity.enemy.Zombie;
import com.mygdx.zombieland.entity.enemy.ZombieType;
import com.mygdx.zombieland.entity.undestructable.Fence;

import com.mygdx.zombieland.hud.HUD;
import com.mygdx.zombieland.inventory.Inventory;
import com.mygdx.zombieland.inventory.InventoryPistol;
import com.mygdx.zombieland.inventory.InventoryRifle;
import com.mygdx.zombieland.location.Location;
import com.mygdx.zombieland.location.Vector2D;
import com.mygdx.zombieland.scheduler.Scheduler;
import com.mygdx.zombieland.setting.GameSetting;
import com.mygdx.zombieland.spawner.BoxSpawner;
import com.mygdx.zombieland.spawner.Spawner;
import com.mygdx.zombieland.spawner.ZombieSpawner;
import com.mygdx.zombieland.state.GameState;
import com.mygdx.zombieland.utils.EntityMask;
import com.mygdx.zombieland.utils.FastMatrix;
import com.mygdx.zombieland.utils.VisualizeHelper;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

import static com.mygdx.zombieland.state.GameState.PAUSING;
import static com.mygdx.zombieland.state.GameState.PLAYING;

public class World implements Renderable {

    public static final int WINDOW_WIDTH = 800;
    public static final int WINDOW_HEIGHT = 600;
    private static final Texture BACKGROUND_TEXTURE = new Texture(Gdx.files.internal("background.png"));
    private static final Texture LOGO_TEXTURE = new Texture(Gdx.files.internal("logo.png"));
    //    private static final Music BGM_SOUND = Gdx.audio.newMusic(Gdx.files.internal("audio/BGM.mp3"));

    private static boolean isMoveUp = false;

    private static boolean isMoveDown = false;
    private static boolean isMoveLeft = false;
    private static boolean isMoveRight = false;

    public SpriteBatch batch;
    public BitmapFont font;
    private Player player;
    private final OrthographicCamera camera;
    private final ShapeRenderer shapeRenderer;
    private Texture background;
    private GameState gameState;
    private boolean debug;

    private final GameSetting gameSetting;
    private final Set<Entity> projectiles = new CopyOnWriteArraySet<>();
    private final Set<Entity> entities = new CopyOnWriteArraySet<>();
    private final Set<Spawner> spawners = new CopyOnWriteArraySet<>();
    private final Inventory inventory;
    private final Scheduler scheduler;
    private final TextIndicator textIndicator;
    private final HUD hud;
    //    private final MapRenderer<Entity> entitiesMap;
    private final FastMatrix<Set<Entity>> entitiesMap;
    private final FastMatrix<Boolean> movableMask;

    public World(SpriteBatch batch) {
        this.gameSetting = new GameSetting();
        this.batch = batch;
        this.scheduler = new Scheduler();

        this.font = new BitmapFont(Gdx.files.internal("fonts/default.fnt"),
                new TextureRegion(new Texture(Gdx.files.internal("fonts/default.png"))));
        this.textIndicator = new TextIndicator(this);
        this.camera = new OrthographicCamera(800, 600);
        this.shapeRenderer = new ShapeRenderer();
        this.gameState = GameState.STARTING;
        this.hud = new HUD(this);
        this.debug = false;
        this.inventory = new Inventory(this);
        this.entitiesMap = new FastMatrix<>();
        this.movableMask = new FastMatrix<>();
    }


    @Override
    public void create() {
        // Load assets and materials
        this.loadBackground();

        // Load inventory
        this.inventory.getItems().add(new InventoryPistol());
        this.inventory.getItems().add(new InventoryRifle());

        // Load player and inject world into player
        this.player = new Player(this);
        this.player.create();

        this.entities.add(new Fence(this, new Location(this.player.getLocation().x - 200 , this.player.getLocation().y)));
        this.entities.add(new Fence(this, new Location(this.player.getLocation().x + 200 , this.player.getLocation().y)));

        // Load entities
        for (Entity entity : entities) {
            entity.create();
        }

        // Load projectiles
        for (Entity projectile : this.projectiles) {
            projectile.create();
        }

        // Load spawners
        // Zombie spawner
        this.spawners.clear();
        this.spawners.add(new ZombieSpawner(this,
                new Location(this.player.getLocation().x - 300, this.player.getLocation().y), 0, 5000));
        this.spawners.add(new ZombieSpawner(this,
                new Location(this.player.getLocation().x, this.player.getLocation().y + 300), 0, 5000));

        // Box spawner, spaw amunition box
//        this.spawners.add(new BoxSpawner(this, new Location(this.getPlayer().getLocation()),
//                120f, 12000));

        for (Spawner spawner : this.spawners) {
            spawner.create();
        }

        // HUD initialization
        this.hud.create();

//        BGM_SOUND.setLooping(true);
//        BGM_SOUND.setVolume(this.getGameSetting().getMusicSoundLevel());
//        BGM_SOUND.play();s
    }

    @Override
    public void render() {

        // Update camera
        this.camera.update();

        this.batch.setProjectionMatrix(this.camera.combined);
        this.camera.position.x = (float) 800 / 2;
        this.camera.position.y = (float) 600 / 2;

        // Update background
        this.updateBackground();

        // Render HUD
        switch (this.gameState) {
            case STARTING: {
                Gdx.app.log("Game status", "Starting status");

                // Render logo
                this.batch.draw(LOGO_TEXTURE, 29, (386));

                // Register press to start
                this.font.draw(this.batch,
                        "Press LEFT MOUSE key to start",
                        300,
                        320,
                        200,
                        Align.center,
                        true);

                // Register keyboard for starting
                Gdx.input.setInputProcessor(new InputProcessor() {
                    @Override
                    public boolean keyDown(int keycode) {
                        if (keycode == Input.Buttons.LEFT) {
                            // The game still running
                            if (getGameState() == GameState.STARTING) {
                                setGameState(PLAYING);
                            }

                            return true;
                        }
                        return false;
                    }

                    @Override
                    public boolean keyUp(int keycode) {
                        return false;
                    }

                    @Override
                    public boolean keyTyped(char character) {
                        return false;
                    }

                    @Override
                    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                        if (button == Input.Buttons.LEFT) {
                            // The game still running
                            if (getGameState() == GameState.STARTING) {
                                setGameState(PLAYING);
                            }
                            return true;
                        }
                        return false;
                    }

                    @Override
                    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
                        return false;
                    }

                    @Override
                    public boolean touchDragged(int screenX, int screenY, int pointer) {
                        return false;
                    }

                    @Override
                    public boolean mouseMoved(int screenX, int screenY) {
                        return false;
                    }

                    @Override
                    public boolean scrolled(float amountX, float amountY) {
                        return false;
                    }
                });

                break;
            }
            case PAUSING:
            case ENDING:
            case PLAYING: {
                // Render player
                this.player.render();

                // Render all projectiles
                for (Entity projectile : this.projectiles) {
                    projectile.render();
                }

                // Render all entities
                for (Entity entity : entities) {
                    entity.render();
                }

                // Text indicator render
                this.getTextIndicator().render();

                // Spawner
                for (Spawner spawner : spawners) {
                    spawner.render();
                }

                this.hud.render();

                // Key movement register
                if (isMoveUp) player.moveUp();
                if (isMoveDown) player.moveDown();
                if (isMoveLeft) player.moveLeft();
                if (isMoveRight) player.moveRight();

                // Music pause
//                if (this.getGameState() == PAUSING && BGM_SOUND.isPlaying()) {
//                    BGM_SOUND.pause();
//                }
//
//                if (this.getGameState() == PLAYING && !BGM_SOUND.isPlaying()) {
//                    BGM_SOUND.play();
//                }

                // Esc to pause
                Gdx.input.setInputProcessor(new InputProcessor() {
                    @Override
                    public boolean keyDown(int keycode) {
                        // Pause the game if the game is starting
                        if (keycode == Input.Keys.ESCAPE) {
                            if (getGameState() != GameState.STARTING
                                    || getGameState() != GameState.ENDING) {
                                setGameState((getGameState() == PAUSING
                                        ? PLAYING
                                        : PAUSING)
                                );
                            }
                            return true;
                        }


                        if (keycode == Input.Keys.W) {

                            isMoveUp = true;

                        }
                        if (keycode == Input.Keys.S) {

                            isMoveDown = true;

                        }
                        if (keycode == Input.Keys.A) {

                            isMoveLeft = true;

                        }

                        if (keycode == Input.Keys.D) {

                            isMoveRight = true;

                        }


                        // Debug shortcut
                        if (keycode == Input.Keys.F3) {
                            getTextIndicator().createText(new Location(getPlayer().getLocation()).add(-64, 64),
                                    new Vector2D(0, 3F),
                                    String.format("Debug is %s", (!isDebug() ? "on" : "off")),
                                    1200,
                                    .3F, (!isDebug() ? Color.GREEN : Color.RED));

                            setDebug(!isDebug());
                            return true;
                        }
                        return false;
                    }

                    @Override
                    public boolean keyUp(int keycode) {
                        if (keycode == Input.Keys.W) {
                            isMoveUp = false;
                        }
                        if (keycode == Input.Keys.S) {
                            isMoveDown = false;
                        }
                        if (keycode == Input.Keys.A) {
                            isMoveLeft = false;
                        }
                        if (keycode == Input.Keys.D) {
                            isMoveRight = false;
                        }
                        return false;
                    }

                    @Override
                    public boolean keyTyped(char character) {
                        return false;
                    }

                    @Override
                    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                        // The game is end, click to restart
                        if (getGameState() == GameState.ENDING
                                && button == Input.Buttons.LEFT) {
//                            System.out.println("Hiii");
                            restart();
                            return true;
                        }
                        return false;
                    }

                    @Override
                    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
                        return false;
                    }

                    @Override
                    public boolean touchDragged(int screenX, int screenY, int pointer) {
                        return false;
                    }

                    @Override
                    public boolean mouseMoved(int screenX, int screenY) {
                        return false;
                    }

                    @Override
                    public boolean scrolled(float amountX, float amountY) {
                        return false;
                    }
                });

                break;
            }
            default: {
                throw new UnsupportedOperationException();
            }
        }

        // Render block movement area if user are in debug mode
        if (this.isDebug()) {
            VisualizeHelper.drawFastMatrix(this, this.getMovableMask(), 4, 1, Color.RED);
        }
    }

    @Override
    public void dispose() {
        this.player.dispose();
    }

    public SpriteBatch getBatch() {
        return batch;
    }

    public Location getCenterLocation(int offset) {
        return new Location(
                ((float) WINDOW_WIDTH / 2) - offset,
                ((float) WINDOW_HEIGHT / 2) - offset
        );
    }

    public Projectile createProjectile(Projectile projectile) {
        this.projectiles.add(projectile);
        projectile.create();
        return projectile;
    }

    public boolean removeProjectile(Projectile projectile) {
        projectile.dispose();
        return this.projectiles.remove(projectile);
    }

    public boolean removeEntity(Entity entity) {
        entity.dispose();
        return this.entities.remove(entity);
    }

    public Entity createEntity(Entity entity) {
        this.entities.add(entity);
        entity.create();
        return entity;
    }

    public Set<Entity> getProjectiles() {
        return projectiles;
    }

    public Set<Entity> getEntities() {
        return entities;
    }

    public Scheduler getScheduler() {
        return scheduler;
    }

    public Player getPlayer() {
        return player;
    }

    public BitmapFont getFont() {
        return font;
    }

    public TextIndicator getTextIndicator() {
        return textIndicator;
    }

    public void loadBackground() {
        this.background = BACKGROUND_TEXTURE;
    }

    public void updateBackground() {
        this.batch.draw(this.background, 0, 0);
    }

    public void playingRender() {

    }

    public void pausingRender() {

    }

    public ShapeRenderer getShapeRenderer() {
        return shapeRenderer;
    }

    public boolean isDebug() {
        return debug;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public GameSetting getGameSetting() {
        return gameSetting;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    public GameState getGameState() {
        return gameState;
    }

    public void restart() {
        // Clear entities
        for (Entity entity : entities) {
            removeEntity(entity);
        }

        // Clear projectile
        for (Entity projectile : projectiles) {
            removeProjectile((Projectile) projectile);
        }

        // Reset spawner counter
        for (Spawner spawner : spawners) {
            spawner.setLastSpawn(0);
        }

        // Inventory reset
        this.getInventory().clearItems();

        this.create();

        this.setGameState(PLAYING);
    }

    public FastMatrix<Set<Entity>> getEntitiesMap() {
        return entitiesMap;
    }

    public Collection<Set<Entity>> updateEntityMaskPosition(Entity entity) {
        Collection<Set<Entity>> updatedSets = new ArrayList<>();
        if (!(entity.getLocation().x > 0)
                || !(entity.getLocation().y > 0)
                || !(entity.getLocation().x < World.WINDOW_WIDTH)
                || !(entity.getLocation().y < World.WINDOW_HEIGHT)
        ) {
            return updatedSets;
        }

        EntityMask entityMask = this.getEntityMask(entity);

        for (int x = entityMask.getLeft(); x < entityMask.getRight(); x++) {
            for (int y = entityMask.getBottom(); y < entityMask.getTop(); y++) {
                // Set entity chunk
                Set<Entity> currentChunk = entity.getWorld()
                        .getEntitiesMap()
                        .get(x, y);

                // Create a new set and interact with a value of the set
                if (currentChunk == null) {
                    Set<Entity> entities = Collections.newSetFromMap(new ConcurrentHashMap<Entity, Boolean>());
                    entities.add(entity);
                    entity.getWorld().getEntitiesMap().set(x, y, entities);
                    updatedSets.add(entities);
                    continue;
                }

                currentChunk.add(entity);
                updatedSets.add(currentChunk);
            }
        }
        return updatedSets;
    }

    /**
     * Returns the value of the entity position, which addition with size.
     * The result is an array of numbers contains a position as clock-wise position started from the top.
     *
     * @param entity the entity to get mask
     * @return an array of numbers contains a position as clock-wise position started from the top.
     */
    public EntityMask getEntityMask(Entity entity) {
        int left = (int) (entity.getLocation().x - entity.getSize() / 2);
        int bottom = (int) (entity.getLocation().y - entity.getSize() / 2);
        int top = (int) (entity.getLocation().y + entity.getSize() / 2);
        int right = (int) (entity.getLocation().x + entity.getSize() / 2);

        return new EntityMask(top, right, bottom, left);
    }

    public void setBlockMoveAtPosition(int x, int y, boolean isBlockMove) {
        // Set movable mask
        this.movableMask.set(x, y, isBlockMove);
    }

    public FastMatrix<Boolean> getMovableMask() {
        return movableMask;
    }
}
