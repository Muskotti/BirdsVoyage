package fi.tamk.tiko;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

/**
 * Map holds the .tmx files of all maps and loads them accordingly to user selection in level select screen.
 * Also hold methods that checks player and camera collisions with tiled map object layers.
 *
 * @author Toni Vänttinen and Jimi Savola
 * @version 1.8, 05/02/18
 * @since 1.8
 */
public class Map {
    // Tiled map
    private TiledMap tiledMap;

    // Renderer for tiled map
    private TiledMapRenderer tiledMapRenderer;

    // Object layer of the tiled map
    private MapLayer collisionObjectLayer;

    // Map objects
    private MapObjects mapObjects;

    // Aray for multiple objects
    private Array<RectangleMapObject> rectangleObjects;

    // Location for the clock spawn
    float clockRecLocX;
    float clockRecLocY;

    /**
     * Constructor for map
     *
     * @param currentLevel level currently selected by the player, file will be loaded accordingly
     */
    public Map(String currentLevel){
        if (currentLevel.equals("level1")){
            tiledMap = new TmxMapLoader().load("map1-1.tmx");
        }

        if (currentLevel.equals("level2")){
            tiledMap = new TmxMapLoader().load("map1-2.tmx");
        }

        if (currentLevel.equals("level3")){
            tiledMap = new TmxMapLoader().load("map1-3.tmx");
        }

        if (currentLevel.equals("level4")){
            tiledMap = new TmxMapLoader().load("map2-1.tmx");
        }

        if (currentLevel.equals("level5")){
            tiledMap = new TmxMapLoader().load("map2-2.tmx");
        }

        if (currentLevel.equals("level6")){
            tiledMap = new TmxMapLoader().load("map2-3.tmx");
        }

        if (currentLevel.equals("level7")){
            tiledMap = new TmxMapLoader().load("map3-1.tmx");
        }

        if (currentLevel.equals("level8")){
            tiledMap = new TmxMapLoader().load("map3-2.tmx");
        }

        if (currentLevel.equals("level9")){
            tiledMap = new TmxMapLoader().load("map3-3.tmx");
        }

        // Initialization of the map renderer
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);
    }

    /**
     * Sets the camera to the beginning of the level.
     *
     * @param camera games camera
     */
    public void setMap(OrthographicCamera camera) {
        tiledMapRenderer.setView(camera);
        tiledMapRenderer.render();
    }

    /**
     * Checks if player collides with a tree.
     *
     * @param player player
     */
    public void checkCollision(Player player) {
        collisionObjectLayer = tiledMap.getLayers().get("treesRect");
        mapObjects = collisionObjectLayer.getObjects();
        rectangleObjects = mapObjects.getByType(RectangleMapObject.class);
        for (RectangleMapObject rectangleObject : rectangleObjects) {
            Rectangle rectangle = rectangleObject.getRectangle();
            player.collision(rectangle);
        }
    }

    /**
     * Checks if camera rectangle hits the bird spawn rectangle in tiled map.
     *
     * @param camera games camera
     * @return boolean for enemy bird spawning. If true, enemy bird will spawn on the map
     */
    public boolean enemySpawn(Camera camera) {
        boolean spawn = false;

        collisionObjectLayer = tiledMap.getLayers().get("enemySpawn");
        mapObjects = collisionObjectLayer.getObjects();
        rectangleObjects = mapObjects.getByType(RectangleMapObject.class);
        for (RectangleMapObject rectangleObject : rectangleObjects) {
            Rectangle rectangle = rectangleObject.getRectangle();
            if (camera.doSpawn(rectangle)) {
                spawn = true;
            }
        }
        return spawn;
    }

    /**
     * Checks if camera rectangle hits the cloud spawn rectangle in tiled map.
     *
     * @param camera games camera
     * @return boolean for cloud spawning. If true, cloud will come up from under the camera
     */
    public boolean cloudSpawn(Camera camera) {
        boolean spawn = false;

        collisionObjectLayer = tiledMap.getLayers().get("cloudSpawn");
        mapObjects = collisionObjectLayer.getObjects();
        rectangleObjects = mapObjects.getByType(RectangleMapObject.class);
        for (RectangleMapObject rectangleObject : rectangleObjects) {
            Rectangle rectangle = rectangleObject.getRectangle();
            if (camera.doSpawn(rectangle)) {
                spawn = true;
            }
        }
        return spawn;
    }

    /**
     * Checks if camera rectangle hits the clock spawn rectangle in tiled map.
     *
     * @param camera games camera
     * @return boolean for clock spawning. If true, clock will spawn on the level
     */
    public boolean clockSpawn(Camera camera) {
        boolean spawn = false;

        collisionObjectLayer = tiledMap.getLayers().get("clockSpawn");
        mapObjects = collisionObjectLayer.getObjects();
        rectangleObjects = mapObjects.getByType(RectangleMapObject.class);
        for (RectangleMapObject rectangleObject : rectangleObjects) {
            Rectangle rectangle = rectangleObject.getRectangle();
            if (camera.doSpawn(rectangle)) {
                spawn = true;
                clockRecLocX = rectangle.x;
                clockRecLocY = rectangle.y;
            }
        }
        return spawn;
    }

    /**
     * Clocks X location
     *
     * @return clocks X location
     */
    public float getClockRecLocX() {
        return clockRecLocX;
    }

    /**
     * Clocks Y location
     *
     * @return clocks Y location
     */
    public float getClockRecLocY() {
        return clockRecLocY;
    }

    /**
     * Checks if player hits the finish line on the map
     *
     * @param player player
     * @return boolean for map finish. If true, map will finish
     */
    public boolean checkFinish(Player player) {
        collisionObjectLayer = tiledMap.getLayers().get("mapFinish");
        mapObjects = collisionObjectLayer.getObjects();
        rectangleObjects = mapObjects.getByType(RectangleMapObject.class);
        for (RectangleMapObject rectangleObject : rectangleObjects) {
            Rectangle rectangle = rectangleObject.getRectangle();
            if (rectangle.contains(player.getRectangle())) {
                return true;
            }
        }
        return false;
    }
}
