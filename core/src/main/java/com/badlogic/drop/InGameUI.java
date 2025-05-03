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

public class InGameUI {
    final Main game;
    private final ShapeRenderer shapeRenderer;
    private final SpriteBatch batch;
    private final Stage stage;
    private float upperUI_width = 50f;
    private final String exitButton_Text = "EXIT";
    private Label scoreAmountText;
    private Label moneyAmountText;
    //umieszczenie tekstu score
    float labelWidth = 200f; // szerokość pola tekstowego na gameUI (gorna czesc ekranu
    float scoreAmountTextPosX = Gdx.graphics.getWidth() - labelWidth - 20f; //20px margines od prawej - dla wyniku score
    float scoreTextPosX = Gdx.graphics.getWidth() - 500f; //X dla tekstu Score
    float scoreTextPosY = Gdx.graphics.getHeight() - upperUI_width - 12;//Y dla tekstu Score

    //umieszczenie money amount text
    float MoneyAmount_offset = 300f;

    public InGameUI(final Main game){
        this.game = game;
        shapeRenderer = new ShapeRenderer();
        batch = new SpriteBatch();
        stage = new Stage(new ScreenViewport());
        scoreAmountText = TextFieldFactory.create(
            "0", UsefullConstans.textSize, scoreAmountTextPosX, scoreTextPosY, Color.WHITE
        );
        moneyAmountText = TextFieldFactory.create(
            "0", UsefullConstans.textSize, MoneyAmount_offset, scoreTextPosY, Color.WHITE
        );

    }

    // rysowanie nakladki UI
    public void show() {
        stage.clear();
        //od prawej strony ekranu
        //Score napis
        Label scoreText = TextFieldFactory.create(
            "SCORE:", UsefullConstans.textSize, scoreTextPosX, scoreTextPosY, Color.WHITE
        );
        //Money napis
        Label MoneyText = TextFieldFactory.create(
            "MONEY:", UsefullConstans.textSize,  20f, scoreTextPosY, Color.WHITE
        );
        //Score - liczba
        // ustawiamy szerokość i justowanie w Label
        scoreAmountText.setSize(labelWidth, UsefullConstans.textSize + 10);
        scoreAmountText.setAlignment(Align.right);//od prawej strony ekranu align

        //Money - liczba
        // ustawiamy szerokość i justowanie w Label
        scoreAmountText.setSize(labelWidth, UsefullConstans.textSize + 10);

        stage.addActor(scoreText);
        stage.addActor(MoneyText);
        stage.addActor(scoreAmountText);
        stage.addActor(moneyAmountText);
    }
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
    public void hide() {
        stage.clear(); // czyści aktorów
    }
    public void dispose() {
        shapeRenderer.dispose();
        batch.dispose();
        stage.dispose();
    }

}
