package fi.tamk.tiko;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

/**
 * Created by Jimi on 17.4.2018.
 */

class textInputListener extends ApplicationAdapter implements Input.TextInputListener {
    String text;

    @Override
    public void input(String text) {
        this.text = text;
    }

    @Override
    public void canceled() {

    }
}