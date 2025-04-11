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

public class PlayerBullet {
    private Texture bulletTexture;
    private Sound bulletSound;
    private Sprite bulletSprite;
    private Rectangle bulletRectangle;
    private float bulletTimer;
    private float speed = 4f;
    private boolean isAlive = true;

    public PlayerBullet(float x, float y) {
        bulletTexture = new Texture("drop.png");
        //do zmiany

        bulletSprite = new Sprite(bulletTexture);
        bulletSprite.setSize(0.1f, 0.3f);
        bulletSprite.setPosition(x, y);
        bulletRectangle = new Rectangle(x, y, bulletSprite.getWidth(), bulletSprite.getHeight());
    }

    public void update(float delta, Viewport viewport) {
        if (!isAlive) return;

        bulletSprite.translateY(speed * delta);
        bulletRectangle.setPosition(bulletSprite.getX(), bulletSprite.getY());

        if (bulletSprite.getY() > viewport.getWorldHeight()) {
            isAlive = false;
        }
    }

    public void render(SpriteBatch batch) {
        if (isAlive) bulletSprite.draw(batch);
    }

    public boolean collides(Rectangle otherRect) {
        return isAlive && bulletRectangle.overlaps(otherRect);
    }
    public boolean isAlive() {
        return isAlive;
    }

    public void destroy() {
        isAlive = false;
    }
}
