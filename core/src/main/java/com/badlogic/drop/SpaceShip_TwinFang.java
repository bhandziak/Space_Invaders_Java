package com.badlogic.drop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Klasa reprezentująca statek kosmiczny typu Twin Fang.
 * <p>
 * Rozszerza klasę {@link Player} i ustawia statystykę statku:
 * <ul>
 *   <li>Obrażenia: 4</li>
 *   <li>Opóźnienie strzału: 0,4 s</li>
 *   <li>HP: 10</li>
 *   <li>Prędkość: 6</li>
 * </ul>
 * Dodatkowo działa animacja wystrzału z 2 luf.
 *
 * @author Bartłomiej Handziak
 */


public class SpaceShip_TwinFang extends Player{
    protected float leftRightBulletOffset = 0.2f;
    protected boolean isLeftCannonActive = true;
    /**
     * Tworzy nową instancję statku kosmicznego Twin Fang
     */
    public SpaceShip_TwinFang(){
        super();

        texture = new Texture("spaceship_twin_fang.png");
        sprite.setTexture(texture);

        PlayerDamage = 4f;
        shootDelay = 0.4f;
        PlayerHP = 10f;
        PlayerMaxHP = PlayerHP;
        speed = 6f;
    }

    /**
     * Logika wystrzału z 2 luf na przemian.
     * @param delta
     * @param viewport
     */
    @Override
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

            if(isLeftCannonActive){
                bulletX += leftRightBulletOffset;

            }else{
                bulletX -= leftRightBulletOffset;
            }
            isLeftCannonActive = !isLeftCannonActive;

            float bulletY = sprite.getY() + sprite.getHeight();
            bullets.add(new PlayerBullet(bulletX, bulletY,PlayerDamage, bulletWidth, bulletHeight));
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
}
