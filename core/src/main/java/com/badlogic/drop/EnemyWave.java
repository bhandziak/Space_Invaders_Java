package com.badlogic.drop;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;


/**
 * Zarządza falą przeciwników w grze, w tym ich ruchem, strzelaniem, kolizjami oraz generowaniem nowych losowych fal.
 * Klasa odpowiada za tworzenie przeciwników, poruszanie ich w prawo/lewo i w dół po zetknięciu z krawędzią ekranu,
 * losowe strzelanie przeciwników, wykrywanie kolizji z graczem i budynkami, wydawanie dźwięków zależnie od rodzaju kolizji,
 * wyświetlanie pasków życia pod każdym przeciwnikiem oraz zmiany ich kolorów w zależności od pozostałej ilości,
 * oraz obsługę monet po pokonaniu wrogów (szansa na pojawienie się monety).
 *
 * @author Kacper Dziduch
 */
public class EnemyWave {
    /** Szablon przeciwnika, na podstawie którego tworzone są nowe instancje. */
    private Enemy enemyTemplate;
    /** Lista przeciwników aktualnie na polu gry. */
    private Array<Enemy> enemies;

    //odpowiada za ruch przeciwnikow
    /** Timer odpowiedzialny za opóźnienie ruchu przeciwników. */
    private float moveTimer = 0f;
    /** Interwał czasu (w sekundach) między kolejnymi ruchami przeciwników. */
    private float moveInterval = 1f; //move delay
    /** Odległość przesunięcia przeciwników na osi X podczas ruchu. */
    private float moveStep = 0.5f;
    /** Odległość przesunięcia przeciwników na osi Y podczas ruchu w dół. */
    private float moveDownStep;//ustawiane w 'EnemyWave'
    /** Flaga określająca kierunek ruchu przeciwników (true = w prawo). */
    private boolean movingRight = true;
    ////////////

    //odpowiada za strzelanie przeciwnikow
    /** Timer odpowiedzialny za opóźnienie strzelania przeciwników. */
    private float shootTimer = 0f;
    /** Interwał czasu (w sekundach) między kolejnymi seriami strzałów przeciwników. */
    private float shootInterval = 2f; //shooting delay
    /** Maksymalna liczba przeciwników mogących strzelać w jednej fazie strzału. */
    private int shootingEnemies=5; //max liczba przeciwnikow ktorzy moga strzelic podczas klatki

    //tekstury dla paska HP wrogow
    /** Tekstura paska zdrowia w wysokim stanie zdrowia (zielona). */
    Texture barHighTexture = new Texture("progressBar_green.png");
    /** Tekstura paska zdrowia w średnim stanie zdrowia (pomarańczowa). */
    Texture barMediumTexture = new Texture("progressBar_orange.png");
    /** Tekstura paska zdrowia w niskim stanie zdrowia (czerwona). */
    Texture barLowTexture = new Texture("progressBar_red.png");

    //potrzebne do zmiany Score
    /** Referencja do głównej klasy {@link Main} umożliwia dostęp do zmiennych globalnych*/
    final Main game;
    /** Lista pocisków wystrzelonych przez przeciwników. */
    public Array<EnemyBullet> enemyBullets = new Array<>();

    //Coin
    /** Szansa na drop monety po zabiciu przeciwnika (0.0 - 1.0), gdzie 1.0 to 100% szansy. */
    private float dropCoinRate = .3f;//szansa na drop monety
    /** Lista monet, które pojawiły się na ekranie. */
    Array<Coin> coins = new Array<>();

    //Sprawdzenie czy przeciwnicy przemieścili się na sam dół ekranu(zetknięcie z budynkami)
    /** Flaga sygnalizująca czy przeciwnicy dotarli do dolnej części ekranu (koniec gry). */
    public boolean enemiesReachedBottom=false;

    ////////////////////////

    /**
     * Konstruktor klasy EnemyWave.
     * @param enemyTemplate szablon przeciwnika
     * @param game referencja do głównej klasy {@link Main} umożliwia dostęp do zmiennych globalnych
     */
    public EnemyWave(Enemy enemyTemplate, final Main game) {
        this.game = game;
        this.enemyTemplate = enemyTemplate;
        this.enemies = new Array<>();
        this.moveDownStep = enemyTemplate.sprite.getHeight();//przesuniecie o wysokość przeciwnika
    }


