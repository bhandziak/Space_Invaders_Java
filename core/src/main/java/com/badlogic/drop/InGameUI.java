package com.badlogic.drop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;


/**
 * Klasa odpowiadająca za rysowanie interfejsu użytkownika (UI) w trakcie gry.
 * Wyświetla informacje o aktualnym wyniku gracza (score) oraz zgromadzonych monetach (money) na górze ekranu gry.
 *
 *
 * @author Kacper Dziduch
 */
public class InGameUI {
    /** Referencja do głównej klasy {@link Main} umożliwia dostęp do zmiennych globalnych*/
    final Main game;
    /** Renderer kształtów - używany do rysowania tła UI */
    private final ShapeRenderer shapeRenderer;
    /** Stage (scena) do wyświetlania aktorów UI takich jak Label */
    private final Stage stage;
    /** Wysokość górnego paska UI */
    private float upperUI_width = 50f;
    /** Label pokazujący aktualny wynik gracza - score */
    private Label scoreAmountText;
    /** Label pokazujący aktualny stan pieniędzy gracza - money */
    private Label moneyAmountText;
    //umieszczenie tekstu score
    /** Szerokość pola tekstowego dla wyników */
    float labelWidth = 200f; // szerokość pola tekstowego na gameUI (gorna czesc ekranu
    /** Pozycja X dla wartości wyniku */
    float scoreAmountTextPosX = Gdx.graphics.getWidth() - labelWidth - 20f; //20px margines od prawej - dla wyniku score
    /** Pozycja X dla napisu "SCORE:" */
    float scoreTextPosX = Gdx.graphics.getWidth() - 500f; //X dla tekstu Score
    /** Pozycja Y dla wszystkich tekstów w górnym UI */
    float scoreTextPosY = Gdx.graphics.getHeight() - upperUI_width - 12;//Y dla tekstu Score

    //umieszczenie money amount text
    /** Przesunięcie X dla napisu o ilości monet */
    float MoneyAmount_offset = 300f;

    /**
     * Tworzy nową instancję UI w grze i inicjalizuje wszystkie komponenty.
     *
     * @param game Referencja do głównej klasy {@link Main} umożliwia dostęp do zmiennych globalnych
     */
    public InGameUI(final Main game){
        this.game = game;
        shapeRenderer = new ShapeRenderer();
        stage = new Stage(new ScreenViewport());
        scoreAmountText = TextFieldFactory.create(
            "0", UsefulConstans.textSize, scoreAmountTextPosX, scoreTextPosY, Color.WHITE
        );
        moneyAmountText = TextFieldFactory.create(
            "0", UsefulConstans.textSize, MoneyAmount_offset, scoreTextPosY, Color.WHITE
        );

    }

    // rysowanie nakladki UI
    /**
     * Tworzy i dodaje do sceny wszystkie elementy UI (napisy: SCORE, MONEY, ich wartości).
     * Jest wywoływana przy starcie gry czyli przejściu z {@link MainMenuScreen} prez wciśnięcie 'PLAY'
     */
    public void show() {
        stage.clear();
        //od prawej strony ekranu
        //Score napis
        Label scoreText = TextFieldFactory.create(
            "SCORE:", UsefulConstans.textSize, scoreTextPosX, scoreTextPosY, Color.WHITE
        );
        //Money napis
        Label MoneyText = TextFieldFactory.create(
            "MONEY:", UsefulConstans.textSize,  20f, scoreTextPosY, Color.WHITE
        );
        //Score - liczba
        // ustawiamy szerokość i justowanie w Label
        scoreAmountText.setSize(labelWidth, UsefulConstans.textSize + 10);
        scoreAmountText.setAlignment(Align.right);//od prawej strony ekranu align

        //Money - liczba
        // ustawiamy szerokość i justowanie w Label
        scoreAmountText.setSize(labelWidth, UsefulConstans.textSize + 10);

        stage.addActor(scoreText);
        stage.addActor(MoneyText);
        stage.addActor(scoreAmountText);
        stage.addActor(moneyAmountText);
    }
    /**
     * Renderuje górny pasek UI gry, w tym tło i aktualne wartości punktów oraz pieniędzy.
     * Metoda jest wywoływana w każdej klatce gry aby zmieniać wartości danych po każdej zmianie.
     */
    public void render() {
        // Tło górnego paska UI
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        //aktualizacja wyniku score do rysowania
        scoreAmountText.setText(game.score);
        //aktualizacja money amount do rysowania
        moneyAmountText.setText(game.money);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0, 0, 0, 1f); // Czarne tło
        shapeRenderer.rect(0, Gdx.graphics.getHeight() - upperUI_width, Gdx.graphics.getWidth(), upperUI_width);
        shapeRenderer.end();

        Gdx.gl.glDisable(GL20.GL_BLEND);

        // Aktualizacja i rysowanie UI
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }
    /**
     * Ukrywa UI, czyszcząc wszystkich aktorów ze sceny.
     * Używana przy zmianie ekranu na np. {@link MainMenuScreen}
     */
    public void hide() {
        stage.clear(); // czyści aktorów
    }
    /**
     * Zwalnia zasoby powiązane z UI, w tym ShapeRenderer, Stage i inne.
     */
    public void dispose() {
        shapeRenderer.dispose();
        stage.dispose();
        TextureManager.disposeAll();
        FontManager.disposeAll();
    }

}
