package fi.tamk.tiko;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * class for the in game timer
 *
 * @author Toni Vänttinen and Jimi Savola
 * @version 1.8, 05/02/18
 * @since 1.8
 */
public class GameTimer {

    // the actual time
    private float time;

    // seconds
    private int sec;

    //minutes
    private int min;

    // text for seconds
    private String secText;

    // text for the minutes
    private String minText;

    // text for the time
    private String text;

    // boolean if the game is stopped
    private boolean timeStop;

    /**
     * Constructs the time
     */
    public GameTimer(){
        time = 0;
        sec = 0;
        min = 0;
        timeStop = true;
    }

    /**
     * Updates the time
     *
     * First the code checks if the game is paused
     * if it's not the raw time is added to the time float
     * next the code checks if the time value is 1f or over
     * if it is the time value is reset and 1 is added to the second
     * then the code checks if there are 60 in second
     * if there is a minute is added
     * Next the code checks if if 0 are added to the printable text
     * lastly the printable text is created
     */
    public void update() {
        if (timeStop){
            time += Gdx.graphics.getRawDeltaTime();
        }
        // calculates time
        if(time >= 1f){
            time = 0;
            sec++;
        } else if(sec == 60){
            sec = 0;
            min++;
        }

        // makes sec text so it shows 00
        if(sec > 9){
            secText = "" + sec;
        } else {
            secText = "0" + sec;
        }

        // makes min text so it shows 00
        if(min > 9){
            minText = "" + min;
        } else {
            minText = "0" + min;
        }
        // creates time text
        text = minText + ":" + secText;

    }

    /**
     * Draws the time text at the given location
     * @param batch what draws
     * @param camera used to help draw the timer to correct position
     * @param cameraWidth cameras width that is used to help draw the timer to correct position
     * @param cameraHeight cameras width that is used to help draw the timer to correct position
     * @param fontMedium the font
     */
    public void drawText(SpriteBatch batch, Camera camera, int cameraWidth, int cameraHeight, BitmapFont fontMedium) {
        fontMedium.draw(batch, text, camera.getPositionX() - (cameraWidth/2) + 5,camera.getPositionY() + (cameraHeight/2) - 5);
    }

    /**
     * stops the time for 5 seconds
     */
    public void removeTime() {
        time = time - 5;
    }

    /**
     * stops the timer
     */
    public void stop() {
        timeStop = false;
    }

    /**
     * Starts the timer
     */
    public void resume() {
        timeStop = true;
    }

    /**
     * Gets the timers seconds
     * @return seconds value
     */
    public int getSeconds() {
        return sec;
    }

    /**
     * Gets the timers minutes
     * @return minutes value
     */
    public int getMinutes() {
        return min;
    }

    /**
     * Reset the timer to 0
     */
    public void reset() {
        time = 0;
        sec = 0;
        min = 0;
    }

    /**
     * gets the drawn text
     * @return timers text
     */
    public String getTime() {
        return text;
    }
}
