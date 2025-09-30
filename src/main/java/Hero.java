import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.screen.Screen;

public class Hero {
    private int x;
    private int y;

    public Hero(int x, int y) {
        this.x = x;
        this.y = y;
    }
    public void draw(Screen screen) {
        screen.setCharacter(this.x, this.y, TextCharacter.fromCharacter('X')[0]);
    }
    public void moveUp() {
        this.y--;
    }
    public void moveDown() {
        this.y++;
    }
    public void moveLeft() {
        this.x--;
    }
    public void moveRight() {
        this.x++;
    }



}
