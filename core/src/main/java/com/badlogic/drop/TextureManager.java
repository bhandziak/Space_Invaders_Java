package com.badlogic.drop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

import java.util.HashMap;
import java.util.Map;

public class TextureManager {
    private static final Map<String, Texture> textures = new HashMap<>();

    public static Texture get(String fileName) {
        if (!textures.containsKey(fileName)) {
            textures.put(fileName, new Texture(Gdx.files.internal(fileName)));
        }
        return textures.get(fileName);
    }

    public static void disposeAll() {
        for (Texture t : textures.values()) {
            t.dispose();
        }
        textures.clear();
    }
}
