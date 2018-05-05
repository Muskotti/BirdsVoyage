package fi.tamk.tiko;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

/**
 * class for the high score screen
 *
 * @author Toni Vänttinen & Jimi Savola
 * @version 1.8, 05/02/18
 * @since 1.8
 */
public class highscoreScreen implements Screen, SoundAndMusic {

    // the main class
    BirdsVoyage host;

    // background texture
    private Texture background;

    // Vector3 for touching
    private Vector3 touch;

    // Texture fot menu button
    private Texture menuButtonTex;

    // Texture for language buttons
    private Texture enGBButtonTex;
    private Texture fiFIButtonTex;

    // Rectangles for buttons
    private Rectangle enGBButtonRec;
    private Rectangle fiFIButtonRec;
    private Rectangle menuButtonRec;

    /**
     * Constructor for the high score screen
     * @param host main game java class
     */
    public highscoreScreen(BirdsVoyage host) {

        // makes the vector
        touch = new Vector3(0,0,0);

        // gets the main class
        this.host = host;

        // loads the background image
        background = new Texture(Gdx.files.internal("menuBack4.png"));

        //loads menu button
        menuButtonTex = new Texture(Gdx.files.internal("goback.png"));

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
    }

    /**
     * does nothing
     */
    @Override
    public void show() {

    }

    /**
     * Renders the highscore screen
     * @param delta gets the delta time
     */
    @Override
    public void render(float delta) {
        setCamera();
        refreshScreen();
        saveTouch();
        drawButtons();
        drawHighScore();
        checkButton();
    }

