package com.badlogic.drop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

import java.util.HashMap;
import java.util.Map;

public class FontManager {
    private static final Map<Integer, BitmapFont> fonts = new HashMap<>();

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

    public static void disposeAll() {
        for (BitmapFont font : fonts.values()) {
            font.dispose();
        }
        fonts.clear();
    }
}
