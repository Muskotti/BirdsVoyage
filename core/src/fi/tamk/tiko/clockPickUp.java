package fi.tamk.tiko;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class clockPickUp {

    private Sprite clock;
    private Rectangle clockRect;

    public clockPickUp(){
        clock = new Sprite();
        clockRect = new Rectangle(0,0,64,64);
    }

    public void spawn(float x, float y) {
        clock.setPosition(x, y);
    }

    public void draw(BirdsVoyage host) {
        host.clockTime += Gdx.graphics.getDeltaTime();
        clockRect.setPosition(clock.getX(),clock.getY());
        TextureRegion currentFrame = host.clockAnimation.getKeyFrame(host.clockTime, true);
        host.batch.draw(currentFrame, clock.getX(), clock.getY());
    }

    public boolean hits(Rectangle rectangle) {
        boolean hits = false;
        if (rectangle.overlaps(clockRect)){
            hits = true;
        }
        return hits;
    }
}
