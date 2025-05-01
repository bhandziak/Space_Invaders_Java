package com.badlogic.drop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class ImageFactory {
    public static Image create(
        String fileName, float xPosition, float yPosition,
        float width, float height){

        Texture texture = new Texture(Gdx.files.internal(fileName));
        Image image = new Image(texture);

        image.setSize(width, height);
        image.setPosition(
            xPosition,
            yPosition
        );
        return image;
    }
}
