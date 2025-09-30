import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.Screen;

public class Arena {
    int width;
    int height;
    Hero hero;

    public Arena(int w, int h) {
        this.width = w;
        this.height = h;
        this.hero = new Hero(new Position(0, 0));
    }

    public void draw(Screen screen) {
        screen.setCharacter(hero.getPosition().getX(), hero.getPosition().getY(), TextCharacter.fromCharacter('X')[0]);
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
