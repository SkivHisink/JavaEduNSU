package org.example;
import java.util.Scanner;

public class Main {
    public static void bubbleSort(int arr[]) {
        int length = arr.length;
        for (int i = 0; i < length - 1; i++)
            for (int j = 0; j < length - i - 1; j++) {
                if (arr[j] > arr[j + 1]) {
                    int temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                }
            }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int elementNumber = sc.nextInt();
        if (elementNumber <= 0) {
            System.out.println("Количество элементов должно быть больше равно чем 1");
            return;
        }
        var input = new int[elementNumber];
        for (int i = 0; i < elementNumber; i++) {
            input[i] = sc.nextInt();
        }
        bubbleSort(input);
        for (int i = 0; i < elementNumber; i++) {
            System.out.print(input[i] + " ");
        }
    }
}