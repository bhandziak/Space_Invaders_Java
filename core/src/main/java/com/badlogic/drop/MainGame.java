package com.badlogic.drop;

import com.badlogic.gdx.*;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

/**
 * Główna klasa (ekran) gry Space Invaders.
 *
 * <p>
 * Ekran składa się 4 głównych elementów:
 * <ul>
 *     <li>UI: pieniądze, aktualny wynik</li>
 *     <li>Statek kosmiczny wybrany przez gracza</li>
 *     <li>Budynki podlegające destrukcji</li>
 *     <li>Wrogowie</li>
 * </ul>
 *
 * @author Kacper Dziduch
 * @author Bartłomiej Handziak
 *
 * @version 1.0
 */
public class MainGame implements Screen {
    /** Referencja do głównej klasy {@link Main} umożliwia dostęp do zmiennych globalnych*/
    final Main game;
    Texture backgroundTexture;

    SpriteBatch spriteBatch;
    FitViewport viewport;
    //Player
    Player player;
    /** Dźwięk otrzymania obraźeń */
    Sound hitSound;
    /** Dźwięk niszczenia budynku */
    Sound damageBuildingSound;
    /** Dźwięk trafienia budynku przez gracza */
    Sound hitBuildingSound;

    //Enemy
    Texture enemyWhiteTexture;
    Texture enemyGreenTexture;
    Texture enemyRedTexture;
    Enemy enemyWhite;
    Enemy enemyGreen;
    Enemy enemyRed;
    //coin
    Array<Coin> coins = new Array<>();
    /** Wave - fala przeciwników */
    EnemyWave enemyWave;

    /** Tablica na typy przeciwników */
    Array<Enemy> EnemyTypes = new Array<>();

    /** Tablica budynków oslaniających */
    Array<ShieldBuilding> bunkers;
    ShieldBuilding bunker;
    /** Flaga zatrzymania gry */
    private boolean isPaused = false;
    /** Flaga poprzedniego stanu zatrzymania gry */
    private boolean wasPaused = false;
    /** Nakładka Pauzy */
    private PauseScreen pauseScreen;
    private InGameUI inGameUI;
    /** Flaga stanu końca gry */
    private boolean isGameOver = false;
    private boolean isGameUIshowed = false;

    /** Nakładka Game Over */
    private GameOverScreen gameOverScreen;
    /** Potrzebna do jednorazowego otworzenia dźwięku końca gry */
    private boolean hasPlayedGameOverSound = false;

    /**
     * Konstruktor klasy MainGame, który inicjalizuje główne komponenty gry.
     * <p>
     * Tworzy i ustawia zasoby, takie jak tekstury przeciwników,
     * ekran pauzy, interfejs użytkownika podczas gry, dźwięki oraz obiekty
     * reprezentujące gracza, przeciwników i osłony (bunkers).
     * </p>
     *
     * @param game referencja do głównej klasy {@link Main} umożliwia dostęp do zmiennych globalnych
     */
    public MainGame(Main game) {
        this.game = game;
        pauseScreen = new PauseScreen(game, this);
        //ingame UI
        inGameUI = new InGameUI(game);
        // wszystko z metody create()
        // Prepare your application here.
        spriteBatch = new SpriteBatch();//batch
        //rozmiar ekranu
        viewport = new FitViewport(16, 9);

        backgroundTexture = new Texture("background_black.png");//tlo
        enemyWhiteTexture = new Texture("enemy_white.png");//tekstura enemy-white
        enemyGreenTexture = new Texture("enemy_green.png");//tekstura enemy-green
        enemyRedTexture = new Texture("enemy_red.png");//tekstura enemy-red (najciezszy przeciwnik)
        //player
        if(game.selectedSpaceShip == null){
            player = new Player();
        }else{
            player = game.selectedSpaceShip;
        }
        gameOverScreen = new GameOverScreen(game);

        // reset stanów
        isGameOver = false;
        isGameUIshowed = false;
        isPaused = false;
        wasPaused = false;
        hasPlayedGameOverSound = false;


        hitSound = Gdx.audio.newSound(Gdx.files.internal("playerDamage.wav"));//dziwiek otrzymania obrazen
        damageBuildingSound = Gdx.audio.newSound(Gdx.files.internal("buildingDamage.wav"));
        hitBuildingSound =Gdx.audio.newSound(Gdx.files.internal("buildingHit.wav"));

        //enemy - tutaj mozna dodac nowe typy przeciwnikow
        enemyWhite = new Enemy(enemyWhiteTexture, 0, 0, .7f, .7f,5f,2f,2.5f,100,0);
        enemyGreen = new Enemy(enemyGreenTexture, 0, 0, .7f, .7f,10f,3f,3.5f,250,0);
        enemyRed = new Enemy(enemyRedTexture, 0, 0, .7f, .7f,15f,5f,3.0f,350,1);


        //wave - reczne tworzenie fali - w razie potrzeby
        //enemyWave = new EnemyWave(enemyWhite);
        //dodanie rzedu z nowym typem przeciwnika
        //enemyWave.addRow(viewport, 10, 0, enemyGreen);
        //enemyWave.addRow(viewport, 10, 1, enemyWhite);
        //enemyWave.addRow(viewport, 10, 2, enemyGreen);
        //enemyWave.spawnWave(viewport, 12, 0);
        //enemyWave.spawnWave(viewport, 12, 1);

        //zapisanie typow przeciwnikow do tablicy (potrzebne do tworzenia nowych fal przeciwnikow)
        EnemyTypes.add(enemyWhite,enemyGreen, enemyRed);

        //generowanie losowej pierwszej fali
        enemyWave = new EnemyWave(enemyWhite,game);
        enemyWave.generateNewWave(EnemyTypes, viewport,player);

        //budynki osłaniające w tablicy
        bunker = new ShieldBuilding(0,0);
        bunkers = new Array<>();
        bunkers.add(new ShieldBuilding(1, 2f));
        bunkers.add(new ShieldBuilding(5.33f, 2f));
        bunkers.add(new ShieldBuilding(9.66f, 2f));
        bunkers.add(new ShieldBuilding(14, 2f));

        //test only
        //player.activeCheatCode();
    }

