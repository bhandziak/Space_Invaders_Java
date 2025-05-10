package com.badlogic.drop;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

public class ShieldBuilding {
    private Sprite spriteBuilding;
    private int health;
    private int maxHealth;
    private boolean destroyed = false;

    protected Array<ShieldBuilding> buildings;
    //tekstury dla paska HP wrogow
    Texture barFillTexture = new Texture("progressBar_green.png");
    private final int buildingImageSize = 603;

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

    public void render(SpriteBatch batch) {
        if (!destroyed) {
            spriteBuilding.draw(batch);
        }
    }

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

    public float getBuildingHP(){
        return health;
    }
    public float getBuildingMaxHP(){
        return maxHealth;
    }
    public Sprite getBuildingSprite(){
        return spriteBuilding;
    }

    public Rectangle getBounds() {
        return spriteBuilding.getBoundingRectangle();
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
        spriteBuilding.setRegion(0,0,buildingImageSize,buildingImageSize);
    }
    public void resetAllBuildingsState(Array<ShieldBuilding> bunkers){
        for (ShieldBuilding bunker : bunkers){
            bunker.destroyed=false;
            bunker.resetHPBuilding(bunker);
            bunker.spriteBuilding.setRegion(0,0,buildingImageSize,buildingImageSize);
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

