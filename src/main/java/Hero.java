import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.screen.Screen;

public class Hero {
    private final Position position;

    public Hero(Position p) {
        this.position = p;
    }
    public void draw(Screen screen) {
        screen.setCharacter(this.position.getX(), this.position.getY(), TextCharacter.fromCharacter('X')[0]);
    }
    public void moveUp() {
        this.position.setY(this.position.getY() - 1);
    }
    public void moveDown() {
        this.position.setY(this.position.getY() + 1);
    }
    public void moveLeft() {
        this.position.setX(this.position.getX() - 1);
    }
    public void moveRight() {
        this.position.setX(this.position.getX() + 1);
    }
}
