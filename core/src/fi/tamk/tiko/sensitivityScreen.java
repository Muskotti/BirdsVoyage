package fi.tamk.tiko;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

/**
 * Sensitivity screen where user can adjust the accelerometer sensitivities. Each 4 direction
 * can be adjusted.
 *
 * @author Toni Vänttinen & Jimi Savola
 * @version 1.8, 05/02/18
 * @since 1.8
 */
public class sensitivityScreen implements Screen, SoundAndMusic {

    // Main game java-class as host
    BirdsVoyage host;

    // Backround for the screen
    private Texture background;

    // Users touch location
    private Vector3 touch;

    // Boolean if the slider bar was just touched
    boolean tap;

    // Boolean if user released the touch from slider bar
    boolean sliderRelease;

    // Textures for sliders
    private Texture sliderBar;
    private Texture sliderButton;


    // Texture for menu button
    private Texture menuButtonTex;

    // Texture for language buttons
    private Texture enGBButtonTex;
    private Texture fiFIButtonTex;

    // Rectangles for buttons
    private Rectangle enGBButtonRec;
    private Rectangle fiFIButtonRec;
    private Rectangle menuButtonRec;

    // Rectangles for sliders
    private Rectangle sliderBarRec1, sliderBarRec2, sliderBarRec3, sliderBarRec4;
    private Rectangle sliderButtonRec1, sliderButtonRec2, sliderButtonRec3, sliderButtonRec4;

    /**
     * Constructor for the sensitivity screen
     * @param host main java class of the game
     */
    public sensitivityScreen(BirdsVoyage host) {
        // Last location of the users touch
        touch = new Vector3(0,0,0);
        tap = false;
        this.host = host;

        // Loads the backround image
        background = new Texture(Gdx.files.internal("menuBack2.png"));

        //loads menu button
        menuButtonTex = new Texture(Gdx.files.internal("goback.png"));

        // Loads slider
        sliderBar = new Texture(Gdx.files.internal("sliderBar.png"));
        sliderButton = new Texture(Gdx.files.internal("sliderButton.png"));

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

        // Rectangles for sliders
        sliderBarRec1 = new Rectangle(190, 590, sliderBar.getWidth()+10,sliderBar.getHeight()+10);
        sliderButtonRec1 = new Rectangle(host.preferences.getFloat("button1", sliderBarRec1.getX()+250)
                , 593, sliderButton.getWidth(),sliderButton.getHeight());

        sliderBarRec2 = new Rectangle(190, 490, sliderBar.getWidth()+10,sliderBar.getHeight()+10);
        sliderButtonRec2 = new Rectangle(host.preferences.getFloat("button2", sliderBarRec2.getX()+250)
                , 493, sliderButton.getWidth(),sliderButton.getHeight());

        sliderBarRec3 = new Rectangle(190, 390, sliderBar.getWidth()+10,sliderBar.getHeight()+10);
        sliderButtonRec3 = new Rectangle(host.preferences.getFloat("button3", sliderBarRec3.getX()+250)
                , 393, sliderButton.getWidth(),sliderButton.getHeight());

        sliderBarRec4 = new Rectangle(190, 290, sliderBar.getWidth()+10,sliderBar.getHeight()+10);
        sliderButtonRec4 = new Rectangle(host.preferences.getFloat("button4", sliderBarRec4.getX()+250)
                , 293, sliderButton.getWidth(),sliderButton.getHeight());
    }

    @Override
    public void show() {

    }

    @Override
    /**
     * Renders the game
     */
    public void render(float delta) {
        updateCamera();
        refreshScreen();
        saveTouchLocation();

        host.batch.begin();
        drawImages();
        drawTextButtons();
        host.batch.end();

        checkButtonPresses();
        adjustSensitivity();
    }

