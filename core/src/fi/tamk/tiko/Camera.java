package fi.tamk.tiko;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

/**
 * Class for the came camera
 *
 * @author Toni Vänttinen & Jimi Savola
 * @version 1.8, 05/02/18
 * @since 1.8
 */
public class Camera implements MapProperties, PlayerProperties{

    // The camera
    OrthographicCamera camera;

    // player class
    Player player;

    // Rectangle for the camera
    Rectangle cameraRect;

    // Boolean for the camera move
    boolean stopMove;

    /**
     * Constructor for the camera
     * @param player player class
     */
    public Camera(Player player){
        this.player = player;
        camera = new OrthographicCamera();
        camera.setToOrtho(false,cameraWidth,cameraHeight);
        cameraRect = new Rectangle(0, camera.position.y + (cameraHeight/2), Gdx.graphics.getWidth(), 1);
        stopMove = true;
    }

    /**
     * Updates the camera rectangle so it is at the top of the screen
     * Updates the camera
     */
    public void update(){
        cameraRect.y = camera.position.y + (cameraHeight/2) + 65;
        camera.update();
    }

    /**
     * returns the camera
     * @return the camera
     */
    public OrthographicCamera getCamera(){
        return camera;
    }

    /**
     * Moves the camera
     *
     * First the code checks if the camera movement is stopped
     * if it not stopped the code checks if the players movement is slow
     * if it is the camera move up in half speed
     * if it is not the camera moves up in normal speed
     * other wise the camera movement is limited on the maps width and height
     */
    public void cameraMove() {
        if (stopMove) {
            // Jos pelaaja on hidastunut, kamerakin hidastuu
            if (player.getSpeedY() == player.getHalfspeed()) {
                camera.position.y = camera.position.y + player.getHalfspeed();
            } else {
                camera.position.y = camera.position.y + player.getNormalSpeed();
            }
            if (camera.position.x < cameraWidth / 2) {
                camera.position.x = cameraWidth / 2;
            }

            if (camera.position.y > mapHeight - cameraHeight / 2) {
                camera.position.y = mapHeight - cameraHeight / 2;
            }

            if (camera.position.y < cameraHeight / 2) {
                camera.position.y = cameraHeight / 2;
            }

            if (camera.position.x > mapWidth - cameraWidth / 2f) {
                camera.position.x = mapWidth - cameraWidth / 2f;
            }
        }
    }

    /**
     * Checks if the rectangle is spawned on the map
     *
     * First the return value is made
     * next the code checks if the rectangles overlap
     * if they do the return value is changed to true
     * Lastly the value is returned
     *
     * @param rectangle object that is checked
     * @return a boolean if the rectangle overlaps
     */
    public boolean doSpawn(Rectangle rectangle) {
        boolean overlap = false;
        if (cameraRect.overlaps(rectangle)) {
            overlap = true;
        }
        return overlap;
    }

    /**
     * Returns cameras Matrix4
     * @return Matrix4 of the camera
     */
    public Matrix4 combined() {
        return camera.combined;
    }

    /**
     * Returns cameras current x position
     * @return float value of the cameras x position
     */
    public float getPositionX() {
        return camera.position.x;
    }

    /**
     * Returns cameras current y position
     * @return float value of the cameras y position
     */
    public float getPositionY() {
        return camera.position.y;
    }

    /**
     * Unprojects the camera
     * @param vec Vector3 that is used in the camera.unproject
     */
    public void unproject(Vector3 vec) {
        camera.unproject(vec);
    }

    /**
     * Changes the stop move value
     * if the value is changed the camera stops to move
     */
    public void stop() {
        stopMove = false;
    }

    /**
     * Changes the stop move value
     * if the value is changed the camera starts to move
     */
    public void resume() {
        stopMove = true;
    }

    /**
     * Moves the camera to default position
     */
    public void setPos() {
        camera.position.set(cameraWidth/2,cameraHeight/2,0);
    }

    /**
     * Moves the camera to specific position that are given
     * @param x value for cameras x position
     * @param y value for cameras y position
     */
    public void returnPos(float x, float y) {
        camera.position.set(x,y,0);
    }

}
