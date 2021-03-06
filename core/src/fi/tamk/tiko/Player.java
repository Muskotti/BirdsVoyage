package fi.tamk.tiko;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;

/**
 * Class for the player.
 *
 * @author Toni Vänttinen and Jimi Savola
 * @version 1.8, 05/02/18
 * @since 1.8
 */
public class Player implements MapProperties, PlayerProperties{

    // Sprite for the player
    private Sprite player;

    // flying animation
    private Animation<TextureRegion> flyingAnimation;
    private Texture flyingSheet;
    float stateTime;

    // feather animation
    private Animation<TextureRegion> featherAnimation;
    private Texture featherSheet;
    float featherTime;

    // Collision sound for player
    private Sound collisionSound;

    // Players rectangle and rectangle for object currently overlapping with the player.
    private Rectangle boundingRectangle;
    private Rectangle playerRectangle;

    // Movement speeds of the player
    float moveSpeedX = moveSpeedXog;
    float moveSpeedY = moveSpeedYog;
    float speedY = normalSpeed;
    float speedX = normalSpeed;

    // Default tilt positions, these are determined by calibration
    float defaultPositionX;
    float defaultPositionY;

    // Sensitivities for each leaning direction
    float upSens;
    float downSens;
    float rightSens;
    float leftSens;

    float playerRestTop;

    // Timer for the slowdown, that occurs when player collides with an enemy bird
    float slowdownTimer;

    // Boolean for movement
    boolean stopMove;

    // Timer for collision sound
    float collisionTimer;

    /**
     * Player constructor
     */
    public Player(){
        flyingAnim();
        featherAnim();
        player = new Sprite();
        playerRectangle = new Rectangle(player.getX(),player.getY(),128, 64);
        boundingRectangle = new Rectangle();
        playerRestTop = cameraHeight;
        slowdownTimer = 0;
        collisionTimer = 0;
        stopMove = true;

        // Loads collision sound
        collisionSound = Gdx.audio.newSound(Gdx.files.internal("BirdCollision.wav"));
    }

    /**
     * Animation for collision
     */
    private void featherAnim() {
        featherSheet = new Texture(Gdx.files.internal("feathers.png"));
        TextureRegion[][] tmp = TextureRegion.split(featherSheet,
                featherSheet.getWidth() / 2,
                featherSheet.getHeight() / 1);
        TextureRegion[] flyingFrames = new TextureRegion[2 * 1];
        int index = 0;
        for (int i = 0; i < 1; i++){
            for (int j = 0; j < 2; j++){
                flyingFrames[index++] = tmp[i][j];
            }
        }
        featherAnimation = new Animation<TextureRegion>(1 / 5f, flyingFrames);
        featherTime = 0f;
    }

    /**
     * Animation for flapping wings
     */
    private void flyingAnim() {
        flyingSheet = new Texture(Gdx.files.internal("player.png"));
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
    }

    /**
     * Pushes player back to screen if going out of bounds
     * @param camera is the same orthographic camera used in the game
     */
    public void fixPosition(OrthographicCamera camera) {
        // method that restricts player movements on the screen
        if (player.getX()<0) {
            player.setX(0);
        }
        if (player.getX()>MapProperties.mapWidth-playerWidth) {
            player.setX(MapProperties.mapWidth-playerWidth);
        }
        // checks if player goes over the screen
        if (player.getY() > (camera.position.y + (cameraHeight/2)) - playerHeight){
            player.setY((camera.position.y + (cameraHeight/2)) - playerHeight);
        }
        // checks that player doesn't go under the screen
        if (player.getY() < camera.position.y - (cameraHeight/2)){
            player.setY(camera.position.y - (cameraHeight/2));
        }
    }

    /**
     *
     * @return returns players X position
     */
    public float getX() {
        return player.getX();
    }

    /**
     *
     * @return returns players Y position
     */
    public float getY() {
        return player.getY();
    }

