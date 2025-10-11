import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;

import java.util.Random;

public class Monster extends Element {

    public Monster(int x, int y) {
        super(x, y);
    }

    @Override
    public void draw(TextGraphics graphics) {
        graphics.setForegroundColor(TextColor.Factory.fromString("#FFFFFF")); // set text color
        graphics.putString(new TerminalPosition(position.getX(), position.getY()), "Ω");
    }

    public Position move() {
        // can not do Position next = this.position because both variables would point to the
        // exact same object in memory (objects are passed through references to the heap)
        Position next = new Position(this.position.getX(), this.position.getY());
        Random random = new Random();
        int d = random.nextInt(2) < 0.5 ? -1 : 1;
        int axis = random.nextInt(2);

        if (axis == 0) { // x axis
            next.setX(next.getX() + d);

        } else { // y axis
            next.setY(next.getY() + d);
        }
        return next;
    }
}
