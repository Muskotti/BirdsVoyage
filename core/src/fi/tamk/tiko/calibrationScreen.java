package fi.tamk.tiko;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

import java.awt.TextArea;

/**
 * class for the calibration sceen
 *
 * @author Toni Vänttinen & Jimi Savola
 * @version 1.8, 05/02/18
 * @since 1.8
 */

public class calibrationScreen implements Screen, SoundAndMusic {

    // main game java class
    BirdsVoyage host;

    // Texture for the background
    private Texture background;

    // A vector used when the player touches the screen
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

    /**
     * Constructor for the calibration screen
     * @param host main game java class
     */

    public calibrationScreen(BirdsVoyage host) {

        // makes the vector
        touch = new Vector3(0,0,0);

        // gets the main class
        this.host = host;

        // loads the background texture
        background = new Texture(Gdx.files.internal("menuBack2.png"));

        //loads menu button texture
        menuButtonTex = new Texture(Gdx.files.internal("goback.png"));

        // Loads calibration buttons texture
        calibrationFI = new Texture(Gdx.files.internal("calibrateFIN.png"));
        calibrationEN = new Texture(Gdx.files.internal("calibrateENG.png"));

        // loads flags textures
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

    /**
     * does nothing
     */
    @Override
    public void show() {

    }

    /**
     * Renders the calibration screen refresh
     * @param delta gets the delta time
     */
    @Override
    public void render(float delta) {
        setCamera();
        refreshScreen();
        saveTouch();
        drawButtons();
        calibrate();
        checkButton();
    }

    /**
     * Checks if the buttons are pressed
     *
     * First the code checks if the menu button is pressed
     * If it is the screen goes back to the settings screen
     * Next the code checks if the finnish button is pressed
     * If it is the language changes to finnish
     * Next the code checks if the english button is pressed
     * If it is the language changes to english
     */
    private void checkButton() {
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

    /**
     * Calibrates a new position if the button is pressed and prints a confirmation message
     *
     * First the the code checks if the calibration button is pressed
     * If it is the code plays the confirmation sound if the mute is not activated
     * next the code sets the new default position for player class and the preferences
     * ----JIMI----
     * Next the code prints out a confirmation text either in finnish or english
     */
    private void calibrate() {
        host.batch.begin();
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
    }

    /**
     * Draws the buttons and some text
     *
     * First the code draws the background
     * Next the tip text is printed either in finnish or english
     * Next the language buttons are drawn
     * After the back to the menu buttons is drawn
     */
    private void drawButtons() {
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
        host.batch.end();
    }

    /**
     * Saves the touch location
     */
    private void saveTouch() {
        if (Gdx.input.justTouched()){
            touch.set(Gdx.input.getX(),Gdx.input.getY(),0);
            host.camera.unproject(touch);
        }
    }

    /**
     * Refreshes the screen
     */
    private void refreshScreen() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    /**
     * Updates and sets the camera to the correct position
     */
    private void setCamera() {
        host.camera.update();
        host.camera.setPos();
        host.batch.setProjectionMatrix(host.camera.combined());
    }

    /**
     * Does nothing
     * @param width nothing
     * @param height nothing
     */
    @Override
    public void resize(int width, int height) {

    }

    /**
     * Pauses the screen
     */

    @Override
    public void pause() {

    }

    /**
     * Resumes the screen
     */
    @Override
    public void resume() {

    }

    /**
     * Does nothing
     */
    @Override
    public void hide() {

    }

    /**
     * Disposes of textures
     */
    @Override
    public void dispose() {
        enGBButtonTex.dispose();
        fiFIButtonTex.dispose();
        menuButtonTex.dispose();
        calibrationEN.dispose();
        calibrationFI.dispose();
        background.dispose();
    }
}
