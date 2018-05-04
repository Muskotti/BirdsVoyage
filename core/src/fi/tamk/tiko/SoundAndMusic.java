package fi.tamk.tiko;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

/**
 * Class for all the music and sound effect files.
 *
 * @author Toni Vänttinen & Jimi Savola
 * @version 1.8, 05/02/18
 * @since 1.8
 */
public interface SoundAndMusic {

    // Music
    Music easyTheme = Gdx.audio.newMusic(Gdx.files.internal("EasyTheme.ogg"));
    Music mediumTheme = Gdx.audio.newMusic(Gdx.files.internal("MediumTheme.ogg"));
    Music hardTheme = Gdx.audio.newMusic(Gdx.files.internal("HardTheme.ogg"));
    Music mainMenuTheme = Gdx.audio.newMusic(Gdx.files.internal("MainMenuTheme.ogg"));

    // Sound effects
    Sound collisionSound = Gdx.audio.newSound(Gdx.files.internal("BirdCollision.wav"));
    Sound buttonSound = Gdx.audio.newSound(Gdx.files.internal("ButtonClick.wav"));
    Sound clockSound = Gdx.audio.newSound(Gdx.files.internal("ClockSound.wav"));
    Sound mapFinishSound = Gdx.audio.newSound(Gdx.files.internal("MapFinishSound.wav"));
    Sound sliderPressSound = Gdx.audio.newSound(Gdx.files.internal("SliderPress.wav"));
    Sound sliderReleaseSound = Gdx.audio.newSound(Gdx.files.internal("SliderRelease.wav"));
}
