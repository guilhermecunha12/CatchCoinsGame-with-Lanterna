import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;

import java.io.IOException;

public class Game {
    private Screen screen;
    private Arena arena;

    public Game() {
        try {
            TerminalSize terminalSize = new TerminalSize(40, 20);
            DefaultTerminalFactory terminalFactory = new DefaultTerminalFactory().setInitialTerminalSize(terminalSize);
            Terminal terminal = terminalFactory.createTerminal();

            this.screen = new TerminalScreen(terminal);
            screen.setCursorPosition(null);   // we don't need a cursor
            screen.startScreen();             // screens must be started
            screen.doResizeIfNecessary();     // resize screen if necessary

            this.arena = new Arena(40, 20); // inicializar a arena

            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    private void draw() throws IOException { // o method draw, caso falhe, lança uma exceção para a function call chain
        screen.clear();
        arena.draw(screen.newTextGraphics());
        screen.refresh();
    }

    private void processKey(KeyStroke key) {
        arena.processKey(key);
    }

    public void run() {

        Thread monsterThread = new Thread() {
            @Override
            public void run() {
                while (!isInterrupted()) {
                    try {
                        arena.moveMonsters();
                        Thread.sleep(500); // monsters move every 500ms
                    } catch (InterruptedException e) {
                        break;
                    }
                }
            }
        };

        Thread drawThread = new Thread() {
            @Override
            public void run() {
                while (!isInterrupted()) {
                    try {
                        draw();
                        Thread.sleep(10); // draw every 10ms
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        break;
                    }
                }
            }
        };

        try {
            // start draw and monster threads
            drawThread.start();
            monsterThread.start();
            KeyStroke key = screen.readInput(); // read first input
            System.out.println(key); // print the key stroke

            while (true) {
                if (key.getKeyType() == KeyType.Character && key.getCharacter() == 'q') {
                    System.out.println("GAME INTERRUPTED");
                    screen.close();
                    break;

                } else if (arena.isGameOver) {
                    System.out.println("GAME OVER");
                    screen.close();
                    break;

                }  else if (key.getKeyType() == KeyType.EOF) {
                    break;

                } else {
                    // doing input polling using Lanterna
                    KeyStroke pollInput = screen.pollInput();
                    while (pollInput == null) {
                        processKey(key);
                        if (arena.isGameOver) break;
                        Thread.sleep(300);
                        pollInput = screen.pollInput();
                    }

                    if (pollInput != null) {
                        key = pollInput;
                        System.out.println(key); // print the key stroke
                    }
                }
            }
            // interrupt the other threads
            drawThread.interrupt();
            monsterThread.interrupt();

        } catch (IOException | InterruptedException e) {
            // interrupt the other threads
            drawThread.interrupt();
            monsterThread.interrupt();
            e.printStackTrace();
        }
    }
}