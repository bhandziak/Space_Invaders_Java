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
        //wczytanie save z pliku
        loadGame();
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
        prefs.putInteger("selectedSpaceShipId", selectedSpaceShipId); // id statku
        // zapisanie tablicy bool bought_spaceship
        StringBuilder convertedTable = new StringBuilder();
        for(int i = 0; i < bought_spaceship.length; i++){
            convertedTable.append(bought_spaceship[i] ? "1" : "0");
            if (i < bought_spaceship.length - 1) {
                convertedTable.append(",");
            }
        }
        prefs.putString("boughtSpaceships", convertedTable.toString());

        prefs.flush(); // zapisuje dane na dysk
    }
    public void loadGame() {
        Preferences prefs = Gdx.app.getPreferences("Save");
        //wczytanie danych
        recordScore = prefs.getInteger("score", 0); // 0 - domyślna wartość, gdy nie ma save
        money = prefs.getInteger("money", 0);
        selectedSpaceShipId = prefs.getInteger("selectedSpaceShipId", 5);
        // bool[] bought_spaceship
        String saved_bought_spaceship = prefs.getString("boughtSpaceships", "0,0,0"); // domyślnie nic nie kupione
        String[] parts = saved_bought_spaceship.split(",");

        for (int i = 0; i < parts.length; i++) {
            bought_spaceship[i] = parts[i].equals("1");
        }

    }

}