    /**
     * Adjusts movement sensitivity on each direction, the amount is stored in preferences.
     * All four sliders uses the same code.
     */
    private void adjustSensitivity() {
        for (int i=0; i<4; i++) {
            Rectangle sliderRec = sliderBarRec1;
            Rectangle buttonRec = sliderButtonRec1;
            String direction = "upSens";
            String button = "button1";
            float sensitivity = 1f;
            float printSens = 1f;
            switch (i) {
                case 0:
                    sliderRec = sliderBarRec1;
                    buttonRec = sliderButtonRec1;
                    direction = "upSens";
                    button = "button1";
                    break;
                case 1:
                    sliderRec = sliderBarRec2;
                    buttonRec = sliderButtonRec2;
                    direction = "downSens";
                    button = "button2";
                    break;
                case 2:
                    sliderRec = sliderBarRec3;
                    buttonRec = sliderButtonRec3;
                    direction = "leftSens";
                    button = "button3";
                    break;
                case 3:
                    sliderRec = sliderBarRec4;
                    buttonRec = sliderButtonRec4;
                    direction = "rightSens";
                    button = "button4";
                    break;
            }

            // When user presses on the slider rectangle, button is moved to that position and then
            // it follows users touch sliding until released.
            if (sliderRec.contains(touch.x,touch.y)) {
                if (Gdx.input.justTouched() && !host.mute) {
                    sliderPressSound.play();
                    tap = true;
                }
                if (tap) {
                    buttonRec.x = touch.x-sliderButton.getWidth()/2;
                    tap = false;
                }
                if (Gdx.input.isTouched()) {
                    sliderRelease = false;
                    buttonRec.x += Gdx.input.getDeltaX();
                    host.preferences.putFloat(button, buttonRec.x);

                    if (buttonRec.x<sliderRec.getX()) {
                        buttonRec.x = sliderRec.getX();
                    }
                    else if (buttonRec.x>sliderRec.getX()+sliderBar.getWidth()-sliderButton.getWidth()/2) {
                        buttonRec.x = sliderRec.getX()+sliderBar.getWidth()-sliderButton.getWidth()/2;
                    }
                }
                else {
                    if (!sliderRelease  && !host.mute) {
                        sliderReleaseSound.play();
                        sliderRelease = true;
                    }
                    host.preferences.putFloat(button, buttonRec.x);
                    host.preferences.flush();
                }
            }
            // Saves sensitivity and button position
            if (buttonRec.getX()>=sliderRec.getX() && buttonRec.getX()<sliderRec.getX()+49) {
                sensitivity = 2f;
                printSens = 0.2f;
            }
            if (buttonRec.getX()>=sliderRec.getX()+50 && buttonRec.getX()<sliderRec.getX()+99) {
                sensitivity = 1.8f;
                printSens = 0.4f;
            }
            if (buttonRec.getX()>=sliderRec.getX()+100 && buttonRec.getX()<sliderRec.getX()+149) {
                sensitivity = 1.6f;
                printSens = 0.6f;
            }
            if (buttonRec.getX()>=sliderRec.getX()+150 && buttonRec.getX()<sliderRec.getX()+199) {
                sensitivity = 1.4f;
                printSens = 0.8f;
            }
            if (buttonRec.getX()>=sliderRec.getX()+200 && buttonRec.getX()<sliderRec.getX()+249) {
                sensitivity = 1.2f;
                printSens = 1f;
            }
            if (buttonRec.getX()>=sliderRec.getX()+250 && buttonRec.getX()<sliderRec.getX()+299) {
                sensitivity = 1f;
                printSens = 1.2f;
            }
            if (buttonRec.getX()>=sliderRec.getX()+300 && buttonRec.getX()<sliderRec.getX()+349) {
                sensitivity = 0.8f;
                printSens = 1.4f;
            }
            if (buttonRec.getX()>=sliderRec.getX()+350 && buttonRec.getX()<sliderRec.getX()+399) {
                sensitivity = 0.6f;
                printSens = 1.6f;
            }
            if (buttonRec.getX()>=sliderRec.getX()+400 && buttonRec.getX()<sliderRec.getX()+449) {
                sensitivity = 0.4f;
                printSens = 1.8f;
            }
            if (buttonRec.getX()>=sliderRec.getX()+450 && buttonRec.getX()<sliderRec.getX()+500) {
                sensitivity = 0.2f;
                printSens = 2f;
            }
            // Sensitivity negative values, if up/right. Positive if down/left
            if (direction == "upSens" || direction == "rightSens") {
                host.preferences.putFloat(direction, -sensitivity);
                host.preferences.flush();
            }
            else {
                host.preferences.putFloat(direction, sensitivity);
                host.preferences.flush();
            }

            // Draws sensitivity values and sensitivity info
            host.batch.begin();
            host.fontSmall.draw(host.batch, String.valueOf(printSens), sliderRec.getX()-30, sliderRec.getY());
            if (host.currentLang.equals("fin")) {
                host.fontSmall.draw(host.batch,"Mitä suurempi arvo sitä herkempi liike",host.camera.getPositionX() - 400,host.camera.getPositionY() - 150);
            } else {
                host.fontSmall.draw(host.batch,"The higher the value the more sensitive movement",host.camera.getPositionX() - 600,host.camera.getPositionY() - 150);
            }
            host.batch.end();
        }
    }

