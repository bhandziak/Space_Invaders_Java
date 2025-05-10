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
    float velocityX=0;//kierunek strzalu w osi x
    float velocityY;//kierunek strzalu w osi y
    public EnemyBullet(float x, float y,Enemy enemyTemplate) {
        texture = new Texture("bullet.png");
        sprite = new Sprite(texture);
        sprite.setSize(0.1f, 0.4f);
        sprite.setPosition(x, y);
        sprite.setOriginCenter();//centrowanie sprite
        enemy = enemyTemplate;
        bulletSpeed = enemyTemplate.EnemyBulletSpeed;
    }

    public void update(float delta) {
        //długość wektora prędkości
        float length = (float) Math.sqrt(velocityX * velocityX + velocityY * velocityY);

        // Normalizacja
        float vx = 0;
        float vy = 0;
        if (length != 0) {
            vx = (velocityX / length) * bulletSpeed;
            vy = (velocityY / length) * bulletSpeed;
        }

        sprite.translate(vx * delta, vy * delta);
    }
    public void render(SpriteBatch batch) {
        sprite.draw(batch);
    }

    public boolean isAlive(Viewport viewport) {
        float spriteX = sprite.getX();
        float spriteY = sprite.getY();
        float spriteWidth = sprite.getWidth();
        float spriteHeight = sprite.getHeight();

        float worldWidth = viewport.getWorldWidth();
        //float worldHeight = viewport.getWorldHeight();

        return spriteX + spriteWidth > 0 &&           //pocisk nie wyszedł z lewej
            spriteX < worldWidth &&                //pocisk nie wyszedł z prawej
            spriteY + spriteHeight > 0;// &&          //pocisk nie wyszedł z dołu
            //spriteY < worldHeight;                 //pocisk nie wyszedł z góry
    }
    //kierunek strzalu
    private void setVelocity(float vx, float vy) {
        this.velocityX = vx;
        this.velocityY = vy;
    }
    //kierunek strzalu i obrot tekstury
    public void setDirectionAndRotate(float vx, float vy) {
        setVelocity(vx, vy);

        float angleRad = (float) Math.atan2(vy, vx);
        float angleDeg = (float) Math.toDegrees(angleRad);
        sprite.setRotation(angleDeg - 90);//rotacja tekstury
    }
    public Rectangle getBounds() {
        return sprite.getBoundingRectangle();
    }

    public Enemy getEnemyByBullet(){
        return enemy;
    }
}
