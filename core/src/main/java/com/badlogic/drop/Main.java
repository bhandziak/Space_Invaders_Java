package com.badlogic.drop;


import com.badlogic.gdx.Game;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends Game {

    public boolean[] bought_spaceship ={
        true, false, false
    };
    @Override
    public void create() {
        setScreen(new MainMenuScreen(this));
    }
}
