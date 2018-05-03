package fi.tamk.tiko;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

public class Camera implements MapProperties, PlayerProperties{

    OrthographicCamera camera;
    Player player;
    Rectangle cameraRect;
    boolean stopMove;

    public Camera(Player player){
        this.player = player;
        camera = new OrthographicCamera();
        camera.setToOrtho(false,cameraWidth,cameraHeight);
        cameraRect = new Rectangle(0, camera.position.y + (cameraHeight/2), Gdx.graphics.getWidth(), 1);
        stopMove = true;
    }
    public void update(){
        cameraRect.y = camera.position.y + (cameraHeight/2) + 65;
        camera.update();
    }
    public OrthographicCamera getCamera(){
        return camera;
    }

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

    public boolean doSpawn(Rectangle rectangle) {
        boolean overlap = false;
        if (cameraRect.overlaps(rectangle)) {
            overlap = true;
        }
        return overlap;
    }

    public Matrix4 combined() {
        return camera.combined;
    }

    public float getPositionX() {
        return camera.position.x;
    }

    public float getPositionY() {
        return camera.position.y;
    }

    public void unproject(Vector3 vec) {
        camera.unproject(vec);
    }

    public void stop() {
        stopMove = false;
    }

    public void resume() {
        stopMove = true;
    }

    public void setPos() {
        camera.position.set(cameraWidth/2,cameraHeight/2,0);
    }

    public void returnPos(float x, float y) {
        camera.position.set(x,y,0);
    }

}
