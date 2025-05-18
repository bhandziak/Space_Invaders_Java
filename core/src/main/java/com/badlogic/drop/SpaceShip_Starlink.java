package com.badlogic.drop;

import com.badlogic.gdx.graphics.Texture;

/**
 * Klasa reprezentująca statek kosmiczny typu Starlink.
 * <p>
 * Rozszerza klasę {@link Player} i ustawia statystykę statku:
 * <ul>
 *   <li>Obrażenia: 6</li>
 *   <li>Opóźnienie strzału: 0,5 s</li>
 *   <li>HP: 10</li>
 *   <li>Prędkość: 8</li>
 * </ul>
 *
 * @author Bartłomiej Handziak
 */

public class SpaceShip_Starlink extends Player {
    /**
     * Tworzy nową instancję statku kosmicznego Starlink
     */
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
