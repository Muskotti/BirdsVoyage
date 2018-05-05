package fi.tamk.tiko;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

import java.util.ArrayList;

/**
 * class for the enemy bird
 *
 * @author Toni Vänttinen & Jimi Savola
 * @version 1.8, 05/02/18
 * @since 1.8
 */
public class gameScreen implements Screen {

    // main game java class
    BirdsVoyage host;

    // The font
    BitmapFont font;

    // general buttons
    private Texture pauseButton;
    private Texture popUp;

    // map finish textures
    private Texture mapWinTex;
    private Texture timeboxEng;
    private Texture timeboxFin;

    // english buttons
    private Texture resumeENG;
    private Texture settingsENG;
    private Texture menuENG;

    // finnish buttons
    private Texture resumeFIN;
    private Texture settingsFIN;
    private Texture menuFIN;

    // general rectangle
    private Rectangle pauseRect;
    private Rectangle resumeRect;
    private Rectangle settingsRect;
    private Rectangle menuRect;

    // A vector used when the player touches the screen
    Vector3 touch;

    // number of enemies
    int enemyNumber = 0;

    // number of clocks
    int clockNumber = 0;

    // timer for the start
    private float startTimer;
    private int startTimerDefault = 5;

    // boolean for pause
    boolean gamePause = false;

    // boolean for a win
    boolean mapWin = false;

    // boolean for the map start
    boolean mapStart = true;

    // boolean fot the cloud movement
    boolean cloudMove = false;

    // Boolean for finish sound played to prevent overlapping
    boolean finishSoundPlayed;

    // array for the enemies
    ArrayList<Enemy> enemies = new ArrayList<Enemy>();

    // array for the clocks
    ArrayList<clockPickUp> clocks = new ArrayList<clockPickUp>();

    // storm cloud
    StormCloud cloud;

    // Sensitivities
    float downSens;
    float leftSens;
    float upSens;
    float rightSens;

    // boolean if there is a new high score
    boolean newHighscore;

    /**
     * Constructor for the game screen
     * @param host
     */
    public gameScreen(BirdsVoyage host){
        // font
        font = new BitmapFont();

        // gets the main class
        this.host = host;

        // makes the vector
        touch = new Vector3(0,0,0);

        // the cloud
        cloud = new StormCloud();

        // pause button
        pauseButton = new Texture(Gdx.files.internal("Pause.png"));

        // background for the pause menu
        popUp = new Texture(Gdx.files.internal("Pausebox.png"));

        // english buttons
        resumeENG = new Texture(Gdx.files.internal("continueENG.png"));
        settingsENG = new Texture(Gdx.files.internal("settingsENG.png"));
        menuENG = new Texture(Gdx.files.internal("mmenuENG.png"));

        // english buttons
        resumeFIN = new Texture(Gdx.files.internal("continueFIN.png"));
        settingsFIN = new Texture(Gdx.files.internal("settingsFIN.png"));
        menuFIN = new Texture(Gdx.files.internal("mmenuFIN.png"));

        // win screen
        mapWinTex = new Texture(Gdx.files.internal("Goalscreen.png"));
        timeboxEng = new Texture(Gdx.files.internal("timeboxENG.png"));
        timeboxFin = new Texture(Gdx.files.internal("timeboxFIN.png"));

        // all the rectangles
        pauseRect = new Rectangle(0, 24, pauseButton.getWidth(), pauseButton.getHeight());
        resumeRect = new Rectangle(host.getCameraWidth()/2 - (resumeENG.getWidth()/2),
                525,
                resumeENG.getWidth(),
                resumeENG.getHeight()
        );
        settingsRect = new Rectangle(
                host.getCameraWidth()/2 - (settingsENG.getWidth()/2),
                390,
                settingsENG.getWidth(),
                settingsENG.getHeight()
        );
        menuRect = new Rectangle(
                host.getCameraWidth()/2 - (menuENG.getWidth()/2),
                250,
                menuENG.getWidth(),
                menuENG.getHeight()
        );

        // sets the current screen string to game
        host.currentScreen = "game";

        // boolean if there is a new high score set to false
        newHighscore = false;

        //boolean to to play win sound one time
        finishSoundPlayed = false;
    }

    /**
     * does nothing
     */
    @Override
    public void show() {

    }