    /**
     * Tworzy nową falę przeciwników i ustawia ich na planszy (służy do manualnego tworzenia fal wg. wzoru,
     * obecnie gra używa generowanych losowo fal)
     *
     * @param viewport widok gry, potrzebny do ustawienia pozycji na ekranie
     * @param amount liczba przeciwników w fali
     * @param row numer wiersza w którym pojawi się fala
     */
    public void spawnWave(Viewport viewport, int amount, int row) {
        float spacing = 0.25f;
        // Szerokość ekranu (do wycentrowania)
        float worldWidth = viewport.getWorldWidth();

        // Szerokość i wysokość przeciwnika (do wycentrowania)
        float width = enemyTemplate.sprite.getWidth();
        float height = enemyTemplate.sprite.getHeight();
        Texture texture = enemyTemplate.sprite.getTexture();
        float enemyHP = enemyTemplate.EnemyHP;
        float enemyBulletDamage = enemyTemplate.EnemyBulletDamage;
        float bulletSpeed = enemyTemplate.EnemyBulletSpeed;
        int scorePoints = enemyTemplate.ScorePoints;
        int enemyType = enemyTemplate.EnemyType;

        // Wysokość ekranu, aby ustawić przeciwników na odpowiedniej wysokości
        float worldHeight = viewport.getWorldHeight();
        float y = worldHeight - height - 0.7f * row;

        // Całkowita szerokość fali przeciwników
        float totalWidth = amount * width + (amount - 1) * spacing;

        // Startowa pozycja X, aby wycentrować falę na ekranie
        float startX = (worldWidth - totalWidth) / 2;

        for (int i = 0; i < amount; i++) {
            float x = startX + i * (width + spacing);
            Enemy newEnemy = new Enemy(texture, x, y, width, height,enemyHP,enemyBulletDamage,bulletSpeed,scorePoints,enemyType);
            enemies.add(newEnemy);
        }
    }
    /**
     * Dodaje dodatkowy wiersz przeciwników do istniejącej fali.
     *
     * @param viewport widok gry
     * @param amount liczba przeciwników w wierszu
     * @param row numer wiersza
     * @param enemyTemplate typ przeciwnika do dodania
     */
    public void addRow(Viewport viewport, int amount, int row, Enemy enemyTemplate) {
        float spacing = 0.25f;

        // Szerokość ekranu (do wycentrowania)
        float worldWidth = viewport.getWorldWidth();

        // Szerokość i wysokość przeciwnika (do wycentrowania)
        float width = enemyTemplate.sprite.getWidth();
        float height = enemyTemplate.sprite.getHeight();
        Texture texture = enemyTemplate.sprite.getTexture();
        float enemyHP = enemyTemplate.EnemyHP;
        float enemyBulletDamage = enemyTemplate.EnemyBulletDamage;
        float bulletSpeed = enemyTemplate.EnemyBulletSpeed;
        int scorePoints = enemyTemplate.ScorePoints;
        int enemyType = enemyTemplate.EnemyType;

        //offset od gory ekranu
        float offset = 0.6f;
        // Wysokość ekranu, aby ustawić przeciwników na odpowiedniej wysokości
        float worldHeight = viewport.getWorldHeight();
        float y = (worldHeight - height - 0.7f * row) -offset;

        // Całkowita szerokość fali przeciwników
        float totalWidth = amount * width + (amount - 1) * spacing;

        // Startowa pozycja X, aby wycentrować falę na ekranie
        float startX = (worldWidth - totalWidth) / 2;


        for (int i = 0; i < amount; i++) {
            float x = startX + i * (width + spacing);
            Enemy enemy = new Enemy(texture, x, y, width, height,enemyHP,enemyBulletDamage,bulletSpeed,scorePoints,enemyType);
            enemies.add(enemy);
        }
    }
    /**
     * Przesuwa przeciwników w poziomie i pionie w zależności od pozycji na ekranie.
     *
     * @param delta czas od ostatniej aktualizacji
     * @param viewport widok gry (do określenia granic ekranu)
     */
    public void moveEnemies(float delta, Viewport viewport) {
        moveTimer += delta;
        if (moveTimer < moveInterval) return;

        moveTimer = 0f; // reset licznika

        boolean needToMoveDown = false;

        for (Enemy enemy : enemies) {
            float x = enemy.sprite.getX();
            float width = enemy.sprite.getWidth();

            if ((movingRight && x + width + moveStep > viewport.getWorldWidth()) ||
                (!movingRight && x - moveStep < 0)) {
                needToMoveDown = true;
                break;
            }
        }

        for (Enemy enemy : enemies) {
            if (needToMoveDown) {
                enemy.sprite.translateY(-moveDownStep);
            } else {
                float dx = movingRight ? moveStep : -moveStep;
                enemy.sprite.translateX(dx);
            }
            // aktualizuj bounds
            enemy.getBounds().setPosition(enemy.sprite.getX(), enemy.sprite.getY());
        }

        if (needToMoveDown) {
            movingRight = !movingRight;
        }
    }

