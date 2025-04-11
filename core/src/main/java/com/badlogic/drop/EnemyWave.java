package com.badlogic.drop;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
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

    public void setShootInterval(float interval) {
        this.shootInterval = interval;
    }

    public Array<EnemyBullet> enemyBullets = new Array<>();
    ////////////////////////

    public EnemyWave(Enemy enemyTemplate) {
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

        // Wysokość ekranu, aby ustawić przeciwników na odpowiedniej wysokości
        float worldHeight = viewport.getWorldHeight();
        float y = worldHeight - height - 0.7f * row;

        // Całkowita szerokość fali przeciwników
        float totalWidth = amount * width + (amount - 1) * spacing;

        // Startowa pozycja X, aby wycentrować falę na ekranie
        float startX = (worldWidth - totalWidth) / 2;

        for (int i = 0; i < amount; i++) {
            float x = startX + i * (width + spacing);
            Enemy newEnemy = new Enemy(texture, x, y, width, height);
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

        // Wysokość ekranu, aby ustawić przeciwników na odpowiedniej wysokości
        float worldHeight = viewport.getWorldHeight();
        float y = worldHeight - height - 0.7f * row;

        // Całkowita szerokość fali przeciwników
        float totalWidth = amount * width + (amount - 1) * spacing;

        // Startowa pozycja X, aby wycentrować falę na ekranie
        float startX = (worldWidth - totalWidth) / 2;


        for (int i = 0; i < amount; i++) {
            float x = startX + i * (width + spacing);
            Enemy enemy = new Enemy(texture, x, y, width, height);
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

        // Losuj przeciwnika
        int index = MathUtils.random(0, enemies.size - 1);
        Enemy shooter = enemies.get(index);

        float x = shooter.sprite.getX() + shooter.sprite.getWidth() / 2f - 0.05f;
        float y = shooter.sprite.getY();

        EnemyBullet bullet = new EnemyBullet(x, y);
        enemyBullets.add(bullet);
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
    public boolean checkCollisionWithPlayer(Rectangle playerBounds, Sound hitSound) {
        for (int i = enemyBullets.size - 1; i >= 0; i--) {
            EnemyBullet bullet = enemyBullets.get(i);
            if (bullet.getBounds().overlaps(playerBounds)) {
                enemyBullets.removeIndex(i);
                hitSound.play();
                return true; // trafienie gracza
            }
        }
        return false;
    }
    public Array<Enemy> getEnemies() {
        return enemies;
    }

    public void render(SpriteBatch batch) {
        for (Enemy enemy : enemies) {
            enemy.sprite.draw(batch);
        }
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
                    enemies.removeIndex(i);
                    bullet.destroy();
                    enemy.killSound.play();
                    break;
                }
            }
        }
    }
}
