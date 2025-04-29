package com.badlogic.drop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.Viewport;

public class Player {
    private Texture texture;
    private Sprite sprite;
    private Rectangle bounds;
    private Sound bulletSound;
    private float speed = 4f;
    private Array<PlayerBullet> bullets;
    float PlayerHP;
    float PlayerMaxHP;
    float PlayerDamage;
    int playerAlive=1;  //zmienna do sprawdzenia czy player zyje
    private float shootDelay = 0.8f;//opoznienie strzalu
    private float shootTimer = 0f;//licznik opoznienia strzalu
    private final float playerStartXPosition = 8-0.5f;
    private final float playerStartYPosition = 0.8f;
    //tekstury
    Texture barFillTexture = new Texture("progressBar_green.png");// textura dla paska shootDelay
    Texture HPBarTexture = new Texture("progressBar_white.png");// textura dla HP gracza

    public Player() {
        texture = new Texture("spaceship.png");
        bulletSound = Gdx.audio.newSound(Gdx.files.internal("laserShoot.wav"));
        sprite = new Sprite(texture);
        sprite.setSize(.5f, .5f);
        sprite.setPosition(playerStartXPosition, playerStartYPosition); // centrowanie na środku
        PlayerHP = 20f;//poczatkowe HP gracza
        PlayerMaxHP = PlayerHP;//max HP gracza
        PlayerDamage = 5f;//poczatkowe damage gracza
        bullets = new Array<>();
        bounds = new Rectangle(sprite.getX(), sprite.getY(),
            sprite.getWidth(), sprite.getHeight());
    }

    public void update(float delta, Viewport viewport) {
        shootTimer += delta;//licznik czasu
        // Sterowanie
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            sprite.translateX(-speed * delta);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            sprite.translateX(speed * delta);
        }

        // Ograniczenia ekranu
        float worldWidth = viewport.getWorldWidth();
        float bucketWidth = sprite.getWidth();
        float clampedX = MathUtils.clamp(sprite.getX(), 0, worldWidth - bucketWidth);
        sprite.setX(clampedX);

        // Aktualizacja prostokąta kolizji
        bounds.setPosition(sprite.getX(), sprite.getY());

        // Strzał
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)&&shootTimer>=shootDelay) {
            shootTimer = 0f; // reset licznika

            float bulletX = sprite.getX() + sprite.getWidth() / 2f-.05f;
            float bulletY = sprite.getY() + sprite.getHeight();
            bullets.add(new PlayerBullet(bulletX, bulletY,PlayerDamage));
            bulletSound.play();
        }

        // Aktualizacja i czyszczenie pocisków
        for (int i = bullets.size - 1; i >= 0; i--) {
            PlayerBullet bullet = bullets.get(i);
            bullet.update(delta, viewport);
            if (!bullet.isAlive()) {
                bullets.removeIndex(i);
            }
        }
    }

    public void render(SpriteBatch batch) {
        sprite.draw(batch);
        for (PlayerBullet bullet : bullets) {
            bullet.render(batch);
        }
    }
    public void renderShootCooldownBar(SpriteBatch batch){
        float barWidth = 2f;//szerokosc
        float barHeight = 0.15f;//wysokosc
        float barX = 0.5f;//polozenie x
        float barY = 0.3f;//polozenie y

        float progress = Math.min(shootTimer / shootDelay, 1f);
        float fillWidth = Math.min(progress * barWidth, barWidth);

        // Rysowanie paska
        batch.draw(barFillTexture, barX, barY, fillWidth, barHeight);
    }
    public void renderPlayerHPBar(SpriteBatch batch){
        float barWidth = 2f;//szerokosc
        float barHeight = 0.15f;//wysokosc
        float barX = 13.5f;//polozenie x
        float barY = 0.3f;//polozenie y
        float progress = Math.max(0, Math.min(PlayerHP / PlayerMaxHP, 1f));
        float fillWidth = Math.min(progress * barWidth, barWidth);
        batch.draw(HPBarTexture, barX, barY, fillWidth, barHeight);
    }
    public Rectangle getBounds() {
        return bounds;
    }

    public void dispose() {
        texture.dispose();
        for (PlayerBullet bullet : bullets) {
            bullet.destroy();
        }
    }
    public Array<PlayerBullet> getBulletsArray(){
        return bullets;
    }
    public void PlayerTakeHit(float damage){
        PlayerHP -= damage;
        if(PlayerHP <= 0){
            playerAlive=0;
        }
    }

    public void clearLeftPlayerBullets(){
        bullets.clear();
    }
    //zwraca 0 gdy hp gracza <= 0
    public int isPlayerAlive(){
        return playerAlive;
    }
    public float getPlayerHP(){
        return PlayerHP;
    }
    public float getPlayerDamage(){
        return PlayerDamage;
    }
    public void resetPlayerPosition(){
        sprite.setPosition(playerStartXPosition, playerStartYPosition); // centrowanie na środku
    }
    public void resetPlayerHP(){
        PlayerHP = 20f;//poczatkowe HP gracza
    }
    public void activeCheatCode(){
        PlayerHP = 1000;
        PlayerDamage = 1000;
        shootDelay = 0;
    }

}
