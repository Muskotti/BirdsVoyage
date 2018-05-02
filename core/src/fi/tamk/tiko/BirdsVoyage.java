package fi.tamk.tiko;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

/**
 * The main class for the game where everything runs
 *
 * @author Toni Vänttinen & Jimi Savola
 * @version 1.8, 05/02/18
 * @since 1.8
 */

public class BirdsVoyage extends Game implements MapProperties, SoundAndMusic {

    // Preferences for saving all info
    Preferences preferences;

	SpriteBatch batch;
	Player player;
	StormCloud cloud;
	Map map;
	Camera camera;
	GameTimer time;
	splashScreen splash;

	// string for current language
    String currentLang;

    // String for current screen
	String currentScreen;

    // String for previous screen
	String lastScreen;

	// boolean for game mutes
	Boolean mute;

	// different fonts
    FreeTypeFontGenerator textFont;
    BitmapFont fontBig;
    BitmapFont fontMedium;
    BitmapFont fontSmall;

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

    // selected level info
    String currentLevel;

    /**
     * Constructor for the start of the game
     */
	@Override
	public void create() {

	    // disables back key
	    Gdx.input.setCatchBackKey(true);

	    // creates/gets preferences
        preferences = Gdx.app.getPreferences("My Preferences");

        // gets language
        currentLang = preferences.getString("language","eng");

        // creates sprite batch
		batch = new SpriteBatch();

        // creates player
		player = new Player();

		// creates storm cloud
		cloud = new StormCloud();

		// creates camera
		camera = new Camera(player);

		// creates timer for levels
		time = new GameTimer();

		// creates splash screen
		splash = new splashScreen(this);

		// sets the players default position
        float x = preferences.getFloat("defX",0f);
        float y = preferences.getFloat("defY",0f);
        if (y == 0f){
            y = 4f;
        }
        player.setDefPos(x,y);

        // sets the mute as false
		mute = false;

		// loads up a font style
		textFont = new FreeTypeFontGenerator(Gdx.files.internal("Bord-Regular.ttf"));

		// makes a big font
		FreeTypeFontGenerator.FreeTypeFontParameter parameterBig = new FreeTypeFontGenerator.FreeTypeFontParameter();
		parameterBig.size = 180;
		parameterBig.borderColor = Color.BLACK;
		parameterBig.borderWidth = 3;
		fontBig = textFont.generateFont(parameterBig);

		// makes a small font
        FreeTypeFontGenerator.FreeTypeFontParameter parameterMedium = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameterMedium.size = 42;
        parameterMedium.borderColor = Color.BLACK;
        parameterMedium.borderWidth = 3;
        fontSmall = textFont.generateFont(parameterMedium);

        // makes medium font
        FreeTypeFontGenerator.FreeTypeFontParameter parameterMedBig = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameterMedBig.size = 100;
        parameterMedBig.borderColor = Color.BLACK;
        parameterMedBig.borderWidth = 3;
        fontMedium = textFont.generateFont(parameterMedBig);

        // creates animations
        makeEnemyAnim();
        makeCloudAnim();
        makeClockAnim();

        // sets the current level to level1
        currentLevel = "level1";

        // sets the screen to splash screen
		setScreen(splash);
	}

    /**
     *
     */

	@Override
	public void render() {
		if ((currentScreen == "menu" || currentScreen == "splash") && lastScreen == "game" || lastScreen == null) {
			if (easyTheme.isPlaying() || mediumTheme.isPlaying() || hardTheme.isPlaying()) {
				easyTheme.stop();
                mediumTheme.stop();
                hardTheme.stop();
			}
			mainMenuTheme.play();
			mainMenuTheme.setVolume(0.5f);
			mainMenuTheme.setLooping(true);

		}
		if ((currentScreen == "game") && lastScreen == "menu") {
			if (mainMenuTheme.isPlaying()) {
				mainMenuTheme.stop();
			}
			if (currentLevel == "level1" || currentLevel == "level2" || currentLevel == "level3") {
			    easyTheme.play();
			    easyTheme.setLooping(true);
            }
            else if (currentLevel == "level4" || currentLevel == "level5" || currentLevel == "level6") {
                mediumTheme.play();
                mediumTheme.setLooping(true);
            }
            else if (currentLevel == "level7" || currentLevel == "level8" || currentLevel == "level9") {
                hardTheme.play();
                hardTheme.setLooping(true);
            }
		}

		super.render();
	}

	@Override
	public void dispose () {
		batch.dispose();
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

    /**
     * Creates the animation for enemy bird
     */
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

    public void loadMap() {
        map = new Map(currentLevel);
    }
    public void setLang(String lang) {
        currentLang = lang;
        preferences.putString("language",lang);
        preferences.flush();
    }

    public float getMinutes() {
	    return time.getMinutes();
    }
    public float getSeconds() {
	    return time.getSeconds();
    }
    public int getCameraHeight() {
        return cameraHeight;
    }
    public int getCameraWidth() {
        return cameraWidth;
    }
    public float getMapWidth() {
        return mapWidth;
    }

}
