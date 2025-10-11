import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import java.io.*;

public class Game {
    private Screen screen;
    private Arena arena;
    private PrintWriter game_moves;


    public Game(int arenaW, int arenaH, int numberCoins, int numberMonster) {
        try {
            game_moves = new PrintWriter(new PrintWriter(new File("last_game_moves.txt")), true);

            TerminalSize terminalSize = new TerminalSize(arenaW, arenaH);
            DefaultTerminalFactory terminalFactory = new DefaultTerminalFactory().setInitialTerminalSize(terminalSize);
            Terminal terminal = terminalFactory.createTerminal();

            this.screen = new TerminalScreen(terminal);
            screen.setCursorPosition(null);   // we don't need a cursor
            screen.startScreen();             // screens must be started
            screen.doResizeIfNecessary();     // resize screen if necessary

            // create the arena, according to the difficulty chosen
            this.arena = new Arena(arenaW, arenaH, numberCoins, numberMonster);

            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    // if this method fails, it throws an exception to the function call chain
    private void draw() throws IOException {
        screen.clear();
        arena.draw(screen.newTextGraphics());
        screen.refresh();
    }

    private void processKey(KeyStroke key) {
        arena.processKey(key);
    }

    // run method returns 0 if the player lost or 1 if the player won
    public int run(int monsterSpeed) {

        Thread monsterThread = new Thread() {
            @Override
            public void run() {
                while (!isInterrupted()) {
                    try {
                        arena.moveMonsters();
                        Thread.sleep(monsterSpeed); // monsters move every 500ms
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
            arena.firstInput(); // to make the hero not immune
            game_moves.println(key);

            while (true) {
                if (key.getKeyType() == KeyType.Character && key.getCharacter() == 'q') {
                    System.out.println("\n================");
                    System.out.println("GAME INTERRUPTED");
                    System.out.println("================");
                    screen.close();
                    break;

                } else if (arena.isGameOver != 0) {
                    System.out.println("\n===========================");

                    switch (arena.isGameOver) {
                        case 1:
                            System.out.println("YOU COLLECTED ALL COINS!");
                            System.out.println("YOU WIN!");
                            break;
                        case 2:
                            System.out.println("YOU GOT KILLED BY MONSTERS.");
                            System.out.println("GAME OVER");
                            break;
                    }
                    System.out.println("===========================");
                    screen.close();
                    break;

                }  else if (key.getKeyType() == KeyType.EOF) {
                    break;

                } else {
                    // doing input polling using Lanterna
                    KeyStroke pollInput = screen.pollInput();
                    while (pollInput == null) {
                        processKey(key);
                        if (arena.isGameOver != 0) break;
                        Thread.sleep(arena.currentHeroSpeed()); // the hero speed increases as it grabs coins
                        pollInput = screen.pollInput();
                    }

                    if (pollInput != null) {
                        key = pollInput;
                        game_moves.println(key);
                    }
                }
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();

        } finally {
            // interrupt the other threads and streams
            drawThread.interrupt();
            monsterThread.interrupt();
            game_moves.close();
        }

        if (arena.isGameOver == 1) return 1;
        else return 0;
    }
}
