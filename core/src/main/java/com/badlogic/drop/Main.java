package com.badlogic.drop;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

/**
 * Główna klasa całej aplikacji.
 * Zarządza przekierowaniami ekranów - ustawia pierwszy ekran na MainMenuScreen.
 * Przechowuje zmienne globalne dostępne dla każdego ekranu.
 * Zapisuje i wczytuje save z gry (z pliku).
 * @author Kacper Dziduch, Bartłomiej Handziak
 * @version 1.0
 */

public class Main extends Game {
    /** Tablica przechowująca stany, czy dany statek został kupiony */
    public boolean[] bought_spaceship ={
        false, false, false
    };
    /** Instancję wybranego statku */
    public Player selectedSpaceShip;
    /** Identyfikator wybranego statku */
    public int selectedSpaceShipId = 5;
    /** Ilość pieniędzy gracza. */
    public int money = 0;
    /** Aktualny wynik gracza. */
    public int score = 0;
    /** Najlepszy wynik gracza (rekord). */
    public int recordScore = 0;

    /**
     * Metoda uruchamiana przy starcie aplikacji.
     * Wczytuje dane zapisane w pliku (save) i ustawia ekran startowy na {@code MainMenuScreen}.
     */
    @Override
    public void create() {
        //wczytanie save z pliku
        loadGame();
        setScreen(new MainMenuScreen(this));
    }

    /**
     * Metoda aktualizująca najlepszy wynik
     */
    public void updateHighscore(){
        if(recordScore < score){
            recordScore = score;
        }
    }

    /**
     * Zapisuje zmienne globalne (score, money, selectedSpaceShipId, boughtSpaceships)
     * do pliku .prefs/Save
     */
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

    /**
     * Wczytuje zmienne globalne (score, money, selectedSpaceShipId, boughtSpaceships)
     * z pliku .prefs/Save
     */
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
