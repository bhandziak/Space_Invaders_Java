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
 * Ekran sklepu gry, który pozwala graczowi przeglądać i kupować statki kosmiczne.
 * <p>
 * Ekran umożliwia graczowi:
 * <ul>
 *     <li>Wybranie jednego z trzech statków (Starling, Twin Fang, Meteor Lance).</li>
 *     <li>Kupienie statku, jeśli nie został jeszcze zakupiony i gracz ma wystarczająco pieniędzy.</li>
 *     <li>Wybranie już kupionego statku.</li>
 * </ul>
 * Ekran zawiera również przycisk "GO BACK", który zapisuje grę i przenosi gracza do menu głównego.
 *
 * @see MainMenuScreen
 * @see Player
 * @see SpaceShip_Starlink
 * @see SpaceShip_TwinFang
 * @see SpaceShip_MeteorLance
 *
 * @author Bartłomiej Handziak
 */

public class StoreScreen implements Screen {
    /** Referencja do głównej klasy {@link Main} umożliwia dostęp do zmiennych globalnych*/
    final Main game;
    /** Scena stage do obsługi elementów UI */
    private Stage stage;

    private final String spaceship_starling_filename = "spaceship_starling.png";
    private final String spaceship_twin_fang_filename = "spaceship_twin_fang.png";
    private final String spaceship_meteor_lance_filename = "spaceship_meteor_lance.png";

    private final String spaceship_starling_name = "Starling";
    private final String spaceship_twin_fang_name = "Twin Fang";
    private final String spaceship_meteor_lance_name = "Meteor Lance";

    private final String spaceship_starling_desc = "A lightweight, high-speed fighter\nideal for fast evasive maneuvers\nand quick strikes.";
    private final String spaceship_twin_fang_desc = "Balanced and efficient,\nits twin blasters offer steady firepower\nin intense battles.";
    private final String spaceship_meteor_lance_desc = "Heavily armored and slow,\nlaunches a single powerful blast to\nbreak through enemy lines.";

    private final String goBackButton_Text = "GO BACK";
    private final String storeTitle_Text = "Welcome to the store!";

    /** Dźwięk kliknięcia w przycisk */
    private Sound clickSound;
    /** Dźwięk kliknięcia w zablokowany przycisk */
    private Sound blockedClickSound;

    /** Tablica cen statków. */
    private final int[] spaceships_price = {
      0, 150, 300
    };

    /**
     * Tworzy nową instancję ekranu Store.
     *
     * @param game instancja głównej klasy gry
     */
    public StoreScreen(final Main game){
        this.game = game;

        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        clickSound = Gdx.audio.newSound(Gdx.files.internal("selectSound.wav"));
        blockedClickSound = Gdx.audio.newSound(Gdx.files.internal("blockedSelectSound.wav"));

        loadContent();
    }