    /**
     * Checks if the buttons are pressed
     *
     * First the code checks if the menu button is pressed
     * If it is the screen goes back to the main menu screen
     * Next the code checks if the finnish button is pressed
     * If it is the language changes to finnish
     * Next the code checks if the english button is pressed
     * If it is the language changes to english
     */
    private void checkButton() {
        // Goes back to main menu
        if (menuButtonRec.contains(touch.x,touch.y)){
            if (Gdx.input.justTouched() && !host.mute) {
                buttonSound.play();
            }
            host.setScreen(new menuScreen(host));
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
     * Draws the high scores to specific locations and checks if the time value is 10 or smaller
     */
    private void drawHighScore() {
        // High scores location variables
        int left = 550;
        int middle = 100;
        int right = 300;

        int high = 300;
        int middleHeight = 100;
        int low = 100;

        int score = 50;

        host.batch.begin();
        // draws english or finnish text
        if (host.currentLang == "eng"){
            // easy levels
            host.fontSmall.draw(host.batch,"Level 1:", host.camera.getPositionX() - left,host.camera.getPositionY() + high);
            host.fontSmall.draw(host.batch,"Level 2:", host.camera.getPositionX() - middle,host.camera.getPositionY() + high);
            host.fontSmall.draw(host.batch,"Level 3:", host.camera.getPositionX() + right,host.camera.getPositionY() + high);

            //medium levels
            host.fontSmall.draw(host.batch,"Level 4:", host.camera.getPositionX() - left,host.camera.getPositionY() + middleHeight);
            host.fontSmall.draw(host.batch,"Level 5:", host.camera.getPositionX() - middle,host.camera.getPositionY() + middleHeight);
            host.fontSmall.draw(host.batch,"Level 6:", host.camera.getPositionX() + right,host.camera.getPositionY() + middleHeight);

            //hard levels
            host.fontSmall.draw(host.batch,"Level 7:", host.camera.getPositionX() - left,host.camera.getPositionY() - low);
            host.fontSmall.draw(host.batch,"Level 8:", host.camera.getPositionX() - middle,host.camera.getPositionY() - low);
            host.fontSmall.draw(host.batch,"Level 9:", host.camera.getPositionX() + right,host.camera.getPositionY() - low);

        } else {
            // easy levels
            host.fontSmall.draw(host.batch,"Taso 1:", host.camera.getPositionX() - left,host.camera.getPositionY() + high);
            host.fontSmall.draw(host.batch,"Taso 2:", host.camera.getPositionX() - middle,host.camera.getPositionY() + high);
            host.fontSmall.draw(host.batch,"Taso 3:", host.camera.getPositionX() + right,host.camera.getPositionY() + high);

            //medium levels
            host.fontSmall.draw(host.batch,"Taso 4:", host.camera.getPositionX() - left,host.camera.getPositionY() + middleHeight);
            host.fontSmall.draw(host.batch,"Taso 5:", host.camera.getPositionX() - middle,host.camera.getPositionY() + middleHeight);
            host.fontSmall.draw(host.batch,"Taso 6:", host.camera.getPositionX() + right,host.camera.getPositionY() + middleHeight);

            //hard levels
            host.fontSmall.draw(host.batch,"Taso 7:", host.camera.getPositionX() - left,host.camera.getPositionY() - low);
            host.fontSmall.draw(host.batch,"Taso 8:", host.camera.getPositionX() - middle,host.camera.getPositionY() - low);
            host.fontSmall.draw(host.batch,"Taso 9:", host.camera.getPositionX() + right,host.camera.getPositionY() - low);
        }

        String minZero1 = "";
        String secZero1 = "";
        String minZero2 = "";
        String secZero2 = "";
        String minZero3 = "";
        String secZero3 = "";
        String minZero4 = "";
        String secZero4 = "";
        String minZero5 = "";
        String secZero5 = "";
        String minZero6 = "";
        String secZero6 = "";
        String minZero7 = "";
        String secZero7 = "";
        String minZero8 = "";
        String secZero8 = "";
        String minZero9 = "";
        String secZero9 = "";

        // generates 0 if the value is smaller then 10
        for (int i = 0; i < 10; i++) {
            int min = host.preferences.getInteger("highscoreMinlevel" + i);
            int sec = host.preferences.getInteger("highscoreSeclevel" + i);
            String levelMin = "highscoreMinlevel" + i;
            String levelSec = "highscoreSeclevel" + i;
            if (min < 10) {
                if (levelMin.equals("highscoreMinlevel1")){
                    minZero1 = "0";
                }
                if (levelMin.equals("highscoreMinlevel2")){
                    minZero2 = "0";
                }
                if (levelMin.equals("highscoreMinlevel3")){
                    minZero3 = "0";
                }
                if (levelMin.equals("highscoreMinlevel4")){
                    minZero4 = "0";
                }
                if (levelMin.equals("highscoreMinlevel5")){
                    minZero5 = "0";
                }
                if (levelMin.equals("highscoreMinlevel6")){
                    minZero6 = "0";
                }
                if (levelMin.equals("highscoreMinlevel7")){
                    minZero7 = "0";
                }
                if (levelMin.equals("highscoreMinlevel8")){
                    minZero8 = "0";
                }
                if (levelMin.equals("highscoreMinlevel9")){
                    minZero9 = "0";
                }
            }
            if (sec < 10) {
                if (levelSec.equals("highscoreSeclevel1")){
                    secZero1 = "0";
                }
                if (levelSec.equals("highscoreSeclevel2")){
                    secZero2 = "0";
                }
                if (levelSec.equals("highscoreSeclevel3")){
                    secZero3 = "0";
                }
                if (levelSec.equals("highscoreSeclevel4")){
                    secZero4 = "0";
                }
                if (levelSec.equals("highscoreSeclevel5")){
                    secZero5 = "0";
                }
                if (levelSec.equals("highscoreSeclevel6")){
                    secZero6 = "0";
                }
                if (levelSec.equals("highscoreSeclevel7")){
                    secZero7 = "0";
                }
                if (levelSec.equals("highscoreSeclevel8")){
                    secZero8 = "0";
                }
                if (levelSec.equals("highscoreSeclevel9")){
                    secZero9 = "0";
                }
            }
        }

        // draws the times
        host.fontSmall.draw(host.batch, minZero1 + host.preferences.getInteger( "highscoreMinlevel1") + ":" + secZero1 + host.preferences.getInteger("highscoreSeclevel1"),
                host.camera.getPositionX() - left, host.camera.getPositionY() + high - score);
        host.fontSmall.draw(host.batch, minZero2 + host.preferences.getInteger("highscoreMinlevel2") + ":" + secZero2 + host.preferences.getInteger("highscoreSeclevel2"),
                host.camera.getPositionX() - middle, host.camera.getPositionY() + high - score);
        host.fontSmall.draw(host.batch, minZero3 + host.preferences.getInteger("highscoreMinlevel3") + ":" + secZero3 + host.preferences.getInteger("highscoreSeclevel3"),
                host.camera.getPositionX() + right, host.camera.getPositionY() + high - score);

        // draws medium levels times
        host.fontSmall.draw(host.batch, minZero4 + host.preferences.getInteger("highscoreMinlevel4") + ":" + secZero4 + host.preferences.getInteger("highscoreSeclevel4"),
                host.camera.getPositionX() - left, host.camera.getPositionY() + middleHeight - score);
        host.fontSmall.draw(host.batch, minZero5 + host.preferences.getInteger("highscoreMinlevel5") + ":" + secZero5 + host.preferences.getInteger("highscoreSeclevel5"),
                host.camera.getPositionX() - middle, host.camera.getPositionY() + middleHeight - score);
        host.fontSmall.draw(host.batch, minZero6 + host.preferences.getInteger("highscoreMinlevel6") + ":" + secZero6 + host.preferences.getInteger("highscoreSeclevel6"),
                host.camera.getPositionX() + right, host.camera.getPositionY() + middleHeight - score);

        // draws hard levels times
        host.fontSmall.draw(host.batch, minZero7 + host.preferences.getInteger("highscoreMinlevel7") + ":" + secZero7 + host.preferences.getInteger("highscoreSeclevel7"),
                host.camera.getPositionX() - left, host.camera.getPositionY() - low - score);
        host.fontSmall.draw(host.batch, minZero8 + host.preferences.getInteger("highscoreMinlevel8") + ":" + secZero8 + host.preferences.getInteger("highscoreSeclevel8"),
                host.camera.getPositionX() - middle, host.camera.getPositionY() - low - score);
        host.fontSmall.draw(host.batch, minZero9 + host.preferences.getInteger("highscoreMinlevel9") + ":" + secZero9 + host.preferences.getInteger("highscoreSeclevel9"),
                host.camera.getPositionX() + right, host.camera.getPositionY() - low - score);
        host.batch.end();
    }

    /**
     * draws the buttons
     */
    private void drawButtons() {
        // draws language buttons
        host.batch.begin();
        host.batch.draw(background,0,0);
        host.batch.draw(enGBButtonTex, enGBButtonRec.getX(), enGBButtonRec.getY());
        host.batch.draw(fiFIButtonTex, fiFIButtonRec.getX(), fiFIButtonRec.getY());

        // Draws back button
        if (!host.gameRun){
            host.batch.draw(menuButtonTex, menuButtonRec.getX(), menuButtonRec.getY());
        }
        host.batch.end();
    }

    /**
     * saves touch location
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
     * does nothing
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
     * does nothing
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
        background.dispose();
    }
}