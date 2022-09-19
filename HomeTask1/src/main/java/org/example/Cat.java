package org.example;

public class Cat
{
    private String name;
    private int age;
    private double weight;
    private boolean isMan;
    public Cat(String name, int age, double weight, boolean isMan)
    {
        this.name=name;
        this.age=age;
        this.weight=weight;
        this.isMan=isMan;
    }

    public String getName()
    {
        return name;
    }

    public int getAge()
    {
        return age;
    }

    public double getWeight() {
        return weight;
    }

    public boolean isMan() {
        return isMan;
    }

    public void getInfo()
    {
        System.out.println("Кошка: имя " + name + ", возраст " + age + ", dtc " +
                weight + "кг, " + "женский пол: " + !isMan + ".");
    }
}
