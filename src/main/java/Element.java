import com.googlecode.lanterna.graphics.TextGraphics;

public abstract class Element {
     protected Position position;

     public Element(int x, int y) {
         this.position = new Position(x, y);
     }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position p) {
         this.position = p;
    }

    public abstract void draw(TextGraphics graphics);
}
