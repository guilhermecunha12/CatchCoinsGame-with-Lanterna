import java.util.Scanner;

public class Application {
    public static void main(String[] args) {
        int arenaH, arenaW, numberCoins, numberMonster, monsterSpeed;
        Scanner scanner = new Scanner(System.in);
        System.out.println("Select game mode: Easy (1), Medium (2), Hard (3), Impossible (4)");
        int mode = scanner.nextInt();

        switch (mode) {
            default: case 1:
                arenaW = 60; arenaH = 30; numberCoins = 15; numberMonster = 5; monsterSpeed = 1000;
                System.out.println("Easy mode chosen");
                break;
            case 2:
                arenaW = 50; arenaH = 25; numberCoins = 12; numberMonster = 7; monsterSpeed = 400;
                System.out.println("Medium mode chosen");
                break;
            case 3:
                arenaW = 40; arenaH = 20; numberCoins = 12; numberMonster = 10; monsterSpeed = 200;
                System.out.println("Hard mode chosen");
                break;
            case 4:
                arenaW = 30; arenaH = 15; numberCoins = 12; numberMonster = 12; monsterSpeed = 150;
                System.out.println("Impossible mode chosen");
                break;
        }

        Game game = new Game(arenaW, arenaH, numberCoins, numberMonster);
        game.run(monsterSpeed);
    }
}