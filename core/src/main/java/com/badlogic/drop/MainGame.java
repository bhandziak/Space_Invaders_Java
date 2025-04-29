package com.badlogic.drop;

import com.badlogic.gdx.Screen;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
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

    //Wave - fala przeciwnikow
    EnemyWave enemyWave;

    //tablica na typy przeciwnikow
    Array<Enemy> EnemyTypes = new Array<>();

    public MainGame(Main game) {
        this.game = game;
        // wszystko z metody create()
        // Prepare your application here.
        spriteBatch = new SpriteBatch();//batch
        //rozmiar ekranu
        viewport = new FitViewport(16, 9);


        backgroundTexture = new Texture("background_black.png");//tlo
        enemyWhiteTexture = new Texture("enemy_white.png");//tekstura enemy-white
        enemyGreenTexture = new Texture("enemy_green.png");//tekstura enemy-green
        //player
        player = new Player();
        hitSound = Gdx.audio.newSound(Gdx.files.internal("playerDamage.wav"));//dziwiek otrzymania obrazen

        //enemy - tutaj mozna dodac nowe typy przeciwnikow
        enemyWhite = new Enemy(enemyWhiteTexture, 0, 0, .7f, .7f,5f,2f);
        enemyGreen = new Enemy(enemyGreenTexture, 0, 0, .7f, .7f,10f,3f);

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
        enemyWave = new EnemyWave(enemyWhite);
        enemyWave.generateNewWave(EnemyTypes, viewport,player);

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
        input();
        logic();
        draw();
    }

    public void input(){

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
        if(enemyWave.isAnyEnemyLeftOnField()==0){//sprawdzenie czy jest jeszcze jakis przeciwnik na ekranie
            enemyWave.generateNewWave(EnemyTypes, viewport,player);
            player.resetPlayerPosition();
            player.resetPlayerHP();//wart. pocz. HP
            //test only
            //player.activeCheatCode();
        }
        if(player.isPlayerAlive()==0){
            //TODO tutaj wywolanie UI z oknem przegranej
        }
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
    }
}
