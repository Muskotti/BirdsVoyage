package fi.tamk.tiko;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;

/**
 * class for the enemy bird
 *
 * @author Toni Vänttinen & Jimi Savola
 * @version 1.8, 05/02/18
 * @since 1.8
 */
public class Enemy implements EnemyProperties {

    // Enemy sprite
    private Sprite enemy;

    // Enemy rectangle
    private Rectangle enemyRect;

    // Value for the enemies speed
    float speed = 3f;

    // boolean to check if the enemy can move
    boolean moveStop;

    /**
     * Enemy constructor
     */
    public Enemy() {
        moveStop = true;
        enemy = new Sprite();
        enemyRect = new Rectangle(0,0,256,128);
    }

    /**
     * Spawns the enemy at one of the 3 spots
     *
     * First a random number between 1-3 is generate
     * Than the given y position is moved
     * After the random value is checked
     * depending on the random value the given x value is moved
     * Finally the enemies position is set
     * @param x value for the enemies x axis
     * @param y value for the enemies y axis
     */
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

    /**
     * Draws the enemy
     *
     * First the enemies animation value is changed
     * next the enemies rectangle is moved where the sprite is
     * next the enemies animation frame is changed
     * lastly the animation is drawn
     * @param host used to make the animation possible
     */
    public void draw(BirdsVoyage host) {
        host.stateTime += Gdx.graphics.getDeltaTime();
        enemyRect.setPosition(enemy.getX(),enemy.getY());
        TextureRegion currentFrame = host.flyingAnimation.getKeyFrame(host.stateTime, true);
        host.batch.draw(currentFrame, enemy.getX(), enemy.getY());
    }

    /**
     * Moves the enemy
     *
     * First the code checks if the game is paused
     * if not the enemy is moved downwards
     * otherwise it doesn't do anything
     */
    public void move() {
        if(moveStop) {
            enemy.translateY(-speed);
        }
    }

    /**
     * Getter for the enemies height
     * @return gives the enemies height
     */
    public float getEnemyHeight() {
        return enemyHeight;
    }

    /**
     * Getter for the enemies y position
     * @return enemies y position
     */
    public float getEnemyYpos() {
        return enemy.getY();
    }

    /**
     * Getter for the enemies x position
     * @return enemies x position
     */
    public float getEnemyXpos() {
        return enemy.getX();
    }

    /**
     * Getter for the enemies rectangle
     * @return enemies rectangle
     */
    public Rectangle getEnemyBoundingRectangle() {
        return enemyRect;
    }

    /**
     * Changes the enemies movement to move
     */
    public void stop() {
        moveStop = false;
    }

    /**
     * Changes the enemies movement to stop
     */
    public void resume() {
        moveStop = true;
    }
}
