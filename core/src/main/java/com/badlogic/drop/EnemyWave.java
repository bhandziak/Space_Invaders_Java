package com.badlogic.drop;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class EnemyWave {
    private Enemy enemyTemplate;
    private Array<Enemy> enemies;

    //odpowiada za ruch przeciwnikow
    private float moveTimer = 0f;
    private float moveInterval = 1f; //move delay
    private float moveStep = 0.5f;
    private float moveDownStep;//ustawiane w 'EnemyWave'
    private boolean movingRight = true;
    ////////////

    //odpowiada za strzelanie przeciwnikow
    private float shootTimer = 0f;
    private float shootInterval = 2f; //shooting delay
    private int shootingEnemies=5; //max liczba przeciwnikow ktorzy moga strzelic podczas klatki
    public void setShootInterval(float interval) {
        this.shootInterval = interval;
    }
    //tekstury dla paska HP wrogow
    Texture barHighTexture = new Texture("progressBar_green.png");
    Texture barMediumTexture = new Texture("progressBar_orange.png");
    Texture barLowTexture = new Texture("progressBar_red.png");

    //potrzebne do zmiany Score
    final Main game;
    public Array<EnemyBullet> enemyBullets = new Array<>();

    //Coin
    private float dropCoinRate = .3f;//szansa na drop monety
    Array<Coin> coins = new Array<>();
    ////////////////////////

    public EnemyWave(Enemy enemyTemplate, final Main game) {
        this.game = game;
        this.enemyTemplate = enemyTemplate;
        this.enemies = new Array<>();
        this.moveDownStep = enemyTemplate.sprite.getHeight();//przesuniecie o wysokość przeciwnika
    }

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

        // Wysokość ekranu, aby ustawić przeciwników na odpowiedniej wysokości
        float worldHeight = viewport.getWorldHeight();
        float y = worldHeight - height - 0.7f * row;

        // Całkowita szerokość fali przeciwników
        float totalWidth = amount * width + (amount - 1) * spacing;

        // Startowa pozycja X, aby wycentrować falę na ekranie
        float startX = (worldWidth - totalWidth) / 2;

        for (int i = 0; i < amount; i++) {
            float x = startX + i * (width + spacing);
            Enemy newEnemy = new Enemy(texture, x, y, width, height,enemyHP,enemyBulletDamage,bulletSpeed,scorePoints);
            enemies.add(newEnemy);
        }
    }
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
            Enemy enemy = new Enemy(texture, x, y, width, height,enemyHP,enemyBulletDamage,bulletSpeed,scorePoints);
            enemies.add(enemy);
        }
    }
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

        //wybranie pierwszych N przeciwników do strzału
        for (int i = 0; i < Math.min(amountOfShootingEnemies, availableIndices.size); i++) {
            Enemy shooter = enemies.get(availableIndices.get(i));

            float x = shooter.sprite.getX() + shooter.sprite.getWidth() / 2f - 0.05f;
            float y = shooter.sprite.getY();

            EnemyBullet bullet = new EnemyBullet(x, y,shooter);//pocisk dostaje ilosc obrazen po podanym Enemy wybranym z listy w wave
            enemyBullets.add(bullet);
        }
    }
    //usuwanie pociskow przeciwnikow
    public void updateEnemyBullets(float delta, Viewport viewport) {
        for (int i = enemyBullets.size - 1; i >= 0; i--) {
            EnemyBullet bullet = enemyBullets.get(i);
            bullet.update(delta);

            if (!bullet.isAlive(viewport)) {
                enemyBullets.removeIndex(i);
            }
        }
    }
    public void renderEnemyBullets(SpriteBatch batch) {
        for (EnemyBullet bullet : enemyBullets) {
            bullet.render(batch);
        }
    }
    //wykrycie kolizji z graczem
    public void checkCollisionWithPlayer(Rectangle playerBounds, Sound hitSound, Player player) {
        for (int i = enemyBullets.size - 1; i >= 0; i--) {
            EnemyBullet bullet = enemyBullets.get(i);
            // trafienie gracza
            if (bullet.getBounds().overlaps(playerBounds)) {
                player.PlayerTakeHit(enemyBullets.get(i).getEnemyByBullet().getEnemyBulletDamage());
                System.out.println("Gracz otrzymal "+enemyBullets.get(i).getEnemyByBullet().getEnemyBulletDamage()+" obrazen, teraz posiada "+player.getPlayerHP()+" HP");//debug note
                enemyBullets.removeIndex(i);
                hitSound.play();
            }
        }
    }
    //wykrycie kolizji z budynkami
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
    //czyszczenie pociskow ktore zostaly z poprzedniej fali
    private void clearLeftEnemiesBullets(){
        enemyBullets.clear();
    }

    //metoda do tworzenia nowej fali po skonczeniu poprzedniej
    public void generateNewWave(Array<Enemy>  enemyTypes, FitViewport viewport,Player player){
        int enemyRows = MathUtils.random(1, 5);
        for(int i=0;i<enemyRows;i++){
            int getEnemyType = MathUtils.random(0,enemyTypes.size-1);
            int enemiesInRow = MathUtils.random(1,12);//12-max rozmiar wiersza wrogow
            addRow(viewport, enemiesInRow, i, enemyTypes.get(getEnemyType));
        }
        clearLeftEnemiesBullets();
        player.clearLeftPlayerBullets();
    }
    //metoda sprawdzania czy zabito wszystkich przeciwnikow
    public int isAnyEnemyLeftOnField(){
        return enemies.size;
    }
    public Array<Enemy> getEnemies() {
        return enemies;
    }

    public void render(SpriteBatch batch) {
        for (Enemy enemy : enemies) {
            enemy.sprite.draw(batch);
        }
    }


    // progi zmiany w procentach np. 0.5f to 50%
    private float mediumThreshold = 0.5f;
    private float lowThreshold = 0.2f;

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
    public boolean chanceForCoinDrop() {
        return MathUtils.random() < dropCoinRate;
    }
    public Array<Coin> getArrayCoins(){
        return coins;
    }

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
                    System.out.println("Przeciwnik otrzymal "+bullet.getBulletDamage()+" obrazen, teraz posiada "+enemies.get(i).getEnemyHP()+" HP");//debug note
                    //TODO tutaj dodac dzwiek otrzymania obrazen przez przeciwnika
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
