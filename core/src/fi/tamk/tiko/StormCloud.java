package fi.tamk.tiko;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class StormCloud implements StormCloudProperties {
    private Sprite stormCloud;
    boolean moveStop;
    boolean compliteMove = false;
    boolean moveBack = false;

    // Storm Cloud constructor
    public StormCloud() {
        moveStop = true;
        stormCloud = new Sprite(new Texture(Gdx.files.internal("cloud.png")));
        stormCloud.setPosition(0, -600);
    }

    public void draw(SpriteBatch b) {
        stormCloud.draw(b);
    }

    public void move(Camera camera) {
        stormCloud.setPosition(0, camera.getPositionY() - 800);
    }

    public void moveUp(Camera camera) {

        if (stormCloud.getY() <= camera.getPositionY() && moveBack == false){
            stormCloud.translateY(2.5f);
            if (stormCloud.getY() >= camera.getPositionY()){
                moveBack = true;
            }
        }
        if (moveBack){
            stormCloud.translateY(-2.5f);
            if (stormCloud.getY() <= camera.getPositionY() - 800){
                compliteMove = true;
            }
        }
    }

    public Rectangle getCloudBoundingRectangle() {
        return stormCloud.getBoundingRectangle();
    }

    public void stop() {
        moveStop = false;
    }

    public void resume() {
        moveStop = true;
    }
}