    /**
     * Wybiera losowych przeciwników, aby oddali strzał w czasie fazy strzału.
     * Dla najtrudniejszego typu przeciwnika (czerwonego) jest limit max 3 może strzelić podczas jednej fazy.
     * Dla pozostałych jest limit max 5 przeciwników może strzelić podczas fazy.
     *
     * @param delta czas od ostatniej aktualizacji
     */
    public void tryShootRandomEnemy(float delta) {
        shootTimer += delta;

        if (shootTimer < shootInterval || enemies.size == 0) return;

        shootTimer = 0f;

        //liczba przeciwnikow ktorzy mogą strzelic
        int amountOfShootingEnemies = MathUtils.random(1, shootingEnemies);

        //lista dostępnych indeksów przeciwników
        Array<Integer> availableIndices = new Array<>();
        for (int i = 0; i < enemies.size; i++) {
            availableIndices.add(i);
        }

        //mieszanie indexow
        availableIndices.shuffle();

        //limit strzalow dla typu przeciwnika 'red'
        int maxShooterType1 = 3; //max liczba strzalow podczas tury strzalu
        int shooterType1Count = 0;//licznik

        //wybranie pierwszych N przeciwników do strzału
        for (int i = 0; i < Math.min(amountOfShootingEnemies, availableIndices.size); i++) {
            Enemy shooter = enemies.get(availableIndices.get(i));
            float x = shooter.sprite.getX() + shooter.sprite.getWidth() / 2f - 0.05f;
            float y = shooter.sprite.getY();
            if(shooter.EnemyType==0) {

                EnemyBullet bullet = new EnemyBullet(x, y, shooter);//pocisk dostaje ilosc obrazen po podanym Enemy wybranym z listy w wave
                bullet.setDirectionAndRotate(0, -1); // w dol
                enemyBullets.add(bullet);
            }else if (shooter.EnemyType == 1) {
                if (shooterType1Count >= maxShooterType1) continue; // pominiecie nadmiarowych gdy przekroczono ograniczenie
                shooterType1Count++; //zwiększenie licznika
                // Trzy pociski w dół: lewy, środkowy, prawy
                EnemyBullet bulletLeft = new EnemyBullet(x, y, shooter);
                bulletLeft.setDirectionAndRotate(-3, -4); // lekko w lewo

                EnemyBullet bulletCenter = new EnemyBullet(x, y, shooter);
                bulletCenter.setDirectionAndRotate(0, -1); // prosto w dół

                EnemyBullet bulletRight = new EnemyBullet(x, y, shooter);
                bulletRight.setDirectionAndRotate(3, -4); // lekko w prawo

                enemyBullets.add(bulletLeft);
                enemyBullets.add(bulletCenter);
                enemyBullets.add(bulletRight);
            }
        }
    }
    //usuwanie pociskow przeciwnikow
    /**
     * Aktualizuje pozycje pocisków przeciwników oraz usuwa te, które opuściły ekran.
     *
     * @param delta czas od ostatniej aktualizacji
     * @param viewport widok gry
     */
    public void updateEnemyBullets(float delta, Viewport viewport) {
        for (int i = enemyBullets.size - 1; i >= 0; i--) {
            EnemyBullet bullet = enemyBullets.get(i);
            bullet.update(delta);

            if (!bullet.isAlive(viewport)) {
                enemyBullets.removeIndex(i);
            }
        }
    }
    /**
     * Rysuje pociski przeciwników na ekranie co klatkę.
     *
     * @param batch obiekt do rysowania (SpriteBatch)
     */
    public void renderEnemyBullets(SpriteBatch batch) {
        for (EnemyBullet bullet : enemyBullets) {
            bullet.render(batch);
        }
    }
    //wykrycie kolizji z graczem
    /**
     * Sprawdza kolizję pocisków przeciwników z graczem i odpowiednio odejmuje życie.
     *
     * @param playerBounds prostokąt reprezentujący obszar gracza
     * @param hitSound dźwięk trafienia
     * @param player instancja gracza
     */
    public void checkCollisionWithPlayer(Rectangle playerBounds, Sound hitSound, Player player) {
        for (int i = enemyBullets.size - 1; i >= 0; i--) {
            EnemyBullet bullet = enemyBullets.get(i);
            // trafienie gracza
            if (bullet.getBounds().overlaps(playerBounds)) {
                player.PlayerTakeHit(enemyBullets.get(i).getEnemyByBullet().getEnemyBulletDamage());
                //System.out.println("Gracz otrzymal "+enemyBullets.get(i).getEnemyByBullet().getEnemyBulletDamage()+" obrazen, teraz posiada "+player.getPlayerHP()+" HP");//debug note
                enemyBullets.removeIndex(i);
                hitSound.play();
            }
        }
    }
    //wykrycie kolizji z budynkami
    /**
     * Sprawdza kolizję pocisków przeciwników z budynkami osłonowymi i odejmuje hp budynku.
     *
     * @param buildings lista budynków osłonowych
     * @param hitSound dźwięk trafienia
     */
    public void checkCollisionWithBuildings(Array<ShieldBuilding> buildings, Sound hitSound) {
        for (int i = enemyBullets.size - 1; i >= 0; i--) {
            EnemyBullet bullet = enemyBullets.get(i);
            // trafienie budynku
            for (ShieldBuilding building : buildings) {
                if (!building.isDestroyed() && bullet.getBounds().overlaps(building.getBounds())) {
                    enemyBullets.removeIndex(i);
                    building.takeHit();
                    hitSound.play();
                    break;
                }
            }
        }
    }
    //wykrycie kolizji wrogow z budynkami i graczem (zakonczenie gry przez dojscie przeciwnikow na dol ekranu)
    /**
     * Sprawdza czy przeciwnicy zetknęli się z budynkami lub graczem (koniec gry - zmiana ekranu na {@link GameOverScreen}).
     *
     * @param buildings lista budynków osłonowych
     * @param player instancja gracza
     */
    public void checkEnemiesSpritesCollisionWithBuildingsAndPlayer(Array<ShieldBuilding> buildings,Player player) {
        for (int i =  enemies.size - 1; i >= 0; i--) {
            Enemy enemy = enemies.get(i);
            // zderzenie przeciwnika z budynkiem i playerem
            for (ShieldBuilding building : buildings) {
                if (!building.isDestroyed() && enemy.getBounds().overlaps(building.getBounds()) || enemy.getBounds().overlaps(player.getBounds())) {
                    enemiesReachedBottom = true;
                    break;
                }
            }
        }
    }

