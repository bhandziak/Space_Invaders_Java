package com.badlogic.drop;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main implements ApplicationListener {
//tlo
Texture backgroundTexture;

SpriteBatch spriteBatch;
FitViewport viewport;

//Player
Player player;
Sound hitSound;//dziwiek otrzymania obrazen

//Enemy
Texture enemyWhiteTexture;
Texture enemyGreenTexture;
Enemy enemyWhite;
Enemy enemyGreen;

//Wave - fala przeciwnikow
EnemyWave enemyWave;

    @Override
    public void create() {
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

        //enemy
        enemyWhite = new Enemy(enemyWhiteTexture, 0, 0, .7f, .7f);
        enemyGreen = new Enemy(enemyGreenTexture, 0, 0, .7f, .7f);

        //wave
        enemyWave = new EnemyWave(enemyWhite);
        enemyWave.spawnWave(viewport, 12, 0);
        enemyWave.spawnWave(viewport, 12, 1);
        //dodanie rzedu z nowym typem przeciwnika
        enemyWave.addRow(viewport, 10, 2, enemyGreen);
    }

    @Override
    public void resize(int width, int height) {
        // Resize your application here. The parameters represent the new window size.
        viewport.update(width, height, true);
    }

    @Override
    public void render() {
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
        if (enemyWave.checkCollisionWithPlayer(player.getBounds(), hitSound)) {
            // np. odejmij życie, zakończ grę,
            //do rozbudowy

            System.out.println("Gracz trafiony!");
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
    public void dispose() {
        // Destroy application's resources here.
        spriteBatch.dispose();
        backgroundTexture.dispose();
        player.dispose();
    }
}
