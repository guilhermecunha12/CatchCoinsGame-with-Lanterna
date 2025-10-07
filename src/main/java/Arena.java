import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import java.util.ArrayList;
import java.util.List;

public class Arena {
    int width;
    int height;
    Hero hero;
    private List<Wall> walls;

    private List<Wall> createWalls() { // create surrounding walls
        List<Wall> walls = new ArrayList<>();

        for (int c = 0; c < width; c++) {
            walls.add(new Wall(new Position(c, 0)));
            walls.add(new Wall(new Position(c, height - 1)));
        }
        for (int r = 1; r < height; r++) { // starting from r = 1 because the corners have already been filled
            walls.add(new Wall(new Position(0, r)));
            walls.add(new Wall(new Position(width - 1, r)));
        }
        /* to test random walls placed in the arena
        walls.add(new Wall(4, 4));
        walls.add(new Wall(6, 6));
        */
        return walls;
    }

    public Arena(int w, int h) {
        this.width = w;
        this.height = h;
        this.hero = new Hero(new Position(width/2, height/2)); // initialise a hero in the middle of the arena
        this.walls = createWalls(); // Create the walls of the arena
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
    }

    private boolean canHeroMove(Position position) {
        // Check if the position is a wall
        for (Wall wall : walls) {
            if (wall.getPosition().equals(position)) {
                return false;
            }
        }
        // else check if the position goes out of the arena
        return (position.getX() >= 0 && position.getX() < width && position.getY() >= 0 && position.getY() < height);
    }

    private void moveHero(Position position) {
        if (canHeroMove(position)) {
            hero.setPosition(position);
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
