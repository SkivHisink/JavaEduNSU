package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static class Employee {
        public String Name;
        public String Job;

        public Employee(String name_, String job_) {
            Name = name_;
            Job = job_;
        }
    }


    public static List<String> findBestJob(List<Employee> input) {
        List<String> jobContainer = new ArrayList<>();
        List<Integer> jobCounter = new ArrayList<>();
        for (int i = 0; i < input.size(); ++i) {
            if (jobCounter.size() == 0) {
                jobContainer.add(input.get(i).Job);
                jobCounter.add(1);
            } else if (jobContainer.contains(input.get(i).Job)) {
                int index = jobContainer.indexOf(input.get(i).Job);
                jobCounter.set(index, jobCounter.get(index) + 1);
            } else {
                jobContainer.add(input.get(i).Job);
                jobCounter.add(1);
            }
        }
        int resultIndex = 0;
        int maxValue = 0;
        for (int i = 0; i < jobCounter.size(); i++) {
            if (jobCounter.get(i) > maxValue) {
                resultIndex = i;
                maxValue = jobCounter.get(i);
            }
        }
        List<String> result = new ArrayList<>();
        result.add(jobCounter.get(resultIndex).toString());
        result.add(jobContainer.get(resultIndex));
        return result;
    }

    public static List<String> getBestJobWithEmployee(List<Employee> input, List<String> job) {
        List<String> result = new ArrayList<>();
        result.add(job.get(0));
        result.add(job.get(1));
        for (int i = 0; i < input.size(); i++) {
            if(result.get(1).equals(input.get(i).Job)){
                result.add(input.get(i).Name);
            }
        }
        return result;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in,"utf-8");
        System.setProperty("console.encoding","utf-8");
        int elementNumber = sc.nextInt();
        if (elementNumber <= 0) {
            System.out.println("Количество элементов должно быть больше равно чем 1");
            return;
        }
        List<Employee> input = new ArrayList<>();
        sc.nextLine();
        for (int i = 0; i < elementNumber; i++) {
            String tempName = sc.nextLine();
            String tempJob = sc.nextLine();
            input.add(new Employee(tempName, tempJob));
        }
        var job = findBestJob(input);
        var result = getBestJobWithEmployee(input, job);
        for(int i=0;i<result.size();i++){
            System.out.println(result.get(i));
        }
    }
}