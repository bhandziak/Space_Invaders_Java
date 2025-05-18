package com.badlogic.drop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

/**
 * Klasa pomocnicza (fabryka) umożliwiająca łatwe tworzenie obiektów {@link Image}
 * z ustaloną pozycją i rozmiarem.
 *
 * <p>Przykład użycia:
 * <pre>{@code
 * Image spaceship_starling = ImageFactory.create(
 *    spaceship_starling_filename, imagePosX, 500f, spaceshipSize, spaceshipSize
 * );
 * stage.addActor(spaceship_starling);
 * }</pre>
 *
 * @author Bartłomiej Handziak
 */

public class ImageFactory {
    /**
     * Tworzy nowy obiekt {@link Image} z teksturą załadowaną z pliku,
     * ustawiając jego pozycję i rozmiar.
     *
     * @param fileName   nazwa pliku tekstury (musi znajdować się /assets)
     * @param xPosition  pozycja X obrazka
     * @param yPosition  pozycja Y obrazka
     * @param width      szerokość obrazka
     * @param height     wysokość obrazka
     * @return gotowy obiekt {@code Image} do dodania na scenę
     */
    public static Image create(
        String fileName, float xPosition, float yPosition,
        float width, float height){

        Texture texture = TextureManager.get(fileName);
        Image image = new Image(texture);

        image.setSize(width, height);
        image.setPosition(
            xPosition,
            yPosition
        );
        return image;
    }
}
