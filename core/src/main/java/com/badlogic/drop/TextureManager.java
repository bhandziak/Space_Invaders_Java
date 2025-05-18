package com.badlogic.drop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

import java.util.HashMap;
import java.util.Map;

/**
 * Klasa zarządzająca teksturami.
 * Przechowuje czcionkę jako instancję obiektu {@link Texture}.
 * Jest po to, aby uniknąć wyciek pamięci.
 * <p>
 *     Po zakończeniu zadziałania aplikacji lub
 *     zmienienia ekranu należy zwolnić załadowaną teksturę.
 * </p>
 *
 * @author Bartłomiej Handziak
 */
public class TextureManager {
    /**
     * Mapa przechowująca tekstury według nazwy pliku.
     */
    private static final Map<String, Texture> textures = new HashMap<>();

    /**
     * Zwraca teksturę po nazwie pliku.
     * @param fileName nazwa pliku
     * @return instancja {@link Texture}
     */
    public static Texture get(String fileName) {
        if (!textures.containsKey(fileName)) {
            textures.put(fileName, new Texture(Gdx.files.internal(fileName)));
        }
        return textures.get(fileName);
    }
    /**
     * Zwalnia wszystkie załadowane tekstury.
     */
    public static void disposeAll() {
        for (Texture t : textures.values()) {
            t.dispose();
        }
        textures.clear();
    }
}
