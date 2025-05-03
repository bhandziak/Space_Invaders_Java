package com.badlogic.drop;

import com.badlogic.gdx.graphics.Texture;

public class SpaceShip_MeteorLance extends Player {
    public SpaceShip_MeteorLance(){
        super();

        texture = new Texture("spaceship_meteor_lance.png");
        sprite.setTexture(texture);

        PlayerDamage = 15f;
        shootDelay = 1.5f;
        PlayerHP = 60f;
        PlayerMaxHP = PlayerHP;
        speed = 2f;

        bulletWidth = 0.3f;
        bulletHeight = 0.4f;
    }
}
