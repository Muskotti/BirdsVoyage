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

public class calibrationScreen implements Screen {

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
    private Rectangle calibrationFIrec;
    private Rectangle calibrationENrec;

    public calibrationScreen(BirdsVoyage host) {
        touch = new Vector3(0,0,0);
        this.host = host;
        background = new Texture(Gdx.files.internal("menuBack.png"));

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
        calibrationFIrec = new Rectangle(
                100,
                100,
                calibrationFI.getWidth(),
                calibrationFI.getHeight()
        );
        calibrationENrec = new Rectangle(
                100,
                100,
                calibrationEN.getWidth(),
                calibrationEN.getHeight()
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
        if (host.currentLang.equals("fin")) {
            host.batch.draw(calibrationFI, calibrationFIrec.getX(), calibrationFIrec.getY());
        }
        else {
            host.batch.draw(calibrationEN, calibrationENrec.getX(), calibrationENrec.getY());
        }
        host.batch.end();

        // draws language buttons
        host.batch.begin();
        host.batch.draw(background,0,0);
        host.batch.draw(enGBButtonTex, enGBButtonRec.getX(), enGBButtonRec.getY());
        host.batch.draw(fiFIButtonTex, fiFIButtonRec.getX(), fiFIButtonRec.getY());

        if (!host.gameRun){
            host.batch.draw(menuButtonTex, menuButtonRec.getX(), menuButtonRec.getY());
        }
        host.batch.end();

        if (menuButtonRec.contains(touch.x,touch.y)){
            host.setScreen(new settingsScreen(host));
        }

        // changes the language
        if (fiFIButtonRec.contains(touch.x,touch.y)){
            host.setLang("fin");
        }
        if (enGBButtonRec.contains(touch.x,touch.y)){
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
