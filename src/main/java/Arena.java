import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Arena {
    int width;
    int height;
    private final Hero hero;
    private final List<Wall> walls;
    private final List<Coin> coins;
    private final List<Monster> monsters;
    // GameOver:
    // 0 -> not over
    // 1 -> hero collected all coins
    // 2 -> hero killed by monsters
    public int isGameOver = 0;

    public Arena(int w, int h, int nC, int nM) {
        this.width = w;
        this.height = h;
        this.hero = new Hero(width/2, height/2); // initialise a hero in the middle of the arena
        this.walls = createWalls(); // create the walls of the arena
        this.coins = createCoins(nC); // create the coins of the arena
        this.monsters = createMonsters(nM); // create the monsters of the arena
    }

    private List<Wall> createWalls() { // create surrounding walls
        List<Wall> walls = new ArrayList<>();

        for (int c = 0; c < width; c++) {
            walls.add(new Wall(c, 0));
            walls.add(new Wall(c, height - 1));
        }
        for (int r = 1; r < height; r++) { // starting from r = 1 because the corners have already been filled
            walls.add(new Wall(0, r));
            walls.add(new Wall(width - 1, r));
        }
        /* to test random walls placed in the arena
        walls.add(new Wall(4, 4));
        walls.add(new Wall(6, 6));
        */
        return walls;
    }

    private List<Coin> createCoins(int n) {
        Random random = new Random();
        ArrayList<Coin> coins = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            int randomX = random.nextInt(width - 2) + 1;
            int randomY = random.nextInt(height - 2) + 1;

            // while the position is invalid
            while (!isValidPosition(new Position(randomX, randomY), coins)) {
                randomX = random.nextInt(width - 2) + 1;
                randomY = random.nextInt(height - 2) + 1;
            }
            coins.add(new Coin(randomX, randomY));
        }
        return coins;
    }

    private List<Monster> createMonsters(int n) {
        Random random = new Random();
        ArrayList<Monster> monsters = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            int randomX = random.nextInt(width - 2) + 1;
            int randomY = random.nextInt(height - 2) + 1;

            // while the position is invalid
            while (!isValidPosition(new Position(randomX, randomY), monsters)) {
                randomX = random.nextInt(width - 2) + 1;
                randomY = random.nextInt(height - 2) + 1;
            }
            monsters.add(new Monster(randomX, randomY));
        }
        return monsters;
    }


    public void draw(TextGraphics graphics) {
        // Paint the arena:
        graphics.setBackgroundColor(TextColor.Factory.fromString("#336699"));
        graphics.fillRectangle(new TerminalPosition(0, 0), new TerminalSize(width, height), ' ');

        // Draw (paint) the walls
        for (Wall wall : walls)
            wall.draw(graphics);

        graphics.setForegroundColor(TextColor.Factory.fromString("#FFFF00")); // set text color
        graphics.putString(new TerminalPosition(0, 0), "COINS REMAINING: " + coins.size());

        /* To paint a bigger arena:
        graphics.fillRectangle(new TerminalPosition(0, 0), new TerminalSize(width * 2, height * 2), ' ');
         */

        // Draw the hero (must be after painting the arena to be on top):
        hero.draw(graphics);

        // Draw the coins
        for (Coin coin : coins) {
            coin.draw(graphics);
        }

        drawMonsters(graphics);
    }

    public void drawMonsters(TextGraphics graphics) {
        for (Monster monster : monsters) {
            monster.draw(graphics);
        }
    }

    private void retrieveCoins() {
        Position heroPosition = hero.getPosition();
        for (Coin coin : coins) {
            if (coin.getPosition().equals(heroPosition)) {
                coins.remove(coin);
                hero.speedIncrement();
                break; // found the coin to remove
            }
        }
        if (coins.isEmpty()) isGameOver = 1;
    }

    // ArrayList<? extends Element> means "a list of some type that is Element or any subclass of Element"
    // ? é uma wildcard que representa um tipo desconhecido
    private boolean isValidPosition(Position position, ArrayList<? extends Element> list) {
        // Verify if the new position of the element is another coin
        for (Element e : list) {
            if (e.getPosition().equals(position)) return false;
        }
        // Verify if the new position of the element is the hero's
        return !position.equals(hero.getPosition());
    }

    private boolean canHeroMove(Position position) {
        // Check if the position is a wall
        for (Wall wall : walls) {
            if (wall.getPosition().equals(position)) return false;
        }
        // else check if the position goes out of the arena
        return (position.getX() >= 0 && position.getX() < width && position.getY() >= 0 && position.getY() < height);
    }

    private boolean canMonsterMove(Position position) {
        // if the hero can't move to that position so do the monster
        if (!canHeroMove(position)) return false;

        // Check if the position is one of the other monsters'
        for (Monster monster: monsters) {
            if (monster.getPosition().equals(position)) return false;
        }

        return true;
    }


    private void moveHero(Position position) {
        if (canHeroMove(position)) {
            hero.setPosition(position);
            retrieveCoins(); // see if the hero grabbed a coin
            verifyMonsterCollisions(); // to see if the hero touches a monster
        }
    }

    public void moveMonsters() {
        for (Monster monster : monsters) {
            Position nextPosition = monster.move();

            // until the monster can't move to a valid position
            while (!canMonsterMove(nextPosition)) nextPosition = monster.move();

            monster.setPosition(nextPosition);
            if (!hero.isImmune()) verifyMonsterCollisions(); // to see if the hero touches a monster
        }
    }

    private void verifyMonsterCollisions() {
        Position heroPosition = hero.getPosition();
        for (Monster monster : monsters) {
            if (monster.getPosition().equals(heroPosition)) {
                isGameOver = 2;
                break;
            }
        }
    }

    public void firstInput() { // to make hero not immune after first input in Game class
        hero.moved();
    }

    public int currentHeroSpeed() {
        return hero.getSpeed();
    }

    public void processKey(KeyStroke key) {
        switch (key.getKeyType()) {
            case KeyType.ArrowLeft:
                moveHero(hero.moveLeft());
                break;
            case KeyType.ArrowRight:
                moveHero(hero.moveRight());
                break;
            case KeyType.ArrowUp:
                moveHero(hero.moveUp());
                break;
            case KeyType.ArrowDown:
                moveHero(hero.moveDown());
                break;
            default: // just ignore
                break;
        }
    }
}
