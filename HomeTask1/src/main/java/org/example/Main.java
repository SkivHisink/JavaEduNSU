package org.example;

import java.util.Scanner;

public class Main {
    public static final double epsilon = 1e-8;

    public static void taskFormula() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter R:");
        double R = scanner.nextDouble();
        double r;
        while (true) {
            System.out.print("Enter r:");
            r = scanner.nextDouble();
            if (Math.abs(R - r) > epsilon) {
                break;
            }
            System.out.println("r must be not equal R. Please try again.");
        }
        double result = R / Math.pow(R + r, 2) * 100;
        System.out.println("Результат вычислений:" + result + "%");
    }

    public static void taskClassSuffering() {
        var catTest = new Cat("Мурка", 5, 5.5, false);
        var flatTest = new Flat("пентхаус", 3, 80, false);
        catTest.getInfo();
        flatTest.getInfo();
    }

    public static void main(String[] args) {
        taskFormula();
        taskClassSuffering();
    }
}