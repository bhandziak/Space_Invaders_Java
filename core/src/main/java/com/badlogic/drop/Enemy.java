package com.badlogic.drop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

/**
 * Reprezentuje przeciwnika w grze.
 * Przeciwnik posiada punkty życia, zadaje obrażenia.
 *
 * @author Kacper Dziduch
 */
public class Enemy {
    /** Tekstura przeciwnika */
    private Texture enemyTexture;
    /** Dźwięk odtwarzany po zniszczeniu przeciwnika */
    protected Sound killSound;
    /** Obszar kolizji przeciwnika */
    private Rectangle bounds;
    /** Aktualne HP przeciwnika */
    protected float EnemyHP;//zycie przeciwnika
    /** Maksymalne HP przeciwnika */
    protected float EnemyMAXHP = EnemyHP;
    /** Obrażenia zadawane przez przeciwnika */
    protected float EnemyBulletDamage;//damage przeciwnika
    /** Flaga stanu przeciwnika: 1 = żyje, 0 = zniszczony */
    protected int enemyAlive=1;//poczatkowy stan przeciwnika
    /** Prędkość pocisku przeciwnika */
    protected float EnemyBulletSpeed;//poczatkowy stan przeciwnika
    /** Liczba punktów za pokonanie przeciwnika */
    protected int ScorePoints;
    /** Typ przeciwnika (określa np. jego zachowanie, wzorzec ataku, np. liczba wystrzelonych pocisków na raz) */
    protected int EnemyType; //typ przeciwnia (wplywa na rodzaj strzelania przeciwnia)
    /** Sprite przeciwnika */
    Sprite sprite;
    /** Tablica przeciwników */
    Array<Enemy> enemies = new Array<>();

    /**
     * Tworzy nowego przeciwnika z określonymi parametrami.
     *
     * @param enemyTexture       Tekstura przeciwnika
     * @param x                  Pozycja X
     * @param y                  Pozycja Y
     * @param width              Szerokość sprite'a
     * @param height             Wysokość sprite'a
     * @param enemyHP            Ilość punktów życia
     * @param enemyBulletDamage  Obrażenia zadawane przez pociski
     * @param bulletSpeed        Prędkość pocisków
     * @param scorePoints        Punkty za zabicie (score)
     * @param enemyType          Typ przeciwnika (wzorzec strzelania)
     */
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

    /**
     * Rysuje wszystkich przeciwników w tablicy enemies.
     *
     * @param batch SpriteBatch do renderowania
     */
    public void render(SpriteBatch batch) {
        for (Enemy enemy : enemies) {
            enemy.sprite.draw(batch);
        }
    }
    /**
     * Zwraca prostokąt kolizji przeciwnika.
     *
     * @return Obiekt Rectangle określający granice sprite’a
     */
    public Rectangle getBounds(){
        return bounds;
    }
    /**
     * Zwalnia zasoby (tekstura, dźwięk).
     */
    public void dispose() {
        enemyTexture.dispose();
        killSound.dispose();
    }

    /**
     * Zwraca obrażenia zadawane przez przeciwnika.
     *
     * @return Wartość obrażeń pocisków
     */
    public float getEnemyBulletDamage() {
        return EnemyBulletDamage;
    }
    /**
     * Odejmuje od HP przeciwnika określoną ilość obrażeń.
     * Ustawia stan na "martwy", jeśli HP spadnie do zera.
     *
     * @param damage Ilość otrzymanych obrażeń
     */
    public void EnemyTakeHit(float damage){
        EnemyHP -= damage;
        if(EnemyHP <= 0){
            enemyAlive=0;
        }
    }
    //zwraca 0 gdy hp przeciwnika <= 0
    /**
     * Zwraca status przeciwnika.
     *
     * @return 1 jeśli przeciwnik żyje, 0 jeśli został zabity
     */
    public float isEnemyAlive() {
        return enemyAlive;
    }
    /**
     * Zwraca aktualne punkty życia przeciwnika.
     *
     * @return HP
     */
    public float getEnemyHP() {
        return EnemyHP;
    }
    /**
     * Zwraca maksymalne punkty życia przeciwnika.
     *
     * @return Maksymalne HP
     */
    public float getEnemyMaxHP(){
        return EnemyMAXHP;
    }
    /**
     * Zwraca sprite przeciwnika.
     *
     * @return Obiekt Sprite
     */
    public Sprite getEnemySprite(){
        return sprite;
    }

}
