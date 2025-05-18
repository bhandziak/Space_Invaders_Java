package com.badlogic.drop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
/**
 * Klasa pomocnicza (fabryka) umożliwiająca łatwe tworzenie obiektów {@link TextButton}
 * z ustaloną pozycją i rozmiarem.
 *
 * <p>Przykład użycia:
 * <pre>{@code
 * storeButton = TextButtonFactory.create(
 *     storeButton_Text, UsefulConstans.textSize, storeButtonPosX, storeButtonPosY, UsefulConstans.buttonWidth, UsefulConstans.buttonHeight
 * );
 * stage.addActor(storeButton);
 * }</pre>
 *
 * @author Bartłomiej Handziak
 */

public class TextButtonFactory {

    /**
     * Tworzy przycisk z domyślnym białym kolorem tekstu.
     */
    public static TextButton create(
        String buttonText, int fontSize, float xPosition, float yPosition,
        float buttonWidth, float buttonHeight
    ) {
        return create(buttonText, fontSize, xPosition, yPosition, buttonWidth, buttonHeight, Color.WHITE);
    }
    /**
     * Tworzy przycisk z określonym kolorami.
     *
     * @param buttonText tekst przycisku
     * @param fontSize rozmiar czcionki
     * @param xPosition pozycja X
     * @param yPosition pozycja Y
     * @param buttonWidth szerokość
     * @param buttonHeight wysokość
     * @param defaultColor kolor tekstu
     * @return przycisk TextButton
     */
    public static TextButton create(
        String buttonText, int fontSize, float xPosition, float yPosition,
        float buttonWidth, float buttonHeight, Color defaultColor ) {
        // Font generation
        BitmapFont buttonFont = FontManager.get(fontSize);

        // Button style
        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();
        style.font = buttonFont;

        style.fontColor = defaultColor;
        if(defaultColor == Color.BLUE){
            style.overFontColor = defaultColor;
            style.downFontColor = defaultColor;
        }else{
            style.overFontColor = Color.YELLOW;
            style.downFontColor = Color.GRAY;
        }

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
