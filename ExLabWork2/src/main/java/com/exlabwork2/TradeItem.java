package com.exlabwork2;

public class TradeItem {

    private String stock;
    private long time;
    private double price;
    private long volume;
    private String date;
    private double high;
    private double low;
    private double open;

    public TradeItem(String stock, long time, double open, double price, long volume, String date, double high, double low) {
        super();
        this.stock = stock;
        this.time = time;
        this.open = open;
        this.price = price;
        this.volume = volume;
        this.date = date;
        this.high = high;
        this.low = low;
    }

    public double getOpen() {
        return open;
    }

    public TradeItem(String stock, long time, double price, long volume) {
        super();
        this.stock = stock;
        this.time = time;
        this.price = price;
        this.volume = volume;
    }
    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public long getVolume() {
        return volume;
    }

    public void setVolume(long volume) {
        this.volume = volume;
    }

    @Override
    public String toString() {
        return "Trade [stock=" + stock + ", time=" + time + ", price=" + price + ", size=" + volume + "]";
    }

    public double getHigh() {
        return high;
    }

    public double getLow() {
        return low;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setHigh(double high) {
        this.high = high;
    }

    public void setLow(double low) {
        this.low = low;
    }
}
