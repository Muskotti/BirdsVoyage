package fi.tamk.tiko;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;

public class Player implements MapProperties, PlayerProperties{

    private Sprite player;
    private Rectangle boundingRectangle;

    float moveSpeedX = moveSpeedXog;
    float moveSpeedY = moveSpeedYog;
    float speedY = normalSpeed;
    float speedX = normalSpeed;

    // These are determined in calibration, otherwise 0 and 0.
    float defaultPositionX;
    float defaultPositionY;

    float playerRestTop;
    float transTime;
    float slowdownTimer;
    boolean stopMove;

    // player constructor
    public Player(){
        player = new Sprite(new Texture(Gdx.files.internal("player.png")));
        boundingRectangle = new Rectangle();
        playerRestTop = cameraHeight;
        slowdownTimer = 0;
        stopMove = true;
    }

    public void setPosition(float x, float y) {
        player.translate(x,y);
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

    public void draw(SpriteBatch b) {
        player.draw(b);
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
                if (x > 0.5) {
                    if (notCollided() && slowdownTimer == 0) {
                        moveSpeedX = moveSpeedXog;
                    } else {
                        moveSpeedX = moveSpeedXog / 3;
                    }
                    if (x > 1.5) {
                        if (notCollided() && slowdownTimer == 0) {
                            moveSpeedX = 300f;
                        } else {
                            moveSpeedX = 50f;
                        }
                        if (x > 2.5) {
                            if (notCollided() && slowdownTimer == 0) {
                                moveSpeedX = 400f;
                            } else {
                                moveSpeedX = 100f;
                            }
                        }
                    }
                    player.setX(player.getX() - (moveSpeedX - x) * delta);
                }
                if (x < -0.5) {
                    if (notCollided() && slowdownTimer == 0) {
                        moveSpeedX = moveSpeedXog;
                    } else {
                        moveSpeedX = moveSpeedXog / 3;
                    }
                    if (x < -1.5) {
                        if (notCollided() && slowdownTimer == 0) {
                            moveSpeedX = 300f;
                        } else {
                            moveSpeedX = 50f;
                        }
                        if (x < -2.5) {
                            if (notCollided() && slowdownTimer == 0) {
                                moveSpeedX = 400f;
                            } else {
                                moveSpeedX = 100f;
                            }
                        }
                    }
                    player.setX(player.getX() + (moveSpeedX - x) * delta);
                }
                if (y < - 0.5) {
                    if (notCollided() && slowdownTimer == 0) {
                        moveSpeedY = moveSpeedYog;
                    } else {
                        moveSpeedY = moveSpeedYog / 3;
                    }
                    if (y < -1.5) {
                        if (notCollided() && slowdownTimer == 0) {
                            moveSpeedY = 300f;
                        } else {
                            moveSpeedY = 50f;
                        }
                        if (y < -2.5) {
                            if (notCollided() && slowdownTimer == 0) {
                                moveSpeedY = 400f;
                            } else {
                                moveSpeedY = 100f;
                            }
                        }
                    }
                    player.setY(player.getY() + (moveSpeedY - y) * delta);
                }
                if (y > 0.5) {
                    if (notCollided() && slowdownTimer == 0) {
                        moveSpeedY = moveSpeedYog;
                    } else {
                        moveSpeedY = moveSpeedYog / 3;
                    }
                    if (y > 1.5) {
                        if (notCollided() && slowdownTimer == 0) {
                            moveSpeedY = 300f;
                        } else {
                            moveSpeedY = 50f;
                        }
                        if (y > 2.5) {
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

    private void changeTrans() {
        if (stopMove) {
            transTime += Gdx.graphics.getRawDeltaTime();
            if (transTime <= 0.05) {
                player.setAlpha(0.5f);
            } else if (transTime >= 0.1 && transTime <= 0.29999) {
                player.setAlpha(1f);
            } else if (transTime >= 0.3) {
                transTime = 0;
            }
        }
    }

    public void collision(Rectangle rectangle) {
        if (player.getBoundingRectangle().overlaps(rectangle)) {
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

    public void changeSpeed(ArrayList<Enemy> enemies, StormCloud cloud) {
        // Enemy collision
        for (int i = 0; i<enemies.size(); i++) {
            Enemy enemy = enemies.get(i);
            if (player.getBoundingRectangle().overlaps(enemy.getEnemyBoundingRectangle())) {
                collision(enemy.getEnemyBoundingRectangle());
                slowdownTimer += Gdx.graphics.getRawDeltaTime();
            }
        }
        if (slowdownTimer > 0 && slowdownTimer < 3) {
            speedY = halfSpeed;
            speedX = halfSpeed;
            slowdownTimer += Gdx.graphics.getRawDeltaTime();
        }
        else {
            slowdownTimer = 0;
        }

        // Cloud collision
        if (player.getBoundingRectangle().overlaps(cloud.getCloudBoundingRectangle())){
            collision(cloud.getCloudBoundingRectangle());
        }

        // Trees collision
        if (player.getBoundingRectangle().overlaps(boundingRectangle)) {
            speedY = halfSpeed;
            speedX = halfSpeed;
            changeTrans();
            if (stopMove){
                Gdx.input.vibrate(50);
            }
        } else {
            speedY = normalSpeed;
            speedX = normalSpeed;
            player.setAlpha(1f);
        }
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
        return player.getBoundingRectangle();
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
}