    /**
     * Moves the player around using accelerometers. Movement speed, deadzones and
     * default zero position is based on sensitivity and calibration settings on preferences.
     */
    public void move() {
        if (stopMove) {
            float delta = Gdx.graphics.getDeltaTime();

            // Leaning check and movement
            if (!Gdx.input.isKeyPressed(Input.Keys.ANY_KEY)) {
                float x = 0, y = 0;

                x = defaultPositionX - Gdx.input.getAccelerometerY();
                y = defaultPositionY - Gdx.input.getAccelerometerZ();

                // Move left
                if (x > leftSens) {
                    if (notCollided() && slowdownTimer == 0) {
                        moveSpeedX = moveSpeedXog;
                    } else {
                        moveSpeedX = moveSpeedXog / 3;
                    }
                    if (x > leftSens*2) {
                        if (notCollided() && slowdownTimer == 0) {
                            moveSpeedX = 300f;
                        } else {
                            moveSpeedX = 50f;
                        }
                        if (x > leftSens*3) {
                            if (notCollided() && slowdownTimer == 0) {
                                moveSpeedX = 400f;
                            } else {
                                moveSpeedX = 100f;
                            }
                        }
                    }
                    player.setX(player.getX() - (moveSpeedX - x) * delta);
                }

                // Move right
                if (x < rightSens) {
                    if (notCollided() && slowdownTimer == 0) {
                        moveSpeedX = moveSpeedXog;
                    } else {
                        moveSpeedX = moveSpeedXog / 3;
                    }
                    if (x < rightSens*2) {
                        if (notCollided() && slowdownTimer == 0) {
                            moveSpeedX = 300f;
                        } else {
                            moveSpeedX = 50f;
                        }
                        if (x < rightSens*3) {
                            if (notCollided() && slowdownTimer == 0) {
                                moveSpeedX = 400f;
                            } else {
                                moveSpeedX = 100f;
                            }
                        }
                    }
                    player.setX(player.getX() + (moveSpeedX - x) * delta);
                }

                // Move up
                if (y < upSens) {
                    if (notCollided() && slowdownTimer == 0) {
                        moveSpeedY = moveSpeedYog;
                    } else {
                        moveSpeedY = moveSpeedYog / 3;
                    }
                    if (y < upSens*2) {
                        if (notCollided() && slowdownTimer == 0) {
                            moveSpeedY = 300f;
                        } else {
                            moveSpeedY = 50f;
                        }
                        if (y < upSens*3) {
                            if (notCollided() && slowdownTimer == 0) {
                                moveSpeedY = 400f;
                            } else {
                                moveSpeedY = 100f;
                            }
                        }
                    }
                    player.setY(player.getY() + (moveSpeedY - y) * delta);
                }

                // Move down
                if (y > downSens) {
                    if (notCollided() && slowdownTimer == 0) {
                        moveSpeedY = moveSpeedYog;
                    } else {
                        moveSpeedY = moveSpeedYog / 3;
                    }
                    if (y > downSens*2) {
                        if (notCollided() && slowdownTimer == 0) {
                            moveSpeedY = 300f;
                        } else {
                            moveSpeedY = 50f;
                        }
                        if (y > downSens*3) {
                            if (notCollided() && slowdownTimer == 0) {
                                moveSpeedY = 400f;
                            } else {
                                moveSpeedY = 100f;
                            }
                        }
                    }
                    player.setY(player.getY() - (moveSpeedY - y) * delta);
                }
            }
            // moves player constantly forward
            player.translateY(speedY);
        }
    }

    /**
     * Checks if player continues overlapping with the same rectangle hit earlier.
     * @param rectangle rectangle for enemies, cloud or trees
     */
    public void collision(Rectangle rectangle) {
        if (playerRectangle.overlaps(rectangle)) {
            boundingRectangle = rectangle;
        }
    }

    /**
     * Checks if player collides with an object
     * @return boolean for collision
     */
    public boolean notCollided() {
        boolean collision = true;
        if(speedY == halfSpeed) {
            collision = false;
        }
        return collision;
    }

