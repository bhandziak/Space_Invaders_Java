package com.badlogic.drop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class TextButtonFactory {

    // defaultColor is WHITE
    public static TextButton create(
        String buttonText, int fontSize, float xPosition, float yPosition,
        float buttonWidth, float buttonHeight
    ) {
        return create(buttonText, fontSize, xPosition, yPosition, buttonWidth, buttonHeight, Color.WHITE);
    }

    public static TextButton create(
        String buttonText, int fontSize, float xPosition, float yPosition,
        float buttonWidth, float buttonHeight, Color defaultColor ) {
        // Font generation
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("font_retro_gaming.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = fontSize;
        BitmapFont buttonFont = generator.generateFont(parameter);
        generator.dispose();

        // Button style
        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();
        style.font = buttonFont;
        style.fontColor = defaultColor;
        style.overFontColor = Color.YELLOW;
        style.downFontColor = Color.GRAY;

        TextButton playButton = new TextButton(buttonText, style);

        // Size and position
        playButton.setSize(buttonWidth, buttonHeight);
        playButton.setPosition(
            xPosition,
            yPosition
        );

        return playButton;
    }
}
