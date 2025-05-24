package com.badlogic.drop;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;


/**
 * Reprezentuje budynek obronny (osłonę) w grze.
 * Obsługuje jego stany (zmiana tekstury przy trafieniach przeciwników), rysowanie, kolizje, resetowanie (przy nowych falach),
 * oraz renderowanie paska hp budynku.
 *
 * @author Kacper Dziduch
 */
public class ShieldBuilding {

    /** Sprite budynku */
    private Sprite spriteBuilding;
    /** Aktualne zdrowie budynku */
    private int health;
    /** Maksymalne zdrowie budynku */
    private int maxHealth;
    /** Czy budynek został zniszczony */
    private boolean destroyed = false;
    /** Lista budynków — używana np. do resetowania stanu */
    protected Array<ShieldBuilding> buildings;
    //tekstury dla paska HP wrogow
    /** Tekstura paska HP budynku */
    Texture barFillTexture = new Texture("progressBar_green.png");
    /** Obrazek budynku w podziale na różne stany zniszczenia */
    private final int buildingImageSize = 603;


    /**
     * Tworzy nowy budynek ochronny na zadanej pozycji, z parametrami:
     * Liczba trafień: 5
     *
     * @param x Pozycja X budynku
     * @param y Pozycja Y budynku
     */
    public ShieldBuilding(float x, float y) {
        Texture texture = new Texture("shieldBuilding.png");
        spriteBuilding = new Sprite(texture);
        spriteBuilding.setPosition(x, y);
        spriteBuilding.setSize(1, 1); //rozmiar
        spriteBuilding.setRegion(0,0,buildingImageSize,buildingImageSize);
        health = 5; //hp budynku (l. trafien)
        maxHealth = health;
        buildings = new Array<>();
    }
    /**
     * Rysuje budynek, jeśli nie został zniszczony.
     *
     * @param batch Obiekt SpriteBatch używany do renderowania
     */
    public void render(SpriteBatch batch) {
        if (!destroyed) {
            spriteBuilding.draw(batch);
        }
    }

    /**
     * Aktualizuje wygląd budynku w zależności od jego poziomu zdrowia.
     * Zmienia region sprite’a na uszkodzony, jeśli HP spada.
     */
    public void update() {
        if (health <= 0) {
            destroyed = true;
        }else if(health < 5 && health >= 4){
        } else if (health < 4 && health >= 3) {
            spriteBuilding.setRegion(buildingImageSize, 0 , buildingImageSize,buildingImageSize);
        }else{
            spriteBuilding.setRegion(2*buildingImageSize, 0 , buildingImageSize,buildingImageSize);
        }
    }
    /**
     * Zwraca aktualne HP budynku.
     *
     * @return Wartość zdrowia
     */
    public float getBuildingHP(){
        return health;
    }
    /**
     * Zwraca maksymalne HP budynku.
     *
     * @return Maksymalna wartość zdrowia
     */
    public float getBuildingMaxHP(){
        return maxHealth;
    }
    /**
     * Zwraca sprite budynku.
     *
     * @return Obiekt Sprite
     */
    public Sprite getBuildingSprite(){
        return spriteBuilding;
    }

    /**
     * Zwraca prostokąt kolizji budynku.
     *
     * @return Prostokąt kolizji
     */
    public Rectangle getBounds() {
        return spriteBuilding.getBoundingRectangle();
    }

    /**
     * Zmniejsza zdrowie budynku o 1 i aktualizuje jego wygląd.
     */
    public void takeHit() {
        if (!destroyed) {
            health--;
            update();
        }
    }

    /**
     * Zwraca, czy budynek został zniszczony.
     *
     * @return true, jeśli budynek nie ma już zdrowia
     */
    public boolean isDestroyed() {
        return destroyed;
    }
    /**
     * Resetuje HP i wygląd pojedynczego budynku na początkowy (używane przy zmianie fali wrogów).
     *
     * @param bunker Budynek do zresetowania
     */
    private void resetHPBuilding(ShieldBuilding bunker){

        bunker.health = maxHealth;
        spriteBuilding.setRegion(0,0,buildingImageSize,buildingImageSize);
    }
    /**
     * Resetuje stan wszystkich budynków w podanej tablicy (używane przy zmianie fali wrogów).
     *
     * @param bunkers Tablica budynków do zresetowania
     */
    public void resetAllBuildingsState(Array<ShieldBuilding> bunkers){
        for (ShieldBuilding bunker : bunkers){
            bunker.destroyed=false;
            bunker.resetHPBuilding(bunker);
            bunker.spriteBuilding.setRegion(0,0,buildingImageSize,buildingImageSize);
        }
    }

    //paski hp budynkow
    /**
     * Renderuje paski HP dla wszystkich budynków w podanej tablicy.
     *
     * @param batch     SpriteBatch używany do renderowania
     * @param buildings Tablica budynków
     */
    public void renderBuildingHPBar(SpriteBatch batch,Array<ShieldBuilding> buildings){
        for (ShieldBuilding building : buildings) {
            float progress = Math.max(0, Math.min(building.getBuildingHP() / building.getBuildingMaxHP(), 1f));
            float barWidth = 0.6f;//szerokosc
            float barHeight = 0.06f;//wysokosc
            float centerX = building.getBuildingSprite().getX()+ building.getBuildingSprite().getWidth() / 2f;//srodek sprite'a tekstury przeciwnika
            float barX = centerX - barWidth /2f;//polozenie x
            float barY = building.getBuildingSprite().getY() - .5f;//polozenie y
            float fillWidth = Math.min(progress * barWidth, barWidth);
            batch.draw(barFillTexture, barX, barY, fillWidth, barHeight);
        }
    }
}

