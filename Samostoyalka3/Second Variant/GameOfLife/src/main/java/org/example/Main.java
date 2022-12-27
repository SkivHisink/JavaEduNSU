package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static class Game {
        boolean[][] field;
        List<boolean[][]> historyList;
        boolean isActive = true;
        int x = 0;
        int dead = 0;
        int alive = 0;
        int beginDead = 0;
        int beginBorn = 0;
        int y = 0;
        int steps = 0;
        int reasonOfEnd = 0;

        // Iнициализация игры
        public Game(int x_, int y_, int n) {
            x = x_;
            y = y_;
            historyList = new ArrayList<>();
            field = new boolean[x][];
            for (int i = 0; i < x; i++) {
                field[i] = new boolean[y];
            }
            Scanner sc = new Scanner(System.in);
            System.out.println("Вводите координаты");
            for (int i = 0; i < n; ++i) {
                while (true) {
                    int temp_x = sc.nextInt();
                    int temp_y = sc.nextInt();
                    // Считаем что координаты у нас с нуля и до размеров нашего поля -1
                    if (temp_x < 0 || temp_y < 0 || temp_x > x || temp_y > y) {
                        System.out.println("Позиция клетки должна быть больше равна нулю во всех направлениях и не быть за пределами поля. Попробуйте ещё раз!");
                    } else if (field[temp_x][temp_y]) {
                        System.out.println("Здесь уже есть живая клетка. Попробуйте ещё раз!");
                    } else {
                        field[temp_x][temp_y] = true;
                        break;
                    }
                }
            }
            var clone = field.clone();
            for(int i=0;i<y;i++){
                clone[i] = field[i].clone();
            }
            historyList.add(clone);
            isActive = findAlive();
        }

        public void showField(boolean[][] data) {
            for (int i = 0; i < x; i++) {
                for (int j = 0; j < y; j++) {
                    System.out.print(data[i][j] + " ");
                }
                System.out.print("\n");
            }
            System.out.println("Статистика прошлой эпохи:");
            System.out.println("Живых - " + alive);
            System.out.println("Мёртвых - " + dead);
            System.out.println("Родилось - " + beginBorn);
            System.out.println("Умерло - " + beginDead);
        }

        public boolean findAlive() {
            for (int i = 0; i < x; i++) {
                for (int j = 0; j < y; j++) {
                    if (field[i][j]) {
                        return true;
                    }
                }
            }
            return false;
        }

        public void countAlive() {
            alive = 0;
            for (int i = 0; i < x; i++) {
                for (int j = 0; j < y; j++) {
                    if (field[i][j]) {
                        alive++;
                    }
                }
            }
        }

        public boolean nextGen() {
            boolean isChanged = false;
            dead = 0;
            alive = 0;
            for (int i = 0; i < x; i++) {
                for (int j = 0; j < y; j++) {
                    int counter = 0;
                    //1
                    int left = i - 1;
                    int front = i;
                    int right = i + 1;
                    int up = j - 1;
                    int middle = j;
                    int bottom = j + 1;
                    if (left > -1 && up > -1 && historyList.get(historyList.size() - 1)[left][up]) {
                        counter++;
                    }
                    //2
                    if (front > -1 && up > -1 && historyList.get(historyList.size() - 1)[front][up]) {
                        counter++;
                    }
                    //3
                    if (right > -1 && up > -1 && right < x && historyList.get(historyList.size() - 1)[right][up]) {
                        counter++;
                    }
                    //4
                    if (left > -1 && j > -1 && historyList.get(historyList.size() - 1)[i - 1][middle]) {
                        counter++;
                    }
                    //5
                    if (left > -1 && bottom > -1 && bottom < y && historyList.get(historyList.size() - 1)[i - 1][bottom]) {
                        counter++;
                    }
                    //6
                    if (right > -1 && j > -1 && right < x && historyList.get(historyList.size() - 1)[right][middle]) {
                        counter++;
                    }
                    //7
                    if (right > -1 && bottom > -1 && right < x && bottom < y && historyList.get(historyList.size() - 1)[right][bottom]) {
                        counter++;
                    }
                    //8
                    if (front > -1 && bottom > -1 && bottom < y && historyList.get(historyList.size() - 1)[front][bottom]) {
                        counter++;
                    }
                    //Правило 4. Если клетка жива и у нее 2−3 живых соседа, то она остается живой
                    if ((counter >= 2 && counter <= 3) && field[i][j]) {
                        //nothing happens
                    }
                    // иначе умирает
                    else {
                        if (field[i][j]) {
                            field[i][j] = false;
                            beginDead++;
                            isChanged = true;
                        }
                    }
                    // Правило 5. Если клетка мертва и у нее 3 живых соседа, то она становится живой, иначе остается мертвой.
                    if (!field[i][j] && counter == 3) {
                        field[i][j] = true;
                        beginBorn++;
                        isChanged = true;
                    }
                }
            }
            // Правило 6. Iгра прекращается, если на поле не останется ни одной живой клетки.
            boolean isAlive = findAlive();
            countAlive();
            dead = x * y - alive;
            if (!isAlive) {
                reasonOfEnd = 1;
            } else if (!isChanged) {
                reasonOfEnd = 2;
            }
            // Правило 7. Iгра прекращается, если при очередном шаге ни одна из клеток не меняет своего состояния (то есть состояние поля остается неизменным).
            return isAlive && isChanged;
        }

        // Реализовать функцию для расчета нового состояния на основе старого (по правилам, описанным ранее).
        public boolean step() {
            return step(true);
        }

        // Модифицировать функцию из шага 5,
        // чтобы печаталось новое состояние (функция из шага 4) и
        // статистика шага (сколько стало живых клеток, сколько мертвых клеток, сколько клеток поменяло свое состояние с живого на мертвое,
        // сколько клеток поменяло свое состояние с мертвого на живое).
        // Добавить аргумент в функцию (аргумент типа boolean),
        // который отвечал бы, будет ли печататься эта информация после хода или нет.
        public boolean step(boolean isShowInfo) {
            if (isActive) {
                isActive = nextGen();
            } else {
                System.out.println("Game over!");
                return false;
            }
            var clone = field.clone();
            for(int i=0;i<y;i++){
                clone[i] = field[i].clone();
            }
            historyList.add(clone);
            if (historyList.size() > 10) {
                historyList.remove(0);
            }
            // Модифицировать функцию из шага 5, чтобы печаталось новое состояние (функция из шага 4) и статистика шага (сколько стало живых клеток,
            // сколько мертвых клеток, сколько клеток поменяло свое состояние с живого на мертвое, сколько клеток поменяло свое состояние с мертвого на живое).
            // Добавить аргумент в функцию (аргумент типа boolean), который отвечал бы, будет ли печататься эта информация после хода или нет.
            if (isShowInfo) {
                showField(field);
            }
            return true;
        }

        // Реализовать функцию, которая будет рассчитывать k шагов игры на основании текущего (с использованием функции, реализованной на предыдущем шаге).
        public void multiStep(int stepNum) {
            multiStep(stepNum, true);
        }

        public void multiStep(int stepNum, boolean isPrintInfo) {
            if (stepNum < 1) {
                return;
            }
            for (int i = 0; i < stepNum; i++) {
                if (!step(isPrintInfo)) {
                    return;
                }
                steps++;
            }
            reasonOfEnd = 3;
        }

        //Сделать функцию, которое будет рассчитывать все дальнейшие шаги,
        // пока не наступит условие завершения игры (не останется живых клеток, или состояние перестанет меняться).
        // Для этого требуется использовать цикл while с указанными условиями завершения,
        // плюс добавить ограничение по количеству шагов в 1000 - то есть цикл останавливается либо по достижению условий,
        // описанных выше, либо если достигнуто максимальное количество шагов.
        public void endGame(boolean isShowResult) {
            multiStep(1000);
            if (isShowResult) {
                System.out.println("Шагов прошло - " + steps);
                System.out.print("Iгра закончена по причине - ");
                if (reasonOfEnd == 1) {
                    System.out.print("все мертвы\n");
                } else if (reasonOfEnd == 2) {
                    System.out.print("не было изменений поля\n");
                } else if (reasonOfEnd == 3) {
                    System.out.print("достигнут лимит по шагам\n");
                }
            }
        }

        public void clear() {
            historyList.clear();
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in, "utf-8");
        System.setProperty("console.encoding", "utf-8");
        int x = 0;
        int y = 0;
        System.out.println("Введите размеры вашего поля x и y через Enter");
        while (true) {
            x = sc.nextInt();
            y = sc.nextInt();
            if (x <= 0 || y <= 0) {
                System.out.println("Размер поля должен быть больше равен еденицы во всех направлениях. Попробуйте ещё раз!");
            } else {
                break;
            }
        }
        int n = 0;
        System.out.println("Введите начальное кол-во живых клеток");
        int fieldSize = x * y;
        while (true) {
            n = sc.nextInt();
            if (n <= 0 || n > fieldSize) {
                System.out.println("Количество живых клеток должно быть больше равно еденицы и меньше размера поля. Попробуйте ещё раз!");
            } else {
                break;
            }
        }
        // Iнициализация игры
        var game = new Game(x, y, n);
        System.out.println("Iгра инициализирована. Вводите команды");
        while (true) {
            String command = sc.nextLine();
            if (command.length() == 0) {
                continue;
            }
            if (command.contains("step")) {
                if (command.equals("step")) {
                    game.step();
                    continue;
                }
                String[] splitted = command.split("\\s+");
                if (splitted.length != 3) {
                    System.out.println("Wrong command!!");
                    continue;
                }
                try {
                    int k = Integer.parseInt(splitted[0]);
                    int isPrintInfo = Integer.parseInt(splitted[2]);
                    game.multiStep(5, isPrintInfo == 1 ? true : false);
                } catch (Exception e) {
                    System.out.println("Wrong command!!");
                    continue;
                }
            } else if (command.contains("forever")) {
                String[] splitted = command.split("\\s+");
                if (splitted.length != 2) {
                    System.out.println("Wrong command!!");
                    continue;
                }
                try {
                    int isPrintInfo = Integer.parseInt(splitted[1]);
                    game.endGame(isPrintInfo == 1 ? true : false);
                } catch (Exception e) {
                    System.out.println("Wrong command!!");
                    continue;
                }
            } else if (command.equals("history")) {

                for (int i = 0; i < game.historyList.size(); i++) {
                    game.showField(game.historyList.get(i));
                }
            } else if (command.equals("clear")) {
                game.clear();
            } else {
                System.out.println("Wrong command!!");
                continue;
            }
        }
    }
}