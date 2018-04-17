package fi.tamk.tiko;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;

public class Enemy implements EnemyProperties {
    private Sprite enemy;
    private Rectangle enemyRect;
    float speed = 3f;
    boolean moveStop;

    // Enemy constructor
    public Enemy() {
        moveStop = true;
        enemy = new Sprite();
        enemyRect = new Rectangle(0,0,256,128);
    }

    public void spawn(float x, float y) {
        int random = MathUtils.random(1,3);
        y = y + (enemyHeight * 7);

        if (random == 1) {
            x = x + 200f;
        }
        else if (random == 2){
            x = x - enemyWidth;
        }
        else {
            x = x - (enemyWidth * 4);
        }
        enemy.setPosition(x, y);
    }

    public void draw(BirdsVoyage host) {
        host.stateTime += Gdx.graphics.getDeltaTime();
        enemyRect.setPosition(enemy.getX(),enemy.getY());
        TextureRegion currentFrame = host.flyingAnimation.getKeyFrame(host.stateTime, true);
        host.batch.draw(currentFrame, enemy.getX(), enemy.getY());
    }

    public void move() {
        if(moveStop) {
            enemy.translateY(-speed);
        }
    }

    public float getEnemyWidth() {
        return enemyWidth;
    }
    public float getEnemyHeight() {
        return enemyHeight;
    }

    public float getEnemyYpos() {
        return enemy.getY();
    }
    public float getEnemyXpos() {
        return enemy.getX();
    }
    public Rectangle getEnemyBoundingRectangle() {
        return enemyRect;
    }

    public void stop() {
        moveStop = false;
    }

    public void resume() {
        moveStop = true;
    }
}