    /**
     * Renderer for the game
     * @param delta gets the delta time
     */
    @Override
    public void render(float delta) {
        refreshScreen();
        update();
        setSensitivity();
        saveTouch();
        firstSens();

        // sets sensitivity
        host.player.setSens(upSens, downSens, leftSens, rightSens);

        // moves the player
        host.player.move();

        // fixes players position
        host.player.fixPosition(host.camera.getCamera());

        // Checks if player collides
        host.map.checkCollision(host.player);

        //checks if the map ends
        mapWin = host.map.checkFinish(host.player);

        // moves the camera
        host.camera.cameraMove();

        // keeps the pause on screen
        pauseRect.setPosition(host.camera.getPositionX() + (host.getCameraWidth()/2) - pauseButton.getWidth() - 5,
                host.camera.getPositionY() - (host.getCameraHeight()/2));

        checkMapStart();
        checkButtons();
        spawnEnemy();
        spawnClock();
        moveEnemy();
        moveCloud();
        playerDraw();
        drawClock();
        drawUI();
        drawHelp();
        checkMapWin();
    }

    /**
     * checks if the the player has finished the map
     *
     * First the code checks if the map has finished by checking the boolean
     * Next the map win sound is played if the mute is not active
     * Next the time, player, camera, all the enemies, and the cloud are stopped
     * Lastly other methods are activated
     */
    private void checkMapWin() {
        if (mapWin){
            // Plays map finish sound only once
            if (!finishSoundPlayed && !host.mute) {
                host.mapFinishSound.play();
                finishSoundPlayed = true;
            }
            host.time.stop();
            host.player.stop();
            host.camera.stop();
            for (int i = 0; i<enemies.size(); i++) {
                Enemy enemy = enemies.get(i);
                enemy.stop();
            }
            cloud.stop();
            drawWin();
            checkNewHighScore();
            returnMenu();
        }
    }

    /**
     * Draws the winning screen either in english or finnish
     */
    private void drawWin() {
        host.batch.begin();
        menuRect.setPosition(host.camera.getPositionX() - 600, host.camera.getPositionY() - 350);
        host.batch.draw(mapWinTex,0,host.camera.getPositionY() - mapWinTex.getHeight()/2,mapWinTex.getWidth(),mapWinTex.getHeight());
        if (host.currentLang.equals("eng")){
            host.batch.draw(timeboxEng,host.camera.getPositionX() + 200,host.camera.getPositionY() - 150,timeboxEng.getWidth(),timeboxEng.getHeight());
            host.batch.draw(menuENG, menuRect.getX(), menuRect.getY());
        } else {
            host.batch.draw(timeboxFin,host.camera.getPositionX() + 200,host.camera.getPositionY() - 150,timeboxEng.getWidth(),timeboxEng.getHeight());
            host.batch.draw(menuFIN, menuRect.getX(), menuRect.getY());
        }
        host.fontMedium.draw(host.batch,host.time.getTime(),host.camera.getPositionX() + 225, host.camera.getPositionY() - 30);
        host.batch.end();
    }

    /**
     * Checks if a new high score is achieved
     */
    private void checkNewHighScore() {
        // checks for highscore
        if (host.time.getMinutes() < (host.preferences.getInteger("highscoreMin" + host.currentLevel, 100))) {
            newHighscore = true;
        }
        if (!newHighscore && (host.time.getMinutes() == (host.preferences.getInteger("highscoreMin" + host.currentLevel, 100))) &&
                (host.time.getSeconds() < host.preferences.getInteger("highscoreSec" + host.currentLevel, 100))) {
            newHighscore = true;
        }
        if (newHighscore) {
            Gdx.app.log("final time","min:" + host.time.getMinutes() + " sec:" + host.time.getSeconds());
            host.preferences.putInteger("highscoreMin" + host.currentLevel, host.time.getMinutes());
            host.preferences.putInteger("highscoreSec" + host.currentLevel, host.time.getSeconds());
            host.preferences.flush();
        }
    }

    /**
     * Returns back to the menu
     */
    private void returnMenu() {
        if (menuRect.contains(touch.x,touch.y)){
            if (Gdx.input.justTouched() && !host.mute) {
                host.buttonSound.play();
            }
            touch.set(0,0,0);
            host.gameRun = false;
            host.setScreen(new menuScreen(host));
        }
    }

    /**
     * Draws the help text at the beginning
     */
    private void drawHelp() {
        host.batch.begin();
        if (startTimerDefault > 0){
            host.fontBig.draw(host.batch, "" + startTimerDefault, host.camera.getPositionX() - 45,host.camera.getPositionY() + 90);
            if (host.currentLang.equals("eng")) {
                host.fontSmall.draw(host.batch, "Dodge trees and other objects", host.camera.getPositionX() - 350,host.camera.getPositionY() - 80);
            } else {
                host.fontSmall.draw(host.batch, "Väistä puita ja muita esineitä", host.camera.getPositionX() - 350,host.camera.getPositionY() - 80);
            }
        }
        host.batch.end();
    }

