import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;

public class Arena {
    int width;
    int height;
    Hero hero;

    public Arena(int w, int h) {
        this.width = w;
        this.height = h;
        this.hero = new Hero(new Position(5, 5)); // initialise a hero in the middle of the arena
    }

    public void draw(TextGraphics graphics) {
        // Paint the arena:
        graphics.setBackgroundColor(TextColor.Factory.fromString("#336699"));
        graphics.fillRectangle(new TerminalPosition(0, 0), new TerminalSize(width + 1, height + 1), ' ');
        /* To paint a bigger arena:
        graphics.fillRectangle(new TerminalPosition(0, 0), new TerminalSize(width * 2, height * 2), ' ');
         */
        // Draw the hero (must be after painting the arena to be on top):
        hero.draw(graphics);
    }

    private boolean canHeroMove(Position position) {
        return position.getX() >= 0 && position.getX() <= width && position.getY() >= 0 && position.getY() <= height;
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
