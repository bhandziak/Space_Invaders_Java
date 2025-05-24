package com.badlogic.drop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

/**
 * Reprezentuje monetę w grze.
 * Pojawia się w miejscu pokonanego przeciwnika.
 * Zawiera logikę animacji, dźwięku oraz renderowania.
 * Monety umożliwiają kupowanie nowych statków.
 *
 * @author Kacper Dziduch
 */
public class Coin {
    /** Tekstura monety */
    private Texture coinTexture;
    /** Dźwięk odtwarzany przy wyświetleniu monety */
    protected Sound pickSound;
    /** Sprite reprezentujący graficznie monetę */
    Sprite sprite;
    //pozycja monety
    /** Pozycja X monety */
    float Xpos;
    /** Pozycja Y monety */
    float Ypos;
    //parametry dla znikajacej monety
    /** Czas, który upłynął od momentu aktywacji animacji */
    float elapsed = 0f;
    /** Czas trwania animacji zanikania (w sekundach) */
    float fadeDuration = .4f; // czas zanikania
    /** Dystans w pionie, o jaki moneta się unosi w trakcie animacji */
    float floatDistance = .5f; //zasieg unoszenia;
    /** Flaga informująca, czy animacja została zakończona */
    private boolean finished = false;//koniec animacji
    /**
     * Tworzy nową monetę w określonej pozycji.
     *
     * @param xpos pozycja X
     * @param ypos pozycja Y
     */
    public Coin(float xpos,float ypos) {
        pickSound = Gdx.audio.newSound(Gdx.files.internal("pickupCoin.wav"));
        coinTexture = new Texture("coin.png");
        sprite = new Sprite(coinTexture);
        Xpos = xpos;
        Ypos = ypos;
        sprite.setPosition(Xpos, Ypos);
        sprite.setSize(.5f,.5f);
    }
    /**
     * Odtwarza dźwięk zebrania monety.
     */
    public void playSound(){
        pickSound.play();
    }
    /**
     * Aktualizuje stan animacji zanikania i unoszenia monety.
     *
     * @param delta czas w sekundach, jaki upłynął od ostatniego wywołania metody.
     *
     */
    //Logika animacji
    public void update(float delta) {
        if (finished) return;

        elapsed += delta;
        float progress = Math.min(elapsed / fadeDuration, 1f);

        // przezroczystość
        float alpha = 1f - progress;
        sprite.setColor(1f, 1f, 1f, alpha);

        // unoszenie
        float newY = Ypos + floatDistance * progress;
        sprite.setPosition(sprite.getX(), newY);

        if (elapsed >= fadeDuration) {
            finished = true;
        }
    }
    /**
     * Rysuje monetę w trakcie animacji.
     *
     * @param batch SpriteBatch używany do renderowania
     */
    //Rysowanie animacji
    public void render(SpriteBatch batch) {
        if (!finished) {
            sprite.draw(batch);
        }
    }
    /**
     * Sprawdza, czy animacja została zakończona.
     *
     * @return true jeśli animacja się zakończyła; false w przeciwnym razie
     */
    public boolean isFinished() {
        return finished;
    }

}
