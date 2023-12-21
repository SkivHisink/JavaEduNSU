package org.lab.exlabwork1.Utility;

public class TableData {
    private int N;
    private int dayOfUsing;
    private String paymentDateVal;
    private double generalPaymentSize;
    private double percentSum;
    private double sumOfFee;
    private double feeLeft;

    public double getFeeLeft() {
        return feeLeft;
    }

    public static String getStaticFeeLeft(TableData table) {
        return Double.toString(table.getFeeLeft());
    }

    public double getGeneralPaymentSize() {
        return generalPaymentSize;
    }

    public static String getStaticGeneralPaymentSize(TableData table) {
        return Double.toString(table.getGeneralPaymentSize());
    }

    public double getPercentSum() {
        return percentSum;
    }

    public static String getStaticPercentSum(TableData table) {
        return Double.toString(table.getPercentSum());
    }

    public double getSumOfFee() {
        return sumOfFee;
    }

    public static String getStaticSumOfFee(TableData table) {
        return Double.toString(table.getPercentSum());
    }

    public int getN() {
        return N;
    }

    public static String getStaticN(TableData table) {
        return Integer.toString(table.getN());
    }

    public int getDayOfUsing() {
        return dayOfUsing;
    }

    public static String getStaticDayOfUsing(TableData table) {
        return Integer.toString(table.getDayOfUsing());
    }

    public String getPaymentDateVal() {
        return paymentDateVal;
    }

    public static String getStaticPaymentDateVal(TableData table) {
        return table.getPaymentDateVal();
    }

    public void setDayOfUsing(int dayOfUsing) {
        this.dayOfUsing = dayOfUsing;
    }

    public void setFeeLeft(double feeLeft) {
        this.feeLeft = feeLeft;
    }

    public void setN(int n) {
        this.N = n;
    }

    public void setGeneralPaymentSize(double generalPaymentSize) {
        this.generalPaymentSize = generalPaymentSize;
    }

    public void setPaymentDateVal(String paymentDateVal) {
        this.paymentDateVal = paymentDateVal;
    }

    public void setPercentSum(double percentSum) {
        this.percentSum = percentSum;
    }

    public void setSumOfFee(double sumOfFee) {
        this.sumOfFee = sumOfFee;
    }
}
