package com.badlogic.drop;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.viewport.Viewport;

public class EnemyBullet {
    private Texture texture;
    private Sprite sprite;
    private float speed = 3f;
    protected float EnemyBulletDamage;//damage przeciwnika
    public EnemyBullet(float x, float y,float enemyBulletDamage) {
        texture = new Texture("drop.png");
        sprite = new Sprite(texture);
        sprite.setSize(0.1f, 0.4f) ;
        sprite.setPosition(x, y);
        EnemyBulletDamage = enemyBulletDamage;
    }

    public void update(float delta) {
        sprite.translateY(-speed * delta);
    }

    public void render(SpriteBatch batch) {
        sprite.draw(batch);
    }

    public boolean isAlive(Viewport viewport) {
        return sprite.getY() + sprite.getHeight() > 0;
    }

    public Rectangle getBounds() {
        return sprite.getBoundingRectangle();
    }

    public float getEnemyBulletDamage() {
        return EnemyBulletDamage;
    }
}
