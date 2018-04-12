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
    private Animation<TextureRegion> flyingAnimation;
    private Texture flyingSheet;
    float speed = 3f;
    float stateTime;
    boolean moveStop;

    // Enemy constructor
    public Enemy() {
        flyingSheet = new Texture(Gdx.files.internal("enemy.png"));
        TextureRegion[][] tmp = TextureRegion.split(flyingSheet,
                flyingSheet.getWidth() / 4,
                flyingSheet.getHeight() / 1);
        TextureRegion[] flyingFrames = new TextureRegion[4 * 1];
        int index = 0;
        for (int i = 0; i < 1; i++){
            for (int j = 0; j < 4; j++){
                flyingFrames[index++] = tmp[i][j];
            }
        }
        flyingAnimation = new Animation<TextureRegion>(1 / 10f, flyingFrames);
        stateTime = 0f;
        moveStop = true;
        enemy = new Sprite();
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

    public void draw(SpriteBatch b) {
        stateTime += Gdx.graphics.getDeltaTime();
        TextureRegion currentFrame = flyingAnimation.getKeyFrame(stateTime, true);
        b.draw(currentFrame, enemy.getX(), enemy.getY());
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
        return enemy.getBoundingRectangle();
    }

    public void stop() {
        moveStop = false;
    }

    public void resume() {
        moveStop = true;
    }
}
