package com.badlogic.drop;

import com.badlogic.gdx.graphics.Texture;

/**
 * Klasa reprezentująca statek kosmiczny typu Meteor Lance.
 * <p>
 * Rozszerza klasę {@link Player} i ustawia statystykę statku:
 * <ul>
 *   <li>Obrażenia: 15</li>
 *   <li>Opóźnienie strzału: 1,5 s</li>
 *   <li>HP: 15</li>
 *   <li>Prędkość: 2</li>
 * </ul>
 *
 * @author Bartłomiej Handziak
 */

public class SpaceShip_MeteorLance extends Player {
    /**
     * Tworzy nową instancję statku kosmicznego Meteor Lance
     */
    public SpaceShip_MeteorLance(){
        super();

        texture = new Texture("spaceship_meteor_lance.png");
        sprite.setTexture(texture);

        PlayerDamage = 15f;
        shootDelay = 1.5f;
        PlayerHP = 15f;
        PlayerMaxHP = PlayerHP;
        speed = 2f;

        bulletWidth = 0.3f;
        bulletHeight = 0.4f;
    }
}
