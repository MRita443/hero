import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.screen.Screen;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class Arena {
    public int width, height;
    public Hero hero;
    public List<Wall> walls;

    public List<Coin> coins;

    public List<Monster> monsters;

    public boolean gameOver = false;


    public Arena(int width, int height, Hero hero) {
        this.width = width;
        this.height = height;
        this.hero = hero;
        this.walls = createWalls();
        this.coins = createCoins();
        this.monsters = createMonsters();
    }

    public void draw(TextGraphics graphics) {
        graphics.setBackgroundColor(TextColor.Factory.fromString("#336699"));
        graphics.fillRectangle(new TerminalPosition(0, 0), new TerminalSize(width, height), ' ');
        for (Wall wall : walls)
            wall.draw(graphics);
        for (Coin coin : coins)
            coin.draw(graphics);
        for (Monster monster : monsters)
            monster.draw(graphics);
        hero.draw(graphics);
    }


    public void moveHero(Position position) {
        if (canItMove(position)) hero.setPosition(position);
        retrieveCoins();
        moveMonsters();
        verifyMonsterCollisions();
    }


    public boolean canItMove(Position position) {
        for (Wall wall : walls) {
            if (wall.getPosition().equals(position)) return false;
        }
        return true;
    }


    public void processKey(KeyStroke key, Screen screen) throws IOException {
        //System.out.println(key);
        switch (key.getKeyType()) {
            case ArrowUp -> moveHero(hero.moveUp());
            case ArrowDown -> moveHero(hero.moveDown());
            case ArrowRight -> moveHero(hero.moveRight());
            case ArrowLeft -> moveHero(hero.moveLeft());
            case Character -> {
                if (key.getCharacter() == 'q') screen.close();
            }
        }
    }

    public List<Wall> createWalls() {
        List<Wall> walls = new ArrayList<>();
        for (int c = 0; c < width; c++) {
            walls.add(new Wall(c, 0));
            walls.add(new Wall(c, height - 1));
        }
        for (int r = 1; r < height - 1; r++) {
            walls.add(new Wall(0, r));
            walls.add(new Wall(width - 1, r));
        }
        return walls;
    }

    public List<Coin> createCoins() {
        Random random = new Random();
        ArrayList<Coin> coins = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            int nextX = random.nextInt(width - 2) + 1, nextY = random.nextInt(height - 2) + 1;
            Coin nextCoin = new Coin(nextX, nextY);
            while (coins.contains(nextCoin) || hero.getPosition() == nextCoin.getPosition()) {
                nextX = random.nextInt(width - 2) + 1;
                nextY = random.nextInt(height - 2) + 1;
                nextCoin.setPosition(new Position(nextX, nextY));
            }
            coins.add(nextCoin);
        }
        return coins;
    }

    public List<Monster> createMonsters() {
        Random random = new Random();
        ArrayList<Monster> monsters = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            int nextX = random.nextInt(width - 2) + 1, nextY = random.nextInt(height - 2) + 1;
            Monster nextMonster = new Monster(nextX, nextY);
            while (monsters.contains(nextMonster) || hero.getPosition() == nextMonster.getPosition()) {
                nextX = random.nextInt(width - 2) + 1;
                nextY = random.nextInt(height - 2) + 1;
                nextMonster.setPosition(new Position(nextX, nextY));
            }
            monsters.add(nextMonster);
        }
        return monsters;
    }

    public void retrieveCoins() {
        for (Coin coin : coins) {
            if (hero.getPosition().equals(coin.getPosition())) {
                coins.remove(coin);
                break;
            }
        }
    }

    public void moveMonsters() {
        for (Monster monster : monsters) {
            Position nextPosition = monster.move();
            if (canItMove(nextPosition)) monster.setPosition(nextPosition);
        }
    }

    public void verifyMonsterCollisions() {
        for (Monster monster : monsters) {
            if (hero.getPosition().equals(monster.getPosition())) {
                System.out.println("You were caught by a monster. Game Over!");
                gameOver = true;
            }
        }
    }
}


