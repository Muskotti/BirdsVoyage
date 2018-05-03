package fi.tamk.tiko;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.particles.ResourceData;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

import java.awt.TextArea;

public class highscoreScreen implements Screen, SoundAndMusic {

    BirdsVoyage host;

    private Texture background;

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

    public highscoreScreen(BirdsVoyage host) {
        touch = new Vector3(0,0,0);
        this.host = host;
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

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

        //updates camera
        host.camera.update();

        // sets camera position
        host.camera.setPos();
        host.batch.setProjectionMatrix(host.camera.combined());

        //draws background
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // saves touch location
        if (Gdx.input.justTouched()){
            touch.set(Gdx.input.getX(),Gdx.input.getY(),0);
            host.camera.unproject(touch);
        }

        // draws language buttons
        host.batch.begin();
        host.batch.draw(background,0,0);
        host.batch.draw(enGBButtonTex, enGBButtonRec.getX(), enGBButtonRec.getY());
        host.batch.draw(fiFIButtonTex, fiFIButtonRec.getX(), fiFIButtonRec.getY());

        // Draws back button
        if (!host.gameRun){
            host.batch.draw(menuButtonTex, menuButtonRec.getX(), menuButtonRec.getY());
        }

        // High scores location variables
        int left = 550;
        int middle = 100;
        int right = 300;

        int high = 300;
        int middleHeight = 100;
        int low = 100;

        int score = 50;

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


        // draws easy level times
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