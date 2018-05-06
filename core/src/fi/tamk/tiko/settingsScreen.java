package fi.tamk.tiko;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

/**
 * Settings screen where user can mute the game, go to sensitivity and calibration screen to adjust those.
 *
 * @author Toni Vänttinen and Jimi Savola
 * @version 1.8, 05/02/18
 * @since 1.8
 */
public class settingsScreen implements Screen{

    BirdsVoyage host;

    // Background image texture
    private Texture background;

    // Texture for English buttons
    private Texture zeroButtonTexEN;
    private Texture sensButtonTexEN;
    private Texture retGameButTexEN;

    // Texture for Finnish buttons
    private Texture zeroButtonTexFI;
    private Texture sensButtonTexFI;
    private Texture retGameButTexFI;

    // Texture for mute button
    private Texture muteButtonTex;
    private Texture unmuteButtonTex;

    // Texture for menu button
    private Texture menuButtonTex;

    // Texture for language buttons
    private Texture enGBButtonTex;
    private Texture fiFIButtonTex;

    // Rectangles for all the buttons
    private Rectangle zeroButtonRec;
    private Rectangle sensButtonRec;
    private Rectangle muteButtonRec;
    private Rectangle menuButtonRec;
    private Rectangle enGBButtonRec;
    private Rectangle fiFIButtonRec;
    private Rectangle retGameButRec;

    // players touch input location
    private Vector3 touch;

    /**
     * Constructor for the settings screen.
     * @param host main games java class
     */
    public settingsScreen(BirdsVoyage host){
        // Makes the main java class the host
        this.host = host;
        // Users touch input location creation
        touch = new Vector3(0,0,0);

        // Loads the background image for the screen
        background = new Texture(Gdx.files.internal("menuBack2.png"));

        //loads the mute buttons
        muteButtonTex = new Texture(Gdx.files.internal("soundOFF.png"));
        unmuteButtonTex = new Texture(Gdx.files.internal("soundON.png"));

        //loads back button
        menuButtonTex = new Texture(Gdx.files.internal("goback.png"));

        //loads english buttons
        zeroButtonTexEN = new Texture(Gdx.files.internal("startingposENG.png"));
        sensButtonTexEN = new Texture(Gdx.files.internal("sensitivityENG.png"));
        retGameButTexEN = new Texture(Gdx.files.internal("continueENG.png"));

        // loads finnish buttons
        zeroButtonTexFI = new Texture(Gdx.files.internal("startingposFIN.png"));
        sensButtonTexFI = new Texture(Gdx.files.internal("sensitivityFIN.png"));
        retGameButTexFI = new Texture(Gdx.files.internal("continueFIN.png"));

        // loads flags
        enGBButtonTex = new Texture(Gdx.files.internal("eng.png"));
        fiFIButtonTex = new Texture(Gdx.files.internal("fin.png"));

        // setting up rectangles
        retGameButRec = new Rectangle(
                host.getCameraWidth()/2 - (retGameButTexEN.getWidth()/2),
                500,
                retGameButTexEN.getWidth(),
                retGameButTexEN.getHeight()
        );
        zeroButtonRec = new Rectangle(
                host.getCameraWidth()/2 - (zeroButtonTexEN.getWidth()/2),
                375,
                zeroButtonTexEN.getWidth(),
                zeroButtonTexEN.getHeight()
        );
        sensButtonRec = new Rectangle(
                host.getCameraWidth()/2 - (sensButtonTexEN.getWidth()/2),
                250,
                sensButtonTexEN.getWidth(),
                sensButtonTexEN.getHeight()
        );
        muteButtonRec = new Rectangle(
                host.getCameraWidth()/2 - (muteButtonTex.getWidth()/2),
                125,
                muteButtonTex.getWidth(),
                muteButtonTex.getHeight()
        );
        menuButtonRec = new Rectangle(
                5,
                15,
                menuButtonTex.getWidth(),
                menuButtonTex.getHeight()
        );
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

        // Saves the current screen to a string
        host.currentScreen = "settings";
    }

    @Override
    /**
     * Does nothing
     */
    public void show() {

    }

    @Override
    /**
     * Rendering of the game.
     */
    public void render(float delta) {
        updateCamera();
        refreshScreen();
        saveTouchLocation();
        loadMuteButton();

        host.batch.begin();
        drawImages();
        drawTextButtons();
        host.batch.end();

        checkButtonPresses();
    }

