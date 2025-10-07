import com.googlecode.lanterna.*;
import com.googlecode.lanterna.graphics.TextGraphics;
import java.awt.*;

public class Hero {
    private Position position;

    public Hero(Position p) {
        this.position = p;
    }

    public void setPosition(Position p) {
        this.position = p;
    }

    public Position getPosition() {
        return this.position;
    }

    public void draw(TextGraphics graphics) {
        // Create the hero in the terminal and make it bold:
        graphics.setBackgroundColor(TextColor.Factory.fromString("#336699")); // to match the color of the arena
        graphics.setForegroundColor(TextColor.Factory.fromString("#FFFF33"));
        graphics.enableModifiers(SGR.BOLD);
        graphics.putString(new TerminalPosition(position.getX(), position.getY()), "X");

        /* to make a bigger hero
        graphics.putString(new TerminalPosition(position.getX() * 2, position.getY() * 2), "\\/");
        graphics.putString(new TerminalPosition(position.getX() * 2, position.getY() * 2 + 1), "/\\");
        */
    }

    public Position moveUp() {
        return new Position(position.getX(), position.getY() - 1);
    }

    public Position moveDown() {
        return new Position(position.getX(), position.getY() + 1);
    }

    public Position moveLeft() {
        return new Position(position.getX() - 1, position.getY());
    }

    public Position moveRight() {
        return new Position(position.getX() + 1, position.getY());
    }
}