package com.badlogic.drop;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

public class ShieldBuilding {
    private Sprite sprite;
    private int health;
    private int maxHealth;
    private boolean destroyed = false;

    protected Array<ShieldBuilding> buildings;
    //tekstury dla paska HP wrogow
    Texture barFillTexture = new Texture("progressBar_green.png");

    public ShieldBuilding(float x, float y) {
        Texture texture = new Texture("progressBar_green.png");//TODO tekstura do zmiany
        sprite = new Sprite(texture);
        sprite.setPosition(x, y);
        sprite.setSize(1, 1); //rozmiar
        health = 5; //hp budynku (l. trafien)
        maxHealth = health;
        buildings = new Array<>();
    }

    public void render(SpriteBatch batch) {
        if (!destroyed) {
            sprite.draw(batch);
        }
    }

    public void update() {
        if (health <= 0) {
            destroyed = true;
        }
    }

    public float getBuildingHP(){
        return health;
    }
    public float getBuildingMaxHP(){
        return maxHealth;
    }
    public Sprite getBuildingSprite(){
        return sprite;
    }

    public Rectangle getBounds() {
        return sprite.getBoundingRectangle();
    }

    public void takeHit() {
        if (!destroyed) {
            health--;
            update();
        }
    }

    public boolean isDestroyed() {
        return destroyed;
    }
    private void resetHPBuilding(ShieldBuilding bunker){
        bunker.health = maxHealth;
    }
    public void resetAllBuildingsState(Array<ShieldBuilding> bunkers){
        for (ShieldBuilding bunker : bunkers){
            bunker.destroyed=false;
            bunker.resetHPBuilding(bunker);
        }
    }

    //paski hp budynkow
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

