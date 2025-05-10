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
    protected Sound killSound;
    private Rectangle bounds;
    protected float EnemyHP;//zycie przeciwnika
    protected float EnemyMAXHP = EnemyHP;
    protected float EnemyBulletDamage;//damage przeciwnika
    protected int enemyAlive=1;//poczatkowy stan przeciwnika
    protected float EnemyBulletSpeed;//poczatkowy stan przeciwnika
    protected int ScorePoints;
    protected int EnemyType; //typ przeciwnia (wplywa na rodzaj strzelania przeciwnia)
    Sprite sprite;
    Array<Enemy> enemies = new Array<>();

    public Enemy(Texture enemyTexture, float x, float y, float width,float height,float enemyHP,float enemyBulletDamage,float bulletSpeed,int scorePoints, int enemyType) {
        killSound = Gdx.audio.newSound(Gdx.files.internal("drop.mp3"));
        sprite = new Sprite(enemyTexture);
        sprite.setPosition(x, y);
        sprite.setSize(width,height);
        bounds = new Rectangle(x, y, width, height);
        EnemyHP = enemyHP;//zycie przeciwnika
        EnemyMAXHP = EnemyHP;
        EnemyBulletDamage = enemyBulletDamage;
        EnemyBulletSpeed = bulletSpeed;
        ScorePoints = scorePoints; //punkty za pokonanie przeciwnika
        EnemyType = enemyType;
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
        killSound.dispose();
    }

    public float getEnemyBulletDamage() {
        return EnemyBulletDamage;
    }
    public void EnemyTakeHit(float damage){
        EnemyHP -= damage;
        if(EnemyHP <= 0){
            enemyAlive=0;
        }
    }
    //zwraca 0 gdy hp przeciwnika <= 0
    public float isEnemyAlive() {
        return enemyAlive;
    }
    public float getEnemyHP() {
        return EnemyHP;
    }
    public float getEnemyMaxHP(){
        return EnemyMAXHP;
    }
    public Sprite getEnemySprite(){
        return sprite;
    }

}
