import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Arena {
    int width;
    int height;
    private final Hero hero;
    private final List<Wall> walls;
    private final List<Coin> coins;

    public Arena(int w, int h) {
        this.width = w;
        this.height = h;
        this.hero = new Hero(width/2, height/2); // initialise a hero in the middle of the arena
        this.walls = createWalls(); // create the walls of the arena
        this.coins = createCoins(10); // create the coins of the arena
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
            while (!isValidCoinPosition(new Position(randomX, randomY), coins)) {
                randomX = random.nextInt(width - 2) + 1;
                randomY = random.nextInt(height - 2) + 1;
            }
            coins.add(new Coin(randomX, randomY));
        }
        return coins;
    }

    public void draw(TextGraphics graphics) {
        // Paint the arena:
        graphics.setBackgroundColor(TextColor.Factory.fromString("#336699"));
        graphics.fillRectangle(new TerminalPosition(0, 0), new TerminalSize(width, height), ' ');

        // Draw (paint) the walls
        for (Wall wall : walls)
            wall.draw(graphics);

        /* To paint a bigger arena:
        graphics.fillRectangle(new TerminalPosition(0, 0), new TerminalSize(width * 2, height * 2), ' ');
         */
        // Draw the hero (must be after painting the arena to be on top):
        hero.draw(graphics);

        // Draw the coins
        for (Coin coin : coins) {
            coin.draw(graphics);
        }
    }

    private void retrieveCoins() {
        Position heroPosition = hero.getPosition();
        for (Coin coin : coins) {
            if (coin.getPosition().equals(heroPosition)) {
                coins.remove(coin);
                break; // found the coin to remove
            }
        }
    }

    private boolean isValidCoinPosition(Position position, ArrayList<Coin> coins) {
        // Verify if the new position of the coin is another coin
        for (Coin coin : coins) {
            if (coin.getPosition().equals(position)) return false;
        }
        // Verify if the new position of the coin is the hero's
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

    private void moveHero(Position position) {
        if (canHeroMove(position)) {
            hero.setPosition(position);
            retrieveCoins(); // see if the hero grabbed a coin
        }
    }

    public void processKey(KeyStroke key) {
        System.out.println(key); // print the key stroke

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
