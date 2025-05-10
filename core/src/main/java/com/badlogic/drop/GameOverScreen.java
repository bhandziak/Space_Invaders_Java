package com.badlogic.drop;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class GameOverScreen {
    final Main game;
    private final ShapeRenderer shapeRenderer;
    private final SpriteBatch batch;
    private final Stage stage;

    private Label pauseText;
    private Label scoreText;
    private TextButton exitButton;

    private final String exitButton_Text = "EXIT";
    private Sound gameOverSound;
    private Sound clickSound;

    GameOverScreen(final Main game){
        this.game = game;
        shapeRenderer = new ShapeRenderer();
        batch = new SpriteBatch();
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        gameOverSound = Gdx.audio.newSound(Gdx.files.internal("gameOverSound.wav"));
        clickSound = Gdx.audio.newSound(Gdx.files.internal("selectSound.wav"));

        // tekst i przycisk
        float pauseTextPosX = Gdx.graphics.getWidth() / 2f - 170f;
        float pauseTextPosY = Gdx.graphics.getHeight() / 2f + 2* UsefulConstans.textSize;

        pauseText = TextFieldFactory.create(
            "GAME OVER", UsefulConstans.textSize, pauseTextPosX, pauseTextPosY, Color.GREEN
        );

        stage.addActor(pauseText);

        // pobranie szerokości czcionki
        BitmapFont font = TextFieldFactory.getFont(UsefulConstans.textSize);
        GlyphLayout layout = new GlyphLayout(font, "score: " + game.score);

        float scoreTextPosX = (Gdx.graphics.getWidth() - layout.width) / 2f - 50f;
        float scoreTextPosY = Gdx.graphics.getHeight() / 2f + UsefulConstans.textSize - 20f;
        scoreText = TextFieldFactory.create(
            "score: ".concat(String.valueOf(game.score)), UsefulConstans.textSize, scoreTextPosX, scoreTextPosY, Color.WHITE
        );
        stage.addActor(scoreText);

        float exitButtonPosX = Gdx.graphics.getWidth() / 2f - UsefulConstans.buttonWidth / 2f;
        float exitButtonPosY = Gdx.graphics.getHeight() / 2f - UsefulConstans.buttonHeight;

        exitButton = TextButtonFactory.create(exitButton_Text, UsefulConstans.textSize, exitButtonPosX , exitButtonPosY, UsefulConstans.buttonWidth, UsefulConstans.buttonHeight);

        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                clickSound.play();
                game.selectedSpaceShip = null;
                game.updateHighscore();//aktualizacja highscore
                //zapisanie danych gry przed wyłączeniem
                game.saveGame();
                game.setScreen(new MainMenuScreen(game));
            }
        });

        stage.addActor(exitButton);
    }

    // renderowanie nakładki game over
    public void render() {
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0, 0, 0, 0.5f); // tło
        shapeRenderer.rect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        shapeRenderer.end();

        Gdx.gl.glDisable(GL20.GL_BLEND);

        scoreText.setText("score: ".concat(String.valueOf(game.score)));

        // Renderowanie wszystkiego na stage
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f)); // Aktualizacja stage
        stage.draw(); // Rysowanie elementów na stage
    }

    public void playGameOverSound(){
        gameOverSound.play();
    }

    public void dispose() {
        shapeRenderer.dispose();
        batch.dispose();
        stage.dispose();
        TextureManager.disposeAll();
        FontManager.disposeAll();
        gameOverSound.dispose();
        clickSound.dispose();
    }
}
