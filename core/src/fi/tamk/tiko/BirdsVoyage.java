package fi.tamk.tiko;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class BirdsVoyage extends Game implements MapProperties{

	SpriteBatch batch;
	Player player;
	Enemy enemy;
	StormCloud cloud;
	Map map;
	Camera camera;
	GameTimer time;
	String currentLang = defaultLang;

	menuScreen menu;
	String currentScreen;
	String lastScreen;

	Music levelTheme;
	Music mainMenuTheme;

	// saved position of the camera after going to setting
	float cameraPosX;
	float cameraPosY;

	// boolean to check if game is running
	boolean gameRun = false;

	public int getCameraHeight() {
		return cameraHeight;
	}

	public int getCameraWidth() {
		return cameraWidth;
	}

	@Override
	public void create() {
		batch = new SpriteBatch();
		player = new Player();
		enemy = new Enemy();
		cloud = new StormCloud();
		camera = new Camera(player);
		time = new GameTimer();
		menu = new menuScreen(this);
		map = new Map();

		levelTheme = Gdx.audio.newMusic(Gdx.files.internal("LevelTheme.ogg"));
		mainMenuTheme = Gdx.audio.newMusic(Gdx.files.internal("MainMenuTheme.ogg"));

		camera.cameraMove();
		setScreen(menu);
	}

	public float getMapWidth() {
		return mapWidth;
	}

	@Override
	public void render() {
		if ((currentScreen == "menu") && lastScreen == "game" || lastScreen == null) {
			if (levelTheme.isPlaying()) {
				levelTheme.stop();
			}
			mainMenuTheme.play();
			mainMenuTheme.setLooping(true);

		}
		if ((currentScreen == "game") && lastScreen == "menu") {
			if (mainMenuTheme.isPlaying()) {
				mainMenuTheme.stop();
			}
			levelTheme.play();
			levelTheme.setLooping(true);
		}
		super.render();
	}

	@Override
	public void dispose () {
		batch.dispose();
	}

	public void setLang(String lang) {
		currentLang = lang;
	}

	public void reset() {
		time.reset();
		time.resume();
		player.resume();
		camera.resume();
		player.setStart();
	}

	public void resumeGame() {
		camera.returnPos(cameraPosX,cameraPosY);
		time.resume();
		player.resume();
		camera.resume();
	}
}
