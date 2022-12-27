import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        // dobavleni nekotorie parametri dlya chteniya russkogo yazika
        Scanner sc = new Scanner(System.in, "utf-8");
        System.setProperty("console.encoding", "utf-8");
        int n = sc.nextInt();
        if (n < 1) {
            return;
        }
        List<Employee> data = new ArrayList<>();
        // nuzhno schitat chobi ne lomalos'
        sc.nextLine();
        for (int i = 0; i < n; i++) {
            //srazy schitivaem name and job name
            data.add(new Employee(sc.nextLine(), sc.nextLine()));
        }
        List<JobAssist> jobList = new ArrayList<>();
        int resultIndex = 0;
        int maxValue = 0;
        for (int i = 0; i < data.size(); ++i) {
            if (jobList.size() == 0) {
                jobList.add(new JobAssist(data.get(i).JobName, 1));
                maxValue = 1;
                resultIndex = 0;
                continue;
            }
            boolean isFound = false;
            for (int j = 0; j < jobList.size(); j++) {
                if (jobList.get(j).jobName.equals(data.get(i).JobName)) {
                    jobList.get(j).counter++;
                    if (jobList.get(j).counter > maxValue) {
                        maxValue = jobList.get(j).counter;
                        resultIndex = j;
                    }
                    isFound = true;
                    break;
                }
            }
            if (!isFound) {
                jobList.add(new JobAssist(data.get(i).JobName, 1));
            }
        }
        List<String> result = new ArrayList<>();
        result.add(jobList.get(resultIndex).counter.toString());
        result.add(jobList.get(resultIndex).jobName);
        System.out.println(result.get(0));
        System.out.println(result.get(1));
        for (int i = 0; i < data.size(); i++) {
            if (result.get(1).equals(data.get(i).JobName)) {
                System.out.println(data.get(i).Name);
            }
        }
    }
}