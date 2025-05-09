package com.badlogic.drop;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends Game {

    public boolean[] bought_spaceship ={
        false, false, false
    };
    public Player selectedSpaceShip;
    public int selectedSpaceShipId = 5;
    public int money = 0;
    public int score = 0;
    public int recordScore = 0;
    @Override
    public void create() {
        setScreen(new MainMenuScreen(this));
    }

    //aktualizacja highscore
    public void updateHighscore(){
        if(recordScore < score){
            recordScore = score;
        }
    }
    public void saveGame() {
        Preferences prefs = Gdx.app.getPreferences("Save");
        prefs.putInteger("score", recordScore);
        prefs.putInteger("money", money);
        prefs.flush(); // zapisuje dane na dysk
    }
    public void loadGame() {
        Preferences prefs = Gdx.app.getPreferences("Save");

        //wczytanie danych
        recordScore = prefs.getInteger("score", 0); // 0 - domyślna wartość, gdy nie ma save
        money = prefs.getInteger("money", 0);

    }

}
