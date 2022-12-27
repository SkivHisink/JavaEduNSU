import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        //chitaem data
        String data = sc.nextLine();
        //delim stroku
        var parsedData = data.split(";");
        int[] translatedData = new int[parsedData.length];
        for (int i = 0; i < translatedData.length; i++) {
            translatedData[i] = Integer.parseInt(parsedData[i]);
        }
        int counter = 0;
        //Massiv tolko po chislam s dvumya sosedyami
        for (int i = 1; i < translatedData.length - 1; i++) {
            if (translatedData[i] > translatedData[i - 1] && translatedData[i] > translatedData[i + 1]){
                counter++;
            }
        }
        System.out.println(counter);
    }
}