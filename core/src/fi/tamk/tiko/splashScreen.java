package fi.tamk.tiko;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;

public class splashScreen implements Screen {
    BirdsVoyage host;

    // logo textures
    private Texture exerium;
    private Texture tiko;
    private Texture team;

    float timer;

    public splashScreen(BirdsVoyage host){
        this.host = host;
        exerium = new Texture(Gdx.files.internal("exLogo.png"));
        tiko = new Texture(Gdx.files.internal("tiko.png"));
        team = new Texture(Gdx.files.internal("parasryha.png"));
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        timer += Gdx.graphics.getRawDeltaTime();
        host.batch.begin();
        if (timer <= 4.1f){
            host.batch.draw(exerium,(Gdx.graphics.getWidth()/2) - (exerium.getWidth()/2),(Gdx.graphics.getHeight()/2) - (exerium.getHeight()/2));
        } else if (timer >= 4.1f && timer <= 6.1f){
            host.batch.draw(tiko,(Gdx.graphics.getWidth()/2) - (tiko.getWidth()/2),(Gdx.graphics.getHeight()/2) - (tiko.getHeight()/2));
        } else if (timer >= 6.1f && timer <= 8.1f){
            host.batch.draw(team,(Gdx.graphics.getWidth()/2) - (team.getWidth()/2),(Gdx.graphics.getHeight()/2) - (team.getHeight()/2));
        } else {
            host.setScreen(host.menu);
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
        tiko.dispose();
        team.dispose();
        exerium.dispose();
    }
}
