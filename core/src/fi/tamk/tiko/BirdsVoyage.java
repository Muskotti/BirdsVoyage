package fi.tamk.tiko;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

public class BirdsVoyage extends Game implements MapProperties{

	SpriteBatch batch;
	Player player;
	Enemy enemy;
	StormCloud cloud;
	Map map;
	Camera camera;
	GameTimer time;
	splashScreen splash;
	String currentLang = defaultLang;

	menuScreen menu;
	String currentScreen;
	String lastScreen;

	Music levelTheme;
	Music mainMenuTheme;
	Boolean mute;

    FreeTypeFontGenerator textFont;
    BitmapFont fontBig;
    BitmapFont fontMedBig;
    BitmapFont fontMedium;

	// saved position of the camera after going to setting
	float cameraPosX;
	float cameraPosY;

	// boolean to check if game is running
	boolean gameRun = false;

	//enemy animation
    Animation<TextureRegion> flyingAnimation;
    private Texture flyingSheet;
    float stateTime;

    //cloud animation
    Animation<TextureRegion> cloudAnimation;
    private Texture cloudSheet;
    float cloudTime;

    //clock animation
    Animation<TextureRegion> clockAnimation;
    private Texture clockSheet;
    float clockTime;

    // Preferences for high score and sensitivity saving
    Preferences preferences;

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
		splash = new splashScreen(this);

		mute = false;

		textFont = new FreeTypeFontGenerator(Gdx.files.internal("Bord-Regular.ttf"));

		FreeTypeFontGenerator.FreeTypeFontParameter parameterBig = new FreeTypeFontGenerator.FreeTypeFontParameter();
		parameterBig.size = 180;
		parameterBig.borderColor = Color.BLACK;
		parameterBig.borderWidth = 3;
		fontBig = textFont.generateFont(parameterBig);

        FreeTypeFontGenerator.FreeTypeFontParameter parameterMedium = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameterMedium.size = 42;
        parameterMedium.borderColor = Color.BLACK;
        parameterMedium.borderWidth = 3;
        fontMedium = textFont.generateFont(parameterMedium);

        FreeTypeFontGenerator.FreeTypeFontParameter parameterMedBig = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameterMedBig.size = 100;
        parameterMedBig.borderColor = Color.BLACK;
        parameterMedBig.borderWidth = 3;
        fontMedBig = textFont.generateFont(parameterMedBig);

        makeEnemyAnim();
        makeCloudAnim();
        makeClockAnim();

		levelTheme = Gdx.audio.newMusic(Gdx.files.internal("LevelTheme.ogg"));
		mainMenuTheme = Gdx.audio.newMusic(Gdx.files.internal("MainMenuTheme.ogg"));

		preferences = Gdx.app.getPreferences("My Preferences");

		camera.cameraMove();
		setScreen(splash);
	}

	private void makeClockAnim() {
		clockSheet = new Texture(Gdx.files.internal("clock.png"));
		TextureRegion[][] tmp = TextureRegion.split(clockSheet,
                clockSheet.getWidth() / 6,
                clockSheet.getHeight() / 1);
		TextureRegion[] flyingFrames = new TextureRegion[6 * 1];
		int index = 0;
		for (int i = 0; i < 1; i++){
			for (int j = 0; j < 6; j++){
				flyingFrames[index++] = tmp[i][j];
			}
		}
		clockAnimation = new Animation<TextureRegion>(6, flyingFrames);
		clockTime = 0f;
	}

	private void makeCloudAnim() {
        cloudSheet = new Texture(Gdx.files.internal("cloud.png"));
        TextureRegion[][] tmp = TextureRegion.split(cloudSheet,
                cloudSheet.getWidth() / 3,
                cloudSheet.getHeight() / 1);
        TextureRegion[] flyingFrames = new TextureRegion[3 * 1];
        int index = 0;
        for (int i = 0; i < 1; i++){
            for (int j = 0; j < 3; j++){
                flyingFrames[index++] = tmp[i][j];
            }
        }
        cloudAnimation = new Animation<TextureRegion>(1 / 10f, flyingFrames);
        cloudTime = 0f;
    }

    private void makeEnemyAnim() {
        flyingSheet = new Texture(Gdx.files.internal("enemy.png"));
        TextureRegion[][] tmp = TextureRegion.split(flyingSheet,
                flyingSheet.getWidth() / 4,
                flyingSheet.getHeight() / 1);
        TextureRegion[] flyingFrames = new TextureRegion[4 * 1];
        int index = 0;
        for (int i = 0; i < 1; i++){
            for (int j = 0; j < 4; j++){
                flyingFrames[index++] = tmp[i][j];
            }
        }
        flyingAnimation = new Animation<TextureRegion>(1 / 10f, flyingFrames);
        stateTime = 0f;
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

	public float getMinutes() {
	    return time.getMinutes();
    }
    public float getSeconds() {
	    return time.getSeconds();
    }
}