    /**
     * Changes player and camera movement speed if player collides with an object.
     *
     * @param enemies array for enemy objects
     * @param cloud cloud object
     * @param b spritebatch
     * @param mute boolean that checks if game is muted
     * @param gamePause boolean that checks if game is currently paused, used in collision sound
     * @param mapWin boolean for ending of the map. If true, goes to victory screen
     */
    public void changeSpeed(ArrayList<Enemy> enemies, StormCloud cloud, SpriteBatch b, Boolean mute, boolean gamePause, boolean mapWin) {
        // Enemy collision
        for (int i = 0; i<enemies.size(); i++) {
            Enemy enemy = enemies.get(i);
            if (playerRectangle.overlaps(enemy.getEnemyBoundingRectangle())) {
                collision(enemy.getEnemyBoundingRectangle());
                slowdownTimer += Gdx.graphics.getRawDeltaTime();
            }
        }

        // Slowdown timer starts if player got hit by an enemy bird
        if (slowdownTimer > 0 && slowdownTimer < 5) {
            speedY = halfSpeed;
            speedX = halfSpeed;
            slowdownTimer += Gdx.graphics.getRawDeltaTime();
        } else {
            speedY = normalSpeed;
            speedX = normalSpeed;
            slowdownTimer = 0;
        }

        // Cloud collision
        if (playerRectangle.overlaps(cloud.getCloudBoundingRectangle())){
            collision(cloud.getCloudBoundingRectangle());
        }

        // Trees collision
        if (playerRectangle.overlaps(boundingRectangle)) {
            speedY = halfSpeed;
            speedX = halfSpeed;
            hitAnim(b);

            // Prevent collision sound from overlapping and playing in wrong time
            if (collisionTimer == 0 && !mute && !gamePause && !mapWin) {
                collisionSound.play(0.7f);
            }
            collisionTimer += Gdx.graphics.getRawDeltaTime();
            if (collisionTimer>1) {
                collisionTimer = 0;
            }

            // Vibrates the device if player hits an object
            if (stopMove){
                Gdx.input.vibrate(50);
            }
        } else {
            collisionSound.stop();
            collisionTimer = 0;
            speedY = normalSpeed;
            speedX = normalSpeed;
        }
    }

    /**
     * Determines collision animations speed and following of the players position.
     * @param b spritebatch
     */
    public void hitAnim(SpriteBatch b) {
        featherTime += Gdx.graphics.getDeltaTime();
        TextureRegion currentFrame = featherAnimation.getKeyFrame(featherTime, true);
        b.draw(currentFrame, player.getX(), player.getY());
    }

    /**
     * Returns current speed
     *
     * @return players vertical speed
     */
    public float getSpeedY() {
        return speedY;
    }

    /**
     * Returns normal speed, when not colliding
     *
     * @return default speed
     */
    public float getNormalSpeed() {
        return normalSpeed;
    }

    /**
     * Speed when colliding with something
     *
     * @return speed when colliding
     */
    public float getHalfspeed() {
        return halfSpeed;
    }

    /**
     * Used to return players bounding rectangle
     *
     * @return player's bounding rectangle
     */
    public Rectangle getRectangle() {
        return playerRectangle;
    }

    /**
     * Stops the movement, this is used when the game pauses.
     */
    public void stop() {
        stopMove = false;
    }

    /**
     * Resumes the movement.
     */
    public void resume() {
        stopMove = true;
    }

    /**
     * Sets starting position for the player when map begins.
     */
    public void setStart() {
        player.setPosition(startX,startY);
    }

    /**
     * Does the flying animation for player.
     * @param b spritebatch
     */
    public void animate(SpriteBatch b) {
        stateTime += Gdx.graphics.getDeltaTime();
        playerRectangle.setPosition(player.getX(), player.getY());
        TextureRegion currentFrame = flyingAnimation.getKeyFrame(stateTime, true);
        b.draw(currentFrame, player.getX(), player.getY());
    }

    /**
     * Updates the sensitivities for each direction. Sensitivities come from preferences, which are
     * determined in sensitivity screen.
     *
     * @param up sensitivity for up/forward direction
     * @param down sensitivity for down/backwards direction
     * @param left sensitivity for left direction
     * @param right sensitivity for right direction
     */
    public void setSens(float up, float down, float left, float right) {
        upSens = up;
        downSens = down;
        leftSens = left;
        rightSens = right;
    }

    /**
     * Sets default position/calibration for the player. This is stored in preferences and determined in
     * calibration screen.
     *
     * @param x default X position
     * @param y default Y position
     */
    public void setDefPos(float x, float y) {
        defaultPositionX = x;
        defaultPositionY = y;
    }
}
