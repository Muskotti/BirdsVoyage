package fi.tamk.tiko;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

public class menuScreen implements Screen, SoundAndMusic{

    private BirdsVoyage host;

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

    // constructor
    public menuScreen(BirdsVoyage host){
        touch = new Vector3(0,0,0);
        this.host = host;

        background = new Texture(Gdx.files.internal("menuBack.png"));

        //loads english buttons
        playButtonTexEN = new Texture(Gdx.files.internal("playENG.png"));
        highButtonTexEN = new Texture(Gdx.files.internal("highscoreENG.png"));
        settingsButtonTexEN = new Texture(Gdx.files.internal("settingsENG.png"));
        exitButtonTexEN = new Texture(Gdx.files.internal("exitENG.png"));

        // loads finnish buttons
        playButtonTexFI = new Texture(Gdx.files.internal("playFIN.png"));
        highButtonTexFI = new Texture(Gdx.files.internal("highscoreFIN.png"));
        settingsButtonTexFI = new Texture(Gdx.files.internal("settingsFIN.png"));
        exitButtonTexFI = new Texture(Gdx.files.internal("exitFIN.png"));

        // loads flags
        enGBButtonTex = new Texture(Gdx.files.internal("eng.png"));
        fiFIButtonTex = new Texture(Gdx.files.internal("fin.png"));

        //loads logo
        logoTex = new Texture(Gdx.files.internal("LOGOtransparent.png"));

        // setting up rectangles
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

        host.currentScreen = "menu";
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

        // draws language buttons
        host.batch.begin();
        host.batch.draw(background,0,0);
        host.batch.draw(enGBButtonTex, enGBButtonRec.getX(), enGBButtonRec.getY());
        host.batch.draw(fiFIButtonTex, fiFIButtonRec.getX(), fiFIButtonRec.getY());
        host.batch.draw(logoTex, host.camera.getPositionX() - logoTex.getWidth()/2,host.camera.getPositionY() + 60);
        host.batch.end();

        // checks if the buttons are touched
        if (playButtonRec.contains(touch.x,touch.y)){
            if (Gdx.input.justTouched()) {
                buttonSound.play();
            }
            host.reset();
            host.lastScreen = "menu";
            host.setScreen(new levelScreen(host));
        }
        if (settingsButtonRec.contains(touch.x,touch.y)){
            if (Gdx.input.justTouched()) {
                buttonSound.play();
            }
            host.lastScreen = "menu";
            host.setScreen(new settingsScreen(host));
        }
        if (highButtonRec.contains(touch.x, touch.y)) {
            if (Gdx.input.justTouched()) {
                buttonSound.play();
            }
            host.setScreen(new highscoreScreen(host));
        }
        if (exitButtonRec.contains(touch.x,touch.y)){
            if (Gdx.input.justTouched()) {
                buttonSound.play();
            }
            Gdx.app.exit();
        }

        // changes the language
        if (fiFIButtonRec.contains(touch.x,touch.y)){
            if (Gdx.input.justTouched()) {
                buttonSound.play();
            }
            host.setLang("fin");
        }
        if (enGBButtonRec.contains(touch.x,touch.y)){
            if (Gdx.input.justTouched()) {
                buttonSound.play();
            }
            host.setLang("eng");
        }

        // draws assets of used language bundle
        if (host.currentLang.equals("fin")){
            host.batch.begin();
            host.batch.draw(playButtonTexFI, playButtonRec.getX(), playButtonRec.getY());
            host.batch.draw(highButtonTexFI, highButtonRec.getX(), highButtonRec.getY());
            host.batch.draw(settingsButtonTexFI, settingsButtonRec.getX(), settingsButtonRec.getY());
            host.batch.draw(exitButtonTexFI, exitButtonRec.getX(), exitButtonRec.getY());
            host.batch.end();
        } else {
            host.batch.begin();
            host.batch.draw(playButtonTexEN, playButtonRec.getX(), playButtonRec.getY());
            host.batch.draw(highButtonTexEN, highButtonRec.getX(), highButtonRec.getY());
            host.batch.draw(settingsButtonTexEN, settingsButtonRec.getX(), settingsButtonRec.getY());
            host.batch.draw(exitButtonTexEN, exitButtonRec.getX(), exitButtonRec.getY());
            host.batch.end();
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
