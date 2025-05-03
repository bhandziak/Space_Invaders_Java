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

    public ShieldBuilding(float x, float y) {
        Texture texture = new Texture("progressBar_green.png");//TODO tekstura do zmiany
        sprite = new Sprite(texture);
        sprite.setPosition(x, y);
        sprite.setSize(1, 1); //rozmiar
        health = 5; //hp budynku (l. trafien)
        maxHealth = health;
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
}

