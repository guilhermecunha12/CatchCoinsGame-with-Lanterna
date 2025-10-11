import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Application {
    public static void main(String[] args) {
        int arenaH, arenaW, numberCoins, numberMonster, monsterSpeed;
        Scanner scanner = new Scanner(System.in);
        System.out.println("Select game mode: Easy (1), Medium (2), Hard (3), Impossible (4), See records (5)");

        // Validate input
        while (!scanner.hasNextInt()) {
            System.out.println("Invalid option. Try again:");
            scanner.next(); // steps over invalid input
        }
        int mode = scanner.nextInt();

        switch (mode) {
            default: case 1:
                arenaW = 60; arenaH = 30; numberCoins = 15; numberMonster = 7; monsterSpeed = 1000;
                System.out.println("Easy mode chosen.");
                break;
            case 2:
                arenaW = 50; arenaH = 25; numberCoins = 12; numberMonster = 11; monsterSpeed = 400;
                System.out.println("Medium mode chosen.");
                break;
            case 3:
                arenaW = 40; arenaH = 20; numberCoins = 12; numberMonster = 12; monsterSpeed = 200;
                System.out.println("Hard mode chosen.");
                break;
            case 4:
                arenaW = 30; arenaH = 15; numberCoins = 12; numberMonster = 12; monsterSpeed = 150;
                System.out.println("Impossible mode chosen.");
                break;
            case 5:
                printRecords();
                return;
        }


        Game game = new Game(arenaW, arenaH, numberCoins, numberMonster);
        double startTime = System.currentTimeMillis();
        int result = game.run(monsterSpeed);
        if (result == 1) {
            double endTime = System.currentTimeMillis();
            updateRecords(endTime - startTime, mode);
        }
    }

    private static void updateRecords(double time, int mode) {
        try {
            Scanner scanner = new Scanner(System.in);
            BufferedReader br = new BufferedReader(new FileReader("records.txt"));
            boolean printRec = false;

            // get the lines from the records.txt file
            List<String> lines = new ArrayList<>();
            String l = br.readLine();
            while (l != null) {// enquanto o ficheiro não tenha terminado
                lines.add(l);
                l = br.readLine();
            }

            // if the mode's time record was broken, modify that line of the file
            for (int i = 0; i < lines.size(); i++) {
                String line = lines.get(i);
                String[] fields = line.split(",");
                if (Integer.parseInt(fields[0]) == mode) { // to find the line related to the game mode
                    String recordTime = fields[2];
                    if (recordTime.equals("none") || time < Double.parseDouble(recordTime)) {
                        System.out.print("WOW! You broke the record for the ");

                        switch (mode) {
                            default:
                            case 1:
                                System.out.print("Easy");
                                break;
                            case 2:
                                System.out.print("Medium");
                                break;
                            case 3:
                                System.out.print("Hard");
                                break;
                            case 4:
                                System.out.print("Impossible");
                        }
                        System.out.println(" mode with the time " + time/1000 + "s.");

                        System.out.println("Enter your record holder name:");
                        String name = scanner.nextLine();

                        // modify that line of the file
                        lines.set(i, fields[0] + ',' + name + ',' + time);
                        System.out.println("You are in the record books.");
                        printRec = true;
                    }
                    break; // found the mode
                }
            }

            // write the lines back to the records.txt file
            BufferedWriter bw = new BufferedWriter(new FileWriter("records.txt"));
            for (String line : lines) {
                bw.write(line);
                bw.newLine();
            }

            bw.flush(); // to update the records immediately
            if (printRec) printRecords();

            bw.close();
            br.close();
            scanner.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void printRecords() {
        System.out.println("\n==========RECORDS==========");
        try {
            BufferedReader br = new BufferedReader(new FileReader("records.txt"));
            String line = br.readLine();
            while (line != null) {
                String[] fields = line.split(",");
                switch (Integer.parseInt(fields[0])) {
                    default:
                    case 1:
                        System.out.print("Easy: ");
                        break;
                    case 2:
                        System.out.print("Medium: ");
                        break;
                    case 3:
                        System.out.print("Hard: ");
                        break;
                    case 4:
                        System.out.print("Impossible: ");
                }
                if (fields[1].equals("none")) System.out.println("none");
                else System.out.println(Double.parseDouble(fields[2])/1000 + "s by " + fields[1]);

                line = br.readLine();
            }
            System.out.println("===========================");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