    /**
     * draws all the ui elements
     *
     * First the timer is drawn
     * Next the pause button
     * If the pause button is pressed the pause menu is drawn
     */
    private void drawUI() {
        host.batch.begin();
        // draws the time text
        host.time.drawText(host.batch,host.camera, host.getCameraWidth(), host.getCameraHeight(), host.fontSmall);

        // draws the pause button
        host.batch.draw(pauseButton, pauseRect.getX(),pauseRect.getY());

        // pause menu
        if (gamePause){
            resumeRect.setPosition(host.camera.getPositionX() - resumeENG.getWidth() / 2, host.camera.getPositionY() + 50);
            settingsRect.setPosition(host.camera.getPositionX() - settingsENG.getWidth() / 2, host.camera.getPositionY() - 85);
            menuRect.setPosition(host.camera.getPositionX() - menuENG.getWidth() / 2, host.camera.getPositionY() - 225);
            host.batch.draw(popUp, host.getCameraWidth() / 2 - popUp.getWidth() / 2, host.camera.getPositionY() - popUp.getHeight() / 2);
            if (host.currentLang.equals("eng")) {
                host.batch.draw(resumeENG, resumeRect.getX(), resumeRect.getY());
                host.batch.draw(menuENG, menuRect.getX(), menuRect.getY());
                host.batch.draw(settingsENG, settingsRect.getX(), settingsRect.getY());
            } else {
                host.batch.draw(resumeFIN, resumeRect.getX(), resumeRect.getY());
                host.batch.draw(menuFIN, menuRect.getX(), menuRect.getY());
                host.batch.draw(settingsFIN, settingsRect.getX(), settingsRect.getY());
            }
        }
        host.batch.end();
    }

    /**
     * Draws the clock
     */
    private void drawClock() {
        host.batch.begin();
        // draws clock
        for (int i = 0; i<clocks.size(); i++) {
            clockPickUp clock = clocks.get(i);
            clock.draw(host);

            // Deletes clock when picked up
            if (clock.hits(host.player.getRectangle())){
                host.clockSound.play(0.8f);
                clocks.remove(i);
                host.time.removeTime();
            }
        }
        host.batch.end();
    }

    /**
     * Changes the players movement if needed
     * Draws the player too
     */
    private void playerDraw() {
        host.batch.begin();
        host.player.changeSpeed(enemies, cloud, host.batch, host.mute, gamePause, mapWin);
        host.player.animate(host.batch);
        host.batch.end();
    }

    /**
     * Does the clouds movement and drawing
     */
    private void moveCloud() {
        host.batch.begin();
        //Moves cloud to the screen
        if (host.map.cloudSpawn(host.camera)) {
            cloudMove = true;
            cloud.compliteMove = false;
        }
        if (cloudMove){
            cloud.moveUp(host.camera);
        } else {
            cloud.move(host.camera);
        }
        if (cloud.compliteMove){
            cloudMove = false;
        }
        cloud.draw(host);
        host.batch.end();
    }

    /**
     * moves and draws all the enemies
     */
    private void moveEnemy() {
        host.batch.begin();
        // Moves enemies
        for (int i = 0; i<enemies.size(); i++) {
            Enemy enemy = enemies.get(i);
            enemy.move();
            enemy.draw(host);

            // Deletes enemy if it goes out of bounds
            if (enemy.getEnemyXpos()<0 || enemy.getEnemyXpos()>host.getMapWidth() ||
                    enemy.getEnemyYpos()<host.camera.getPositionY()-(host.getMapWidth() / 2 + enemy.getEnemyHeight())) {
                enemies.remove(i);
            }
        }
        host.batch.end();
    }

    /**
     * spawns the clocks
     */
    private void spawnClock() {
        // spawns clock if cameras rectangle hits clockSpawn rectangle
        if (host.map.clockSpawn(host.camera)) {
            clocks.add(new clockPickUp());

            clockPickUp clock = clocks.get(clockNumber);
            clock.spawn(host.map.getClockRecLocX(),host.map.getClockRecLocY());

            for (int i=0; i<clocks.size(); i++) {
                if (clocks.get(i) == null) {
                    clockNumber = i;
                }
            }
        }
    }

    /**
     * spawns the enemies
     */
    private void spawnEnemy() {
        // spawns enemy if cameras rectangle hits enemyspawn rectangle
        if (host.map.enemySpawn(host.camera)) {
            enemies.add(new Enemy());

            Enemy enemy = enemies.get(enemyNumber);
            enemy.spawn(host.camera.getPositionX(),host.camera.getPositionY());

            for (int i=0; i<enemies.size(); i++) {
                if (enemies.get(i) == null) {
                    enemyNumber = i;
                }
            }
        }
    }