    /**
     * Sprawdza czy przeciwnicy dotarli do dolnej krawędzi ekranu (koniec gry - zmiana ekranu na {@link GameOverScreen}).
     *
     * @return true jeśli przeciwnicy dotarli do dołu ekranu (zetknęli się z graczem lub budynkiem)
     */
    public boolean enemiesReachedBottomScreen(){
        return enemiesReachedBottom; //true - koniec gry
    }

    //czyszczenie pociskow ktore zostaly z poprzedniej fali
    /**
     * Usuwa wszystkie pozostałe pociski przeciwników z poprzedniej fali, przy zmianie na kolejną.
     */
    private void clearLeftEnemiesBullets(){
        enemyBullets.clear();
    }

    int maxRedEnemiesAmount = 6;//max liczba przeciwnikow typu 'red' w rzedzie
    //metoda do tworzenia nowej fali po skonczeniu poprzedniej
    /**
     * Generuje nową falę przeciwników o losowej liczbie wierszy i przeciwników w wierszu.
     * Jeden wiersz to jeden typ przeciwnika.
     *
     * @param enemyTypes lista różnych typów przeciwników
     * @param viewport widok gry
     * @param player instancja gracza
     */
    public void generateNewWave(Array<Enemy>  enemyTypes, FitViewport viewport,Player player){
        int enemyRows = MathUtils.random(1, 5);
        for(int i=0;i<enemyRows;i++){
            int getEnemyType = MathUtils.random(0,enemyTypes.size-1);
            int enemiesInRow = MathUtils.random(1,12);//12-max rozmiar wiersza wrogow
            if(getEnemyType==2){//typ 'red' - ograniczenie rozmiaru rzedu fali przeciwnikow typu 'red'
                enemiesInRow = MathUtils.random(1,maxRedEnemiesAmount);//maxRedEnemiesAmount-max rozmiar wiersza wrogow
                addRow(viewport, enemiesInRow, i, enemyTypes.get(getEnemyType));
            }
            else{
                addRow(viewport, enemiesInRow, i, enemyTypes.get(getEnemyType));
            }

        }
        clearLeftEnemiesBullets();
        player.clearLeftPlayerBullets();
    }
    //metoda sprawdzania czy zabito wszystkich przeciwnikow
    /**
     * Sprawdza, czy na polu gry pozostał jeszcze jakiś przeciwnik (używane do wykrywania kiedy zmienić falę na nową).
     *
     * @return liczba przeciwników na polu gry
     */
    public int isAnyEnemyLeftOnField(){
        return enemies.size;
    }
    /**
     * Zwraca listę przeciwników aktualnie na polu gry.
     *
     * @return tablica przeciwników
     */
    public Array<Enemy> getEnemies() {
        return enemies;
    }

