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
    private Enemy enemy;
    public EnemyBullet(float x, float y,Enemy enemyTemplate) {
        texture = new Texture("drop.png");
        sprite = new Sprite(texture);
        sprite.setSize(0.1f, 0.4f);
        sprite.setPosition(x, y);
        enemy = enemyTemplate;
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

    public Enemy getEnemyByBullet(){
        return enemy;
    }
}
