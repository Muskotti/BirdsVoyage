package fi.tamk.tiko;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class clockPickUp {

    private Sprite clock;
    float speed = 3f;

    public clockPickUp(){
        clock = new Sprite(new Texture(Gdx.files.internal("clock.png")));
    }

    public void spawn(float x, float y) {
        clock.setPosition(x, y);
    }

    public void draw(SpriteBatch batch) {
        clock.draw(batch);
    }

    public boolean hits(Rectangle rectangle) {
        boolean hits = false;
        if (rectangle.overlaps(clock.getBoundingRectangle())){
            hits = true;
        }
        return hits;
    }
}
