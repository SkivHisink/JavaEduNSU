import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class GameControl {
    GameOfLife game;
    boolean isGameInitialized;
    List<String> gameCommands;

    public GameControl() {
        isGameInitialized = false;
        gameCommands = new ArrayList<>();
        gameCommands.add("step");
        gameCommands.add("forever");
        gameCommands.add("history");
        gameCommands.add("clear");
        gameCommands.add("stop");
    }

    public void InitializeGame() {
        int n;
        int x;
        int y;
        Scanner sc = new Scanner(System.in, "utf-8");
        System.setProperty("console.encoding", "utf-8");
        while (true) {
            System.out.println("Print x size");
            x = sc.nextInt();
            System.out.println("Print y size");
            y = sc.nextInt();
            if (x <= 0 || y <= 0) {
                System.out.println("Error");
            } else {
                break;
            }
        }
        int fieldSize = x * y;
        while (true) {
            System.out.println("Print n");
            n = sc.nextInt();
            if (n < 1 || n > fieldSize) {
                System.out.println("Error");
            } else {
                break;
            }
        }
        game = new GameOfLife(x, y, n);
        isGameInitialized = true;
    }

    void stepCommand(String gameCommand) {
        if (gameCommand.equals("step")) {
            game.step(true);
            return;
        }
        String[] splitted = gameCommand.split("\\s+");
        if (splitted.length != 3) {
            System.out.println("Error");
            return;
        }
        try {
            int k = Integer.parseInt(splitted[0]);
            int isShowHistoryInt = Integer.parseInt(splitted[2]);
            boolean isShowHistory = isShowHistoryInt == 1 ? true : false;
            game.multiStep(k, isShowHistory);
        } catch (Exception e) {
            System.out.println("Error");
        }
    }

    void foreverCommand(String gameCommand) {
        String[] splitted = gameCommand.split("\\s+");
        if (splitted.length != 2) {
            System.out.println("Error");
            return;
        }
        try {
            int isShowHistoryInt = Integer.parseInt(splitted[1]);
            boolean isShowHistory = isShowHistoryInt == 1 ? true : false;
            game.tryFinishGame(isShowHistory);
        } catch (Exception e) {
            System.out.println("Error");
        }
    }

    void historyCommand() {
        for (int i = 0; i < game.historyArray.length; i++) {
            if (game.historyArray[i] != null) {
                game.printGameField(game.historyArray[i]);
            }
        }
    }

    void clearCommand() {
        game.clear();
    }

    public void PlayGame() {
        if (!isGameInitialized) {
            System.out.println("Game not initialized.");
        }
        Scanner sc = new Scanner(System.in, "utf-8");
        System.setProperty("console.encoding", "utf-8");
        System.out.println("Game initialized. Print commands.");
        while (true) {
            String gameCommand = sc.nextLine();
            if (gameCommand.length() == 0) {
                continue;
            }
            if (gameCommand.contains(gameCommands.get(0))) {
                stepCommand(gameCommand);
            } else if (gameCommand.contains(gameCommands.get(1))) {
                foreverCommand(gameCommand);
            } else if (gameCommand.equals(gameCommands.get(2))) {
                historyCommand();
            } else if (gameCommand.equals(gameCommands.get(3))) {
                clearCommand();
            } else if (gameCommand.equals(gameCommands.get(4))) {
                break;
            }
        }
    }
}
