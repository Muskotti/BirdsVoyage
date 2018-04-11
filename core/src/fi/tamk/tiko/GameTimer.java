package fi.tamk.tiko;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

public class GameTimer {

    private float time;

    private int sec;
    private int min;

    private String secText;
    private String minText;
    private String text;

    private boolean timeStop;

    public GameTimer(){
        time = 0;
        sec = 0;
        min = 0;
        timeStop = true;
    }

    public void update() {
        if (timeStop){
            time += Gdx.graphics.getRawDeltaTime();
        }
        // calculates time
        if(time >= 0.9f){
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

    // draws the time text
    public void drawText(SpriteBatch batch, Camera camera, int cameraWidth, int cameraHeight, BitmapFont fontMedium) {
        fontMedium.draw(batch, text, camera.getPositionX() - (cameraWidth/2) + 5,camera.getPositionY() + (cameraHeight/2) - 5);
    }

    public void removeTime() {
        time = time - 5;
    }

    public void stop() {
        timeStop = false;
    }

    public void resume() {
        timeStop = true;
    }

    public void reset() {
        time = 0;
        sec = 0;
        min = 0;
    }
}
