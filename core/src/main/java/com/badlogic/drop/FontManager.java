package com.badlogic.drop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

import java.util.HashMap;
import java.util.Map;

/**
 * Klasa zarządzająca czcionkami a dokładnie czcionką {@code font_retro_gaming.ttf}.
 * Przechowuje czcionkę jako instancję obiektu {@link BitmapFont}.
 * Jest po to, aby uniknąć wyciek pamięci.
 *
 * <p>
 *     Po zakończeniu zadziałania aplikacji lub
 *     zmienienia ekranu należy zwolnić załadowaną czcionkę.
 * </p>
 *
 * @author Bartłomiej Handziak
 */
public class FontManager {
    /**
     * Mapa przechowująca czcionki według rozmiaru.
     */
    private static final Map<Integer, BitmapFont> fonts = new HashMap<>();
    /**
     * Zwraca czcionkę o zadanym rozmiarze.
     * @param fontSize rozmiar czcionki
     * @return instancja {@link BitmapFont} o podanym rozmiarze
     */
    public static BitmapFont get(int fontSize) {
        if (!fonts.containsKey(fontSize)) {
            FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("font_retro_gaming.ttf"));
            FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
            parameter.size = fontSize;
            BitmapFont font = generator.generateFont(parameter);
            generator.dispose();
            fonts.put(fontSize, font);
        }
        return fonts.get(fontSize);
    }
    /**
     * Zwalnia wszystkie załadowane czcionki.
     */
    public static void disposeAll() {
        for (BitmapFont font : fonts.values()) {
            font.dispose();
        }
        fonts.clear();
    }
}
