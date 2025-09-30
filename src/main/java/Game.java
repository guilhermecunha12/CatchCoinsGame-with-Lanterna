import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;

import java.io.IOException;

public class Game {
    private Screen screen;
    private int x = 10;
    private int y = 10;

    public Game() {
        try {
            TerminalSize terminalSize = new TerminalSize(40, 20);
            DefaultTerminalFactory terminalFactory = new DefaultTerminalFactory()
                    .setInitialTerminalSize(terminalSize);
            Terminal terminal = terminalFactory.createTerminal();

            this.screen = new TerminalScreen(terminal);
            screen.setCursorPosition(null);   // we don't need a cursor
            screen.startScreen();             // screens must be started
            screen.doResizeIfNecessary();     // resize screen if necessary

            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    private void draw() throws IOException { // o method draw, caso falhe, lança uma exceção para a function call chain
        screen.clear();
        screen.setCharacter(this.x, this.y, TextCharacter.fromCharacter('X')[0]);
        screen.refresh();
    }

    public void run() {
        try {
            while (true) {
                draw();
                KeyStroke key = screen.readInput();
                if (key.getKeyType() == KeyType.Character && key.getCharacter() == 'q') {
                    screen.close();
                } else if (key.getKeyType() == KeyType.EOF) {
                    break;
                }
                processKey(key);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void processKey(KeyStroke key) {
        switch (key.getKeyType()) {
            case KeyType.ArrowLeft:
                this.x--;
                break;
            case KeyType.ArrowRight:
                this.x++;
                break;
            case KeyType.ArrowUp:
                this.y--;
                break;
            case KeyType.ArrowDown:
                this.y++;
                break;
        }
    }
}
