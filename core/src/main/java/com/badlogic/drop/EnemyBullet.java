package com.badlogic.drop;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.viewport.Viewport;


/**
 * Klasa reprezentująca pocisk przeciwnika.
 * Odpowiada za ruch pocisku, jego pozycję, kolizje i wizualizację.
 *
 * @author Kacper Dziduch
 */
public class EnemyBullet {
    /** Tekstura pocisku */
    private Texture texture;
    /** Sprite pocisku */
    private Sprite sprite;
    /** Prędkość pocisku */
    private float bulletSpeed;
    /** Przeciwnik, który wystrzelił ten pocisk */
    private Enemy enemy;
    /** Kierunek ruchu pocisku na osi X */
    float velocityX=0;//kierunek strzalu w osi x
    /** Kierunek ruchu pocisku na osi Y */
    float velocityY;//kierunek strzalu w osi y

    /**
     * Konstruktor tworzący pocisk w pozycji (x, y), przypisany do określonego przeciwnika.
     *
     * @param x             Pozycja początkowa pocisku na osi X
     * @param y             Pozycja początkowa pocisku na osi Y
     * @param enemyTemplate Referencja do przeciwnika, który wystrzelił pocisk
     */
    public EnemyBullet(float x, float y,Enemy enemyTemplate) {
        texture = new Texture("bullet.png");
        sprite = new Sprite(texture);
        sprite.setSize(0.1f, 0.4f);
        sprite.setPosition(x, y);
        sprite.setOriginCenter();//centrowanie sprite
        enemy = enemyTemplate;
        bulletSpeed = enemyTemplate.EnemyBulletSpeed;
    }

    /**
     * Aktualizuje pozycję pocisku na podstawie kierunku i prędkości.
     * Normalizuje wektor ruchu i przesuwa sprite pocisku o odpowiednią wartość.
     *
     * @param delta Czas, jaki upłynął od ostatniej aktualizacji (w sekundach)
     */
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

    /**
     * Rysuje pocisk na ekranie.
     *
     * @param batch Batch do renderowania sprite'ów
     */
    public void render(SpriteBatch batch) {
        sprite.draw(batch);
    }

    /**
     * Sprawdza, czy pocisk znajduje się nadal w granicach widoku (viewportu).
     *
     * @param viewport Aktualny viewport świata gry
     * @return true, jeśli pocisk jest widoczny w obszarze gry; false, jeśli wyszedł poza ekran
     */
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
    /**
     * Ustawia kierunek ruchu pocisku.
     *
     * @param vx Składowa prędkości na osi X
     * @param vy Składowa prędkości na osi Y
     */
    private void setVelocity(float vx, float vy) {
        this.velocityX = vx;
        this.velocityY = vy;
    }
    //kierunek strzalu i obrot tekstury
    /**
     * Ustawia kierunek ruchu pocisku i odpowiednio obraca sprite,
     * aby wskazywał kierunek lotu.
     *
     * @param vx Składowa prędkości na osi X
     * @param vy Składowa prędkości na osi Y
     */
    public void setDirectionAndRotate(float vx, float vy) {
        setVelocity(vx, vy);

        float angleRad = (float) Math.atan2(vy, vx);
        float angleDeg = (float) Math.toDegrees(angleRad);
        sprite.setRotation(angleDeg - 90);//rotacja tekstury
    }
    /**
     * Zwraca prostokąt kolizji pocisku (do detekcji kolizji z innymi obiektami).
     *
     * @return Obiekt Rectangle reprezentujący obszar pocisku
     */
    public Rectangle getBounds() {
        return sprite.getBoundingRectangle();
    }

    /**
     * Zwraca przeciwnika, który wystrzelił ten pocisk.
     *
     * @return Obiekt Enemy powiązany z tym pociskiem
     */
    public Enemy getEnemyByBullet(){
        return enemy;
    }
}
