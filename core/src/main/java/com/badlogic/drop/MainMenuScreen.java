package com.badlogic.drop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

/**
 * Ekran menu głównego.
 * Wyświetla logo gry Space Invaders,
 * przycisk przekierowujący do ekranu gry {@link MainGame},
 * przycisk przekierowujący do ekranu sklepu ze statkami kosmicznymi {@link StoreScreen},
 * rekordowy wynik.
 *
 * @author Bartłomiej Handziak
 */
public class MainMenuScreen implements Screen {
    /** Referencja do głównej klasy {@link Main} umożliwia dostęp do zmiennych globalnych*/
    final Main game;
    /** Scena stage do obsługi elementów UI */
    private Stage stage;
    /** Przycisk PLAY, który przekierowuje do ekranu {@link MainGame}*/
    private TextButton playButton;
    /** Przycisk STORE, który przekierowuje do ekranu {@link StoreScreen}*/
    private TextButton storeButton;

    /** Napis przycisku PLAY */
    private final String playButton_Text = "PLAY";
    /** Napis przycisku STORE */
    private final String storeButton_Text = "STORE";
    /** Dźwięk kliknięcia w przycisk */
    private Sound clickSound;

    /**
     * Tworzy nową instancję ekranu Main Menu.
     *
     * @param game instancja głównej klasy gry
     */
    public MainMenuScreen(final Main game) {
        this.game = game;

        clickSound = Gdx.audio.newSound(Gdx.files.internal("selectSound.wav"));

        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        float playButtonPosX = Gdx.graphics.getWidth() / 2f - UsefulConstans.buttonWidth / 2f;
        float playButtonPosY = Gdx.graphics.getHeight() / 2f - UsefulConstans.buttonHeight / 2f;

        playButton = TextButtonFactory.create(playButton_Text, UsefulConstans.textSize, playButtonPosX, playButtonPosY, UsefulConstans.buttonWidth, UsefulConstans.buttonHeight);

        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                clickSound.play();
                game.setScreen(new MainGame(game));
            }
        });

        float storeButtonPosX = Gdx.graphics.getWidth() / 2f - UsefulConstans.buttonWidth / 2f;
        float storeButtonPosY = Gdx.graphics.getHeight() / 2f - 1.8f* UsefulConstans.buttonHeight;

        storeButton = TextButtonFactory.create(storeButton_Text, UsefulConstans.textSize, storeButtonPosX, storeButtonPosY, UsefulConstans.buttonWidth, UsefulConstans.buttonHeight);

        storeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                clickSound.play();
                game.setScreen(new StoreScreen(game));
            }

        });

        stage.addActor(playButton);
        stage.addActor(storeButton);

        // high score info

        float highScorePosX = storeButtonPosX - 25f;
        float highScorePosY = storeButtonPosY - UsefulConstans.textSize - 10f;

        Label highScoreText = TextFieldFactory.create(
            "RECORD SCORE: ".concat(String.valueOf(game.recordScore)), UsefulConstans.textSize2, highScorePosX, highScorePosY, Color.WHITE
        );
        stage.addActor(highScoreText);

        // LOGO

        float logoPosX = Gdx.graphics.getWidth() / 2f -  500f / 2f;
        float logoPosY = Gdx.graphics.getHeight() - 250f;

        Image logo = ImageFactory.create("SpaceInvadersLogo.png", logoPosX, logoPosY, 500, 250);

        stage.addActor(logo);

        // load selected spaceship
        switch (game.selectedSpaceShipId){
            case 0:
                game.selectedSpaceShip = new SpaceShip_Starlink();
                break;
            case 1:
                game.selectedSpaceShip = new SpaceShip_TwinFang();
                break;
            case 2:
                game.selectedSpaceShip = new SpaceShip_MeteorLance();
                break;
            default:
                game.selectedSpaceShip = new Player();

        }
        // reset score
        game.score = 0;
    }

    /**
     *  Renderowanie ekranu Game Over
     */
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(delta);
        stage.draw();
    }

    /**
     * Zwalnia wszystkie czcionki, elementy UI, dźwięki
     */
    @Override public void dispose() {
        stage.dispose();
        TextureManager.disposeAll();
        FontManager.disposeAll();
        clickSound.dispose();
    }

    @Override public void show() {}
    @Override public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }
    @Override public void pause() {}
    @Override public void resume() {}
    /**
     * Usuwa obsługę eventów z myszki
     */
    @Override public void hide() {
        Gdx.input.setInputProcessor(null);
    }

}
