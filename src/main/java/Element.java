import com.googlecode.lanterna.graphics.TextGraphics;

public abstract class Element {
     protected Position position;

     public Element(Position p) {
         this.position = p;
     }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position p) {
         this.position = p;
    }

    public abstract void draw(TextGraphics graphics);
}
