package fi.tamk.tiko;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class StormCloud implements StormCloudProperties {
    private Sprite stormCloud;
    private Rectangle cloudRectangle;
    boolean moveStop;
    boolean compliteMove = false;
    boolean moveBack = false;

    // Storm Cloud constructor
    public StormCloud() {
        moveStop = true;
        stormCloud = new Sprite();
        cloudRectangle = new Rectangle(0,0,1280,384);
        stormCloud.setPosition(0, -stormCloud.getHeight() * 2);
    }

    public void draw(BirdsVoyage host) {
        host.cloudTime += Gdx.graphics.getDeltaTime();
        cloudRectangle.setPosition(stormCloud.getX(),stormCloud.getY());
        TextureRegion currentFrame = host.cloudAnimation.getKeyFrame(host.cloudTime, true);
        host.batch.draw(currentFrame, stormCloud.getX(), stormCloud.getY());
    }

    public void move(Camera camera) {
        stormCloud.setPosition(0, camera.getPositionY() - 387 * 2);
        cloudRectangle.setPosition(stormCloud.getX(),stormCloud.getY());
    }

    public void moveUp(Camera camera) {
        if (moveStop) {
            cloudRectangle.setPosition(stormCloud.getX(),stormCloud.getY());
            if (stormCloud.getY() < camera.getPositionY() - 387 && !moveBack) {
                stormCloud.translateY(2.5f);
            } else {
                moveBack = true;
            }
            if (moveBack) {
                stormCloud.translateY(-.5f);
                if (stormCloud.getY() < camera.getPositionY() - 387 * 2) {
                    moveBack = false;
                    compliteMove = true;
                }
            }
        }
    }

    public Rectangle getCloudBoundingRectangle() {
        return cloudRectangle;
    }

    public void stop() {
        moveStop = false;
    }

    public void resume() {
        moveStop = true;
    }
}