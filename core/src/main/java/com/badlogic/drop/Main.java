package com.badlogic.drop;


import com.badlogic.gdx.Game;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends Game {

    public boolean[] bought_spaceship ={
        false, false, false
    };
    public Player selectedSpaceShip;
    public int selectedSpaceShipId = 5;
    public int money = 600;
    public int score = 0;
    public int recordScore = 0;
    @Override
    public void create() {
        setScreen(new MainMenuScreen(this));
    }
}
