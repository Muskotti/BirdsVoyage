package fi.tamk.tiko;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

import java.util.ArrayList;

public class gameScreen implements Screen, SoundAndMusic {
    BirdsVoyage host;

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

    Vector3 touch;

    int enemyNumber = 0;
    int clockNumber = 0;

    private float startTimer;
    private int startTimerDefault = 5;

    boolean gamePause = false;
    boolean mapWin = false;
    boolean mapStart = true;
    boolean cloudMove = false;

    ArrayList<Enemy> enemies = new ArrayList<Enemy>();
    ArrayList<clockPickUp> clocks = new ArrayList<clockPickUp>();

    StormCloud cloud;

    // Sensitivities
    float downSens;
    float leftSens;
    float upSens;
    float rightSens;

    boolean newHighscore;

    public gameScreen(BirdsVoyage host){
        font = new BitmapFont();

        this.host = host;
        touch = new Vector3(0,0,0);

        cloud = new StormCloud();

        pauseButton = new Texture(Gdx.files.internal("Pause.png"));
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

        host.currentScreen = "game";
        newHighscore = false;
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        host.camera.update();
        host.time.update();
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        host.map.setMap(host.camera.getCamera());
        host.batch.setProjectionMatrix(host.camera.combined());

        upSens = host.preferences.getFloat("upSens", 1.2f);
        downSens = host.preferences.getFloat("downSens", 1.2f);
        leftSens = host.preferences.getFloat("leftSens", 1.2f);
        rightSens = host.preferences.getFloat("rightSens", 1.2f);

        host.player.setSens(upSens, downSens, leftSens, rightSens);
        host.player.move();
        host.player.fixPosition(host.camera.getCamera());
        host.map.checkCollision(host.player);
        mapWin = host.map.checkFinish(host.player);
        host.camera.cameraMove();
        pauseRect.setPosition(host.camera.getPositionX() + (host.getCameraWidth()/2) - pauseButton.getWidth() - 5,
                host.camera.getPositionY() - (host.getCameraHeight()/2));

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

        if (Gdx.input.justTouched() && !mapStart){
            touch.set(Gdx.input.getX(),Gdx.input.getY(),0);
            host.camera.unproject(touch);
        }

        // pauses the game
        if (pauseRect.contains(touch.x,touch.y)){
            if (Gdx.input.justTouched()) {
                buttonSound.play();
            }
            touch.set(0,0,0);
            pause();
        }

        // resumes the game
        if (resumeRect.contains(touch.x,touch.y) && gamePause){
            if (Gdx.input.justTouched()) {
                buttonSound.play();
            }
            touch.set(0,0,0);
            resume();
        }

        // goes to settings screen
        if (settingsRect.contains(touch.x,touch.y) && gamePause){
            if (Gdx.input.justTouched()) {
                buttonSound.play();
            }
            touch.set(0,0,0);
            host.cameraPosX = host.camera.getPositionX();
            host.cameraPosY = host.camera.getPositionY();
            host.gameRun = true;
            host.lastScreen = "game";
            host.setScreen(new settingsScreen(host));
        }

        // goes to menu
        if (menuRect.contains(touch.x,touch.y) && gamePause){
            if (Gdx.input.justTouched()) {
                buttonSound.play();
            }
            touch.set(0,0,0);
            host.lastScreen = "game";
            host.gameRun = false;
            host.setScreen(new menuScreen(host));
        }

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
        host.player.changeSpeed(enemies, cloud, host.batch);
        host.player.animate(host.batch);

        // draws clock
        for (int i = 0; i<clocks.size(); i++) {
            clockPickUp clock = clocks.get(i);
            clock.draw(host);

            // Deletes clock when picked up
            if (clock.hits(host.player.getRectangle())){
                clockSound.play();
                clocks.remove(i);
                host.time.removeTime();
            }
        }

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

        // Draws the help text at the beginning
        if (startTimerDefault > 0){
            host.fontBig.draw(host.batch, "" + startTimerDefault, host.camera.getPositionX() - 45,host.camera.getPositionY() + 90);
            if (host.currentLang.equals("eng")) {
                host.fontSmall.draw(host.batch, "Dodge trees and other objects", host.camera.getPositionX() - 350,host.camera.getPositionY() - 80);
            } else {
                host.fontSmall.draw(host.batch, "Väistä puita ja muita esineitä", host.camera.getPositionX() - 350,host.camera.getPositionY() - 80);
            }
        }

        // Poista myöhemmin. Tässä anturiarvot
        //font.draw(host.batch, "X = " + Gdx.input.getAccelerometerY(), host.camera.getPositionX() + 420, host.camera.getPositionY() + 350);
        //font.draw(host.batch, "Y = " + Gdx.input.getAccelerometerZ(), host.camera.getPositionX() + 420, host.camera.getPositionY() + 300);
        host.batch.end();

        if (mapWin){
            Gdx.app.log("asd","min:" + host.time.getMinutes() + " sec:" + host.time.getSeconds());
            host.time.stop();
            host.player.stop();
            host.camera.stop();
            for (int i = 0; i<enemies.size(); i++) {
                Enemy enemy = enemies.get(i);
                enemy.stop();
            }
            cloud.stop();
            host.lastScreen = "game";
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

            // checks for highscore
            if (host.time.getMinutes() < (host.preferences.getInteger("highscoreMin" + host.currentLevel, 100))) {
                newHighscore = true;
            }
            if (!newHighscore && (host.time.getMinutes() <= (host.preferences.getInteger("highscoreMin" + host.currentLevel, 100))) &&
                    (host.time.getSeconds() < host.preferences.getInteger("highscoreSec" + host.currentLevel, 100))) {
                newHighscore = true;
            }
            if (newHighscore) {
                Gdx.app.log("final time","min:" + host.time.getMinutes() + " sec:" + host.time.getSeconds());
                host.preferences.putInteger("highscoreMin" + host.currentLevel, host.time.getMinutes());
                host.preferences.putInteger("highscoreSec" + host.currentLevel, host.time.getSeconds());
                host.preferences.flush();
            }

            // Returns to menu
            if (menuRect.contains(touch.x,touch.y)){
                if (Gdx.input.justTouched()) {
                    buttonSound.play();
                }
                touch.set(0,0,0);
                host.lastScreen = "game";
                host.gameRun = false;
                host.setScreen(new menuScreen(host));
            }
        }
    }

    @Override
    public void resize(int width, int height) {

    }

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

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        resumeENG.dispose();
        settingsENG.dispose();
        menuENG.dispose();
        resumeFIN.dispose();
        settingsFIN.dispose();
        menuFIN.dispose();
    }
}
