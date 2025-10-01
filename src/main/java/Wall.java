import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;

public class Wall {
    private final int x;
    private final int y;

    Wall(int x, int y) {
        this.x = x;
        this.y = y;
    }

    int  getX() {
        return x;
    }
    int getY() {
        return y;
    }

    public void draw(TextGraphics graphics) {
        graphics.setBackgroundColor(TextColor.Factory.fromString("#A52A2A"));
        graphics.fillRectangle(new TerminalPosition(x, y), new TerminalSize(1, 1), ' ');
    }
}
