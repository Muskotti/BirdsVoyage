package fi.tamk.tiko;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

/**
 * Menu is the games main screen and it holds access to level select, high scores and settings.
 *
 * @author Toni Vänttinen & Jimi Savola
 * @version 1.8, 05/02/18
 * @since 1.8
 */
public class menuScreen implements Screen {

    // Main java class as host
    private BirdsVoyage host;

    // Texture for background image
    private Texture background;

    // Texture for English buttons
    private Texture playButtonTexEN;
    private Texture highButtonTexEN;
    private Texture settingsButtonTexEN;
    private Texture exitButtonTexEN;

    // Texture for Finnish buttons
    private Texture playButtonTexFI;
    private Texture highButtonTexFI;
    private Texture settingsButtonTexFI;
    private Texture exitButtonTexFI;

    // Texture for language buttons
    private Texture enGBButtonTex;
    private Texture fiFIButtonTex;

    // game logo
    private Texture logoTex;

    // Rectangles for all the buttons
    private Rectangle playButtonRec;
    private Rectangle highButtonRec;
    private Rectangle settingsButtonRec;
    private Rectangle exitButtonRec;
    private Rectangle enGBButtonRec;
    private Rectangle fiFIButtonRec;

    // players touch input
    private Vector3 touch;

    /**
     * Constructor for the menu screen.
     *
     * @param host main java class of the game
     */
    public menuScreen(BirdsVoyage host){
        // Initialization of the touch location
        touch = new Vector3(0,0,0);

        // Main java class as host
        this.host = host;

        // Loading the background image
        background = new Texture(Gdx.files.internal("menuBack.png"));

        // Loads english buttons
        playButtonTexEN = new Texture(Gdx.files.internal("playENG.png"));
        highButtonTexEN = new Texture(Gdx.files.internal("highscoreENG.png"));
        settingsButtonTexEN = new Texture(Gdx.files.internal("settingsENG.png"));
        exitButtonTexEN = new Texture(Gdx.files.internal("exitENG.png"));

        // Loads finnish buttons
        playButtonTexFI = new Texture(Gdx.files.internal("playFIN.png"));
        highButtonTexFI = new Texture(Gdx.files.internal("highscoreFIN.png"));
        settingsButtonTexFI = new Texture(Gdx.files.internal("settingsFIN.png"));
        exitButtonTexFI = new Texture(Gdx.files.internal("exitFIN.png"));

        // Loads the flags
        enGBButtonTex = new Texture(Gdx.files.internal("eng.png"));
        fiFIButtonTex = new Texture(Gdx.files.internal("fin.png"));

        // Loads the logo
        logoTex = new Texture(Gdx.files.internal("LOGOtransparent.png"));

        // Setting up rectangles
        playButtonRec = new Rectangle(
                host.getCameraWidth()/2 - (playButtonTexEN.getWidth()/2),
                317,
                playButtonTexEN.getWidth(),
                playButtonTexEN.getHeight()
        );

        highButtonRec = new Rectangle(
                host.getCameraWidth()/2 - (playButtonTexEN.getWidth()/2),
                212,
                playButtonTexEN.getWidth(),
                playButtonTexEN.getHeight()
        );

        settingsButtonRec = new Rectangle(
                host.getCameraWidth()/2 - (settingsButtonTexEN.getWidth()/2),
                107,
                settingsButtonTexEN.getWidth(),
                settingsButtonTexEN.getHeight()
        );
        exitButtonRec = new Rectangle(
                host.getCameraWidth()/2 - (exitButtonTexEN.getWidth()/2),
                2,
                exitButtonTexEN.getWidth(),
                exitButtonTexEN.getHeight()
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

        // Current screen to string
        host.currentScreen = "menu";
    }

    @Override
    /**
     * Does nothing.
     */
    public void show() {

    }

    @Override
    /**
     * Renders the menu screen
     */
    public void render(float delta) {
        updateCamera();
        refreshScreen();
        saveTouchLocation();

        // draws language buttons
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
        // Checks if the menus buttons are touched
        if (playButtonRec.contains(touch.x,touch.y)){
            if (Gdx.input.justTouched() && !host.mute) {
                host.buttonSound.play();
            }
            host.reset();
            host.setScreen(new levelScreen(host));
        }
        if (settingsButtonRec.contains(touch.x,touch.y)){
            if (Gdx.input.justTouched() && !host.mute) {
                host.buttonSound.play();
            }
            host.setScreen(new settingsScreen(host));
        }
        if (highButtonRec.contains(touch.x, touch.y)) {
            if (Gdx.input.justTouched() && !host.mute) {
                host.buttonSound.play();
            }
            host.setScreen(new highscoreScreen(host));
        }
        if (exitButtonRec.contains(touch.x,touch.y)){
            if (Gdx.input.justTouched() && !host.mute) {
                host.buttonSound.play();
            }
            Gdx.app.exit();
        }

        // Changes the language
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
            host.batch.draw(playButtonTexFI, playButtonRec.getX(), playButtonRec.getY());
            host.batch.draw(highButtonTexFI, highButtonRec.getX(), highButtonRec.getY());
            host.batch.draw(settingsButtonTexFI, settingsButtonRec.getX(), settingsButtonRec.getY());
            host.batch.draw(exitButtonTexFI, exitButtonRec.getX(), exitButtonRec.getY());
        } else {
            host.batch.draw(playButtonTexEN, playButtonRec.getX(), playButtonRec.getY());
            host.batch.draw(highButtonTexEN, highButtonRec.getX(), highButtonRec.getY());
            host.batch.draw(settingsButtonTexEN, settingsButtonRec.getX(), settingsButtonRec.getY());
            host.batch.draw(exitButtonTexEN, exitButtonRec.getX(), exitButtonRec.getY());
        }
    }

    /**
     * Draws the background image, game logo and language buttons.
     */
    private void drawImages() {
        // Background image
        host.batch.draw(background,0,0);

        // Language buttons
        host.batch.draw(enGBButtonTex, enGBButtonRec.getX(), enGBButtonRec.getY());
        host.batch.draw(fiFIButtonTex, fiFIButtonRec.getX(), fiFIButtonRec.getY());

        // Logo picture
        host.batch.draw(logoTex, host.camera.getPositionX() - logoTex.getWidth()/2,host.camera.getPositionY() + 60);
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
     * Does nothing.
     */
    public void resize(int width, int height) {

    }

    @Override
    /**
     * Does nothing.
     */
    public void pause() {

    }

    @Override
    /**
     * Does nothing.
     */
    public void resume() {

    }

    @Override
    /**
     * Does nothing.
     */
    public void hide() {

    }

    @Override
    /**
     * Disposes everything.
     */
    public void dispose() {
        playButtonTexEN.dispose();
        highButtonTexEN.dispose();
        settingsButtonTexEN.dispose();
        exitButtonTexEN.dispose();
        playButtonTexFI.dispose();
        highButtonTexFI.dispose();
        settingsButtonTexFI.dispose();
        exitButtonTexFI.dispose();
        enGBButtonTex.dispose();
        fiFIButtonTex.dispose();
        background.dispose();
    }
}
