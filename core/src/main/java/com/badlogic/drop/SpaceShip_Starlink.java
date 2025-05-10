package com.badlogic.drop;

import com.badlogic.gdx.graphics.Texture;

public class SpaceShip_Starlink extends Player {
    public SpaceShip_Starlink(){
        super();

        texture = new Texture("spaceship_starling.png");
        sprite.setTexture(texture);

        PlayerDamage = 6f;
        shootDelay = 0.5f;
        PlayerHP = 10f;
        PlayerMaxHP = PlayerHP;
        speed = 8f;
    }
}
