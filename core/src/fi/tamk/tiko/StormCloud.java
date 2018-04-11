package fi.tamk.tiko;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class StormCloud implements StormCloudProperties {
    private Sprite stormCloud;
    float speed = 2.5f;
    float reverseSpeed = -0.5f;
    boolean goBack = false;
    boolean moveStop;
    boolean despawnReady;

    // Storm Cloud constructor
    public StormCloud() {
        moveStop = true;
        despawnReady = false;
        stormCloud = new Sprite(new Texture(Gdx.files.internal("cloud.png")));
    }

    public void spawn(float x, float y) {
        x = x - stormCloud.getWidth()/2;

        stormCloud.setPosition(x,y);
    }

    public void draw(SpriteBatch b) {
        stormCloud.draw(b);
    }

    public void move(Camera camera) {
        if (moveStop) {
            if (stormCloud.getY()<camera.getPositionY() && !goBack) {
                stormCloud.translateY(speed);
            }
            if (stormCloud.getY()>=(camera.getPositionY()-(stormCloud.getHeight())) && !goBack) {
                goBack = true;
                despawnReady = true;
            }
            if (goBack) {
                stormCloud.translateY(reverseSpeed);
            }
        }
    }

    public float getCloudWidth() {
        return cloudWidth;
    }
    public float getCloudHeight() {
        return cloudHeight;
    }
    public float getCloudYpos() {
        return stormCloud.getY();
    }
    public float getCloudXpos() {
        return stormCloud.getX();
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