    /**
     * Checks if any of the buttons are touched and does actions according to the touch location.
     */
    private void checkButtonPresses() {
        // Goes back to previous screen
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
     * Draws all the buttons that has text based on the current language selected.
     */
    private void drawTextButtons() {
        // Draws sensitivity texts
        if (host.currentLang.equals("fin")) {
            host.fontSmall.draw(host.batch, "Ylös",
                    sliderBarRec1.getX()+sliderBarRec1.getWidth()+sliderButton.getWidth(),
                    sliderBarRec1.getY()+sliderButton.getHeight()
            );
            host.fontSmall.draw(host.batch, "Alas",
                    sliderBarRec2.getX()+sliderBarRec2.getWidth()+sliderButton.getWidth(),
                    sliderBarRec2.getY()+sliderButton.getHeight()
            );
            host.fontSmall.draw(host.batch, "Vasen",
                    sliderBarRec3.getX()+sliderBarRec3.getWidth()+sliderButton.getWidth(),
                    sliderBarRec3.getY()+sliderButton.getHeight()
            );
            host.fontSmall.draw(host.batch, "Oikea",
                    sliderBarRec4.getX()+sliderBarRec4.getWidth()+sliderButton.getWidth(),
                    sliderBarRec4.getY()+sliderButton.getHeight()
            );
        }
        else {
            host.fontSmall.draw(host.batch, "Up",
                    sliderBarRec1.getX()+sliderBarRec1.getWidth()+sliderButton.getWidth(),
                    sliderBarRec1.getY()+sliderButton.getHeight()
            );
            host.fontSmall.draw(host.batch, "Down",
                    sliderBarRec2.getX()+sliderBarRec2.getWidth()+sliderButton.getWidth(),
                    sliderBarRec2.getY()+sliderButton.getHeight()
            );
            host.fontSmall.draw(host.batch, "Left",
                    sliderBarRec3.getX()+sliderBarRec3.getWidth()+sliderButton.getWidth(),
                    sliderBarRec3.getY()+sliderButton.getHeight()
            );
            host.fontSmall.draw(host.batch, "Right",
                    sliderBarRec4.getX()+sliderBarRec4.getWidth()+sliderButton.getWidth(),
                    sliderBarRec4.getY()+sliderButton.getHeight()
            );
        }
    }

    /**
     * Draws the backround image, mute button and language buttons.
     */
    private void drawImages() {
        // draws language buttons
        host.batch.draw(background,0,0);
        host.batch.draw(enGBButtonTex, enGBButtonRec.getX(), enGBButtonRec.getY());
        host.batch.draw(fiFIButtonTex, fiFIButtonRec.getX(), fiFIButtonRec.getY());

        // Draws sliders and buttons
        host.batch.draw(sliderBar,200, 600);
        host.batch.draw(sliderButton,sliderButtonRec1.getX(), sliderButtonRec1.getY());

        host.batch.draw(sliderBar,200, 500);
        host.batch.draw(sliderButton,sliderButtonRec2.getX(), sliderButtonRec2.getY());

        host.batch.draw(sliderBar,200, 400);
        host.batch.draw(sliderButton,sliderButtonRec3.getX(), sliderButtonRec3.getY());

        host.batch.draw(sliderBar,200, 300);
        host.batch.draw(sliderButton,sliderButtonRec4.getX(), sliderButtonRec4.getY());

        // Draws back button
        host.batch.draw(menuButtonTex, menuButtonRec.getX(), menuButtonRec.getY());
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
     * Updates the camera position.
     */
    private void updateCamera() {
        host.camera.update();
        host.camera.setPos();
        host.batch.setProjectionMatrix(host.camera.combined());
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