package com.badlogic.drop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;

public class TextFieldFactory {

    public static Label create(
        String text, int fontSize,
        float xPosition, float yPosition,
        Color color
    ) {
        // Font generation
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("font_retro_gaming.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = fontSize;
        BitmapFont font = generator.generateFont(parameter);
        generator.dispose();

        // Label style
        LabelStyle style = new LabelStyle();
        style.font = font;
        style.fontColor = color;

        // Create label
        Label label = new Label(text, style);

        // Position
        label.setPosition(xPosition, yPosition);

        return label;
    }
}
