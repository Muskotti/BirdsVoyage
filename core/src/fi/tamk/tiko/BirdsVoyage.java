package fi.tamk.tiko;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
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

public class BirdsVoyage extends Game implements MapProperties {
    // Music
    Music mainMenuTheme;
    Music easyTheme;
    Music mediumTheme;
    Music hardTheme;

    // Sound effects
    Sound buttonSound;
    Sound clockSound;
    Sound mapFinishSound;
    Sound sliderPressSound;
    Sound sliderReleaseSound;

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
	    // Loads music
        mainMenuTheme = Gdx.audio.newMusic(Gdx.files.internal("MainMenuTheme.ogg"));
        easyTheme = Gdx.audio.newMusic(Gdx.files.internal("EasyTheme.ogg"));
        mediumTheme = Gdx.audio.newMusic(Gdx.files.internal("MediumTheme.ogg"));
        hardTheme = Gdx.audio.newMusic(Gdx.files.internal("HardTheme.ogg"));

        // Loads Sound effects
        buttonSound = Gdx.audio.newSound(Gdx.files.internal("ButtonClick.wav"));
        clockSound = Gdx.audio.newSound(Gdx.files.internal("ClockSound.wav"));
        mapFinishSound = Gdx.audio.newSound(Gdx.files.internal("MapFinishSound.wav"));
        sliderPressSound = Gdx.audio.newSound(Gdx.files.internal("SliderPress.wav"));
        sliderReleaseSound = Gdx.audio.newSound(Gdx.files.internal("SliderRelease.wav"));

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

		// makes fonts
        makeBigfont();
        makeMediumfont();
        makeSmallfont();

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
     * Makes a big font
     */
    private void makeBigfont() {
        FreeTypeFontGenerator.FreeTypeFontParameter parameterBig = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameterBig.size = 180;
        parameterBig.borderColor = Color.BLACK;
        parameterBig.borderWidth = 3;
        fontBig = textFont.generateFont(parameterBig);
    }

    /**
     * Makes a medium font
     */
    private void makeMediumfont() {
        FreeTypeFontGenerator.FreeTypeFontParameter parameterMedBig = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameterMedBig.size = 100;
        parameterMedBig.borderColor = Color.BLACK;
        parameterMedBig.borderWidth = 3;
        fontMedium = textFont.generateFont(parameterMedBig);
    }

    /**
     * Makes a small font
     */
    private void makeSmallfont() {
        FreeTypeFontGenerator.FreeTypeFontParameter parameterMedium = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameterMedium.size = 42;
        parameterMedium.borderColor = Color.BLACK;
        parameterMedium.borderWidth = 3;
        fontSmall = textFont.generateFont(parameterMedium);
    }

    /**
     * Renders everything in the game
     */

	@Override
	public void render() {
	    playMusic();
		super.render();
	}

    /**
     * Plays the music in the game
     *
     * First thing the code checks is if the mute is activated
     * if not the code checks what screen is active
     * in the menu, splash or settings the code plays menu theme
     * in game the code plays depending on what level is selected
     * if mute is activated the game doesn't play any music
     */
    private void playMusic() {
        if (!mute) {
            if (currentScreen == "menu" || currentScreen == "splash" || currentScreen == "settings") {
                if (easyTheme.isPlaying() || mediumTheme.isPlaying() || hardTheme.isPlaying()) {
                    easyTheme.stop();
                    mediumTheme.stop();
                    hardTheme.stop();
                }
                mainMenuTheme.play();
                mainMenuTheme.setVolume(0.5f);
                mainMenuTheme.setLooping(true);

            }
            if (currentScreen == "game") {
                if (mainMenuTheme.isPlaying()) {
                    mainMenuTheme.stop();
                }
                if (currentLevel == "level1" || currentLevel == "level2" || currentLevel == "level3") {
                    easyTheme.play();
                    easyTheme.setLooping(true);
                } else if (currentLevel == "level4" || currentLevel == "level5" || currentLevel == "level6") {
                    mediumTheme.setVolume(0.85f);
                    mediumTheme.play();
                    mediumTheme.setLooping(true);
                } else if (currentLevel == "level7" || currentLevel == "level8" || currentLevel == "level9") {
                    hardTheme.setVolume(0.65f);
                    hardTheme.play();
                    hardTheme.setLooping(true);
                }
            }
        } else {
            easyTheme.stop();
            mediumTheme.stop();
            hardTheme.stop();
            mainMenuTheme.stop();
        }
    }

    /**
     * Disposes
     */

    @Override
	public void dispose () {
		hardTheme.dispose();
		easyTheme.dispose();
		mediumTheme.dispose();
		mainMenuTheme.dispose();
	    batch.dispose();
	    fontMedium.dispose();
	    fontBig.dispose();
	    fontSmall.dispose();
	    textFont.dispose();
	    flyingSheet.dispose();
	    cloudSheet.dispose();
	    clockSheet.dispose();
	}

    /**
     * Resets the specific values to default
     */

	public void reset() {
		time.reset();
		time.resume();
		player.resume();
		camera.resume();
		player.setStart();
	}

	/**
	 * Resumes the game
	 */
	public void resumeGame() {
		camera.returnPos(cameraPosX,cameraPosY);
		time.resume();
		player.resume();
		camera.resume();
	}

    /**
     * Creates the animation for the enemy bird
     *
     * First the code loads up the animation sheet
     * next the code generates a texture region from the animation sheet
     * after that 1d array is generated
     * the 2d array is placed in to the 1d array next
     * the animation is made from the 1d array
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

    /**
     * Creates the animation for the clocks
     *
     * First the code loads up the animation sheet
     * next the code generates a texture region from the animation sheet
     * after that 1d array is generated
     * the 2d array is placed in to the 1d array next
     * the animation is made from the 1d array
     */
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

    /**
     * Creates the animation for the cloud
     *
     * First the code loads up the animation sheet
     * next the code generates a texture region from the animation sheet
     * after that 1d array is generated
     * the 2d array is placed in to the 1d array next
     * the animation is made from the 1d array
     */
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

	/**
	 * Loads the map based on current level selected in the level select
	 */
	public void loadMap() {
        map = new Map(currentLevel);
    }

    /**
     * Updates current language to preferences, so the game knows to load the last used language
     * when booted.
     * @param lang current language user has chosen.
     */
    public void setLang(String lang) {
        currentLang = lang;
        preferences.putString("language",lang);
        preferences.flush();
    }

    /**
     * Returns the camera height
     * @return camera height
     */
    public int getCameraHeight() {
        return cameraHeight;
    }

    /**
     * Returns the camera widths
     * @return camera widths
     */
    public int getCameraWidth() {
        return cameraWidth;
    }

    /**
     * Returns the map width
     * @return map width
     */
    public float getMapWidth() {
        return mapWidth;
    }
}
