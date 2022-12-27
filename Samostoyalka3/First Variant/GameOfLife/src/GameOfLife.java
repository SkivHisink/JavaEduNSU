import java.util.Scanner;

public class GameOfLife {
    int[][] gameField;
    int[][][] historyArray;
    int isDead = 0;
    int isAlive = 1;
    boolean isActive;
    int deadCounter = 0;
    int aliveCounter = 0;
    int startDead = 0;
    int startBorn = 0;
    int stepsCounter = 0;
    int reasonOfEnd = 0;
    int historySize = 10;

    int allGoodState = 0;
    int gameOverState = 1;
    int allDiedState = 2;
    int stepLimitState = 3;

    public GameOfLife(int x_, int y_, int n) {
        isActive = true;
        historyArray = new int[historySize][][];
        gameField = new int[x_][];
        for (int i = 0; i < x_; i++) {
            gameField[i] = new int[y_];
        }
        Scanner sc = new Scanner(System.in);
        for (int i = 0; i < n; ++i) {
            while (true) {
                System.out.println("Print x pos");
                int new_x = sc.nextInt();
                System.out.println("Print y pos");
                int new_y = sc.nextInt();
                if (new_x < 0 ||
                        new_y < 0 ||
                        new_x > x_ ||
                        new_y > y_) {
                    System.out.println("Error");
                } else if (gameField[new_x][new_y] == isAlive) {
                    System.out.println("Error");
                } else {
                    gameField[new_x][new_y] = isAlive;
                    break;
                }
            }
        }
        int[][] clone = gameField.clone();
        for (int i = 0; i < y_; i++) {
            clone[i] = gameField[i].clone();
        }
        historyArray[historySize - 1] = clone;
        isActive = false;
        for (int i = 0; i < historyArray[historySize - 1].length; i++) {
            for (int j = 0; j < historyArray[historySize - 1][i].length; j++) {
                if (gameField[i][j] == 1) {
                    isActive = true;
                }
            }
        }
    }

    public void printGameField(int[][] data) {
        System.out.println("Alive : " + aliveCounter +
                ", Dead : " + deadCounter +
                ", Born - " + startBorn +
                ", Died - " + startDead);
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[i].length; j++) {
                System.out.print(data[i][j] + " ");
            }
            System.out.println("");
        }
    }

    public boolean step(boolean isPrintInfo) {
        if (isActive) {
            boolean isChanged = false;
            for (int i = 0; i < historyArray[historySize - 1].length; i++) {
                for (int j = 0; j < historyArray[historySize - 1][i].length; j++) {
                    int counter = 0;
                    if (i + 1 > -1 && j + 1 > -1 && i + 1 < historyArray[historySize - 1].length &&
                            j + 1 < historyArray[historySize - 1][i].length &&
                            historyArray[historyArray.length - 1][i + 1][j + 1] == isAlive) {
                        counter++;
                    }
                    if (i - 1 > -1 && j - 1 > -1 &&
                            historyArray[historyArray.length - 1][i - 1][j - 1] == isAlive) {
                        counter++;
                    }
                    if (i + 1 > -1 && j > -1 && i + 1 < historyArray[historySize - 1].length &&
                            historyArray[historyArray.length - 1][i + 1][j] == isAlive) {
                        counter++;
                    }
                    if (i > -1 && j + 1 > -1 && j + 1 < historyArray[historySize - 1][i].length &&
                            historyArray[historyArray.length - 1][i][j + 1] == isAlive) {
                        counter++;
                    }
                    if (i > -1 && j - 1 > -1 &&
                            historyArray[historyArray.length - 1][i][j - 1] == isAlive) {
                        counter++;
                    }
                    if (i - 1 > -1 && j > -1 &&
                            historyArray[historyArray.length - 1][i - 1][j] == isAlive) {
                        counter++;
                    }
                    if (i + 1 > -1 && j - 1 > -1 && i + 1 < historyArray[historySize - 1].length &&
                            historyArray[historyArray.length - 1][i + 1][j - 1] == isAlive) {
                        counter++;
                    }
                    if (i - 1 > -1 && j + 1 > -1 && j + 1 < historyArray[historySize - 1][i].length &&
                            historyArray[historyArray.length - 1][i - 1][j + 1] == isAlive) {
                        counter++;
                    }
                    if (!((counter >= 2 && counter <= 3) && gameField[i][j] == isAlive)) {
                        if (gameField[i][j] == isAlive) {
                            gameField[i][j] = isDead;
                            startDead++;
                            isChanged = true;
                        }
                    }
                    if (gameField[i][j] == isDead && counter == 3) {
                        gameField[i][j] = isAlive;
                        startBorn++;
                        isChanged = true;
                    }
                }
            }
            boolean isAliveBool = false;
            for (int i = 0; i < historyArray[historySize - 1].length; i++) {
                for (int j = 0; j < historyArray[historySize - 1][i].length; j++) {
                    if (gameField[i][j] == 1) {
                        isAliveBool = true;
                    }
                }
            }
            aliveCounter = 0;
            for (int i = 0; i < historyArray[historySize - 1].length; i++) {
                for (int j = 0; j < historyArray[historySize - 1][i].length; j++) {
                    if (gameField[i][j] == isAlive) {
                        aliveCounter++;
                    }
                }
            }
            deadCounter = historyArray[historySize - 1].length * historyArray[historySize - 1][0].length - aliveCounter;
            if (!isAliveBool) {
                reasonOfEnd = 1;
            } else if (!isChanged) {
                reasonOfEnd = 2;
            }
            isActive = isAliveBool && isChanged;
        } else {
            System.out.println("Game over!");
            return false;
        }
        int[][] clone = gameField.clone();
        for (int i = 0; i < historyArray[historySize - 1][0].length; i++) {
            clone[i] = gameField[i].clone();
        }
        for (int i = 1; i < historyArray.length; i++) {
            historyArray[i - 1] = historyArray[i];
        }
        historyArray[historyArray.length - 1] = clone;
        if (isPrintInfo) {
            printGameField(gameField);
        }
        return true;
    }

    public void multiStep(int stepNum, boolean isPrintHistory) {
        if (stepNum < 1) {
            return;
        }
        for (int i = 0; i < stepNum; i++) {
            if (!step(isPrintHistory)) {
                return;
            }
            stepsCounter++;
        }
        reasonOfEnd = stepLimitState;
    }

    void printReasonOfEnd(boolean isPrintResult) {
        if (isPrintResult) {
            System.out.println("Steps - " + stepsCounter);
            System.out.print("Game over - ");
            if (reasonOfEnd == gameOverState) {
                System.out.println("all died");
            } else if (reasonOfEnd == allDiedState) {
                System.out.println("no field changes");
            } else if (reasonOfEnd == stepLimitState) {
                System.out.println("step limit achieved");
            }
        }

    }

    public void tryFinishGame(boolean isPrintResult) {
        multiStep(1000, isPrintResult);
        printReasonOfEnd(isPrintResult);
    }

    public void clear() {
        for (int i = 0; i < historyArray.length; i++) {
            historyArray[i] = null;
        }
    }
}