import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int m = sc.nextInt();
        int n = sc.nextInt();
        String mine = "m";
        if (n < 1) {
            System.out.println("n value problem");
        }
        if (m < 1) {
            System.out.println("m value problem");
        }
        int mineNum = sc.nextInt();
        int[][] coords = new int[mineNum][];
        // создаём минное поле
        String[][] mineField = new String[m][];
        for (int i = 0; i < m; i++) {
            mineField[i] = new String[n];
            for (int j = 0; j < n; j++) {
                mineField[i][j] = "0";
            }
        }
        // заполняем минное поле минами
        for (int i = 0; i < mineNum; i++) {
            coords[i] = new int[2];
            coords[i][1] = sc.nextInt();
            coords[i][0] = sc.nextInt();
            if (coords[i][0] > 0 && coords[i][0] - 1 < n && coords[i][1] > 0 && coords[i][1] - 1 < m) {
                mineField[coords[i][1] - 1][coords[i][0] - 1] = mine;
            } else {
                System.out.println("Coordinates problem");
                return;
            }
        }
        boolean up = false;
        boolean down = false;
        boolean left = false;
        boolean right = false;
        // заполняем минное поле значениями
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                int leftVal = j - 1;
                int rightVal = j + 1;
                int upVal = i - 1;
                int downVal = i + 1;
                int counter = 0;
                if (mineField[i][j].equals(mine)) {
                    continue;
                }
                up = upVal > -1;
                down = downVal < m;
                left = leftVal > -1;
                right = rightVal < n;
                if (up && left && mineField[upVal][leftVal].equals(mine)) {
                    ++counter;
                }
                if (up && mineField[upVal][j].equals(mine)) {
                    ++counter;
                }
                if (up && right && mineField[upVal][rightVal].equals(mine)) {
                    ++counter;
                }
                if (down && left && mineField[downVal][leftVal].equals(mine)) {
                    ++counter;
                }
                if (down && mineField[downVal][j].equals(mine)) {
                    ++counter;
                }
                if (down && right && mineField[downVal][rightVal].equals(mine)) {
                    ++counter;
                }
                if (left && mineField[i][leftVal].equals(mine)) {
                    ++counter;
                }
                if (right && mineField[i][rightVal].equals(mine)) {
                    ++counter;
                }
                mineField[i][j] = Integer.toString(counter);
            }
        }
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                System.out.print(mineField[i][j] + " ");
            }
            System.out.println();
        }
    }
}