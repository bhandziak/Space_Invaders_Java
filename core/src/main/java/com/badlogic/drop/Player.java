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
    float PlayerDamage;
    int playerAlive=1;  //zmienna do sprawdzenia czy player zyje
    private float shootDelay = 0.8f;//opoznienie strzalu
    private float shootTimer = 0f;//licznik opoznienia strzalu

    public Player() {
        texture = new Texture("spaceship.png");
        bulletSound = Gdx.audio.newSound(Gdx.files.internal("laserShoot.wav"));
        sprite = new Sprite(texture);
        sprite.setSize(.5f, .5f);
        sprite.setPosition(4 - 0.5f, .8f); // centrowanie na środku
        PlayerHP = 20f;//poczatkowe HP gracza
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
            bullets.add(new PlayerBullet(bulletX, bulletY, getPlayerDamage()));
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
        float barWidth = 200f;    // Szerokość paska
        float barHeight = 20f;    // Wysokość paska
        float barX = 20f;         // Pozycja X paska (lewy margines)
        float barY = 20f;         // Pozycja Y paska (dolny margines)
        ShapeRenderer shapeRenderer = new ShapeRenderer();

        //oblicza procent wypełnienia (ograniczony do zakresu 0-1)
        float progress = Math.min(shootTimer / shootDelay, 1f);

        //oblicza szerokość wypełnienia (ograniczoną do barWidth)
        float fillWidth = Math.min(progress * barWidth, barWidth);

        // Tło paska (szary prostokąt)
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.GRAY);
        shapeRenderer.rect(barX, barY, barWidth, barHeight);

        shapeRenderer.setColor(Color.GREEN);
        shapeRenderer.rect(barX, barY, fillWidth, barHeight);
        shapeRenderer.end();
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
}
