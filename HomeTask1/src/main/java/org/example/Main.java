package org.example;

import java.util.Scanner;

public class Main {
    public static void taskFormula() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter R:");
        double R = scanner.nextDouble();
        double r;
        while (true) {
            System.out.print("Enter r:");
            r = scanner.nextDouble();
            if (R != r) {
                break;
            }
            System.out.println("r must be not equal R. Please try again.");
        }
        double result = R / Math.pow(R + r, 2) * 100;
        System.out.println("Результат вычислений:" + result + "%");
    }

    public static void taskClassSuffering() {
        Scanner scanner = new Scanner(System.in); // создаём сканер для чтения с консоли
        String catName = scanner.next(); // создаём переменную типа String и считываем значение с консоли
        int age = scanner.nextInt(); // создаём переменную типа int и -||-
        double weight = scanner.nextDouble(); // создаём переменную типа double -||-
        boolean isMan = scanner.nextBoolean(); // создаём переменную типа bool -||-
        var catTest = new Cat(catName, age, weight, isMan); // создаём объект класса Cat  и передаём прочитанные поля
        // Делаем всё аналогично
        String type = scanner.next();
        int numberOfRooms = scanner.nextInt();
        double square = scanner.nextDouble();
        boolean isSold = scanner.nextBoolean();
        var flatTest = new Flat(type, numberOfRooms, square, isSold);
        catTest.getInfo();
        flatTest.getInfo();
    }

    public static void main(String[] args)
    {
        taskFormula();
        taskClassSuffering();
    }
}