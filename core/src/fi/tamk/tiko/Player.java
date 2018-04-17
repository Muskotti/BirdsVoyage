package fi.tamk.tiko;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;

public class Player implements MapProperties, PlayerProperties{

    private Sprite player;

    // flying animation
    private Animation<TextureRegion> flyingAnimation;
    private Texture flyingSheet;
    float stateTime;

    // feather animation
    private Animation<TextureRegion> featherAnimation;
    private Texture featherSheet;
    float featherTime;

    private Rectangle boundingRectangle;
    private Rectangle playerRectangle;

    float moveSpeedX = moveSpeedXog;
    float moveSpeedY = moveSpeedYog;
    float speedY = normalSpeed;
    float speedX = normalSpeed;

    // These are determined in calibration, otherwise 0 and 0.
    float defaultPositionX;
    float defaultPositionY;

    // Sensitivities for each leaning direction
    double upSens;
    double downSens;
    double rightSens;
    double leftSens;

    float playerRestTop;
    float transTime;
    float slowdownTimer;

    boolean stopMove;

    // player constructor
    public Player(){
        flyingAnim();
        featherAnim();
        player = new Sprite();
        playerRectangle = new Rectangle(player.getX(),player.getY(),128, 64);
        boundingRectangle = new Rectangle();
        playerRestTop = cameraHeight;
        slowdownTimer = 0;
        stopMove = true;

        // default sensitivities
        // Got to be positive
        downSens = 0.8;
        leftSens = 0.8;
        // Got to be negative
        upSens = -0.8;
        rightSens = -0.8;
    }

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
        featherAnimation = new Animation<TextureRegion>(1 / 10f, flyingFrames);
        featherTime = 0f;
    }

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

    public float getX() {
        return player.getX();
    }

    public float getY() {
        return player.getY();
    }

    public void move() {
        if (stopMove) {
            float delta = Gdx.graphics.getDeltaTime();
            float moveAmount = 500f * delta;

            // For testing only, remember to delete later
            if (Gdx.input.isKeyPressed(Input.Keys.DPAD_UP)) {
                player.translateY(moveAmount);
            }
            if (Gdx.input.isKeyPressed(Input.Keys.DPAD_DOWN)) {
                player.translateY(-moveAmount);
            }
            if (Gdx.input.isKeyPressed(Input.Keys.DPAD_RIGHT)) {
                player.translateX(moveAmount);
            }
            if (Gdx.input.isKeyPressed(Input.Keys.DPAD_LEFT)) {
                player.translateX(-moveAmount);
            }

            // Final movement code
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

    public void collision(Rectangle rectangle) {
        if (playerRectangle.overlaps(rectangle)) {
            boundingRectangle = rectangle;
        }
    }

    public boolean notCollided() {
        boolean collision = true;
        if(speedY == halfSpeed) {
            collision = false;
        }
        return collision;
    }

    public void changeSpeed(ArrayList<Enemy> enemies, StormCloud cloud, SpriteBatch b) {
        // Enemy collision
        for (int i = 0; i<enemies.size(); i++) {
            Enemy enemy = enemies.get(i);
            if (playerRectangle.overlaps(enemy.getEnemyBoundingRectangle())) {
                collision(enemy.getEnemyBoundingRectangle());
                slowdownTimer += Gdx.graphics.getRawDeltaTime();
            }
        }
        if (slowdownTimer > 0 && slowdownTimer < 5) {
            speedY = halfSpeed;
            speedX = halfSpeed;
            slowdownTimer += Gdx.graphics.getRawDeltaTime();
        }
        else {
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
            if (stopMove){
                Gdx.input.vibrate(50);
            }
        } else {
            speedY = normalSpeed;
            speedX = normalSpeed;
        }
    }

    public void hitAnim(SpriteBatch b) {
        featherTime += Gdx.graphics.getDeltaTime();
        TextureRegion currentFrame = featherAnimation.getKeyFrame(featherTime, true);
        b.draw(currentFrame, player.getX(), player.getY());
    }

    public float getSpeedY() {
        return speedY;
    }
    public float getSpeedX() {
        return speedX;
    }
    public float getNormalSpeed() {
        return normalSpeed;
    }
    public float getHalfspeed() {
        return halfSpeed;
    }
    public float getPlayerWidth() {
        return playerWidth;
    }
    public float getPlayerHeight() {
        return playerHeight;
    }

    public Rectangle getRectangle() {
        return playerRectangle;
    }

    public void stop() {
        stopMove = false;
    }

    public void resume() {
        stopMove = true;
    }

    public void setStart() {
        player.setPosition(startX,startY);
    }

    public void animate(SpriteBatch b) {
        stateTime += Gdx.graphics.getDeltaTime();
        playerRectangle.setPosition(player.getX(), player.getY());
        TextureRegion currentFrame = flyingAnimation.getKeyFrame(stateTime, true);
        b.draw(currentFrame, player.getX(), player.getY());
    }
}
