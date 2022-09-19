package org.example;

public class Flat
{
    private String type;
    private int numberOfRooms;
    private double square;
    private boolean isSold;
    public Flat(String type, int numberOfRooms, double square, boolean isSold)
    {
        this.type=type;
        this.numberOfRooms=numberOfRooms;
        this.square=square;
        this.isSold=isSold;
    }

    public double getSquare() {
        return square;
    }

    public int getNumberOfRooms() {
        return numberOfRooms;
    }

    public String getType() {
        return type;
    }

    public boolean isSold() {
        return isSold;
    }

    public void getInfo()
    {
        System.out.println("Квартира: тип " + type + ", комнат " + numberOfRooms + ", площадь " +
                square + "м^2, " + "продана: " + isSold + ".");
    }
}
