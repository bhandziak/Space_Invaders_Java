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

/**
 * Klasa zarządzająca nakładką ekranu Game Over.
 * Nakładka wyświetla:
 * <p>Napis "Game Over", </p>
 * <p>Zdobyte punkty, </p>
 * <p>Przycisk EXIT, który przekierowuje do ekranu {@link MainMenuScreen}</p>
 *
 * @author Bartłomiej Handziak
 */
public class GameOverScreen {
    /** Referencja do głównej klasy {@link Main} umożliwia dostęp do zmiennych globalnych*/
    final Main game;
    /** Renderer do rysowania półprzezroczystego tła */
    private final ShapeRenderer shapeRenderer;
    /** Scena stage do obsługi elementów UI */
    private final Stage stage;

    /** Napis "GAME OVER" */
    private Label pauseText;
    /** Napis "score: ..." */
    private Label scoreText;
    /** Przycisk EXIT, który przekierowuje do ekranu {@link MainMenuScreen}*/
    private TextButton exitButton;

    /** Tekst przycisku EXIT */
    private final String exitButton_Text = "EXIT";
    /** Dźwięk przegranej gry */
    private Sound gameOverSound;
    /** Dźwięk kliknięcia w przycisk */
    private Sound clickSound;

    /**
     * Tworzy nową instancję nakładki Game Over.
     *
     * @param game instancja głównej klasy gry
     */
    GameOverScreen(final Main game){
        this.game = game;
        shapeRenderer = new ShapeRenderer();
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
        if(game.score == 0){
            scoreTextPosX += 20;
        }

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

    /**
     * Zezwala na eventy od myszki
     */
    public void show(){
        Gdx.input.setInputProcessor(stage);
    }

    /**
     *  Renderowanie nakładki Game Over
     */
    public void render() {
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0, 0, 0, 0.5f); // tło
        shapeRenderer.rect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        shapeRenderer.end();

        Gdx.gl.glDisable(GL20.GL_BLEND);

        scoreText.setText("score: ".concat(String.valueOf(game.score)));

        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    /**
     * Odtwarza dźwięk {@code gameOverSound}
     */
    public void playGameOverSound(){
        gameOverSound.play();
    }

    /**
     * Zwalnia wszystkie czcionki, elementy UI, dźwięki
     */
    public void dispose() {
        shapeRenderer.dispose();
        stage.dispose();
        TextureManager.disposeAll();
        FontManager.disposeAll();
        gameOverSound.dispose();
        clickSound.dispose();
    }
}
