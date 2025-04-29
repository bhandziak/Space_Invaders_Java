package com.badlogic.drop;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.viewport.Viewport;

public class EnemyBullet {
    private Texture texture;
    private Sprite sprite;
    private float bulletSpeed;
    private Enemy enemy;
    public EnemyBullet(float x, float y,Enemy enemyTemplate) {
        texture = new Texture("drop.png");
        sprite = new Sprite(texture);
        sprite.setSize(0.1f, 0.4f);
        sprite.setPosition(x, y);
        enemy = enemyTemplate;
        bulletSpeed = enemyTemplate.EnemyBulletSpeed;
    }

    public void update(float delta) {
        sprite.translateY(-bulletSpeed * delta);
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

    public Enemy getEnemyByBullet(){
        return enemy;
    }
}
