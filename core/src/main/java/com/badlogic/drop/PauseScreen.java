package com.badlogic.drop;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class PauseScreen {
    final Main game;
    private final ShapeRenderer shapeRenderer;
    private final SpriteBatch batch;
    private final Stage stage;

    private final String exitButton_Text = "EXIT";

    public PauseScreen(final Main game){
        this.game = game;
        shapeRenderer = new ShapeRenderer();
        batch = new SpriteBatch();
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage); // Ustawiamy stage jako procesor wejścia
    }

    // renderowanie nakładki pauzy
    public void render() {
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0, 0, 0, 0.5f); // Półprzeźroczysty czarny
        shapeRenderer.rect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        shapeRenderer.end();

        Gdx.gl.glDisable(GL20.GL_BLEND);

        // tekst i przycisk
        float pauseTextPosX = Gdx.graphics.getWidth() / 2f - 250f;
        float pauseTextPosY = Gdx.graphics.getHeight() / 2f + 2* UsefullConstans.textSize;

        Label pauseText = TextFieldFactory.create(
            "GAME IS PAUSED", UsefullConstans.textSize, pauseTextPosX, pauseTextPosY, Color.GREEN
        );

        stage.addActor(pauseText);

        float exitButtonPosX = Gdx.graphics.getWidth() / 2f - UsefullConstans.buttonWidth / 2f;
        float exitButtonPosY = Gdx.graphics.getHeight() / 2f - UsefullConstans.buttonHeight;

        TextButton exitButton = TextButtonFactory.create(exitButton_Text, UsefullConstans.textSize, exitButtonPosX , exitButtonPosY, UsefullConstans.buttonWidth, UsefullConstans.buttonHeight);

        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MainMenuScreen(game));
                dispose();
            }
        });

        stage.addActor(exitButton);

        // Renderowanie wszystkiego na stage
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f)); // Aktualizacja stage
        stage.draw(); // Rysowanie elementów na stage
    }

    public void dispose() {
        shapeRenderer.dispose();
        batch.dispose();
        stage.dispose();
    }
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }
}
