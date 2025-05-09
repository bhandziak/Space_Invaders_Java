package com.badlogic.drop;

import com.badlogic.gdx.*;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class MainGame implements Screen {
    final Main game;
    // wszystkie pola: player, enemy, viewport, spriteBatch itd...
    Texture backgroundTexture;

    SpriteBatch spriteBatch;
    FitViewport viewport;
    //Player
    Player player;
    Sound hitSound;//dziwiek otrzymania obrazen

    //Enemy
    Texture enemyWhiteTexture;
    Texture enemyGreenTexture;
    Enemy enemyWhite;//5hp   2dmg
    Enemy enemyGreen;//10hp  3dmg
    //coin
    Array<Coin> coins = new Array<>();
    //Wave - fala przeciwnikow
    EnemyWave enemyWave;

    //tablica na typy przeciwnikow
    Array<Enemy> EnemyTypes = new Array<>();

    //budynki oslaniajace
    Array<ShieldBuilding> bunkers;
    ShieldBuilding bunker;
    //flaga zatrzymania gry
    private boolean isPaused = false;
    private boolean wasPaused = false;
    private PauseScreen pauseScreen;
    private InGameUI inGameUI;
    private boolean isGameOver = false;
    private boolean isGameUIshowed = false;
    private GameOverScreen gameOverScreen;

    public MainGame(Main game) {
        this.game = game;
        pauseScreen = new PauseScreen(game, this);
        //ingame UI
        inGameUI = new InGameUI(game);
        gameOverScreen = new GameOverScreen(game);
        // wszystko z metody create()
        // Prepare your application here.
        spriteBatch = new SpriteBatch();//batch
        //rozmiar ekranu
        viewport = new FitViewport(16, 9);

        backgroundTexture = new Texture("background_black.png");//tlo
        enemyWhiteTexture = new Texture("enemy_white.png");//tekstura enemy-white
        enemyGreenTexture = new Texture("enemy_green.png");//tekstura enemy-green
        //player
        if(game.selectedSpaceShip == null){
            player = new Player();
        }else{
            player = game.selectedSpaceShip;
        }


        hitSound = Gdx.audio.newSound(Gdx.files.internal("playerDamage.wav"));//dziwiek otrzymania obrazen

        //enemy - tutaj mozna dodac nowe typy przeciwnikow
        enemyWhite = new Enemy(enemyWhiteTexture, 0, 0, .7f, .7f,5f,2f,2.5f,100);
        enemyGreen = new Enemy(enemyGreenTexture, 0, 0, .7f, .7f,10f,3f,3.5f,250);

        //wave - reczne tworzenie fali - w razie potrzeby
        //enemyWave = new EnemyWave(enemyWhite);
        //dodanie rzedu z nowym typem przeciwnika
        //enemyWave.addRow(viewport, 10, 0, enemyGreen);
        //enemyWave.addRow(viewport, 10, 1, enemyWhite);
        //enemyWave.addRow(viewport, 10, 2, enemyGreen);
        //enemyWave.spawnWave(viewport, 12, 0);
        //enemyWave.spawnWave(viewport, 12, 1);

        //zapisanie typow przeciwnikow do tablicy (potrzebne do tworzenia nowych fal przeciwnikow)
        EnemyTypes.add(enemyWhite,enemyGreen);

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

    @Override
    public void show() {
    }

    @Override
    public void render(float v) {
        // Draw your application here.
        if(isGameOver){
            inGameUI.hide(); // wyłączenie interfejsu gry (score)
            draw();
            gameOverScreen.render();
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

    public void input(){
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            isPaused = !isPaused;
        }
    }
    public void logic(){
        float delta = Gdx.graphics.getDeltaTime();//czas gry
        player.update(delta, viewport);//aktualizacja pozycji playera


        //wave
        enemyWave.update(delta, viewport, player.getBulletsArray());//update pociskow (usuwanie itp)
        enemyWave.moveEnemies(delta, viewport);//ruch przeciwnikow
        enemyWave.tryShootRandomEnemy(delta);//strzelanie przeciwnikow
        enemyWave.updateEnemyBullets(delta, viewport);//update pociskow przeciwnikow
        enemyWave.checkCollisionWithPlayer(player.getBounds(), hitSound,player);//sprawdzenie kolizji pociskow przeciwnikow z graczem - aktualizacja hp gdy kolizja
        enemyWave.checkCollisionWithBuildings(bunkers,hitSound);//sprawdzenie kolizji z budynkami
        if(enemyWave.isAnyEnemyLeftOnField()==0){//sprawdzenie czy jest jeszcze jakis przeciwnik na ekranie
            enemyWave.generateNewWave(EnemyTypes, viewport,player);
            player.resetPlayerPosition();
            player.resetPlayerHP();//wart. pocz. HP
            bunker.resetAllBuildingsState(bunkers);//resetowanie stanu budynkow
            //test only
            //player.activeCheatCode();
        }
        if(player.isPlayerAlive()==0){
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
        player.checkCollisionWithBuildings(bunkers,hitSound);//sprawdzenie kolizji poc. gracza z budynkami
    }
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
        // Invoked when your application is paused.
    }

    @Override
    public void resume() {
        // Invoked when your application is resumed after pause.
    }

    @Override
    public void hide() {

    }

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
