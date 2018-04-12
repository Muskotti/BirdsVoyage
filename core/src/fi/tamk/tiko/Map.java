package fi.tamk.tiko;

import com.badlogic.gdx.Gdx;
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

public class Map {
    private TiledMap tiledMap;
    private TiledMapRenderer tiledMapRenderer;
    private MapLayer collisionObjectLayer;
    private MapObjects mapObjects;
    private Array<RectangleMapObject> rectangleObjects;
    float clockRecLocX;
    float clockRecLocY;

    public Map(){
        tiledMap = new TmxMapLoader().load("map2-1.tmx");
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);
    }

    public void setMap(OrthographicCamera camera) {
        tiledMapRenderer.setView(camera);
        tiledMapRenderer.render();
    }

    public void checkCollision(Player player) {
        collisionObjectLayer = tiledMap.getLayers().get("treesRect");
        mapObjects = collisionObjectLayer.getObjects();
        rectangleObjects = mapObjects.getByType(RectangleMapObject.class);
        for (RectangleMapObject rectangleObject : rectangleObjects) {
            Rectangle rectangle = rectangleObject.getRectangle();
            player.collision(rectangle);
        }
    }

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

    public float getClockRecLocX() {
        return clockRecLocX;
    }

    public float getClockRecLocY() {
        return clockRecLocY;
    }

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
