package com.badlogic.drop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
public class Enemy {
    private Texture enemyTexture;
    protected Sound dropSound;
    private Rectangle bounds;
    Sprite sprite;
    Array<Enemy> enemies = new Array<>();
    public Enemy(Texture enemyTexture, float x, float y, float width,float height) {
        dropSound = Gdx.audio.newSound(Gdx.files.internal("drop.mp3"));
        sprite = new Sprite(enemyTexture);
        sprite.setPosition(x, y);
        sprite.setSize(width,height);
        bounds = new Rectangle(x, y, width, height);
    }

    public void render(SpriteBatch batch) {
        for (Enemy enemy : enemies) {
            enemy.sprite.draw(batch);
        }
    }
    public Rectangle getBounds(){
        return bounds;
    }
    public void dispose() {
        enemyTexture.dispose();
        dropSound.dispose();
    }
}