    /**
     * Rysuje przyciski, obrazki statków, opisy statków.
     */
    private void loadContent(){
        stage.clear();

        // GO BACK button

        float goBackButtonPosX = Gdx.graphics.getWidth() - UsefulConstans.buttonWidth - 20f;
        float goBackButtonPosY = 20f;

        TextButton goBackButton = TextButtonFactory.create(goBackButton_Text, UsefulConstans.textSize, goBackButtonPosX, goBackButtonPosY, UsefulConstans.buttonWidth, UsefulConstans.buttonHeight);

        goBackButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                clickSound.play();
                game.updateHighscore();//aktualizacja highscore
                //zapisanie danych gry przed wyłączeniem
                game.saveGame();
                game.setScreen(new MainMenuScreen(game));
            }
        });

        stage.addActor(goBackButton);

        Label welcomeText = TextFieldFactory.create(
            storeTitle_Text, UsefulConstans.textSize2, 10f, Gdx.graphics.getHeight() - UsefulConstans.textSize2 - 10f, Color.GREEN
        );
        stage.addActor(welcomeText);

        Label moneyText = TextFieldFactory.create(
            "MONEY : ".concat(String.valueOf(game.money)), UsefulConstans.textSize, Gdx.graphics.getWidth() - 450f, Gdx.graphics.getHeight() - UsefulConstans.textSize - 10f, Color.GREEN
        );
        stage.addActor(moneyText);

        // SPACESHIPS
        float imagePosX = 200f;
        float spaceshipSize = 100f;
        float namePosX = imagePosX + spaceshipSize + 50f;
        float pricePosX = namePosX + 330f;


        Image spaceship_starling = ImageFactory.create(
            spaceship_starling_filename, imagePosX, 500f, spaceshipSize, spaceshipSize
        );
        stage.addActor(spaceship_starling);

        Label spaceship_starling_text = TextFieldFactory.create(
            spaceship_starling_name, UsefulConstans.textSize2, namePosX, 525f, Color.GREEN
        );
        stage.addActor(spaceship_starling_text);
        Label spaceship_starling_text2 = TextFieldFactory.create(
            spaceship_starling_desc , UsefulConstans.textSize3, namePosX, 465f, Color.WHITE
        );
        stage.addActor(spaceship_starling_text2);

        Image spaceship_twin_fang = ImageFactory.create(
            spaceship_twin_fang_filename, imagePosX, 350f, spaceshipSize, spaceshipSize
        );
        stage.addActor(spaceship_twin_fang);

        Label spaceship_twin_fang_text = TextFieldFactory.create(
            spaceship_twin_fang_name, UsefulConstans.textSize2, namePosX, 375f, Color.BLUE
        );
        stage.addActor(spaceship_twin_fang_text);
        Label spaceship_twin_fang_text2 = TextFieldFactory.create(
            spaceship_twin_fang_desc, UsefulConstans.textSize3, namePosX, 315f, Color.WHITE
        );
        stage.addActor(spaceship_twin_fang_text2);

        Image spaceship_meteor_lance = ImageFactory.create(
            spaceship_meteor_lance_filename, imagePosX, 200f, spaceshipSize, spaceshipSize
        );
        stage.addActor(spaceship_meteor_lance);

        Label spaceship_meteor_lance_text = TextFieldFactory.create(
            spaceship_meteor_lance_name, UsefulConstans.textSize2, namePosX, 225f, Color.RED
        );
        stage.addActor(spaceship_meteor_lance_text);
        Label spaceship_meteor_lance_text2 = TextFieldFactory.create(
            spaceship_meteor_lance_desc, UsefulConstans.textSize3, namePosX, 165f, Color.WHITE
        );
        stage.addActor(spaceship_meteor_lance_text2);

        float buyPosX = pricePosX + 230f;
        float selectPosX = buyPosX + 150f;
        float pricePosY = 525f;

        for(int i =0; i < 3; i++){
            int finalI = i;
            Label price_text = TextFieldFactory.create(
                "price: ".concat(String.valueOf(spaceships_price[i])), UsefulConstans.textSize2, pricePosX, pricePosY, Color.YELLOW
            );

            stage.addActor(price_text);


            if(!game.bought_spaceship[i]){
                TextButton buy_button = TextButtonFactory.create(
                    "BUY", UsefulConstans.textSize2, buyPosX, pricePosY, 100f, 40f
                );
                stage.addActor(buy_button);

                buy_button.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        // buy spaceship
                        if(spaceships_price[finalI] <= game.money){
                            clickSound.play();
                            System.out.println("Buy spaceship nr. ".concat(String.valueOf(finalI)));
                            game.bought_spaceship[finalI] = true;
                            game.money -= spaceships_price[finalI];
                            loadContent();
                        }else{
                            blockedClickSound.play();
                            System.out.println("Not enough money!");
                        }

                    }
                });
            }

            TextButton select_button = TextButtonFactory.create(
                "SELECT", UsefulConstans.textSize2, selectPosX, pricePosY, 100f, 40f,
                game.selectedSpaceShipId == finalI ? Color.BLUE : Color.WHITE
            );
            stage.addActor(select_button);
            select_button.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    // buy spaceship
                    System.out.println("Select spaceship nr. ".concat(String.valueOf(finalI)));

                    if(game.bought_spaceship[finalI]){
                        clickSound.play();
                        switch (finalI) {
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
                        game.selectedSpaceShipId = finalI;
                    }else{
                        blockedClickSound.play();
                        System.out.println("Statek musi byc najpierw kupiony!");
                    }


                    loadContent();
                }
            });

            pricePosY -= 150f;
        }
    }

    /**
     *  Renderowanie ekranu Store
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
        blockedClickSound.dispose();
    }

    /**
     * Zezwala na eventy od myszki
     */
    @Override public void show() {
        Gdx.input.setInputProcessor(stage);
    }
    @Override public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
}
