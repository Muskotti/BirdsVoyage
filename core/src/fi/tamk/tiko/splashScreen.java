package fi.tamk.tiko;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;

/**
 * Displays the logos of the team, tiko and Exerium (client) when the game starts.
 */
public class splashScreen implements Screen {
    BirdsVoyage host;

    // logo textures
    private Texture exerium;
    private Texture tiko;
    private Texture team;

    // Timer for how long the logos will be displayed
    float timer;

    /**
     * Constructor for the splash screen. Logo textures are loaded here.
     * @param host Main game java class
     */
    public splashScreen(BirdsVoyage host){
        this.host = host;
        exerium = new Texture(Gdx.files.internal("exLogo.png"));
        tiko = new Texture(Gdx.files.internal("tiko.png"));
        team = new Texture(Gdx.files.internal("parasryha.png"));
        host.currentScreen = "splash";
    }

    @Override
    /**
     * Does nothing.
     */
    public void show() {

    }

    @Override
    /**
     * Rendering of the game
     */
    public void render(float delta) {
        addTime();
        refreshScreen();
        drawLogos();
    }

    /**
     * Adds time to the timer.
     */
    private void addTime() {
        timer += Gdx.graphics.getRawDeltaTime();
    }

    /**
     * Refreshes the screen and makes the background color.
     */
    private void refreshScreen() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    /**
     * Draws all the logos in order, every logo is displayed for the same amount of time.
     */
    private void drawLogos() {
        host.batch.begin();
        if (timer <= 4.1f){
            host.batch.draw(exerium,(Gdx.graphics.getWidth()/2) - (exerium.getWidth()/2),(Gdx.graphics.getHeight()/2) - (exerium.getHeight()/2));
        } else if (timer >= 4.1f && timer <= 6.1f){
            host.batch.draw(tiko,(Gdx.graphics.getWidth()/2) - (tiko.getWidth()/2),(Gdx.graphics.getHeight()/2) - (tiko.getHeight()/2));
        } else if (timer >= 6.1f && timer <= 8.1f){
            host.batch.draw(team,(Gdx.graphics.getWidth()/2) - (team.getWidth()/2),(Gdx.graphics.getHeight()/2) - (team.getHeight()/2));
        } else {
            host.setScreen(new menuScreen(host));
        }
        host.batch.end();
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
     * Disposes every logo
     */
    public void dispose() {
        tiko.dispose();
        team.dispose();
        exerium.dispose();
    }
}
