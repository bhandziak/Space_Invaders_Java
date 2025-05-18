package com.badlogic.drop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Reprezentuje pocisk wystrzelony przez gracza.
 * Obsługuje ruch pocisku, kolizje oraz renderowanie.
 *
 * @author Kacper Dziduch
 */
public class PlayerBullet {
    /** Tekstura pocisku */
    private Texture bulletTexture;
    /** Sprite pocisku */
    private Sprite bulletSprite;
    /** Prostokąt kolizji pocisku */
    private Rectangle bulletRectangle;
    /** Prędkość pocisku */
    private float speed = 4f;
    /** Czy pocisk jest nadal aktywny (widoczny na ekranie) */
    private boolean isAlive = true;
    /** Obrażenia zadawane przez pocisk */
    float PlayerBulletDamage;

    /**
     * Tworzy nowy pocisk gracza.
     *
     * @param x                 Pozycja początkowa pocisku X
     * @param y                 Pozycja początkowa pocisku Y
     * @param playerBulletDamage Obrażenia zadawane przez pocisk
     * @param width             Szerokość pocisku
     * @param height            Wysokość pocisku
     */
    public PlayerBullet(float x, float y,float playerBulletDamage,
                        float width, float height) {
        bulletTexture = new Texture("bullet.png");

        bulletSprite = new Sprite(bulletTexture);
        bulletSprite.setSize(width, height);
        bulletSprite.setPosition(x, y);
        bulletRectangle = new Rectangle(x, y, bulletSprite.getWidth(), bulletSprite.getHeight());
        PlayerBulletDamage = playerBulletDamage;
    }

    /**
     * Aktualizuje pozycję pocisku oraz jego status.
     * Jeśli wyjdzie poza ekran, oznaczany jest jako martwy i jest usuwany aby nie zaśmiecać pamięci.
     *
     * @param delta    Czas między klatkami
     * @param viewport Widok gry, używany do określenia granic ekranu
     */
    public void update(float delta, Viewport viewport) {
        if (!isAlive) return;

        bulletSprite.translateY(speed * delta);
        bulletRectangle.setPosition(bulletSprite.getX(), bulletSprite.getY());

        if (bulletSprite.getY() > viewport.getWorldHeight()) {
            isAlive = false;
        }
    }

    /**
     * Rysuje pocisk, jeśli jest aktywny.
     *
     * @param batch SpriteBatch używany do rysowania
     */
    public void render(SpriteBatch batch) {
        if (isAlive) bulletSprite.draw(batch);
    }

    /**
     * Sprawdza, czy pocisk koliduje z innym prostokątem (np. przeciwnikiem)
     *
     * @param otherRect Prostokąt kolizji, z którym sprawdzana jest kolizja
     * @return true jeśli kolizja występuje i pocisk jest aktywny
     */
    public boolean collides(Rectangle otherRect) {
        return isAlive && bulletRectangle.overlaps(otherRect);
    }


    /**
     * Zwraca informację, czy pocisk jest aktywny (nie wszedł z niczym jeszcze w kolizje i jest w obrębie ekranu).
     *
     * @return true jeśli jest
     */
    public boolean isAlive() {
        return isAlive;
    }

    /**
     * Dezaktywuje pocisk (gdy już nie jest aktywny).
     */
    public void destroy() {
        isAlive = false;
    }

    /**
     * Zwraca wartość obrażeń zadawanych przez ten pocisk.
     *
     * @return Obrażenia pocisku
     */
    public float getBulletDamage(){
        return PlayerBulletDamage;
    }
}
