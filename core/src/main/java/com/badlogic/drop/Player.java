package com.badlogic.drop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.Viewport;


/**
 * Reprezentuje gracza w grze.
 * Obsługuje ruch, strzelanie, pasek HP, pasek z 'cooldown' strzału,
 * animacje napełniania paska 'cooldown' z czasem, dźwięki strzałow,
 * oraz logikę kolizji pocisków gracza z budynkami osłonnymi {@link ShieldBuilding}
 *
 *
 *
 *  @author Kacper Dziduch
 */
public class Player {
    /** Tekstura statku gracza */
    protected Texture texture;
    /** Sprite gracza */
    protected Sprite sprite;
    /** Prostokąt kolizji dla gracza */
    protected Rectangle bounds;
    /** Dźwięk odtwarzany przy strzale */
    protected Sound bulletSound;
    /** Prędkość poruszania się gracza */
    protected float speed = 4f;
    /** Lista pocisków wystrzelonych przez gracza */
    protected Array<PlayerBullet> bullets;
    /** Aktualne punkty życia gracza */
    protected float PlayerHP;
    /** Maksymalne punkty życia gracza */
    float PlayerMaxHP;
    /** Obrażenia zadawane przez gracza */
    float PlayerDamage;
    /** Flaga: 1 jeśli gracz żyje, 0 jeśli zginął */
    int playerAlive=1;  //zmienna do sprawdzenia czy player zyje
    /** Opóźnienie między strzałami (sekundy) */
    protected float shootDelay = 0.8f;//opoznienie strzalu
    /** Licznik opóźnienia między strzałami */
    protected float shootTimer = 0f;//licznik opoznienia strzalu
    /** Początkowa pozycja X gracza */
    private final float playerStartXPosition = 8-0.5f;
    /** Początkowa pozycja Y gracza */
    private final float playerStartYPosition = 0.8f;
    //tekstury
    /** Tekstura paska ładowania strzału (cooldown) */
    Texture barFillTexture = new Texture("progressBar_green.png");// textura dla paska shootDelay
    /** Tekstura paska zdrowia gracza */
    Texture HPBarTexture = new Texture("progressBar_white.png");// textura dla HP gracza
    // rozmiar pocisku
    /** Szerokość pocisku */
    protected float bulletWidth = 0.05f;
    /** Wysokość pocisku */
    protected float bulletHeight = 0.3f;

    /**
     * Tworzy nową instancję gracza, inicjalizując tekstury, pozycję startową, dźwięki oraz statystyki takie jak:
     * ilość HP czy obrażenia strzału.
     */
    public Player() {
        texture = new Texture("spaceship.png");
        bulletSound = Gdx.audio.newSound(Gdx.files.internal("laserShoot.wav"));
        sprite = new Sprite(texture);
        sprite.setSize(.5f, .5f);
        sprite.setPosition(playerStartXPosition, playerStartYPosition); // centrowanie na środku
        PlayerHP = 20f;//poczatkowe HP gracza
        PlayerMaxHP = PlayerHP;//max HP gracza
        PlayerDamage = 5f;//poczatkowe damage gracza
        bullets = new Array<>();
        bounds = new Rectangle(sprite.getX(), sprite.getY(),
            sprite.getWidth(), sprite.getHeight());
    }
    /**
     * Aktualizuje logikę gracza: ruch, strzelanie i pociski.
     *
     * @param delta    Czas między klatkami
     * @param viewport Widok gry, używany do ograniczenia ruchu przy krawędziach ekranu gry
     */
    public void update(float delta, Viewport viewport) {
        shootTimer += delta;//licznik czasu
        // Sterowanie
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            sprite.translateX(-speed * delta);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            sprite.translateX(speed * delta);
        }

        // Ograniczenia ekranu
        float worldWidth = viewport.getWorldWidth();
        float bucketWidth = sprite.getWidth();
        float clampedX = MathUtils.clamp(sprite.getX(), 0, worldWidth - bucketWidth);
        sprite.setX(clampedX);

        // Aktualizacja prostokąta kolizji
        bounds.setPosition(sprite.getX(), sprite.getY());