    /**
     * checks if any of the buttons are pressed
     */
    private void checkButtons() {
        // pauses the game
        if (pauseRect.contains(touch.x,touch.y)){
            if (Gdx.input.justTouched() && !host.mute) {
                host.buttonSound.play();
            }
            touch.set(0,0,0);
            pause();
        }

        // resumes the game
        if (resumeRect.contains(touch.x,touch.y) && gamePause){
            if (Gdx.input.justTouched() && !host.mute) {
                host.buttonSound.play();
            }
            touch.set(0,0,0);
            resume();
        }

        // goes to settings screen
        if (settingsRect.contains(touch.x,touch.y) && gamePause){
            if (Gdx.input.justTouched() && !host.mute) {
                host.buttonSound.play();
            }
            touch.set(0,0,0);
            host.cameraPosX = host.camera.getPositionX();
            host.cameraPosY = host.camera.getPositionY();
            host.gameRun = true;
            host.setScreen(new settingsScreen(host));
        }

        // goes to menu
        if (menuRect.contains(touch.x,touch.y) && gamePause){
            if (Gdx.input.justTouched() && !host.mute) {
                host.buttonSound.play();
            }
            touch.set(0,0,0);
            host.gameRun = false;
            host.setScreen(new menuScreen(host));
        }
    }

    /**
     * checks if the map has started
     */
    private void checkMapStart() {
        if(mapStart){
            host.time.stop();
            host.player.stop();
            host.camera.stop();
            for (int i = 0; i<enemies.size(); i++) {
                Enemy enemy = enemies.get(i);
                enemy.stop();
            }
            cloud.stop();
            startTimer += Gdx.graphics.getRawDeltaTime();
            if(startTimer >= 0.9f){
                startTimer = 0;
                startTimerDefault--;
            }
            if (startTimerDefault == 0){
                mapStart = false;
                host.time.resume();
                host.player.resume();
                host.camera.resume();
                for (int i = 0; i<enemies.size(); i++) {
                    Enemy enemy = enemies.get(i);
                    enemy.resume();
                }
                cloud.resume();
            }
        }
    }

    /**
     * sets the sensitivity if player is playing for the first time
     */
    private void firstSens() {
        // First time setting for default sensitivities
        if (upSens == 0 || downSens == 0 || leftSens == 0 || rightSens == 0) {
            upSens = 1f;
            downSens = 1f;
            rightSens = 1f;
            leftSens = 1f;
        }
    }

    /**
     * saves the touch location
     */
    private void saveTouch() {
        if (Gdx.input.justTouched() && !mapStart){
            touch.set(Gdx.input.getX(),Gdx.input.getY(),0);
            host.camera.unproject(touch);
        }
    }

    /**
     * Sets the sensitivity to preference
     */
    private void setSensitivity() {
        upSens = host.preferences.getFloat("upSens", -1.2f);
        downSens = host.preferences.getFloat("downSens", 1.2f);
        leftSens = host.preferences.getFloat("leftSens", 1.2f);
        rightSens = host.preferences.getFloat("rightSens", -1.2f);
    }

    /**
     * refresh the screen
     */
    private void refreshScreen() {
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    /**
     * Updates the camera and time
     */
    private void update() {
        host.camera.update();
        host.time.update();
        host.map.setMap(host.camera.getCamera());
        host.batch.setProjectionMatrix(host.camera.combined());
    }

    /**
     * does nothing
     * @param width nothing
     * @param height nothing
     */
    @Override
    public void resize(int width, int height) {

    }

    /**
     * pauses the game
     */
    @Override
    public void pause() {
        gamePause = true;
        host.time.stop();
        host.player.stop();
        host.camera.stop();
        for (int i = 0; i<enemies.size(); i++) {
            Enemy enemy = enemies.get(i);
            enemy.stop();
        }
        cloud.stop();
    }

    /**
     * resumes the game
     */
    @Override
    public void resume() {
        gamePause = false;
        host.time.resume();
        host.player.resume();
        host.camera.resume();
        for (int i = 0; i<enemies.size(); i++) {
            Enemy enemy = enemies.get(i);
            enemy.resume();
        }
        cloud.resume();
    }

    /**
     * does nothing
     */
    @Override
    public void hide() {

    }

    /**
     * disposes of textures
     */
    @Override
    public void dispose() {
        resumeENG.dispose();
        settingsENG.dispose();
        menuENG.dispose();
        resumeFIN.dispose();
        settingsFIN.dispose();
        menuFIN.dispose();
        pauseButton.dispose();
        popUp.dispose();
        mapWinTex.dispose();
        timeboxEng.dispose();
        timeboxFin.dispose();
    }
}
