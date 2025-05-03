package com.badlogic.drop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

public class Coin {
    private Texture coinTexture;
    protected Sound pickSound;
    Sprite sprite;


    //pozycja monety
    float Xpos;
    float Ypos;
    //parametry dla znikajacej monety
    float elapsed = 0f;
    float fadeDuration = .4f; // czas zanikania
    float floatDistance = .5f; //zasieg unoszenia;
    private boolean finished = false;//koniec animacji
    public Coin(float xpos,float ypos) {
        pickSound = Gdx.audio.newSound(Gdx.files.internal("pickupCoin.wav"));
        coinTexture = new Texture("coin.png");
        sprite = new Sprite(coinTexture);
        Xpos = xpos;
        Ypos = ypos;
        sprite.setPosition(Xpos, Ypos);
        sprite.setSize(.5f,.5f);
    }
    public void playSound(){
        pickSound.play();
    }
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

    //Rysowanie animacji
    public void render(SpriteBatch batch) {
        if (!finished) {
            sprite.draw(batch);
        }
    }
    public boolean isFinished() {
        return finished;
    }

}
