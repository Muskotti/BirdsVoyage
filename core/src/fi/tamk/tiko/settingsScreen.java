package fi.tamk.tiko;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

public class settingsScreen implements Screen{

    BirdsVoyage host;

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

    // players touch input
    private Vector3 touch;

    public settingsScreen(BirdsVoyage host){
        this.host = host;
        touch = new Vector3(0,0,0);

        background = new Texture(Gdx.files.internal("menuBack2.png"));

        //loads the mute button
        muteButtonTex = new Texture(Gdx.files.internal("soundOFF.png"));
        unmuteButtonTex = new Texture(Gdx.files.internal("soundON.png"));

        //loads menu button
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

        host.currentScreen = "settings";
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

        // draws the language, mute and menu buttons
        host.batch.begin();
        host.batch.draw(background,0,0);
        host.batch.draw(muteButtonTex, muteButtonRec.getX(), muteButtonRec.getY());
        host.batch.draw(enGBButtonTex, enGBButtonRec.getX(), enGBButtonRec.getY());
        host.batch.draw(fiFIButtonTex, fiFIButtonRec.getX(), fiFIButtonRec.getY());

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

        host.batch.end();

        // checks if the buttons are touched
        if (zeroButtonRec.contains(touch.x,touch.y)){
            host.setScreen(new calibrationScreen(host));
        }
        if (sensButtonRec.contains(touch.x,touch.y)){
            host.setScreen(new sensitivityScreen(host));
        }
        if (muteButtonRec.contains(touch.x,touch.y)){

        }
        if (menuButtonRec.contains(touch.x,touch.y) && !host.gameRun){
            host.lastScreen = "settings";
            host.setScreen(new menuScreen(host));
        }
        if (host.gameRun) {
            if (retGameButRec.contains(touch.x, touch.y)) {
                host.resumeGame();
                host.setScreen(new gameScreen(host));
            }
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