        // Strzał
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)&&shootTimer>=shootDelay) {
            shootTimer = 0f; // reset licznika

            float bulletX = sprite.getX() + sprite.getWidth() / 2f-.05f;
            float bulletY = sprite.getY() + sprite.getHeight();
            bullets.add(new PlayerBullet(bulletX, bulletY,PlayerDamage, bulletWidth, bulletHeight));
            bulletSound.play();
        }

        // Aktualizacja i czyszczenie pocisków
        for (int i = bullets.size - 1; i >= 0; i--) {
            PlayerBullet bullet = bullets.get(i);
            bullet.update(delta, viewport);
            if (!bullet.isAlive()) {
                bullets.removeIndex(i);
            }
        }
    }

    /**
     * Rysuje gracza i jego pociski.
     *
     * @param batch SpriteBatch używany do rysowania
     */
    public void render(SpriteBatch batch) {
        sprite.draw(batch);
        for (PlayerBullet bullet : bullets) {
            bullet.render(batch);
        }
    }
    /**
     * Rysuje pasek ładowania strzału (cooldown).
     *
     * @param batch SpriteBatch używany do rysowania
     */
    public void renderShootCooldownBar(SpriteBatch batch){
        float barWidth = 2f;//szerokosc
        float barHeight = 0.15f;//wysokosc
        float barX = 0.5f;//polozenie x
        float barY = 0.3f;//polozenie y

        float progress = Math.min(shootTimer / shootDelay, 1f);
        float fillWidth = Math.min(progress * barWidth, barWidth);

        // Rysowanie paska
        batch.draw(barFillTexture, barX, barY, fillWidth, barHeight);
    }
    /**
     * Rysuje pasek życia gracza.
     *
     * @param batch SpriteBatch używany do rysowania
     */
    public void renderPlayerHPBar(SpriteBatch batch){
        float barWidth = 2f;//szerokosc
        float barHeight = 0.15f;//wysokosc
        float barX = 13.5f;//polozenie x
        float barY = 0.3f;//polozenie y
        float progress = Math.max(0, Math.min(PlayerHP / PlayerMaxHP, 1f));
        float fillWidth = Math.min(progress * barWidth, barWidth);
        batch.draw(HPBarTexture, barX, barY, fillWidth, barHeight);
    }
    /**
     * Zwraca prostokąt kolizji gracza.
     *
     * @return Rectangle - prostokąt kolizji
     */
    public Rectangle getBounds() {
        return bounds;
    }

    /**
     * Niszczy zasoby gracza (tekstury i pociski).
     */
    public void dispose() {
        texture.dispose();
        for (PlayerBullet bullet : bullets) {
            bullet.destroy();
        }
    }
    /**
     * Sprawdza kolizje pocisków gracza z budynkami.
     *
     * @param buildings Lista budynków
     * @param hitSound  Dźwięk odtwarzany przy trafieniu budynku
     */
    public void checkCollisionWithBuildings(Array<ShieldBuilding> buildings, Sound hitSound) {
        for (int i = bullets.size - 1; i >= 0; i--) {
            for (ShieldBuilding building : buildings) {
                // trafienie budynku
                if (!building.isDestroyed() && bullets.get(i).collides(building.getBounds())) {
                    bullets.removeIndex(i);
                    hitSound.play();
                    break;
                }
            }
        }
    }
    /**
     * Zwraca listę aktywnych (wystrzelonych i obecnych na ekranie) pocisków gracza.
     *
     * @return Tablica pocisków
     */
    public Array<PlayerBullet> getBulletsArray(){
        return bullets;
    }
    /**
     * Redukuje życie gracza o daną wartość. Jeśli HP spadnie do 0, oznacza gracza jako martwego.
     *
     * @param damage Ilość obrażeń
     */
    public void PlayerTakeHit(float damage){
        PlayerHP -= damage;
        if(PlayerHP <= 0){
            playerAlive=0;
        }
    }


    /**
     * Usuwa wszystkie pociski gracza (używane przy zmianie fali przeciwników na nową po pokonaniu wszystkich z poprzedniej).
     */
    public void clearLeftPlayerBullets(){
        bullets.clear();
    }
    //zwraca 0 gdy hp gracza <= 0
    /**
     * Sprawdza, czy gracz żyje (używane do włączania {@link GameOverScreen}
     *
     * @return 1 jeśli żyje, 0 jeśli nie
     */
    public int isPlayerAlive(){
        return playerAlive;
    }
    /**
     * Zwraca aktualne HP gracza.
     *
     * @return Punkty życia
     */
    public float getPlayerHP(){
        return PlayerHP;
    }
    /**
     * Zwraca obrażenia zadawane przez gracza.
     *
     * @return Wartość obrażeń
     */
    public float getPlayerDamage(){
        return PlayerDamage;
    }
    /**
     * Resetuje pozycję gracza do pozycji początkowej (używane przy zmianie fali przeciwników na nową po pokonaniu wszystkich z poprzedniej).
     */
    public void resetPlayerPosition(){
        sprite.setPosition(playerStartXPosition, playerStartYPosition); // centrowanie na środku
    }
    /**
     * Resetuje HP gracza do pełnego (używane przy zmianie fali przeciwników na nową po pokonaniu wszystkich z poprzedniej).
     */
    public void resetPlayerHP(){
        PlayerHP = PlayerMaxHP;//poczatkowe HP gracza
    }

    /**
     * Włącza kod oszustwa: nieskończone życie, duże obrażenia i natychmiastowe strzelanie (brak implementacji ingame - używane tylko w debugerze)
     */
    public void activeCheatCode(){
        PlayerHP = 1000;
        PlayerDamage = 1000;
        shootDelay = 0;
    }

}
