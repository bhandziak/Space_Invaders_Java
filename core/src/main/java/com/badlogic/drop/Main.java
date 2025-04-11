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

    @Override
    public void create() {
        // Prepare your application here.
        spriteBatch = new SpriteBatch();//batch
        backgroundTexture = new Texture("background_black.png");//tlo
        //rozmiar ekranu
        viewport = new FitViewport(8, 5);
        //player
        player = new Player();
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

    }
    public void draw(){
        ScreenUtils.clear(Color.BLACK);
        viewport.apply();
        spriteBatch.setProjectionMatrix(viewport.getCamera().combined);

        spriteBatch.begin();
        spriteBatch.draw(backgroundTexture, 0, 0, viewport.getWorldWidth(), viewport.getWorldHeight());
        //tutaj miejsce na rysowanie rzeczy
        player.render(spriteBatch);//rysowanie gracza

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
