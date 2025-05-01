package com.badlogic.drop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class MainMenuScreen implements Screen {
    final Main game;
    private Stage stage;
    private TextButton playButton;
    private TextButton storeButton;

    private final String playButton_Text = "PLAY";
    private final String storeButton_Text = "STORE";
    private final int textSize = 50;
    private final float buttonWidth = 250f;
    private final float buttonHeight = 80f;

    public MainMenuScreen(final Main game) {
        this.game = game;

        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        float playButtonPosX = Gdx.graphics.getWidth() / 2f - buttonWidth / 2f;
        float playButtonPosY = Gdx.graphics.getHeight() / 2f - buttonHeight / 2f;

        playButton = TextButtonFactory.create(playButton_Text, textSize, playButtonPosX, playButtonPosY, buttonWidth, buttonHeight);

        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MainGame(game));
                dispose();
            }
        });

        float storeButtonPosX = Gdx.graphics.getWidth() / 2f - buttonWidth / 2f;
        float storeButtonPosY = Gdx.graphics.getHeight() / 2f - 1.8f* buttonHeight;

        storeButton = TextButtonFactory.create(storeButton_Text, textSize, storeButtonPosX, storeButtonPosY, buttonWidth, buttonHeight);

        storeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new StoreScreen(game));
                dispose();
            }

        });

        stage.addActor(playButton);
        stage.addActor(storeButton);

        // LOGO

        float logoPosX = Gdx.graphics.getWidth() / 2f -  500f / 2f;
        float logoPosY = Gdx.graphics.getHeight() - 250f;

        Image logo = ImageFactory.create("SpaceInvadersLogo.png", logoPosX, logoPosY, 500, 250);

        stage.addActor(logo);

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(delta);
        stage.draw();
    }

    @Override public void dispose() {
        stage.dispose();
    }

    // Puste metody z interfejsu Screen
    @Override public void show() {}
    @Override public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}

}
