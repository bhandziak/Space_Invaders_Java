package com.badlogic.drop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import org.w3c.dom.Text;

public class StoreScreen implements Screen {
    final Main game;
    private Stage stage;

    private TextButton goBackButton;
    private final String goBackButton_Text = "GO BACK";

    private Label storeTitle;
    private final String storeTitle_Text = "Welcome to store!";

    private final int textSize = 50;
    private final int textSize2 = 30;
    private final float buttonWidth = 250f;
    private final float buttonHeight = 80f;

    public StoreScreen(final Main game){
        this.game = game;

        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        // GO BACK button

        float goBackButtonPosX = Gdx.graphics.getWidth() - buttonWidth - 20f;
        float goBackButtonPosY = 20f;

        goBackButton = TextButtonFactory.create(goBackButton_Text, textSize, goBackButtonPosX, goBackButtonPosY, buttonWidth, buttonHeight);

        goBackButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MainMenuScreen(game));
                dispose();
            }
        });

        stage.addActor(goBackButton);

        Label staticText = TextFieldFactory.create(
            storeTitle_Text, textSize2, 10, Gdx.graphics.getHeight() - textSize2 - 10, Color.GREEN
        );
        stage.addActor(staticText);

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

    @Override public void show() {}
    @Override public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
}
