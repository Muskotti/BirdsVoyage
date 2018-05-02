package fi.tamk.tiko;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

public class levelScreen implements Screen, SoundAndMusic {
    BirdsVoyage host;
    private Texture background;

    // players touch input
    private Vector3 touch;

    // Texture for menu button
    private Texture returnButton;

    // Level textures
    private Texture level1Tex;
    private Texture level2Tex;
    private Texture level3Tex;
    private Texture level4Tex;
    private Texture level5Tex;
    private Texture level6Tex;
    private Texture level7Tex;
    private Texture level8Tex;
    private Texture level9Tex;
    private Texture levelbox;

    // Texture for language buttons
    private Texture enGBButtonTex;
    private Texture fiFIButtonTex;

    //rectangles
    private Rectangle returnButtonRect;
    private Rectangle level1Rec;
    private Rectangle level2Rec;
    private Rectangle level3Rec;
    private Rectangle level4Rec;
    private Rectangle level5Rec;
    private Rectangle level6Rec;
    private Rectangle level7Rec;
    private Rectangle level8Rec;
    private Rectangle level9Rec;

    private Rectangle enGBButtonRec;
    private Rectangle fiFIButtonRec;

    public levelScreen(BirdsVoyage host){
        this.host = host;
        touch = new Vector3(0,0,0);
        background = new Texture(Gdx.files.internal("menuBack.png"));

        //return texture
        returnButton = new Texture(Gdx.files.internal("goback.png"));

        //level textures
        level1Tex = new Texture(Gdx.files.internal("level1.png"));
        level2Tex = new Texture(Gdx.files.internal("level2.png"));
        level3Tex = new Texture(Gdx.files.internal("level3.png"));
        level4Tex = new Texture(Gdx.files.internal("level4.png"));
        level5Tex = new Texture(Gdx.files.internal("level5.png"));
        level6Tex = new Texture(Gdx.files.internal("level6.png"));
        level7Tex = new Texture(Gdx.files.internal("level7.png"));
        level8Tex = new Texture(Gdx.files.internal("level8.png"));
        level9Tex = new Texture(Gdx.files.internal("level9.png"));
        levelbox = new Texture(Gdx.files.internal("levelbox.png"));

        // loads flags
        enGBButtonTex = new Texture(Gdx.files.internal("eng.png"));
        fiFIButtonTex = new Texture(Gdx.files.internal("fin.png"));

        // return rectangle
        returnButtonRect = new Rectangle(
                5,
                15,
                returnButton.getWidth(),
                returnButton.getHeight()
        );

        // level select rectangles
        level1Rec = new Rectangle(
                host.camera.getPositionX() + 30,
                host.camera.getPositionY() + 85,
                level1Tex.getWidth(),
                level1Tex.getHeight()
        );

        level2Rec = new Rectangle(
                host.camera.getPositionX() + 140,
                host.camera.getPositionY() + 85,
                level2Tex.getWidth(),
                level2Tex.getHeight()
        );

        level3Rec = new Rectangle(
                host.camera.getPositionX() + 250,
                host.camera.getPositionY() + 85,
                level3Tex.getWidth(),
                level3Tex.getHeight()
        );

        level4Rec = new Rectangle(
                host.camera.getPositionX() + 30,
                host.camera.getPositionY() - 50,
                level4Tex.getWidth(),
                level4Tex.getHeight()
        );

        level5Rec = new Rectangle(
                host.camera.getPositionX() + 140,
                host.camera.getPositionY() - 50,
                level5Tex.getWidth(),
                level5Tex.getHeight()
        );

        level6Rec = new Rectangle(
                host.camera.getPositionX() + 250,
                host.camera.getPositionY()  - 50,
                level6Tex.getWidth(),
                level6Tex.getHeight()
        );

        level7Rec = new Rectangle(
                host.camera.getPositionX() + 30,
                host.camera.getPositionY() - 185,
                level7Tex.getWidth(),
                level7Tex.getHeight()
        );

        level8Rec = new Rectangle(
                host.camera.getPositionX() + 140,
                host.camera.getPositionY() - 185,
                level8Tex.getWidth(),
                level8Tex.getHeight()
        );

        level9Rec = new Rectangle(
                host.camera.getPositionX() + 250,
                host.camera.getPositionY() - 185,
                level9Tex.getWidth(),
                level9Tex.getHeight()
        );

        // language rectangles
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

        // draws the background and the level buttons
        host.batch.begin();
        host.batch.draw(background,0,0);
        host.batch.draw(enGBButtonTex, enGBButtonRec.getX(), enGBButtonRec.getY());
        host.batch.draw(fiFIButtonTex, fiFIButtonRec.getX(), fiFIButtonRec.getY());
        host.batch.draw(returnButton, returnButtonRect.getX(), returnButtonRect.getY());
        host.batch.draw(levelbox,host.camera.getPositionX() - 370, host.camera.getPositionY() - 207);
        host.batch.draw(level1Tex,level1Rec.getX(),level1Rec.getY());
        host.batch.draw(level2Tex,level2Rec.getX(),level2Rec.getY());
        host.batch.draw(level3Tex,level3Rec.getX(),level3Rec.getY());
        host.batch.draw(level4Tex,level4Rec.getX(),level4Rec.getY());
        host.batch.draw(level5Tex,level5Rec.getX(),level5Rec.getY());
        host.batch.draw(level6Tex,level6Rec.getX(),level6Rec.getY());
        host.batch.draw(level7Tex,level7Rec.getX(),level7Rec.getY());
        host.batch.draw(level8Tex,level8Rec.getX(),level8Rec.getY());
        host.batch.draw(level9Tex,level9Rec.getX(),level9Rec.getY());
        // listens if return is pressed
        if (returnButtonRect.contains(touch.x,touch.y)){
            if (Gdx.input.justTouched()) {
                buttonSound.play();
            }
            host.lastScreen = "level";
            host.setScreen(new menuScreen(host));
        }

        if (level1Rec.contains(touch.x,touch.y)){
            if (Gdx.input.justTouched()) {
                buttonSound.play();
            }
            host.currentLevel = "level1";
        }

        if (level2Rec.contains(touch.x,touch.y)){
            if (Gdx.input.justTouched()) {
                buttonSound.play();
            }
            host.currentLevel = "level2";
        }

        if (level3Rec.contains(touch.x,touch.y)){
            if (Gdx.input.justTouched()) {
                buttonSound.play();
            }
            host.currentLevel = "level3";
        }

        if (level4Rec.contains(touch.x,touch.y)){
            if (Gdx.input.justTouched()) {
                buttonSound.play();
            }
            host.currentLevel = "level4";
        }

        if (level5Rec.contains(touch.x,touch.y)){
            if (Gdx.input.justTouched()) {
                buttonSound.play();
            }
            host.currentLevel = "level5";
        }

        if (level6Rec.contains(touch.x,touch.y)){
            if (Gdx.input.justTouched()) {
                buttonSound.play();
            }
            host.currentLevel = "level6";
        }

        if (level7Rec.contains(touch.x,touch.y)){
            if (Gdx.input.justTouched()) {
                buttonSound.play();
            }
            host.currentLevel = "level7";
        }

        if (level8Rec.contains(touch.x,touch.y)){
            if (Gdx.input.justTouched()) {
                buttonSound.play();
            }
            host.currentLevel = "level8";
        }

        if (level9Rec.contains(touch.x,touch.y)){
            if (Gdx.input.justTouched()) {
                buttonSound.play();
            }
            host.currentLevel = "level9";
        }

        if (level1Rec.contains(touch.x,touch.y) || level2Rec.contains(touch.x,touch.y)
                || level3Rec.contains(touch.x,touch.y) || level4Rec.contains(touch.x,touch.y)
                || level5Rec.contains(touch.x,touch.y) || level6Rec.contains(touch.x,touch.y)
                || level7Rec.contains(touch.x,touch.y) || level8Rec.contains(touch.x,touch.y)
                || level9Rec.contains(touch.x,touch.y)){
            host.loadMap();
            host.setScreen(new gameScreen(host));
        }

        // changes the language
        if (fiFIButtonRec.contains(touch.x,touch.y)){
            host.setLang("fin");
        }
        if (enGBButtonRec.contains(touch.x,touch.y)){
            host.setLang("eng");
        }

        // draws assets of used language bundle
        if (host.currentLang.equals("fin")){
            host.fontSmall.draw(host.batch,"Helppo", host.camera.getPositionX() - 300,host.camera.getPositionY() + 160);
            host.fontSmall.draw(host.batch,"Normaali", host.camera.getPositionX() - 300,host.camera.getPositionY() + 25);
            host.fontSmall.draw(host.batch,"Vaikea", host.camera.getPositionX() - 300,host.camera.getPositionY() - 115);
        } else {
            host.fontSmall.draw(host.batch,"Easy", host.camera.getPositionX() - 300,host.camera.getPositionY() + 160);
            host.fontSmall.draw(host.batch,"Medium", host.camera.getPositionX() - 300,host.camera.getPositionY() + 25);
            host.fontSmall.draw(host.batch,"Hard", host.camera.getPositionX() - 300,host.camera.getPositionY() - 115);
        }
        host.batch.end();
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
        background.dispose();
        level1Tex.dispose();
        level2Tex.dispose();
        level3Tex.dispose();
        level4Tex.dispose();
        level5Tex.dispose();
        level6Tex.dispose();
        level7Tex.dispose();
        level8Tex.dispose();
        level9Tex.dispose();
        returnButton.dispose();
    }
}
