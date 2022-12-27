import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String data = sc.nextLine();
        var parsedData = data.split("\\s+");
        List<Integer> taskList = new ArrayList<>();
        for (int i = 0; i < parsedData.length; i++) {
            taskList.add(Integer.parseInt(parsedData[i]));
        }
        int indx = 0;
        while (true) {
            if (indx >= taskList.size()) {
                break;
            }
            taskList.remove(indx);
            indx++;
        }
        for (int i = taskList.size() - 1; i > -1; i--) {
            System.out.print(taskList.get(i) + " ");
        }
    }
}