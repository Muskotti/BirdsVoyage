package fi.tamk.tiko;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

/**
 * Class for the clock
 *
 * @author Toni Vänttinen & Jimi Savola
 * @version 1.8, 05/02/18
 * @since 1.8
 */
public class clockPickUp {

    // Clocks sprite
    private Sprite clock;

    // Clocks rectangle
    private Rectangle clockRect;

    /**
     * Constructor for clock
     */
    public clockPickUp(){
        clock = new Sprite();
        clockRect = new Rectangle(0,0,64,64);
    }

    /**
     * Places the clock to a specified spot
     * @param x value for the clocks x position
     * @param y value for the clocks y position
     */
    public void spawn(float x, float y) {
        clock.setPosition(x, y);
    }

    public void draw(BirdsVoyage host) {
        host.clockTime += Gdx.graphics.getDeltaTime();
        clockRect.setPosition(clock.getX(),clock.getY());
        TextureRegion currentFrame = host.clockAnimation.getKeyFrame(host.clockTime, true);
        host.batch.draw(currentFrame, clock.getX(), clock.getY());
    }

    /**
     * Checks if the given rectangle overlaps with the clock
     * If it does a boolean value of true is returned
     * Otherwise it returns false
     * @param rectangle checked if it overlaps with the clock
     * @return boolean value if if the rectangles overlap or not
     */
    public boolean hits(Rectangle rectangle) {
        boolean hits = false;
        if (rectangle.overlaps(clockRect)){
            hits = true;
        }
        return hits;
    }
}