    /**
     * Rysuje wszystkich przeciwników na ekranie.
     *
     * @param batch obiekt do rysowania
     */
    public void render(SpriteBatch batch) {
        for (Enemy enemy : enemies) {
            enemy.sprite.draw(batch);
        }
    }


    // progi zmiany w procentach np. 0.5f to 50%
    private float mediumThreshold = 0.5f;
    private float lowThreshold = 0.2f;

    /**
     * Rysuje pasek zdrowia przeciwników pod ich sprite'ami.
     * Kolor paska zależy od aktualnego życia przeciwnika.
     *
     * @param batch obiekt do rysowania
     */
    public void renderEnemyHPBar(SpriteBatch batch){
        for (Enemy enemy : enemies) {
            float hp = enemy.getEnemyHP();
            float maxHp = enemy.getEnemyMaxHP();
            float progress = Math.max(0, Math.min(hp / maxHp, 1f));
            //Wybor tekstury
            Texture selectedTexture;
            if (progress > mediumThreshold) {
                selectedTexture = barHighTexture;
            } else if (progress > lowThreshold) {
                selectedTexture = barMediumTexture;
            } else {
                selectedTexture = barLowTexture;
            }
            float barWidth = 0.25f;//szerokosc
            float barHeight = 0.02f;//wysokosc
            float centerX = enemy.getEnemySprite().getX()+ enemy.getEnemySprite().getWidth() / 2f;//srodek sprite'a tekstury przeciwnika
            float barX = centerX - barWidth /2f;//polozenie x
            float barY = enemy.getEnemySprite().getY();//polozenie y
            float fillWidth = Math.min(progress * barWidth, barWidth);
            batch.draw(selectedTexture, barX, barY, fillWidth, barHeight);
        }
    }
    //Funkcja do liczenia szansy na drop monety
    /**
     * Sprawdza, czy w danym momencie nastąpiła szansa na drop monety po zabiciu przeciwnika.
     *
     * @return true jeśli wylosowano drop monety, false w przeciwnym wypadku
     */
    public boolean chanceForCoinDrop() {
        return MathUtils.random() < dropCoinRate;
    }
    /**
     * Zwraca listę monet aktualnie znajdujących się na ekranie gry.
     *
     * @return tablica monet
     */
    public Array<Coin> getArrayCoins(){
        return coins;
    }

    /**
     * Aktualizuje stan fali przeciwników:
     * - usuwa przeciwników, którzy wypadli poza ekran
     * - sprawdza kolizje pocisków gracza z przeciwnikami i odpowiednio ich uszkadza
     * - po zabiciu przeciwnika zwiększa wynik gracza (score) oraz generuje monetę z pewnym prawdopodobieństwem
     * - usuwa trafione pociski gracza
     *
     * @param delta czas (w sekundach) od ostatniej aktualizacji
     * @param viewport widok gry, do sprawdzania pozycji przeciwników
     * @param bullets lista pocisków wystrzelonych przez gracza, do wykrywania kolizji
     */
    public void update(float delta, Viewport viewport, Array<PlayerBullet> bullets) {
        for (int i = enemies.size - 1; i >= 0; i--) {
            Enemy enemy = enemies.get(i);
            Rectangle bounds = enemy.getBounds();

            if (bounds.y < -bounds.height) {
                enemies.removeIndex(i);
                continue;
            }

            for (PlayerBullet bullet : bullets) {
                if (bullet.collides(bounds)) {
                    enemies.get(i).EnemyTakeHit(bullet.getBulletDamage());
                    //("Przeciwnik otrzymal "+bullet.getBulletDamage()+" obrazen, teraz posiada "+enemies.get(i).getEnemyHP()+" HP");//debug note
                    //Zabicie przeciwnika przez gracza
                    if(enemies.get(i).isEnemyAlive()==0){
                        game.score += enemies.get(i).ScorePoints;//update score
                        //szansa na drop monety przez przeciwnika
                        if(chanceForCoinDrop()){
                            game.money += 5;
                            Coin coin = new Coin(enemies.get(i).sprite.getX(),enemies.get(i).sprite.getY());
                            coins.add(coin);
                            coin.playSound();
                        }
                        enemies.removeIndex(i);
                        enemy.killSound.play();
                    }
                    bullet.destroy();
                    break;
                }
            }
        }
    }
}
