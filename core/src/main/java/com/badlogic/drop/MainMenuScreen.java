package com.badlogic.drop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class MainMenuScreen implements Screen {
    final Main game;
    private Stage stage;
    private Texture playButtonTexture, playButtonPressedTexture, playButtonHoverTexture;
    private Texture shopButtonTexture, shopButtonPressedTexture, shopButtonHoverTexture;
    private ImageButton playButton;
    private ImageButton shopButton;

    public MainMenuScreen(final Main game) {
        this.game = game;

        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        // PLAY BUTTON
        playButtonTexture = new Texture(Gdx.files.internal("play_button.png"));
        playButtonPressedTexture = new Texture(Gdx.files.internal("play_button_pressed.png"));
        playButtonHoverTexture = new Texture(Gdx.files.internal("play_button_hover.png"));

        TextureRegionDrawable up = new TextureRegionDrawable(playButtonTexture);
        TextureRegionDrawable down = new TextureRegionDrawable(playButtonPressedTexture);
        TextureRegionDrawable over = new TextureRegionDrawable(playButtonHoverTexture);

        ImageButton.ImageButtonStyle buttonStyle = new ImageButton.ImageButtonStyle();
        buttonStyle.up = up;
        buttonStyle.down = down;
        buttonStyle.over = over;
        playButton = new ImageButton(buttonStyle);

        playButton.setSize(250, 80);
        playButton.setPosition(
            Gdx.graphics.getWidth() / 2f - playButton.getWidth() / 2f,
            Gdx.graphics.getHeight() / 2f - playButton.getHeight() / 2f
        );

        // Co się stanie po kliknięciu
        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MainGame(game));
                dispose();
            }

        });

        // SHOP BUTTON

        shopButtonTexture = new Texture(Gdx.files.internal("shop_button.png"));
        shopButtonPressedTexture = new Texture(Gdx.files.internal("shop_button_pressed.png"));
        shopButtonHoverTexture = new Texture(Gdx.files.internal("shop_button_hover.png"));

        TextureRegionDrawable upS = new TextureRegionDrawable(shopButtonTexture);
        TextureRegionDrawable downS = new TextureRegionDrawable(shopButtonPressedTexture);
        TextureRegionDrawable overS = new TextureRegionDrawable(shopButtonHoverTexture);

        ImageButton.ImageButtonStyle buttonStyleS = new ImageButton.ImageButtonStyle();
        buttonStyleS.up = upS;
        buttonStyleS.down = downS;
        buttonStyleS.over = overS;

        shopButton = new ImageButton(buttonStyleS);

        shopButton.setSize(250, 80);
        shopButton.setPosition(
            Gdx.graphics.getWidth() / 2f -  shopButton.getWidth() / 2f,
            Gdx.graphics.getHeight() / 2f - 1.8f* shopButton.getHeight()
        );

        // Co się stanie po kliknięciu
        shopButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // przekieruj do ekranu Shop
                // dispose();
            }

        });

        stage.addActor(playButton);
        stage.addActor(shopButton);

        // LOGO
        Texture logoTexture = new Texture(Gdx.files.internal("SpaceInvadersLogo.png"));
        Image logo = new Image(logoTexture);

        logo.setSize(500, 250);
        logo.setPosition(
            Gdx.graphics.getWidth() / 2f -  logo.getWidth() / 2f,
            Gdx.graphics.getHeight() - logo.getHeight()
        );

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
        playButtonTexture.dispose();
        playButtonPressedTexture.dispose();
        playButtonHoverTexture.dispose();

        shopButtonTexture.dispose();
        shopButtonPressedTexture.dispose();
        shopButtonHoverTexture.dispose();
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
