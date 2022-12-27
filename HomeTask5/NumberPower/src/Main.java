import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        double[] data = new double[1000];
        for (int i = 0; i < data.length; i++) {
            data[i] = Math.pow(i + 1, n);
        }
        while (true) {
            n = sc.nextInt() - 1;
            if (n == -1) {
                break;
            }
            System.out.println(data[n]);
        }
    }
}