package com.badlogic.drop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

/**
 * Klasa pomocnicza (fabryka) umożliwiająca łatwe tworzenie obiektów {@link Label}
 * z ustaloną pozycją i rozmiarem. Umożliwia wyświetlenie podanego tekstu jako
 * element UI.
 *
 * <p>Przykład użycia:
 * <pre>{@code
 * pauseText = TextFieldFactory.create(
 *    "GAME OVER", UsefulConstans.textSize, pauseTextPosX, pauseTextPosY, Color.GREEN
 * );
 * stage.addActor(pauseText);
 * }</pre>
 *
 * @author Bartłomiej Handziak
 */
public class TextFieldFactory {
    /**
     * Tworzy nową etykietę (Label).
     *
     * @param text       tekst do wyświetlenia
     * @param fontSize   rozmiar czcionki (punktowy)
     * @param xPosition  pozycja X etykiety
     * @param yPosition  pozycja Y etykiety
     * @param color      kolor tekstu
     * @return etykieta/tekst Label
     */
    public static Label create(
        String text, int fontSize,
        float xPosition, float yPosition,
        Color color
    ) {
        BitmapFont font = FontManager.get(fontSize);

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
    /**
     * Pobiera czcionkę z {@link FontManager}
     *
     * @param fontSize rozmiar czcionki
     * @return instancja czcionki
     */
    public static BitmapFont getFont(int fontSize) {
        return FontManager.get(fontSize);
    }
}