    @Override
    public void resize(int width, int height) {
        // Resize your application here. The parameters represent the new window size.
        viewport.update(width, height, true);
    }

    /**
     * Pokazuje nakładkę Game Over
     */
    @Override
    public void show() {
        if(isGameOver){
            gameOverScreen.show();
        }
    }

    /**
     * Renderuje UI, nakładki Game Over, Pauzy.
     *
     * @param v Czas ostatniego renderu
     */
    @Override
    public void render(float v) {
        // Draw your application here.
        if(isGameOver){
            inGameUI.hide(); // wyłączenie interfejsu gry (score)
            draw();
            gameOverScreen.render();

            if (!hasPlayedGameOverSound) {
                hasPlayedGameOverSound = true;
                gameOverScreen.playGameOverSound();
                this.show();
            }
        }else{
            input();
            if(!isPaused) {
                logic();
                draw();
                //ingameUI
                inGameUI.render();
                if(!isGameUIshowed){
                    inGameUI.show();
                    isGameUIshowed = true;
                }
                wasPaused = false;
                pauseScreen.hide();
            }else{
                if (!wasPaused) {
                    pauseScreen.show();
                    wasPaused = true;
                }
                draw();
                pauseScreen.render();
            }
        }
    }

    /**
     * Obsługa przycisku ESC - wywołanie Pauzy
     */
    public void input(){
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            isPaused = !isPaused;
        }
    }
    /**
     * Główna logika gry wykonywana w każdej klatce (frame).
     * <p>
     * Metoda aktualizuje stan gry na podstawie czasu delta (czas od ostatniej klatki),
     * w tym pozycję gracza, ruch i zachowanie fal przeciwników, kolizje między obiektami,
     * sprawdza warunki zakończenia gry oraz aktualizuje elementy takie jak monety.
     * </p>
     * <ul>
     *     <li>Aktualizuje pozycję i stan gracza.</li>
     *     <li>Aktualizuje i porusza przeciwników oraz ich pociski.</li>
     *     <li>Sprawdza kolizje między pociskami przeciwników a graczem oraz budynkami.</li>
     *     <li>Sprawdza kolizje przeciwników z budynkami i graczem (np. dotarcie do dolnej krawędzi ekranu).</li>
     *     <li>Generuje nową falę przeciwników, jeśli poprzednia została wyeliminowana.</li>
     *     <li>Sprawdza warunki przegranej (śmierć gracza lub dotarcie przeciwników do dołu ekranu).</li>
     *     <li>Aktualizuje animację i stan monet na ekranie.</li>
     *     <li>Sprawdza kolizje pocisków gracza z budynkami.</li>
     * </ul>
     */
    public void logic(){
        float delta = Gdx.graphics.getDeltaTime();//czas gry
        player.update(delta, viewport);//aktualizacja pozycji playera


        //wave
        enemyWave.update(delta, viewport, player.getBulletsArray());//update pociskow (usuwanie itp)
        enemyWave.moveEnemies(delta, viewport);//ruch przeciwnikow
        enemyWave.tryShootRandomEnemy(delta);//strzelanie przeciwnikow
        enemyWave.updateEnemyBullets(delta, viewport);//update pociskow przeciwnikow
        enemyWave.checkCollisionWithPlayer(player.getBounds(), hitSound,player);//sprawdzenie kolizji pociskow przeciwnikow z graczem - aktualizacja hp gdy kolizja
        enemyWave.checkCollisionWithBuildings(bunkers, damageBuildingSound);//sprawdzenie kolizji z budynkami
        enemyWave.checkEnemiesSpritesCollisionWithBuildingsAndPlayer(bunkers, player);//sprawdzenie kolizji przeciwnikow z budynkami i graczem (przeciwnicy doszli na dol ekranu - przegrana)
        if(enemyWave.isAnyEnemyLeftOnField()==0){//sprawdzenie czy jest jeszcze jakis przeciwnik na ekranie
            enemyWave.generateNewWave(EnemyTypes, viewport,player);
            player.resetPlayerPosition();
            player.resetPlayerHP();//wart. pocz. HP
            bunker.resetAllBuildingsState(bunkers);//resetowanie stanu budynkow
            //test only
            //player.activeCheatCode();
        }
        if(player.isPlayerAlive()==0 || enemyWave.enemiesReachedBottomScreen()){
            //wywolanie UI z oknem przegranej
            isGameOver = true;
            game.updateHighscore();//aktualizacja highscore
        }
        // Aktualizacja coins
        for (int i = coins.size - 1; i >= 0; i--) {
            coins.get(i).update(delta);
            if (coins.get(i).isFinished()) {
                coins.removeIndex(i);
            }
        }
        player.checkCollisionWithBuildings(bunkers,hitBuildingSound);//sprawdzenie kolizji poc. gracza z budynkami
    }

    /**
     * Metoda odpowiedzialna za rysowanie wszystkich elementów gry na ekranie.
     * <p>
     * Czyści ekran, ustawia odpowiednią projekcję kamery, a następnie renderuje
     * tło, gracza, przeciwników, pociski, paski życia i inne elementy UI oraz
     * obiekty takie jak monety i budynki osłaniające.
     * </p>
     * <ul>
     *     <li>Czyszczenie ekranu kolorem czarnym.</li>
     *     <li>Ustawienie widoku i macierzy projekcji kamery.</li>
     *     <li>Rysowanie tła gry.</li>
     *     <li>Rysowanie gracza oraz przeciwników wraz z ich pociskami.</li>
     *     <li>Rysowanie pasków stanu, takich jak pasek cooldownu strzału i pasek życia gracza oraz przeciwników.</li>
     *     <li>Rysowanie monet oraz budynków osłaniających.</li>
     * </ul>
     */
    public void draw(){
        ScreenUtils.clear(Color.BLACK);
        viewport.apply();
        spriteBatch.setProjectionMatrix(viewport.getCamera().combined);

        spriteBatch.begin();
        spriteBatch.draw(backgroundTexture, 0, 0, viewport.getWorldWidth(), viewport.getWorldHeight());
        //tutaj miejsce na rysowanie rzeczy
        player.render(spriteBatch);//rysowanie gracza
        enemyWhite.render(spriteBatch);
        enemyWave.render(spriteBatch);
        enemyWave.renderEnemyBullets(spriteBatch);
        player.renderShootCooldownBar(spriteBatch);//rysowanie paska shootCooldown
        player.renderPlayerHPBar(spriteBatch);//rysowanie paska hp gracza
        enemyWave.renderEnemyHPBar(spriteBatch);//rysowanie paskow hp przeciwnikow
        bunker.renderBuildingHPBar(spriteBatch, bunkers);
        //coin
        coins = enemyWave.getArrayCoins();
        //rysowanie
        for (Coin coin : coins) {
            coin.render(spriteBatch);
        }
        //coin-end
        //budynki oslaniajace
        for (ShieldBuilding bunker : bunkers) {
            bunker.render(spriteBatch);
        }
        ////////////////////////
        spriteBatch.end();

    }
    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {

    }

    /**
     * Zwalnia wszystkie zasoby używane przez grę, takie jak tekstury, fonty i obiekty graficzne,
     * aby zapobiec wyciekom pamięci.
     */
    @Override
    public void dispose() {
        // Destroy application's resources here.
        spriteBatch.dispose();
        backgroundTexture.dispose();
        player.dispose();
        pauseScreen.dispose();
        inGameUI.dispose();
        gameOverScreen.dispose();
        TextureManager.disposeAll();
        FontManager.disposeAll();
    }

}