    /**
     * Checks if any of the buttons are touched and does actions according to the touch location.
     */
    private void checkButtonPresses() {
        // Go to other screens
        if (zeroButtonRec.contains(touch.x,touch.y)){
            if (Gdx.input.justTouched() && !host.mute) {
                host.buttonSound.play();
            }
            host.setScreen(new calibrationScreen(host));
        }
        if (sensButtonRec.contains(touch.x,touch.y)){
            if (Gdx.input.justTouched() && !host.mute) {
                host.buttonSound.play();
            }
            host.setScreen(new sensitivityScreen(host));
        }

        // Mutes or unmutes the game
        if (muteButtonRec.contains(touch.x,touch.y) && Gdx.input.justTouched()){
            if (Gdx.input.justTouched() && !host.mute) {
                host.buttonSound.play();
            }
            if (Gdx.input.justTouched() && !host.mute){
                host.mute = true;
            } else if (Gdx.input.justTouched() && host.mute){
                host.mute = false;
            }
        }

        // Goes back to previous screen
        if (menuButtonRec.contains(touch.x,touch.y) && !host.gameRun){
            if (Gdx.input.justTouched() && !host.mute) {
                host.buttonSound.play();
            }
            host.setScreen(new menuScreen(host));
        }

        // Continues the game
        if (host.gameRun) {
            if (retGameButRec.contains(touch.x, touch.y)) {
                if (Gdx.input.justTouched() && !host.mute) {
                    host.buttonSound.play();
                }
                host.resumeGame();
                host.setScreen(new gameScreen(host));
            }
        }

        // changes the language
        if (fiFIButtonRec.contains(touch.x,touch.y)){
            if (Gdx.input.justTouched() && !host.mute) {
                host.buttonSound.play();
            }
            host.setLang("fin");
        }
        if (enGBButtonRec.contains(touch.x,touch.y)){
            if (Gdx.input.justTouched() && !host.mute) {
                host.buttonSound.play();
            }
            host.setLang("eng");
        }
    }

    /**
     * Draws all the buttons that has text based on the current language selected.
     */
    private void drawTextButtons() {
        if (host.currentLang.equals("fin")){
            if (host.gameRun) {
                host.batch.draw(retGameButTexFI, retGameButRec.getX(), retGameButRec.getY());
            }
            host.batch.draw(zeroButtonTexFI, zeroButtonRec.getX(), zeroButtonRec.getY());
            host.batch.draw(sensButtonTexFI, sensButtonRec.getX(), sensButtonRec.getY());
        } else {
            if (host.gameRun) {
                host.batch.draw(retGameButTexEN, retGameButRec.getX(), retGameButRec.getY());
            }
            host.batch.draw(zeroButtonTexEN, zeroButtonRec.getX(), zeroButtonRec.getY());
            host.batch.draw(sensButtonTexEN, sensButtonRec.getX(), sensButtonRec.getY());
        }
        if (!host.gameRun){
            host.batch.draw(menuButtonTex, menuButtonRec.getX(), menuButtonRec.getY());
        }
    }

    /**
     * Draws the background image, mute button and language buttons.
     */
    private void drawImages() {
        host.batch.draw(background,0,0);
        host.batch.draw(muteButtonTex, muteButtonRec.getX(), muteButtonRec.getY());
        host.batch.draw(enGBButtonTex, enGBButtonRec.getX(), enGBButtonRec.getY());
        host.batch.draw(fiFIButtonTex, fiFIButtonRec.getX(), fiFIButtonRec.getY());
    }

    /**
     * Loads the mute button texture based on the mute -boolean, so user has a visual
     * indication if the game is muted or not.
     */
    private void loadMuteButton() {
        if (host.mute) {
            muteButtonTex = new Texture(Gdx.files.internal("soundOFF.png"));
        } else {
            muteButtonTex = new Texture(Gdx.files.internal("soundON.png"));
        }
    }

    /**
     * Saves the touch location to the last coordinates where user touched the screen.
     */
    private void saveTouchLocation() {
        if (Gdx.input.justTouched()){
            touch.set(Gdx.input.getX(),Gdx.input.getY(),0);
            host.camera.unproject(touch);
        }
    }

    /**
     * Refreshes the screen and makes the background color.
     */
    private void refreshScreen() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    /**
     * Updates the camera position
     */
    private void updateCamera() {
        host.camera.update();
        host.camera.setPos();
        host.batch.setProjectionMatrix(host.camera.combined());
    }

    @Override
    /**
     * Does nothing
     */
    public void resize(int width, int height) {

    }

    @Override
    /**
     * Does nothing
     */
    public void pause() {

    }

    @Override
    /**
     * Does nothing
     */
    public void resume() {

    }

    @Override
    /**
     * Does nothing
     */
    public void hide() {

    }

    @Override
    /**
     * Does nothing
     */
    public void dispose() {

    }
}
