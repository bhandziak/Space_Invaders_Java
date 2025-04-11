package com.badlogic.drop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.Viewport;

public class Player {
    private Texture texture;
    private Sprite sprite;
    private Rectangle bounds;
    private Sound bulletSound;
    private float speed = 4f;
    private Array<PlayerBullet> bullets;
    private float shootDelay = 0.8f;//opoznienie strzalu
    private float shootTimer = 0f;//licznik opoznienia strzalu

    public Player() {
        texture = new Texture("spaceship.png");
        bulletSound = Gdx.audio.newSound(Gdx.files.internal("laserShoot.wav"));
        sprite = new Sprite(texture);
        sprite.setSize(.5f, .5f);
        sprite.setPosition(4 - 0.5f, .1f); // centrowanie na środku

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
            bullets.add(new PlayerBullet(bulletX, bulletY));
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
}
