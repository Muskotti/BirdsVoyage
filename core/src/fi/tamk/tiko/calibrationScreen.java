package fi.tamk.tiko;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

import java.awt.TextArea;

/**
 * Created by Jimi on 12.4.2018.
 */

public class calibrationScreen implements Screen, SoundAndMusic {

    BirdsVoyage host;

    private Texture background;

    private Vector3 touch;

    // Texture for menu button
    private Texture menuButtonTex;

    //Textures for calibration buttons
    private Texture calibrationFI;
    private Texture calibrationEN;

    // Textures for language buttons
    private Texture enGBButtonTex;
    private Texture fiFIButtonTex;

    // Rectangles for buttons
    private Rectangle enGBButtonRec;
    private Rectangle fiFIButtonRec;
    private Rectangle menuButtonRec;
    private Rectangle calibrationRec;

    public calibrationScreen(BirdsVoyage host) {
        touch = new Vector3(0,0,0);
        this.host = host;
        background = new Texture(Gdx.files.internal("menuBack2.png"));

        //loads menu button
        menuButtonTex = new Texture(Gdx.files.internal("goback.png"));

        // Loads calibration buttons
        calibrationFI = new Texture(Gdx.files.internal("calibrateFIN.png"));
        calibrationEN = new Texture(Gdx.files.internal("calibrateENG.png"));

        // loads flags
        enGBButtonTex = new Texture(Gdx.files.internal("eng.png"));
        fiFIButtonTex = new Texture(Gdx.files.internal("fin.png"));

        // Setting up rectangles
        enGBButtonRec = new Rectangle(
                host.getCameraWidth() - enGBButtonTex.getWidth()*2 - 5,
                2,
                enGBButtonTex.getWidth(),
                enGBButtonTex.getHeight()
        );
        fiFIButtonRec = new Rectangle(
                host.getCameraWidth() - fiFIButtonTex.getWidth(),
                2,
                fiFIButtonTex.getWidth(),
                fiFIButtonTex.getHeight()
        );
        menuButtonRec = new Rectangle(
                5,
                15,
                menuButtonTex.getWidth(),
                menuButtonTex.getHeight()
        );
        calibrationRec = new Rectangle(
                host.camera.getPositionX() - calibrationEN.getWidth()/2,
                host.camera.getPositionY(),
                calibrationFI.getWidth(),
                calibrationFI.getHeight()
        );
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        host.camera.update();
        host.camera.setPos();
        host.batch.setProjectionMatrix(host.camera.combined());
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // saves touch location
        if (Gdx.input.justTouched()){
            touch.set(Gdx.input.getX(),Gdx.input.getY(),0);
            host.camera.unproject(touch);
        }

        // Draws calibration buttons
        host.batch.begin();
        host.batch.draw(background,0,0);
        if (host.currentLang.equals("fin")) {
            host.batch.draw(calibrationFI, calibrationRec.getX(), calibrationRec.getY());
            host.fontSmall.draw(host.batch,"Aseta uusi nolla piste",host.camera.getPositionX() - 300,host.camera.getPositionY() + 200);
        }
        else {
            host.batch.draw(calibrationEN, calibrationRec.getX(), calibrationRec.getY());
            host.fontSmall.draw(host.batch,"Set new default position",host.camera.getPositionX() - 300,host.camera.getPositionY() + 200);
        }

        // draws language buttons
        host.batch.draw(enGBButtonTex, enGBButtonRec.getX(), enGBButtonRec.getY());
        host.batch.draw(fiFIButtonTex, fiFIButtonRec.getX(), fiFIButtonRec.getY());

        //back to settings
        host.batch.draw(menuButtonTex, menuButtonRec.getX(), menuButtonRec.getY());

        // calibrates the new position
        if (calibrationRec.contains(touch.x,touch.y) && calibrationRec.contains(touch.x,touch.y)) {
            if (Gdx.input.justTouched() && !host.mute) {
                buttonSound.play();
            }
            host.player.defaultPositionX = Gdx.input.getAccelerometerY();
            host.player.defaultPositionY = Gdx.input.getAccelerometerZ();
            host.preferences.putFloat("defX",Gdx.input.getAccelerometerY());
            host.preferences.putFloat("defY",Gdx.input.getAccelerometerZ());
            host.preferences.flush();

            if (host.player.defaultPositionX < 0) {
                host.player.defaultPositionX = -Math.abs(host.player.defaultPositionX);
            }

            if (host.player.defaultPositionY < 0) {
                host.player.defaultPositionY = -Math.abs(host.player.defaultPositionY);
            }
            if (host.currentLang.equals("fin")) {
                host.fontSmall.draw(host.batch,"Uusi nollapiste asetettu!",host.camera.getPositionX() - 300,host.camera.getPositionY() - 50);
            }
            else {
                host.fontSmall.draw(host.batch,"New default position set!",host.camera.getPositionX() - 300,host.camera.getPositionY() - 50);
            }
        }

        host.batch.end();

        if (menuButtonRec.contains(touch.x,touch.y)){
            if (Gdx.input.justTouched() && !host.mute) {
                buttonSound.play();
            }
            host.setScreen(new settingsScreen(host));
        }

        // changes the language
        if (fiFIButtonRec.contains(touch.x,touch.y)){
            if (Gdx.input.justTouched() && !host.mute) {
                buttonSound.play();
            }
            host.setLang("fin");
        }
        if (enGBButtonRec.contains(touch.x,touch.y)){
            if (Gdx.input.justTouched() && !host.mute) {
                buttonSound.play();
            }
            host.setLang("eng");
        }
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
